package dao;

import db.DBContext;
import model.Review;
import java.sql.*;
import java.util.*;

public class ReviewDAO extends DBContext {

    public List<Review> getReviewsByProductId(int productId) throws SQLException {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT r.*, u.FullName FROM Reviews r JOIN Users u ON r.UserID = u.ID WHERE r.ProductID = ? ORDER BY r.ReviewDate DESC";
        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Review r = new Review(
                            rs.getInt("ID"),
                            rs.getInt("UserID"),
                            rs.getInt("ProductID"),
                            rs.getInt("Rating"),
                            rs.getString("Comment"),
                            rs.getTimestamp("ReviewDate"),
                            rs.getString("FullName")
                    );
                    list.add(r);
                }
            }
        }
        return list;
    }

    public Review getReviewById(int id) throws SQLException {
        String sql = "SELECT r.*, u.FullName FROM Reviews r JOIN Users u ON r.UserID = u.ID WHERE r.ID = ?";
        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Review(
                            rs.getInt("ID"),
                            rs.getInt("UserID"),
                            rs.getInt("ProductID"),
                            rs.getInt("Rating"),
                            rs.getString("Comment"),
                            rs.getTimestamp("ReviewDate"),
                            rs.getString("FullName")
                    );
                }
            }
        }
        return null;
    }

    public void addReview(Review review) throws SQLException {
        String sql = "INSERT INTO Reviews (UserID, ProductID, Rating, Comment, ReviewDate) VALUES (?, ?, ?, ?, GETDATE())";
        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, review.getUserId());
            ps.setInt(2, review.getProductId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getComment());
            ps.executeUpdate();
        }
    }

    public void updateReview(Review review) throws SQLException {
        String sql = "UPDATE Reviews SET Rating = ?, Comment = ? WHERE ID = ? AND UserID = ?";
        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, review.getRating());
            ps.setString(2, review.getComment());
            ps.setInt(3, review.getId());
            ps.setInt(4, review.getUserId());
            ps.executeUpdate();
        }
    }

    public void deleteReview(int id, int userId) throws SQLException {
        String sql = "DELETE FROM Reviews WHERE ID = ? AND UserID = ?";
        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    public boolean hasUserPurchasedProduct(int userId, int ProductID) throws SQLException {
        String sql = " SELECT COUNT(*) \n"
                + "        FROM Orders o \n"
                + "        JOIN OrderDetails d ON o.ID = d.ID \n"
                + "        WHERE o.UserID = ? AND d.ProductID = ?";
        try ( Connection con = DBContext.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, ProductID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

}
