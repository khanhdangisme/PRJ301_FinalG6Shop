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
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Product;
import model.ProductDTO;
import model.User;

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
                    List<User> customers = dao.getAll();
                    request.setAttribute(AttributeConstant.LIST, customers);
                    request.getRequestDispatcher(PathConstant.URL_ADMIN_CUSTOMERS).forward(request, response);
                } catch (SQLException ex) {
                    Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "product":
                AdminProductDAO productCate = new AdminProductDAO();
                try {
                    // 1. Lấy danh sách category
                    List<Product> cate = productCate.getAllCategory(); // Product chỉ chứa CategoryID + Name
                    request.setAttribute(AttributeConstant.LIST, cate);

                    // 2. Lấy sản phẩm cho từng category
                    Map<Integer, List<ProductDTO>> productsMap = new HashMap<>();
                    for (Product p : cate) {
                        List<ProductDTO> prodList = productCate.getProduct(p.getCategoryID());
                        productsMap.put(p.getCategoryID(), prodList);
                    }

                    // 3. Gửi sang JSP
                    request.setAttribute("productsMap", productsMap);
                    System.out.println("→ list size: " + (cate != null ? cate.size() : "null"));
                    System.out.println("→ map size: " + productsMap.size());
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
                inserted = dao.insertUser(newUser);
            } catch (SQLException ex) {
                Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (inserted) {
                session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.REGISTER_SUCCESSFULLY);
                session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.SUCCESS);
                response.sendRedirect(request.getContextPath() + PathConstant.URL_SERVLET_ADMIN_CUSTOMERS);
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
            } catch (SQLException ex) {
                Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (inserted) {
                session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.UPDATE_STATUS);
                session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.SUCCESS);
                response.sendRedirect(request.getContextPath() + PathConstant.URL_SERVLET_ADMIN_CUSTOMERS);
            } else {
                session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.UPDATE_STATUS_ERROR);
                session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                response.sendRedirect(request.getContextPath() + PathConstant.URL_SERVLET_ADMIN_CUSTOMERS);
            }

        } else if ("create-order".equals(action)) {

            if (user == null) {                         // bảo vệ đăng nhập
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            Connection conn = null;
            try {
                OrderDAO orderDAO = new OrderDAO();
                conn = orderDAO.getConnection();
                conn.setAutoCommit(false);

                /* -------- 1. Lấy & kiểm tra input -------- */
                String idRaw = request.getParameter("productId");
                String qtyRaw = request.getParameter("quantity");
                if (idRaw == null || qtyRaw == null || idRaw.isBlank() || qtyRaw.isBlank()) {
                    request.setAttribute("errorMessage", "Missing product information");
                    request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
                    return;
                }
                int productId, quantity;
                try {
                    productId = Integer.parseInt(idRaw);
                    quantity = Integer.parseInt(qtyRaw);
                    if (quantity <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Invalid product ID or quantity");
                    request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
                    return;
                }

                /* -------- 2. Ghi bảng Orders -------- */
                String sqlOrder = "INSERT INTO Orders (UserID, OrderDate) VALUES (?, ?)";
                PreparedStatement psO = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
                psO.setInt(1, user.getUserID());
                psO.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                psO.executeUpdate();
                ResultSet rsKey = psO.getGeneratedKeys();
                int orderId = rsKey.next() ? rsKey.getInt(1) : -1;
                psO.close();

                /* -------- 3. Ghi bảng OrderDetails -------- */
                ProductDTO product = orderDAO.getProductById(productId);
                double price = product.getPrice();

                String sqlDetail = "INSERT INTO OrderDetails (OrderID, ProductID, Quantity, Price) VALUES (?, ?, ?, ?)";
                PreparedStatement psD = conn.prepareStatement(sqlDetail);
                for (int i = 0; i < quantity; i++) {
                    psD.setInt(1, orderId);
                    psD.setInt(2, productId);
                    psD.setInt(3, 1);
                    psD.setDouble(4, price);
                    psD.addBatch();
                }
                psD.executeBatch();
                psD.close();

                conn.commit();
                response.sendRedirect(request.getContextPath() + "/history/orders");

            } catch (SQLException ex) {
                if (conn != null) try {
                    conn.rollback();
                } catch (SQLException ignore) {
                }
                Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                request.setAttribute("errorMessage", "Error processing order: " + ex.getMessage());
                request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
            } finally {
                if (conn != null) try {
                    conn.close();
                } catch (SQLException ignore) {
                }
            }
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
