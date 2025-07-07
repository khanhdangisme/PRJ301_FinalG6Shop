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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
//            String[] selectedIds = request.getParameterValues("selectedIds");
            String[] rawSel = request.getParameterValues("selectedIds");

            if (rawSel == null || rawSel.length == 0
                    || // không có gì
                    (rawSel.length == 1 && rawSel[0].isBlank())) {
                session.setAttribute(AttributeConstant.ERROR, "No products selected");
                response.sendRedirect(request.getContextPath() + "/cart?action=view");
                return;
            }

            String[] selectedIds;
            if (rawSel.length == 1 && rawSel[0].contains(",")) {
                selectedIds = rawSel[0].split("\\s*,\\s*");
            } else {
                selectedIds = rawSel;
            }

            String couponCode = request.getParameter(ParamConstant.COUPON);

            if (couponCode == null || couponCode.isBlank()) {
                couponCode = request.getParameter("voucherCode"); // dùng cho Buy Now
            }

            if (selectedIds == null || selectedIds.length == 0) {
                session.setAttribute(AttributeConstant.ERROR, "No products selected");
                response.sendRedirect(request.getContextPath() + "/cart?action=view");
                return;
            }

            // Lấy giỏ hàng từ session
            @SuppressWarnings("unchecked")
            Map<String, Integer> sessionCart = (Map<String, Integer>) session.getAttribute(AttributeConstant.CART);
            Map<String, Integer> cart = new HashMap<String, Integer>();

            String source = request.getParameter("source");

            boolean fromBuyButton = "direct".equals(source)
                    || (selectedIds.length == 1 && (sessionCart == null
                    || !sessionCart.containsKey(selectedIds[0])));
