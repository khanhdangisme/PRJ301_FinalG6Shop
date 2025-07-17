/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import constant.AttributeConstant;
import constant.MessageConstant;
import constant.ParamConstant;
import constant.PathConstant;
import dao.AdminDAO;
import dao.AdminProductDAO;
import dao.UserDAO;
import dao.OrderDAO;
import dao.ReportDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Product;
import model.ProductDTO;
import model.User;
import ultil.PaginationUtil;

/**
 *
 * @author email
 */
@WebServlet(name = "AdminServlet", urlPatterns = {"/admin"})
public class AdminServlet extends HttpServlet {

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
            out.println("<title>Servlet AdminServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AdminServlet at " + request.getContextPath() + "</h1>");
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
        String view = request.getParameter(ParamConstant.VIEW);

        switch (view) { // thêm 1 case lấy order_history [Admin]
            case "dashboard":
                request.getRequestDispatcher(PathConstant.URL_ADMIN_DASHBOARD).forward(request, response);
                break;
            case "customer":
                AdminDAO dao = new AdminDAO();
                try {
                    // Lấy số trang hiện tại từ request, mặc định là trang 1 nếu không có tham số trang
                    String pageRaw = request.getParameter("page");
                    int page = (pageRaw == null || pageRaw.isEmpty()) ? 1 : Integer.parseInt(pageRaw);

                    // Đặt kích thước trang (số lượng khách hàng mỗi trang)
                    int pageSize = PaginationUtil.NUMBER_OF_ACCOUNT;  // Ví dụ, hiển thị 10 khách hàng mỗi trang

                    // Lấy danh sách khách hàng cho trang hiện tại
                    List<User> customers = dao.getAll(page, pageSize);

                    // Tính tổng số trang
                    int totalCustomers = dao.getTotalCustomers();  // Phương thức này cần phải được tạo trong AdminDAO để đếm tổng số khách hàng
                    int totalPages = (int) Math.ceil((double) totalCustomers / pageSize);

                    // Truyền dữ liệu vào request
                    request.setAttribute(AttributeConstant.LIST, customers);
                    request.setAttribute("totalPages", totalPages);
                    request.setAttribute("currentPage", page);

                    // Chuyển tiếp đến JSP
                    request.getRequestDispatcher(PathConstant.URL_ADMIN_CUSTOMERS).forward(request, response);
                } catch (SQLException ex) {
                    Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "product":
                AdminProductDAO productCate = new AdminProductDAO();
                try {
                    // 1. Lấy danh sách category
                    List<Product> cate = productCate.getAllCategory(); // Lấy danh sách danh mục
                    request.setAttribute(AttributeConstant.LIST, cate);

                    // 2. Lấy thông tin phân trang từ request
                    String pageRaw = request.getParameter("page"); // Lấy số trang từ request
                    int page = (pageRaw == null || pageRaw.isEmpty()) ? 1 : Integer.parseInt(pageRaw); // Trang mặc định là 1
                    int pageSize = PaginationUtil.NUMBER_OF_ITEMS_PAER_PAGE_PRODUCT; // Định nghĩa số sản phẩm mỗi trang

                    // 3. Lấy sản phẩm cho từng category (phân trang)
                    Map<Integer, List<ProductDTO>> productsMap = new HashMap<>();
                    for (Product p : cate) {
                        // Lấy sản phẩm theo phân trang cho từng category
                        List<ProductDTO> prodList = productCate.getProduct(p.getCategoryID(), page, pageSize);
                        productsMap.put(p.getCategoryID(), prodList);
                    }

                    // 4. Tính toán tổng số trang cho mỗi category
                    Map<Integer, Integer> totalPagesMap = new HashMap<>();
                    for (Product p : cate) {
                        // Lấy số lượng sản phẩm theo category
                        int totalProduct = productCate.countProductByCategory(p.getCategoryID());

                        // Tính toán số trang cho mỗi category
                        int totalPages = (int) Math.ceil((double) totalProduct / pageSize);
                        totalPagesMap.put(p.getCategoryID(), totalPages);
                    }

                    // 5. Truyền dữ liệu phân trang vào request
                    request.setAttribute("productsMap", productsMap);
                    request.setAttribute("totalPagesMap", totalPagesMap);
                    request.setAttribute("currentPage", page);

                    // 6. Chuyển tiếp tới JSP
                    request.getRequestDispatcher(PathConstant.URL_ADMIN_PRODUCT).forward(request, response);
                } catch (SQLException ex) {
                    Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

            case "orderlist":       // ← menu Integrations sẽ gọi ?view=integrations
                try {
                OrderDAO orderDAO = new OrderDAO();
                List<ProductDTO> allOrders = orderDAO.getAllOrderHistories();
                request.setAttribute("orders", allOrders);
                request.getRequestDispatcher("/WEB-INF/admin/order_list.jsp")
                        .forward(request, response);
            } catch (SQLException ex) {
                Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                request.setAttribute("errorMessage", ex.getMessage());
                request.getRequestDispatcher("/WEB-INF/view/error.jsp")
                        .forward(request, response);
            }
            break;
            case "reports":
                request.getRequestDispatcher("/WEB-INF/admin/reports.jsp")
                        .forward(request, response);
                break;
        }
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
        String action = request.getParameter(ParamConstant.ACTION);
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("loggedUser"); // KHÔNG cần check null nữa
        UserDAO dao = new UserDAO();
        AdminDAO adminDAO = new AdminDAO();

        // Bắt input, nếu null thì ->
        if ("register".equals(action)) {
            String username = request.getParameter(ParamConstant.USERNAME);
            String password = request.getParameter(ParamConstant.PASSWORD);
            String fullname = request.getParameter(ParamConstant.FULLNAME);
            String email = request.getParameter(ParamConstant.EMAIL);
            String phone = request.getParameter(ParamConstant.PHONE);
            if (username != null && !username.trim().isEmpty()
                    && password != null && !password.trim().isEmpty()
                    && fullname != null && !fullname.trim().isEmpty()
                    && email != null && !email.trim().isEmpty()
                    && phone != null && !phone.trim().isEmpty()) {
                if (!dao.isValidPassword(password)) {
                    session.setAttribute(AttributeConstant.MESSAGE, "Password must start with an uppercase letter and contain at least 1 digit.");
                    session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                    response.sendRedirect(request.getContextPath() + PathConstant.URL_SERVLET_ADMIN_CUSTOMERS);
                    return;
                }
                if (!dao.isValidGmail(email)) {
                    session.setAttribute(AttributeConstant.MESSAGE, "Email must be a valid @gmail.com address.");
                    session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                    response.sendRedirect(request.getContextPath() + PathConstant.URL_SERVLET_ADMIN_CUSTOMERS);
                    return;
                }
                if (!dao.isValidPhone(phone)) {
                    session.setAttribute(AttributeConstant.MESSAGE, "Phone number must start with 0 and have exactly 10 digits.");
                    session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                    response.sendRedirect(request.getContextPath() + PathConstant.URL_SERVLET_ADMIN_CUSTOMERS);
                    return;
                }

                // Kiểm tra người dùng đã tồn tại chưa
                if (dao.checkUserExists(username)) {
                    session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.REGISTER_ERROR_EXISTS);
                    session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                    response.sendRedirect(request.getContextPath() + PathConstant.URL_SERVLET_ADMIN_CUSTOMERS);
                    return;
                }
                User newUser = new User(0, username, password, fullname, email, phone, 0, null, "Enable"); // Role = 1 là customer

                boolean inserted = false;
                try {
                    if (dao.insertUser(newUser)) {
                        session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.REGISTER_SUCCESSFULLY);
                        session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.SUCCESS);
                        response.sendRedirect(request.getContextPath() + PathConstant.URL_SERVLET_ADMIN_CUSTOMERS);
                    } else {
                        session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.REGISTER_ERROR);
                        session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                        response.sendRedirect(request.getContextPath() + PathConstant.URL_SERVLET_ADMIN_CUSTOMERS);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.REGISTER_ERROR);
                session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                response.sendRedirect(request.getContextPath() + PathConstant.URL_SERVLET_ADMIN_CUSTOMERS);
            }

        } else if ("update-status".equals(action)) {
            String username = request.getParameter(ParamConstant.USERNAME);

            boolean inserted = false;
            try {
                inserted = adminDAO.updateStatus(username);
                if (inserted) {
                    User refreshedUser = adminDAO.getUserByUsername(username);
                    session.setAttribute(AttributeConstant.LOGGEDUSER, refreshedUser);

                    session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.UPDATE_STATUS);
                    session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.SUCCESS);
                    response.sendRedirect(request.getContextPath() + PathConstant.URL_SERVLET_ADMIN_CUSTOMERS);
                } else {
                    session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.UPDATE_STATUS_ERROR);
                    session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                    response.sendRedirect(request.getContextPath() + PathConstant.URL_SERVLET_ADMIN_CUSTOMERS);
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if ("create-order".equals(action)) {

            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            Connection conn = null;
            try {
                OrderDAO orderDAO = new OrderDAO();
                conn = orderDAO.getConnection();
                conn.setAutoCommit(false);

                // Lấy và kiểm tra tham số
                String idRaw = request.getParameter("productId");
                String categoryRaw = request.getParameter("categoryId");
                String detailRaw = request.getParameter("detailId");
                String qtyRaw = request.getParameter("quantity");

                if (idRaw == null || categoryRaw == null || detailRaw == null || qtyRaw == null
                        || idRaw.isBlank() || categoryRaw.isBlank() || detailRaw.isBlank() || qtyRaw.isBlank()) {
                    request.setAttribute("errorMessage", "Thiếu thông tin sản phẩm.");
                    request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
                    return;
                }

                int productId = Integer.parseInt(idRaw);
                String categoryId = categoryRaw;
                int detailId = Integer.parseInt(detailRaw);
                int quantity = Integer.parseInt(qtyRaw);

                if (quantity <= 0) {
                    throw new NumberFormatException("Quantity must be > 0");
                }

                // Ghi bảng Orders
                String sqlOrder = "INSERT INTO Orders (UserID, OrderDate) VALUES (?, ?)";
                PreparedStatement psO = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
                psO.setInt(1, user.getUserID());
                psO.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                psO.executeUpdate();

                ResultSet rsKey = psO.getGeneratedKeys();
                int orderId = rsKey.next() ? rsKey.getInt(1) : -1;
                psO.close();

                // Lấy đúng sản phẩm theo biến thể
                ProductDTO product = orderDAO.getProductById(productId, categoryId, detailId);

                // Ghi bảng OrderDetails
                String sqlDetail = "INSERT INTO OrderDetails (OrderID, ProductID, Quantity, Price, Version, Color, Storage) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement psD = conn.prepareStatement(sqlDetail);
                psD.setInt(1, orderId);
                psD.setInt(2, productId);
                psD.setInt(3, quantity);
                psD.setDouble(4, product.getPrice());
                psD.setString(5, product.getVersion());
                psD.setString(6, product.getColor());
                psD.setString(7, product.getStorage());
                psD.executeUpdate();
                psD.close();

                conn.commit();
                response.sendRedirect(request.getContextPath() + "/history/orders");

            } catch (Exception ex) {
                if (conn != null) try {
                    conn.rollback();
                } catch (SQLException ignore) {
                }
                Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                request.setAttribute("errorMessage", "Lỗi khi xử lý đơn hàng: " + ex.getMessage());
                request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
            } finally {
                if (conn != null) try {
                    conn.close();
                } catch (SQLException ignore) {
                }
            }
        } else if ("confirm-order".equals(action)) {
            String orderIdRaw = request.getParameter("orderId");
            if (orderIdRaw == null || orderIdRaw.isBlank()) {
                response.sendRedirect(request.getContextPath() + "/admin?view=orderlist");
                return;
            }

            try {
                int orderId = Integer.parseInt(orderIdRaw);
                OrderDAO orderDAO = new OrderDAO();
                boolean updated = orderDAO.updateOrderStatus(orderId, "Confirmed");

                if (updated) {
                    session.setAttribute(AttributeConstant.MESSAGE, "Order confirmed successfully.");
                    session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.SUCCESS);
                } else {
                    session.setAttribute(AttributeConstant.MESSAGE, "Failed to confirm order.");
                    session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                }

                response.sendRedirect(request.getContextPath() + "/admin?view=orderlist");
            } catch (Exception ex) {
                Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                session.setAttribute(AttributeConstant.MESSAGE, "Error: " + ex.getMessage());
                session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                response.sendRedirect(request.getContextPath() + "/admin?view=orderlist");
            }

        } else if ("ship-order".equals(action)) {
            String orderIdRaw = request.getParameter("orderId");
            try {
                int orderId = Integer.parseInt(orderIdRaw);
                OrderDAO orderDAO = new OrderDAO();
                boolean updated = orderDAO.updateOrderStatus(orderId, "Shipping");

                if (updated) {
                    session.setAttribute(AttributeConstant.MESSAGE, "Order is now Shipping.");
                    session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.SUCCESS);
                } else {
                    session.setAttribute(AttributeConstant.MESSAGE, "Failed to update order to Shipping.");
                    session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                }

                response.sendRedirect(request.getContextPath() + "/admin?view=orderlist");
            } catch (Exception ex) {
                Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                session.setAttribute(AttributeConstant.MESSAGE, "Error: " + ex.getMessage());
                session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                response.sendRedirect(request.getContextPath() + "/admin?view=orderlist");
            }
        } else if ("complete-order".equals(action)) {
            String orderIdRaw = request.getParameter("orderId");
            try {
                int orderId = Integer.parseInt(orderIdRaw);
                OrderDAO orderDAO = new OrderDAO();
                boolean updated = orderDAO.updateOrderStatus(orderId, "Completed");

                if (updated) {
                    session.setAttribute(AttributeConstant.MESSAGE, "Order completed successfully.");
                    session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.SUCCESS);
                } else {
                    session.setAttribute(AttributeConstant.MESSAGE, "Failed to complete order.");
                    session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                }

                response.sendRedirect(request.getContextPath() + "/admin?view=orderlist");
            } catch (Exception ex) {
                Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                session.setAttribute(AttributeConstant.MESSAGE, "Error: " + ex.getMessage());
                session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                response.sendRedirect(request.getContextPath() + "/admin?view=orderlist");
            }
        } else if ("cancel-order-confirm".equals(action)) {
            String orderIdRaw = request.getParameter("orderId");
            try {
                int orderId = Integer.parseInt(orderIdRaw);
                OrderDAO orderDAO = new OrderDAO();
                boolean updated = orderDAO.updateOrderStatus(orderId, "Cancelled");

                if (updated) {
                    orderDAO.restoreStockFromOrder(orderId); // <-- cập nhật tồn kho
                    session.setAttribute(AttributeConstant.MESSAGE, "Order has been cancelled and stock restored.");
                    session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.SUCCESS);
                } else {
                    session.setAttribute(AttributeConstant.MESSAGE, "Failed to cancel order.");
                    session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                }

                response.sendRedirect(request.getContextPath() + "/admin?view=orderlist");
            } catch (Exception ex) {
                Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                session.setAttribute(AttributeConstant.MESSAGE, "Error: " + ex.getMessage());
                session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                response.sendRedirect(request.getContextPath() + "/admin?view=orderlist");
            }
        } else if ("return-order-confirm".equals(action)) {
            String orderIdRaw = request.getParameter("orderId");
            try {
                int orderId = Integer.parseInt(orderIdRaw);
                OrderDAO orderDAO = new OrderDAO();
                boolean updated = orderDAO.updateOrderStatus(orderId, "Returned");

                if (updated) {
                    orderDAO.restoreStockFromOrder(orderId);
                    session.setAttribute(AttributeConstant.MESSAGE, "Order return confirmed.");
                    session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.SUCCESS);
                } else {
                    session.setAttribute(AttributeConstant.MESSAGE, "Failed to update return status.");
                    session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                }

                response.sendRedirect(request.getContextPath() + "/admin?view=orderlist");
            } catch (Exception ex) {
                Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                session.setAttribute(AttributeConstant.MESSAGE, "Error: " + ex.getMessage());
                session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                response.sendRedirect(request.getContextPath() + "/admin?view=orderlist");
            }
        } else if ("revenue".equals(action)) {
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            request.setAttribute("action", "revenue");
            request.setAttribute("startDate", startDateStr); // giữ lại input đã nhập
            request.setAttribute("endDate", endDateStr);
            if (startDateStr != null && endDateStr != null) {
                LocalDate fromDate = LocalDate.parse(startDateStr);
                LocalDate toDate = LocalDate.parse(endDateStr);
                ReportDAO rDao = new ReportDAO();
                try {
                    BigDecimal totalRevenue = rDao.getReport(fromDate, toDate);
                    request.setAttribute("fromDate", Timestamp.valueOf(fromDate.atStartOfDay()));
                    request.setAttribute("toDate", Timestamp.valueOf(toDate.atTime(23, 59, 59)));
                    request.setAttribute("total", totalRevenue);
                } catch (SQLException ex) {
                    Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                request.setAttribute("error", "Please select both start date and end date.");
            }
            request.getRequestDispatcher("/WEB-INF/admin/reports.jsp").forward(request, response);
        } else if ("bestselling".equals(action)) {
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            request.setAttribute("action", "bestselling");
            request.setAttribute("startDate", startDateStr);
            request.setAttribute("endDate", endDateStr);
            if (startDateStr != null && endDateStr != null) {
                LocalDate fromDate = LocalDate.parse(startDateStr);
                LocalDate toDate = LocalDate.parse(endDateStr);
                ReportDAO rDao = new ReportDAO();
                try {
                    // ĐỔI HÀM Ở ĐÂY: lấy bản full info
                    List<ProductDTO> topProducts = rDao.getTop5BestSellingProductsFullInfo(fromDate, toDate);
                    request.setAttribute("topProducts", topProducts);
                    request.setAttribute("fromDate", Timestamp.valueOf(fromDate.atStartOfDay()));
                    request.setAttribute("toDate", Timestamp.valueOf(toDate.atTime(23, 59, 59)));
                } catch (SQLException ex) {
                    Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                request.setAttribute("error", "Please select both start date and end date.");
            }
            request.getRequestDispatcher("/WEB-INF/admin/reports.jsp").forward(request, response);
        }

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
