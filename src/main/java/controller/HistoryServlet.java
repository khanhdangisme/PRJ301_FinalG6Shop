package controller;

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
import java.util.ArrayList;
import java.util.List;
import model.ProductDTO;
import model.User;

@WebServlet(name = "HistoryServlet", urlPatterns = {"/history"})
public class HistoryServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedUser");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            OrderDAO orderDAO = new OrderDAO();
            // User: Xem lịch sử đơn hàng cá nhân
            List<ProductDTO> history = orderDAO.getOrderHistoryByUser(user.getUserID());
            request.setAttribute("orderHistories", history);
            request.getRequestDispatcher("/WEB-INF/view/history.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedUser");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        int orderId = Integer.parseInt(request.getParameter("orderId"));

        OrderDAO dao = new OrderDAO();

        try {
            if ("cancel-order".equals(action)) {
                // Gửi yêu cầu hủy tới admin
                dao.updateOrderStatus(orderId, "Cancel Requested");

            } else if ("request-return".equals(action)) {
                // Gửi yêu cầu hoàn hàng tới admin
                dao.updateOrderStatus(orderId, "Return Requested");
            }

            // Redirect lại trang lịch sử để hiển thị trạng thái mới
            response.sendRedirect(request.getContextPath() + "/history");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
        }
    }

    public String getServletInfo() {
        return "Displays user's order history and supports searching for admin/users";
    }
}
