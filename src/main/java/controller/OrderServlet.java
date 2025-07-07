package controller;

import dao.OrderDAO;
import model.ProductDTO;
import model.User;
import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "OrderServlet", urlPatterns = {"/OrderServlet"})
public class OrderServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        java.io.PrintWriter out = response.getWriter();
        try {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet OrderServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet OrderServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedUser");

        if (user == null || user.getUserRole() != 0) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            OrderDAO orderDAO = new OrderDAO();
            List<ProductDTO> allOrders = orderDAO.getAllOrderHistories();
            request.setAttribute("orders", allOrders);
            request.getRequestDispatcher("/WEB-INF/admin/order_list.jsp").forward(request, response);
        } catch (SQLException ex) {
            ex.printStackTrace();
            request.setAttribute("errorMessage", ex.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedUser");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Connection conn = null;
        try {
            OrderDAO orderDAO = new OrderDAO();
            conn = orderDAO.getConnection();
            conn.setAutoCommit(false);

            // Giả định productId và quantity được gửi từ form (ví dụ: productId=1&quantity=10)
            String idRaw = request.getParameter("productId");
            String quantityRaw = request.getParameter("quantity");

            if (idRaw == null || quantityRaw == null || idRaw.trim().isEmpty() || quantityRaw.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Missing product information");
                request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
                return;
            }

            int productId;
            int quantity;
            try {
                productId = Integer.parseInt(idRaw);
                quantity = Integer.parseInt(quantityRaw);
                if (quantity <= 0) {
                    throw new NumberFormatException("Quantity must be positive");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid product ID or quantity");
                request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
                return;
            }

            // Tạo đơn hàng mới
            String insertOrderSQL = "INSERT INTO Orders (UserID, OrderDate) VALUES (?, ?)";
            PreparedStatement psOrder = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS);
            psOrder.setInt(1, user.getUserID());
            psOrder.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            psOrder.executeUpdate();

            ResultSet generatedKeys = psOrder.getGeneratedKeys();
            int orderId = -1;
            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
            }
            psOrder.close();

            // Chèn chi tiết đơn hàng (lặp cho từng sản phẩm)
            String insertDetailSQL = "INSERT INTO OrderDetails (OrderID, ProductID, Quantity, Price) VALUES (?, ?, ?, ?)";
            PreparedStatement psDetail = conn.prepareStatement(insertDetailSQL);
            ProductDTO product = orderDAO.getProductById(productId); // Sử dụng phương thức mới
            double price = product.getPrice();

            for (int i = 0; i < quantity; i++) {
                psDetail.setInt(1, orderId);
                psDetail.setInt(2, productId);
                psDetail.setInt(3, 1); // Mỗi bản ghi có Quantity = 1
                psDetail.setDouble(4, price);
                psDetail.addBatch();
            }
            psDetail.executeBatch();
            psDetail.close();

            conn.commit();
            response.sendRedirect(request.getContextPath() + "/history/orders");
        } catch (SQLException e) {
            if (conn != null) try {
                conn.rollback();
            } catch (SQLException ex) {
            }
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error processing order: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException e) {
            }
        }
    }

    public String getServletInfo() {
        return "Admin-only servlet to view/manage orders";
    }

    // Giả định phương thức getProductById (cần triển khai trong OrderDAO)
    private ProductDTO getProductById(int productId) throws SQLException {
        // Triển khai lấy thông tin sản phẩm từ cơ sở dữ liệu
        return new ProductDTO(); // Placeholder, cần thay bằng logic thực tế
    }
}
