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

        switch (view) {
            case "dashboard":
                request.getRequestDispatcher(PathConstant.URL_ADMIN_DASHBOARD).forward(request, response);
                break;
            case "customer":
                AdminDAO dao = new AdminDAO();
                try {
                    String pageRaw = request.getParameter("page");
                    int page = (pageRaw == null || pageRaw.isEmpty()) ? 1 : Integer.parseInt(pageRaw);
                    int pageSize = PaginationUtil.NUMBER_OF_ACCOUNT;

                    List<User> customers = dao.getAll(page, pageSize);
                    int totalCustomers = dao.getTotalCustomers();
                    int totalPages = (int) Math.ceil((double) totalCustomers / pageSize);

                    request.setAttribute(AttributeConstant.LIST, customers);
                    request.setAttribute("totalPages", totalPages);
                    request.setAttribute("currentPage", page);

                    request.getRequestDispatcher(PathConstant.URL_ADMIN_CUSTOMERS).forward(request, response);
                } catch (SQLException ex) {
                    Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "product":
                AdminProductDAO productCate = new AdminProductDAO();
                try {
                    List<Product> cate = productCate.getAllCategory();
                    request.setAttribute(AttributeConstant.LIST, cate);

                    String pageRaw = request.getParameter("page");
                    int page = (pageRaw == null || pageRaw.isEmpty()) ? 1 : Integer.parseInt(pageRaw);
                    int pageSize = PaginationUtil.NUMBER_OF_ITEMS_PAER_PAGE_PRODUCT;

                    Map<Integer, List<ProductDTO>> productsMap = new HashMap<>();
                    for (Product p : cate) {
                        List<ProductDTO> prodList = productCate.getProduct(p.getCategoryID(), page, pageSize);
                        productsMap.put(p.getCategoryID(), prodList);
                    }

                    Map<Integer, Integer> totalPagesMap = new HashMap<>();
                    for (Product p : cate) {
                        int totalProduct = productCate.countProductByCategory(p.getCategoryID());
                        int totalPages = (int) Math.ceil((double) totalProduct / pageSize);
                        totalPagesMap.put(p.getCategoryID(), totalPages);
                    }

                    request.setAttribute("productsMap", productsMap);
                    request.setAttribute("totalPagesMap", totalPagesMap);
                    request.setAttribute("currentPage", page);

                    request.getRequestDispatcher(PathConstant.URL_ADMIN_PRODUCT).forward(request, response);
                } catch (SQLException ex) {
                    Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

            case "orderlist":
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
            case "voucher": {
                try {
                    request.setAttribute("vouchers", new dao.VoucherDAO().getAllVouchers());
                } catch (SQLException ex) {
                    Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            request.getRequestDispatcher("/WEB-INF/admin/voucherList.jsp").forward(request, response);
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
        User user = (User) session.getAttribute("loggedUser");
        UserDAO dao = new UserDAO();
        AdminDAO adminDAO = new AdminDAO();

        if ("register".equals(action)) {
            String username = request.getParameter(ParamConstant.USERNAME);
            String password = request.getParameter(ParamConstant.PASSWORD);
            String fullname = request.getParameter(ParamConstant.FULLNAME);
            String email = request.getParameter(ParamConstant.EMAIL);
            String phone = request.getParameter(ParamConstant.PHONE);

            request.setAttribute(AttributeConstant.USERNAME, username);
            request.setAttribute(AttributeConstant.FULLNAME, fullname);
            request.setAttribute(AttributeConstant.EMAIL, email);
            request.setAttribute(AttributeConstant.PHONE, phone);

            if (dao.containsScript(username) || dao.containsScript(fullname)
                    || dao.containsScript(email) || dao.containsScript(phone)) {
                session.setAttribute(AttributeConstant.MESSAGE, "Input contains unsafe characters or scripts.");
                session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                request.getRequestDispatcher(PathConstant.URL_ADMIN_CUSTOMERS).forward(request, response);
                return;
            }

            if (username != null && !username.trim().isEmpty()
                    && password != null && !password.trim().isEmpty()
                    && fullname != null && !fullname.trim().isEmpty()
                    && email != null && !email.trim().isEmpty()
                    && phone != null && !phone.trim().isEmpty()) {

                if (!dao.isValidPassword(password)) {
                    session.setAttribute(AttributeConstant.MESSAGE, "Password must start with an uppercase letter and contain at least 1 digit.");
                    session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                    request.getRequestDispatcher(PathConstant.URL_ADMIN_CUSTOMERS).forward(request, response);
                    return;
                }

                if (!dao.isValidGmail(email)) {
                    session.setAttribute(AttributeConstant.MESSAGE, "Email must be a valid @gmail.com address.");
                    session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                    request.getRequestDispatcher(PathConstant.URL_ADMIN_CUSTOMERS).forward(request, response);
                    return;
                }

                if (!dao.isValidPhone(phone)) {
                    session.setAttribute(AttributeConstant.MESSAGE, "Phone number must start with 0 and have exactly 10 digits.");
                    session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                    request.getRequestDispatcher(PathConstant.URL_ADMIN_CUSTOMERS).forward(request, response);
                    return;
                }

                try {
                    if (dao.checkUserExists(username)) {
                        session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.REGISTER_ERROR_EXISTS);
                        session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                        request.getRequestDispatcher(PathConstant.URL_ADMIN_CUSTOMERS).forward(request, response);
                        return;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                    session.setAttribute(AttributeConstant.MESSAGE, "Database error while checking username.");
                    session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                    request.getRequestDispatcher(PathConstant.URL_ADMIN_CUSTOMERS).forward(request, response);
                    return;
                }

                try {
                    if (dao.checkEmailExists(email)) {
                        session.setAttribute(AttributeConstant.MESSAGE, "Email is already registered.");
                        session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                        request.getRequestDispatcher(PathConstant.URL_ADMIN_CUSTOMERS).forward(request, response);
                        return;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                    session.setAttribute(AttributeConstant.MESSAGE, "Database error while checking email.");
                    session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                    request.getRequestDispatcher(PathConstant.URL_ADMIN_CUSTOMERS).forward(request, response);
                    return;
                }

                try {
                    if (dao.checkPhoneExists(phone)) {
                        session.setAttribute(AttributeConstant.MESSAGE, "Phone number is already registered.");
                        session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                        request.getRequestDispatcher(PathConstant.URL_ADMIN_CUSTOMERS).forward(request, response);
                        return;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                    session.setAttribute(AttributeConstant.MESSAGE, "Database error while checking phone number.");
                    session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                    request.getRequestDispatcher(PathConstant.URL_ADMIN_CUSTOMERS).forward(request, response);
                    return;
                }

                User newUser = new User(0, username, password, fullname, email, phone, 1, null, "Enable");

                try {
                    if (dao.insertUser(newUser)) {
                        session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.REGISTER_SUCCESSFULLY);
                        session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.SUCCESS);
                        response.sendRedirect(request.getContextPath() + PathConstant.URL_SERVLET_ADMIN_CUSTOMERS);
                    } else {
                        session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.REGISTER_ERROR);
                        session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                        request.getRequestDispatcher(PathConstant.URL_ADMIN_CUSTOMERS).forward(request, response);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                    session.setAttribute(AttributeConstant.MESSAGE, "Database error during registration.");
                    session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                    request.getRequestDispatcher(PathConstant.URL_ADMIN_CUSTOMERS).forward(request, response);
                }
            } else {
                session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.REGISTER_ERROR);
                session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                request.getRequestDispatcher(PathConstant.URL_ADMIN_CUSTOMERS).forward(request, response);
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

                String sqlOrder = "INSERT INTO Orders (UserID, OrderDate) VALUES (?, ?)";
                PreparedStatement psO = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
                psO.setInt(1, user.getUserID());
                psO.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                psO.executeUpdate();

                ResultSet rsKey = psO.getGeneratedKeys();
                int orderId = rsKey.next() ? rsKey.getInt(1) : -1;
                psO.close();

                ProductDTO product = orderDAO.getProductById(productId, categoryId, detailId);

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
                    orderDAO.restoreStockFromOrder(orderId);
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
            request.setAttribute("startDate", startDateStr);
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