//            boolean fromBuyButton = "direct".equals(source);

            if (fromBuyButton) {
                cart.put(selectedIds[0], 1);
            } else {
                // Tạo bản sao chỉ chứa các sản phẩm được chọn từ giỏ session
                if (sessionCart != null) {
                    for (int i = 0; i < selectedIds.length; i++) {
                        String key = selectedIds[i];
                        if (sessionCart.containsKey(key)) {
                            cart.put(key, sessionCart.get(key));
                        }
                    }
                }
            }

            // Nếu không phải Buy trực tiếp và cart rỗng ⇒ báo lỗi
            if (!fromBuyButton && (cart == null || cart.isEmpty())) {
                session.setAttribute(AttributeConstant.ERROR, "Cart is empty");
                response.sendRedirect(request.getContextPath() + "/cart?action=view");
                return;
            }

            // Tạo danh sách sản phẩm được chọn
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
                int productId;
                try {
                    productId = Integer.parseInt(keys[0]);
                } catch (NumberFormatException e) {
                    session.setAttribute(AttributeConstant.ERROR, "Invalid product ID format");
                    response.sendRedirect(request.getContextPath() + "/cart?action=view");
                    return;
                }

                String color = keys[1];
                String storage = keys[2];
                ProductDTO product = dao.getProductDetailDTO(productId, color, storage);

                if (product != null) {
                    int requestedQuantity = (cart != null && cart.containsKey(id))
                            ? cart.get(id) // đặt từ Cart
                            : 1;               // bấm Buy trực tiếp
                    if (product.getQuantity() < requestedQuantity) {
                        session.setAttribute(AttributeConstant.ERROR,
                                "Product " + product.getProductName() + " is out of stock");
                        response.sendRedirect(request.getContextPath() + "/shop");
                        return;
                    }
                    product.setQuantity(requestedQuantity);
                    product.setSubTotal(product.getPrice() * requestedQuantity);
                    selectedItems.add(product);
                    totalPrice += product.getSubTotal();
                }
            }

            if (selectedItems.isEmpty()) {
                session.setAttribute(AttributeConstant.ERROR, "Invalid products selected");
                response.sendRedirect(request.getContextPath() + "/cart?action=view");
                return;
            }

            // Kiểm tra và áp dụng mã giảm giá
            Integer couponId = null;
            double discount = 0;

            if (couponCode != null && !couponCode.trim().isEmpty()) {
                VoucherDAO voucherDAO = new VoucherDAO();
                Voucher voucher = voucherDAO.getVoucherByCode(couponCode);
                if (voucher != null && voucher.getExpiryDate().after(new Date())) {
                    discount = totalPrice * voucher.getDiscountPercent() / 100.0;
                    couponId = voucher.getId();

                    // Nếu hợp lệ thì xóa lỗi cũ nếu có
                    session.removeAttribute("voucherError");
                } else {
                    session.setAttribute("voucherError", "Invalid or expired coupon");
                }
            } else {
                // Nếu không nhập gì, cũng xóa lỗi cũ (tránh hiện modal hoài)
                session.removeAttribute("voucherError");
            }

            double finalTotal = totalPrice - discount;

            // Lấy thông tin người nhận từ User
            String receiverName = request.getParameter("fullname");
            String receiverPhone = request.getParameter("phone");
            String receiverAddress = request.getParameter("address");
            String receiverCity = request.getParameter("city");
            String receiverProvince = request.getParameter("province");
            String receiverPostalCode = "000000"; // Có thể mở rộng thêm

            if (receiverName == null || receiverName.trim().isEmpty()
                    || receiverPhone == null || receiverPhone.trim().isEmpty()
                    || receiverAddress == null || receiverAddress.trim().isEmpty()
                    || receiverCity == null || receiverCity.trim().isEmpty()
                    || receiverProvince == null || receiverProvince.trim().isEmpty()) {
                session.setAttribute(AttributeConstant.ERROR, "Please fill in all required shipping information.");
                response.sendRedirect(request.getContextPath() + "/shop");
                return;
            }

            // Lưu đơn hàng vào cơ sở dữ liệu
            Connection conn = DBContext.getConnection();
            conn.setAutoCommit(false);
            try {
                // Thêm đơn hàng vào bảng Orders
                String insertOrderSQL = "INSERT INTO Orders (UserID, OrderDate, TotalPrice, CouponID, ReceiverName, ReceiverPhone, ReceiverAddress, ReceiverCity, ReceiverProvince, ReceiverPostalCode) VALUES (?, GETDATE(), ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS);
                try {
                    stmt.setInt(1, user.getUserID());
                    stmt.setDouble(2, finalTotal);
                    if (couponId != null) {
                        stmt.setInt(3, couponId);
                    } else {
                        stmt.setNull(3, java.sql.Types.INTEGER);
                    }
                    stmt.setString(4, receiverName);
                    stmt.setString(5, receiverPhone);
                    stmt.setString(6, receiverAddress);
                    stmt.setString(7, receiverCity);
                    stmt.setString(8, receiverProvince);
                    stmt.setString(9, receiverPostalCode);
                    stmt.executeUpdate();

                    ResultSet rs = stmt.getGeneratedKeys();
                    int orderId = 0;
                    if (rs.next()) {
                        orderId = rs.getInt(1);
                    }
                    rs.close();
                    stmt.close();

                    // Thêm chi tiết đơn hàng vào bảng OrderDetails
                    String insertOrderDetailSQL = "INSERT INTO OrderDetails (OrderID, ProductID, Quantity, Price) VALUES (?, ?, ?, ?)";
                    PreparedStatement detailStmt = conn.prepareStatement(insertOrderDetailSQL);
                    try {
                        for (ProductDTO item : selectedItems) {
                            detailStmt.setInt(1, orderId);
                            detailStmt.setInt(2, item.getProductId());
                            detailStmt.setInt(3, item.getQuantity());
                            detailStmt.setDouble(4, item.getPrice());
                            detailStmt.addBatch();
                        }
                        detailStmt.executeBatch();
                    } catch (SQLException ex) {
                        throw ex;
                    } finally {
                        detailStmt.close();
                    }

                    // Cập nhật số lượng tồn kho
                    String updateStockSQL = "UPDATE %s SET Quantity = Quantity - ? WHERE ProductID = ? AND Color = ? AND Storage = ?";
                    for (ProductDTO item : selectedItems) {
                        String tableName;
                        switch (item.getCategoryId()) {
                            case 1:
                                tableName = "iPhone_Details";
                                break;
                            case 2:
                                tableName = "iPad_Details";
                                break;
                            case 3:
                                tableName = "MacBook_Details";
                                break;
                            default:
                                throw new SQLException("Invalid category");
                        }
                        PreparedStatement stockStmt = conn.prepareStatement(String.format(updateStockSQL, tableName));
                        try {
                            stockStmt.setInt(1, item.getQuantity());
                            stockStmt.setInt(2, item.getProductId());
                            stockStmt.setString(3, item.getColor());
                            stockStmt.setString(4, item.getStorage());
                            int rows = stockStmt.executeUpdate();
                            if (rows == 0) {
                                throw new SQLException("Product not found in stock");
                            }
                        } catch (SQLException ex) {
                            throw ex;
                        } finally {
                            stockStmt.close();
                        }
                    }

                    // Thêm trạng thái "Pending" vào OrderStatusHistory
                    String insertStatusSQL = "INSERT INTO OrderStatusHistory (OrderID, Status, ChangedAt) VALUES (?, ?, GETDATE())";
                    PreparedStatement statusStmt = conn.prepareStatement(insertStatusSQL);
                    try {
                        statusStmt.setInt(1, orderId);
                        statusStmt.setString(2, "Pending");
                        statusStmt.executeUpdate();
                    } catch (SQLException ex) {
                        throw ex;
                    } finally {
                        statusStmt.close();
                    }

                    conn.commit();

                    // Xóa giỏ hàng trong session
                    if (!fromBuyButton) {
                        for (String id : selectedIds) {
                            sessionCart.remove(id); // Xóa từng mục đã chọn
                        }
                        if (sessionCart.isEmpty()) {
                            session.removeAttribute(AttributeConstant.CART);
                            session.removeAttribute(AttributeConstant.CART_COUNT);
                        } else {
                            session.setAttribute(AttributeConstant.CART, sessionCart);
                            updateCartCount(session, sessionCart); // Cập nhật số lượng
                        }
                    }

                    session.removeAttribute(AttributeConstant.COUPON);

                    // session.setAttribute(AttributeConstant.SUCCESS, "Order placed successfully");
                    // response.sendRedirect(PathConstant.URL_ORDER_CONFIRMATION);
                    session.setAttribute("orderSuccess", true);
                    session.removeAttribute("voucherError");

                    String back = request.getHeader("referer");
                    if (back == null || back.isBlank()) {
                        back = request.getContextPath() + "/shop";
                    }
                    response.sendRedirect(back);
                    return;

                } catch (SQLException ex) {
                    throw ex;
                }
            } catch (SQLException ex) {
                conn.rollback();
                Logger.getLogger(CheckoutServlet.class.getName()).log(Level.SEVERE, null, ex);
                session.setAttribute(AttributeConstant.ERROR, "Checkout failed: " + ex.getMessage());
                response.sendRedirect(request.getContextPath() + "/cart?action=view");
            } finally {
                conn.setAutoCommit(true);
                conn.close();
            }

        } catch (SQLException ex) {
            Logger.getLogger(CheckoutServlet.class.getName()).log(Level.SEVERE, null, ex);
            session.setAttribute(AttributeConstant.ERROR, "Database error");
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
