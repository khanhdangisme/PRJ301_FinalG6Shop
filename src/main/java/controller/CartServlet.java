package controller;

import constant.AttributeConstant;
import constant.ParamConstant;
import constant.PathConstant;
import dao.ShopProductDAO;
import dao.VoucherDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.ProductDTO;
import model.Voucher;
import java.util.Date;
import java.net.URLEncoder;
import java.util.Base64;
import model.User;

@WebServlet(name = "CartServlet", urlPatterns = {"/cart"})
public class CartServlet extends HttpServlet {

    /* ─────────────────────────────── helpers ─────────────────────────────── */
    private void updateCartCount(HttpSession session, Map<String, Integer> cart) {
        int total = 0;
        for (Integer v : cart.values()) {
            total += v;
        }
        session.setAttribute(AttributeConstant.CART_COUNT, total);
    }

    /* ─────────────────────────────── doGet ─────────────────────────────── */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter(ParamConstant.ACTION);
        if (action == null) {
            action = "view";                      // mặc định
        }
        ShopProductDAO dao = new ShopProductDAO();
        HttpSession session = request.getSession();

        switch (action) {

            /* ============================== ADD ============================== */
            case "add": {
                try {
                    //int id = Integer.parseInt(request.getParameter(ParamConstant.ID));

                    String idRaw = request.getParameter(ParamConstant.ID);
                    String color = request.getParameter(ParamConstant.COLOR);
                    String storage = request.getParameter(ParamConstant.STORAGE);

                    if (idRaw == null || idRaw.trim().isEmpty()
                            || color == null || color.trim().isEmpty()
                            || storage == null || storage.trim().isEmpty()) {
                        session.setAttribute(AttributeConstant.ERROR, "Missing required parameters");
                        response.sendRedirect(request.getContextPath() + "/shop");
                        return;
                    }

                    int id = Integer.parseInt(idRaw);

                    ProductDTO product = dao.getProductDetailDTO(id, color, storage);
                    if (product == null) {
                        session.setAttribute(AttributeConstant.ERROR, "Product not found");
                        response.sendRedirect(request.getContextPath() + "/shop");
                        return;
                    }
                    if (product.getQuantity() <= 0) {
                        session.setAttribute(AttributeConstant.ERROR, "Product is out of stock");
                        response.sendRedirect(request.getContextPath() + "/shop");
                        return;
                    }

                    @SuppressWarnings("unchecked")
                    Map<String, Integer> cart = (Map<String, Integer>) session.getAttribute(AttributeConstant.CART);
                    if (cart == null) {
                        cart = new HashMap<>();
                    }

                    String key = id + ":" + color + ":" + storage;
                    int currentQty = cart.getOrDefault(key, 0);

                    if (currentQty + 1 > product.getQuantity()) {
                        session.setAttribute(AttributeConstant.ERROR, "Not enough stock for " + product.getProductName());
                        response.sendRedirect(request.getContextPath() + "/shop");
                        return;
                    }
                    cart.put(key, currentQty + 1);

                    session.setAttribute(AttributeConstant.CART, cart);
                    updateCartCount(session, cart);

                    // Cookie for CartItems
                    // Lưu cart vào Cookie (mã hoá để tránh ký tự không hợp lệ)
                    StringBuilder cartData = new StringBuilder();
                    for (Map.Entry<String, Integer> entry : cart.entrySet()) {
                        cartData.append(entry.getKey()).append("=").append(entry.getValue()).append(",");
                    }
                    if (cartData.length() > 0) {
                        cartData.setLength(cartData.length() - 1); // bỏ dấu , cuối
                    }

                    // Encode để tránh lỗi cookie invalid character
                    String encodedCart = URLEncoder.encode(cartData.toString(), "UTF-8");

//                    Cookie cartCookie = new Cookie("cart", encodedCart);
//                    cartCookie.setMaxAge(30 * 24 * 60 * 60); // 30 ngày
//                    cartCookie.setPath("/"); // (khuyến nghị) để cookie áp dụng toàn bộ site
//                    response.addCookie(cartCookie);
                    User loggedUser = (User) session.getAttribute(AttributeConstant.LOGGEDUSER);
                    String username = (loggedUser != null) ? loggedUser.getUsername() : "guest"; // fallback cho khách

                    String cartCookieName = "cart_" + username;

                    Cookie cartCookie = new Cookie(cartCookieName, encodedCart);
                    cartCookie.setMaxAge(30 * 24 * 60 * 60);
                    cartCookie.setPath("/");
                    response.addCookie(cartCookie);

                    // quay về trang trước (nếu có) hoặc /shop
                    String referer = request.getHeader("referer");
                    if (referer != null && !referer.contains("/WEB-INF")) {
                        response.sendRedirect(referer);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/shop");
                    }

                } catch (NumberFormatException e) {
                    session.setAttribute(AttributeConstant.ERROR, "Invalid product ID format");
                    response.sendRedirect(request.getContextPath() + "/shop");
                } catch (SQLException ex) {
                    Logger.getLogger(CartServlet.class.getName()).log(Level.SEVERE, null, ex);
                    session.setAttribute(AttributeConstant.ERROR, "Database error");
                    response.sendRedirect(request.getContextPath() + "/shop");
                }
                break;
            }

            /* ============================== VIEW ============================= */
            case "view": {
                try {
                    @SuppressWarnings("unchecked")
                    Map<String, Integer> cart = (Map<String, Integer>) session.getAttribute(AttributeConstant.CART);
                    if (cart == null) {
                        cart = new HashMap<>();
                    }

                    List<ProductDTO> cartItems = new ArrayList<>();
                    double total = 0;

                    for (Map.Entry<String, Integer> e : cart.entrySet()) {
                        String[] key = e.getKey().split(":");
                        int id = Integer.parseInt(key[0]);
                        String color = key[1];
                        String storage = key[2];
                        ProductDTO p = dao.getProductDetailDTO(id, color, storage);
                        if (p != null && p.getQuantity() > 0) { // Chỉ thêm nếu còn hàng
                            int qty = Math.min(e.getValue(), p.getQuantity()); // Đảm bảo không vượt tồn kho
                            p.setQuantity(qty);
                            p.setSubTotal(p.getPrice() * qty);
                            cartItems.add(p);
                            total += p.getSubTotal();
                        } else {
                            cart.remove(e.getKey()); // Xóa khỏi giỏ nếu hết hàng
                        }
                    }
                    session.setAttribute(AttributeConstant.CART, cart); // Cập nhật giỏ
                    updateCartCount(session, cart);

                    Voucher voucher = (Voucher) session.getAttribute(AttributeConstant.COUPON);
                    double discount = (voucher != null && voucher.getExpiryDate().after(new Date()))
                            ? total * voucher.getDiscountPercent() / 100.0
                            : 0;

                    request.setAttribute(AttributeConstant.CART_ITEMS, cartItems);
                    request.setAttribute(AttributeConstant.TOTAL, total);
                    request.setAttribute(AttributeConstant.DISCOUNT, discount);
                    request.setAttribute(AttributeConstant.FINAL_TOTAL, total - discount);
                    request.getRequestDispatcher(PathConstant.URL_CART).forward(request, response);

                } catch (SQLException ex) {
                    Logger.getLogger(CartServlet.class.getName()).log(Level.SEVERE, null, ex);
                    session.setAttribute(AttributeConstant.ERROR, "Database error");
                    response.sendRedirect(request.getContextPath() + "/shop");
                }
                break;
            }

            /* ============================= UPDATE ============================ */
            case "update": {
                try {
                    // int id = Integer.parseInt(request.getParameter(ParamConstant.ID));

                    String idRaw = request.getParameter(ParamConstant.ID);
                    String color = request.getParameter(ParamConstant.COLOR);
                    String storage = request.getParameter(ParamConstant.STORAGE);
                    String quantityRaw = request.getParameter(ParamConstant.QUANTITY);

                    // int quantity = Integer.parseInt(request.getParameter(ParamConstant.QUANTITY));
                    if (idRaw == null || color == null || storage == null || quantityRaw == null
                            || idRaw.trim().isEmpty() || color.trim().isEmpty()
                            || storage.trim().isEmpty() || quantityRaw.trim().isEmpty()) {
                        session.setAttribute(AttributeConstant.ERROR, "Missing required parameters");
                        response.sendRedirect(request.getContextPath() + "/cart?action=view");
                        return;
                    }

                    int id = Integer.parseInt(idRaw);
                    int quantity = Integer.parseInt(quantityRaw);

                    if (quantity < 0) {
                        session.setAttribute(AttributeConstant.ERROR, "Invalid quantity");
//                        response.sendRedirect(request.getContextPath() + "/cart?action=view");;
                        response.sendRedirect(request.getContextPath() + "/cart?action=view");

                        return;
                    }

                    ProductDTO product = dao.getProductDetailDTO(id, color, storage);
                    if (product == null) {
                        session.setAttribute(AttributeConstant.ERROR, "Product not found");
                        response.sendRedirect(request.getContextPath() + "/cart?action=view");

                        return;
                    }
                    if (quantity > product.getQuantity()) {
                        session.setAttribute(AttributeConstant.ERROR, "Not enough stock for " + product.getProductName());
                        response.sendRedirect(request.getContextPath() + "/cart?action=view");

                        return;
                    }

                    @SuppressWarnings("unchecked")
                    Map<String, Integer> cart = (Map<String, Integer>) session.getAttribute(AttributeConstant.CART);
                    if (cart == null) {
                        cart = new HashMap<>();
                    }

                    String key = id + ":" + color + ":" + storage;
                    if (quantity == 0) {
                        cart.remove(key);
                    } else {
                        cart.put(key, quantity);
                    }

                    session.setAttribute(AttributeConstant.CART, cart);
                    updateCartCount(session, cart);

                    response.sendRedirect(request.getContextPath() + "/cart?action=view");

                } catch (NumberFormatException e) {
                    session.setAttribute(AttributeConstant.ERROR, "Invalid input format");
                    response.sendRedirect(request.getContextPath() + "/cart?action=view");

                } catch (SQLException ex) {
                    Logger.getLogger(CartServlet.class.getName()).log(Level.SEVERE, null, ex);
                    session.setAttribute(AttributeConstant.ERROR, "Database error");
                    response.sendRedirect(request.getContextPath() + "/cart?action=view");

                }
                break;
            }

            /* ============================= REMOVE ============================ */
            case "remove": {
                try {
                    // int id = Integer.parseInt(request.getParameter(ParamConstant.ID));

                    String idRaw = request.getParameter(ParamConstant.ID);
                    String color = request.getParameter(ParamConstant.COLOR);
                    String storage = request.getParameter(ParamConstant.STORAGE);

                    if (idRaw == null || color == null || storage == null
                            || idRaw.trim().isEmpty() || color.trim().isEmpty() || storage.trim().isEmpty()) {
                        session.setAttribute(AttributeConstant.ERROR, "Missing required parameters");
                        response.sendRedirect(request.getContextPath() + "/cart?action=view");
                        return;
                    }

                    int id = Integer.parseInt(idRaw);

                    ProductDTO product = dao.getProductDetailDTO(id, color, storage);
                    if (product == null) {
                        session.setAttribute(AttributeConstant.ERROR, "Product not found");
                        response.sendRedirect(request.getContextPath() + "/cart?action=view");;
                        return;
                    }

                    @SuppressWarnings("unchecked")
                    Map<String, Integer> cart = (Map<String, Integer>) session.getAttribute(AttributeConstant.CART);
                    if (cart == null) {
                        cart = new HashMap<>();
                    }

                    cart.remove(id + ":" + color + ":" + storage);
                    session.setAttribute(AttributeConstant.CART, cart);
                    updateCartCount(session, cart);

                    response.sendRedirect(request.getContextPath() + "/cart?action=view");;

                } catch (NumberFormatException e) {
                    session.setAttribute(AttributeConstant.ERROR, "Invalid input format");
                    response.sendRedirect(request.getContextPath() + "/cart?action=view");;
                } catch (SQLException ex) {
                    Logger.getLogger(CartServlet.class.getName()).log(Level.SEVERE, null, ex);
                    session.setAttribute(AttributeConstant.ERROR, "Database error");
                    response.sendRedirect(request.getContextPath() + "/cart?action=view");;
                }
                break;
            }

            /* =========================== APPLY COUPON ======================== */
            case "applyCoupon": {
                try {
                    String code = request.getParameter(ParamConstant.COUPON);
                    if (code == null || code.trim().isEmpty()) {
                        session.setAttribute(AttributeConstant.COUPON_ERROR, "Coupon code is required");
                        response.sendRedirect(request.getContextPath() + "/cart?action=view");;
                        return;
                    }

                    VoucherDAO voucherDAO = new VoucherDAO();
                    Voucher voucher = voucherDAO.getVoucherByCode(code);

                    if (voucher == null || voucher.getExpiryDate().before(new Date())) {
                        session.setAttribute(AttributeConstant.COUPON_ERROR, "Invalid or expired coupon");
                    } else {
                        session.setAttribute(AttributeConstant.COUPON, voucher);
                        session.setAttribute(AttributeConstant.COUPON_SUCCESS, "Coupon applied successfully");
                    }
                    response.sendRedirect(request.getContextPath() + "/cart?action=view");;

                } catch (SQLException ex) {
                    Logger.getLogger(CartServlet.class.getName()).log(Level.SEVERE, null, ex);
                    session.setAttribute(AttributeConstant.ERROR, "Database error");
                    response.sendRedirect(request.getContextPath() + "/cart?action=view");;
                }
                break;
            }

            /* =========================== INVALID ============================= */
            default:
                session.setAttribute(AttributeConstant.ERROR, "Invalid action");
                response.sendRedirect(request.getContextPath() + "/shop");
        }
    }

    /* ─────────────────────────────── doPost ─────────────────────────────── */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // POST chỉ dùng cho update/remove/applyCoupon → gọi lại doGet
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Handles shopping cart operations";
    }
}
