package controller;

import constant.AttributeConstant;
import constant.ParamConstant;
import constant.PathConstant;
import dao.ShopProductDAO;
import dao.VoucherDAO;
import db.DBContext;
import model.ProductDTO;
import model.User;
import model.Voucher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CheckoutServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CheckoutServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lưu selectedIds để sau login thì mở lại modal
        String selectedIds = request.getParameter("selectedIds");
        if (selectedIds != null && !selectedIds.isBlank()) {
            request.getSession().setAttribute("coSelIds", selectedIds);
        }

        // Sau login quay lại trang shop
        response.sendRedirect(request.getContextPath() + "/shop");
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(AttributeConstant.LOGGEDUSER);

        if (user == null) {
            session.setAttribute(AttributeConstant.ERROR, "Please login to checkout");
            response.sendRedirect(PathConstant.URL_LOGIN);
            return;
        }

        try {
            String[] rawSel = request.getParameterValues("selectedIds");

            if (rawSel == null || rawSel.length == 0 || (rawSel.length == 1 && rawSel[0].trim().isEmpty())) {
                session.setAttribute(AttributeConstant.ERROR, "No products selected");
                response.sendRedirect(request.getContextPath() + "/cart?action=view");
                return;
            }

            String[] selectedIds = rawSel.length == 1 && rawSel[0].contains(",")
                    ? rawSel[0].split("\\s*,\\s*")
                    : rawSel;

            String couponCode = Optional.ofNullable(request.getParameter(ParamConstant.COUPON))
                    .orElse(request.getParameter("voucherCode"));

            @SuppressWarnings("unchecked")
            Map<String, Integer> sessionCart = (Map<String, Integer>) session.getAttribute(AttributeConstant.CART);
            Map<String, Integer> cart = new HashMap<>();

            String source = request.getParameter("source");
            boolean fromBuyButton = "direct".equals(source)
                    || (selectedIds.length == 1 && (sessionCart == null || !sessionCart.containsKey(selectedIds[0])));

            if (fromBuyButton) {
                cart.put(selectedIds[0], 1);
            } else if (sessionCart != null) {
                for (String key : selectedIds) {
                    if (sessionCart.containsKey(key)) {
                        cart.put(key, sessionCart.get(key));
                    }
                }
            }

            if (!fromBuyButton && cart.isEmpty()) {
                session.setAttribute(AttributeConstant.ERROR, "Cart is empty");
                response.sendRedirect(request.getContextPath() + "/cart?action=view");
                return;
            }

            ShopProductDAO dao = new ShopProductDAO();
            List<ProductDTO> selectedItems = new ArrayList<>();
            double totalPrice = 0;

            for (String id : selectedIds) {
                String[] keys = id.split(":");
                if (keys.length != 3) {
                    session.setAttribute(AttributeConstant.ERROR, "Invalid product format");
                    response.sendRedirect(request.getContextPath() + "/cart?action=view");
                    return;
                }

                int productId = Integer.parseInt(keys[0]);
                String color = keys[1];
                String storage = keys[2];

                ProductDTO product = dao.getProductDetailDTO(productId, color, storage);

                if (product != null) {
                    int qty = cart.getOrDefault(id, 1);
                    if (product.getQuantity() < qty) {
                        session.setAttribute(AttributeConstant.ERROR,
                                "Product " + product.getProductName() + " is out of stock");
                        response.sendRedirect(request.getContextPath() + "/shop");
                        return;
                    }

                    product.setQuantity(qty);
                    product.setSubTotal(product.getPrice() * qty);
                    selectedItems.add(product);
                    totalPrice += product.getSubTotal();
                }
            }

            if (selectedItems.isEmpty()) {
                session.setAttribute(AttributeConstant.ERROR, "Invalid products selected");
                response.sendRedirect(request.getContextPath() + "/cart?action=view");
                return;
            }

            Integer couponId = null;
            double discount = 0;

            if (couponCode != null && !couponCode.trim().isEmpty()) {
                VoucherDAO voucherDAO = new VoucherDAO();
                Voucher voucher = voucherDAO.getVoucherByCode(couponCode);
                if (voucher != null && voucher.getExpiryDate().after(new Date())) {
                    discount = totalPrice * voucher.getDiscountPercent() / 100.0;
                    if (discount > voucher.getMaxDiscount()) {
                        discount = voucher.getMaxDiscount();
                    }
                    couponId = voucher.getId();
                    session.removeAttribute("voucherError");
                } else {
                    session.setAttribute("voucherError", "Invalid or expired coupon");
                }
            } else {
                session.removeAttribute("voucherError");
            }

            double finalTotal = totalPrice - discount;

            String receiverName = request.getParameter("fullname");
            String receiverPhone = request.getParameter("phone");
            String receiverAddress = request.getParameter("address");
            String receiverCity = request.getParameter("city");
            String receiverProvince = request.getParameter("province");
            String receiverEmail = request.getParameter("email");

            if (receiverName == null || receiverPhone == null || receiverAddress == null
                    || receiverCity == null || receiverProvince == null || receiverEmail == null
                    || receiverName.isBlank() || receiverPhone.isBlank()
                    || receiverAddress.isBlank() || receiverCity.isBlank()
                    || receiverProvince.isBlank() || receiverEmail.isBlank()) {
                session.setAttribute(AttributeConstant.ERROR, "Please fill in all required shipping information.");
                response.sendRedirect(request.getContextPath() + "/shop");
                return;
            }

// Regex patterns
            String textPattern = "^[a-zA-Z0-9\\s]+$";
            String phonePattern = "^0\\d{9}$";
            String emailPattern = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";

// Kiểm tra từng trường cụ thể
            if (!receiverName.matches(textPattern)) {
                session.setAttribute(AttributeConstant.ERROR, "Full Name must only contain letters, numbers, and spaces.");
                response.sendRedirect(request.getContextPath() + "/shop");
                return;
            }

            if (!receiverAddress.matches(textPattern)) {
                session.setAttribute(AttributeConstant.ERROR, "Address must only contain letters, numbers, and spaces.");
                response.sendRedirect(request.getContextPath() + "/shop");
                return;
            }

            if (!receiverCity.matches(textPattern)) {
                session.setAttribute(AttributeConstant.ERROR, "City must only contain letters, numbers, and spaces.");
                response.sendRedirect(request.getContextPath() + "/shop");
                return;
            }

            if (!receiverProvince.matches(textPattern)) {
                session.setAttribute(AttributeConstant.ERROR, "Province must only contain letters, numbers, and spaces.");
                response.sendRedirect(request.getContextPath() + "/shop");
                return;
            }

            if (!receiverPhone.matches(phonePattern)) {
                session.setAttribute(AttributeConstant.ERROR, "Phone number must start with 0 and be exactly 10 digits.");
                response.sendRedirect(request.getContextPath() + "/shop");
                return;
            }

            if (!receiverEmail.matches(emailPattern)) {
                session.setAttribute(AttributeConstant.ERROR, "Please enter a valid email address (e.g. example@gmail.com).");
                response.sendRedirect(request.getContextPath() + "/shop");
                return;
            }

            Connection conn = null;
            try {
                conn = DBContext.getConnection();
                conn.setAutoCommit(false);

                // Insert Order
                String insertOrderSQL = "INSERT INTO Orders (UserID, OrderDate, TotalPrice, CouponID, ReceiverName, ReceiverPhone, ReceiverAddress, ReceiverCity, ReceiverProvince, ReceiverPostalCode) VALUES (?, GETDATE(), ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS);
                stmt.setInt(1, user.getUserID());
                stmt.setDouble(2, finalTotal);
                if (couponId != null) {
                    stmt.setInt(3, couponId);
                } else {
                    stmt.setNull(3, Types.INTEGER);
                }
                stmt.setString(4, receiverName);
                stmt.setString(5, receiverPhone);
                stmt.setString(6, receiverAddress);
                stmt.setString(7, receiverCity);
                stmt.setString(8, receiverProvince);
                stmt.setString(9, "000000");
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                int orderId = rs.next() ? rs.getInt(1) : 0;
                rs.close();
                stmt.close();

                // Insert OrderDetails
                String insertDetailSQL = "INSERT INTO OrderDetails (OrderID, ProductID, DetailID, Version, Color, Storage, Quantity, Price) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement detailStmt = conn.prepareStatement(insertDetailSQL);
                for (ProductDTO item : selectedItems) {
                    detailStmt.setInt(1, orderId);
                    detailStmt.setInt(2, item.getProductId());
                    detailStmt.setInt(3, item.getDetailId());
                    detailStmt.setString(4, item.getVersion());
                    detailStmt.setString(5, item.getColor());
                    detailStmt.setString(6, item.getStorage());
                    detailStmt.setInt(7, item.getQuantity());
                    detailStmt.setDouble(8, item.getPrice());
                    detailStmt.addBatch();
                }
                detailStmt.executeBatch();
                detailStmt.close();

                // Update stock
                String updateSQL = "UPDATE %s SET Quantity = Quantity - ? WHERE ProductID = ? AND Color = ? AND Storage = ?";
                for (ProductDTO item : selectedItems) {
                    String table;
                    switch (item.getCategoryId()) {
                        case 1:
                            table = "iPhone_Details";
                            break;
                        case 2:
                            table = "iPad_Details";
                            break;
                        case 3:
                            table = "MacBook_Details";
                            break;
                        default:
                            throw new SQLException("Invalid category");
                    }

                    PreparedStatement stockStmt = conn.prepareStatement(String.format(updateSQL, table));
                    stockStmt.setInt(1, item.getQuantity());
                    stockStmt.setInt(2, item.getProductId());
                    stockStmt.setString(3, item.getColor());
                    stockStmt.setString(4, item.getStorage());
                    stockStmt.executeUpdate();
                    stockStmt.close();
                }

                // Insert status
                String insertStatusSQL = "INSERT INTO OrderStatusHistory (OrderID, Status, ChangedAt) VALUES (?, ?, GETDATE())";
                PreparedStatement statusStmt = conn.prepareStatement(insertStatusSQL);
                statusStmt.setInt(1, orderId);
                statusStmt.setString(2, "Pending");
                statusStmt.executeUpdate();
                statusStmt.close();

                conn.commit();

                // Update cart session
                if (!fromBuyButton && sessionCart != null) {
                    for (String id : selectedIds) {
                        sessionCart.remove(id);
                    }
                    if (sessionCart.isEmpty()) {
                        session.removeAttribute(AttributeConstant.CART);
                        session.removeAttribute(AttributeConstant.CART_COUNT);
                    } else {
                        session.setAttribute(AttributeConstant.CART, sessionCart);
                        updateCartCount(session, sessionCart);
                    }
                }

                session.removeAttribute(AttributeConstant.COUPON);
                session.setAttribute("orderSuccess", true);
                session.removeAttribute("voucherError");

                String back = request.getHeader("referer");
                response.sendRedirect(back != null && !back.trim().isEmpty() ? back : request.getContextPath() + "/shop");

            } catch (SQLException ex) {
                if (conn != null) {
                    conn.rollback();
                }
                Logger.getLogger(CheckoutServlet.class.getName()).log(Level.SEVERE, null, ex);
                session.setAttribute(AttributeConstant.ERROR, "Checkout failed: " + ex.getMessage());
                response.sendRedirect(request.getContextPath() + "/cart?action=view");
            } finally {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(CheckoutServlet.class.getName()).log(Level.SEVERE, null, ex);
            session.setAttribute(AttributeConstant.ERROR, "Unexpected error");
            response.sendRedirect(request.getContextPath() + "/cart?action=view");
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles checkout operations";
    }

    private void updateCartCount(HttpSession session, Map<String, Integer> cart) {
        int total = 0;
        for (Integer v : cart.values()) {
            total += v;
        }
        session.setAttribute(AttributeConstant.CART_COUNT, total);
    }
}
