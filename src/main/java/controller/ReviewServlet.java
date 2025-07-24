package controller;

import dao.ReviewDAO;
import model.Review;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ReviewServlet", urlPatterns = {"/review"})
public class ReviewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productIdRaw = request.getParameter("productId");
        if (productIdRaw == null) {
            Object pidObj = request.getAttribute("productId");
            if (pidObj != null) {
                productIdRaw = pidObj.toString();
            }
        }
        if (productIdRaw == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing productId");
            return;
        }
        int productId = Integer.parseInt(productIdRaw);
        try {
            List<Review> reviews = new ReviewDAO().getReviewsByProductId(productId);
            request.setAttribute("reviews", reviews);
            // Nếu là include, chỉ setAttribute, không forward
            if (request.getAttribute("javax.servlet.include.request_uri") == null) {
                request.getRequestDispatcher("/WEB-INF/view/reviewList.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        try {
            ReviewDAO dao = new ReviewDAO();
            if ("add".equals(action)) {
                int productId = Integer.parseInt(request.getParameter("productId"));
                boolean hasPurchased = dao.hasUserPurchasedProduct(user.getUserID(), productId);

                if (!hasPurchased) {
                    // Gửi lại URL kèm thông báo lỗi
                    String back = request.getHeader("referer");
                    response.sendRedirect(back + (back.contains("?") ? "&" : "?") + "error=notpurchased");
                    return;
                }

                int rating = Integer.parseInt(request.getParameter("rating"));
                String comment = escapeHtml(request.getParameter("comment"));
                Review review = new Review(0, user.getUserID(), productId, rating, comment, null, null);
                dao.addReview(review);
            } else if ("edit".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                int rating = Integer.parseInt(request.getParameter("rating"));
                String comment = escapeHtml(request.getParameter("comment"));
                Review review = dao.getReviewById(id);
                if (review != null && review.getUserId() == user.getUserID()) {
                    review.setRating(rating);
                    review.setComment(comment);
                    dao.updateReview(review);
                }
            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                dao.deleteReview(id, user.getUserID());
            }
            // Quay lại trang chi tiết sản phẩm sau thao tác
            String back = request.getHeader("referer");
            response.sendRedirect(back != null ? back : request.getContextPath() + "/shop");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private String escapeHtml(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }

}
