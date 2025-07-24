package dao;

import db.DBContext;
import model.ProductDTO;
import model.User;
import java.sql.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class OrderDAO extends DBContext {
    public static void main(String[] args) throws SQLException {
        OrderDAO dao = new OrderDAO();
        System.out.println(dao.getOrderHistoryByUser('2'));
    }

    public List<ProductDTO> getOrderHistoryByUser(int userId) throws SQLException {
        List<ProductDTO> list = new ArrayList<>();
        Map<String, ProductDTO> productMap = new LinkedHashMap<>();

        String sql = "SELECT \n"
                + "    o.ID AS OrderID,\n"
                + "    o.OrderDate,\n"
                + "    d.ProductID,\n"
                + "    d.DetailID,\n"
                + "    d.Quantity,\n"
                + "    d.Price,\n"
                + "    o.TotalPrice,\n"
                + "    d.Version,\n"
                + "    d.Color,\n"
                + "    d.Storage,\n"
                + "    p.Name AS ProductName,\n"
                + "    CASE \n"
                + "        WHEN p.CategoryID = 1 THEN ip.ImageURL\n"
                + "        WHEN p.CategoryID = 2 THEN ipad.ImageURL\n"
                + "        WHEN p.CategoryID = 3 THEN mac.ImageURL\n"
                + "    END AS ImageURL,\n"
                + "    p.CategoryID,\n"
                + "    c.Name AS CategoryName,\n"
                + "    (\n"
                + "        SELECT TOP 1 Status \n"
                + "        FROM OrderStatusHistory OSH \n"
                + "        WHERE OSH.OrderID = o.ID \n"
                + "        ORDER BY OSH.ChangedAt DESC\n"
                + "    ) AS OrderStatus\n"
                + "FROM Orders o\n"
                + "JOIN OrderDetails d ON o.ID = d.OrderID\n"
                + "JOIN Products p ON d.ProductID = p.ID\n"
                + "JOIN Categories c ON p.CategoryID = c.ID\n"
                + "LEFT JOIN iPhone_Details ip ON ip.DetailID = d.DetailID AND p.CategoryID = 1\n"
                + "LEFT JOIN iPad_Details ipad ON ipad.DetailID = d.DetailID AND p.CategoryID = 2\n"
                + "LEFT JOIN MacBook_Details mac ON mac.DetailID = d.DetailID AND p.CategoryID = 3\n"
                + "WHERE o.UserID = ?\n"
                + "ORDER BY o.OrderDate DESC, o.ID DESC;";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String key = rs.getInt("OrderID") + "-" + rs.getInt("ProductID") + "-" + rs.getInt("DetailID");
                    ProductDTO dto = productMap.get(key);

                    if (dto == null) {
                        dto = new ProductDTO();
                        dto.setOrderId(rs.getInt("OrderID"));
                        dto.setOrderDate(rs.getTimestamp("OrderDate"));
                        dto.setProductId(rs.getInt("ProductID"));
                        dto.setProductName(rs.getString("ProductName"));
                        dto.setImage(rs.getString("ImageURL"));
                        dto.setCategoryId(rs.getInt("CategoryID"));
                        dto.setCategoryName(rs.getString("CategoryName"));
                        dto.setQuantity(rs.getInt("Quantity"));
                        dto.setPrice(rs.getDouble("Price"));
                        dto.setSubTotal(rs.getDouble("TotalPrice"));
                        dto.setStatus(rs.getString("OrderStatus"));
                        dto.setVersion(rs.getString("Version"));
                        dto.setColor(rs.getString("Color"));
                        dto.setStorage(rs.getString("Storage"));
//                        dto.setSubTotal(dto.getPrice() * dto.getQuantity());

                        productMap.put(key, dto);
                    }
                }
            }
        }

        list.addAll(productMap.values());
        return list;
    }

    public List<ProductDTO> getAllOrderHistories() throws SQLException {
        List<ProductDTO> list = new ArrayList<>();
        Map<String, ProductDTO> orderProductMap = new LinkedHashMap<>();

        String sql = "SELECT o.ID AS OrderID, o.OrderDate, u.Username, d.ProductID, d.Quantity, d.Price, "
                + "p.Name AS ProductName, "
                + "CASE "
                + "    WHEN p.CategoryID = 1 THEN ip.ImageURL "
                + "    WHEN p.CategoryID = 2 THEN ipad.ImageURL "
                + "    WHEN p.CategoryID = 3 THEN mac.ImageURL "
                + "    ELSE NULL "
                + "END AS ImageURL, "
                + "p.CategoryID, c.Name AS CategoryName, "
                + "d.Version, d.Color, d.Storage, "
                + "d.DetailID, "
                + "(SELECT TOP 1 Status FROM OrderStatusHistory OSH WHERE OSH.OrderID = o.ID ORDER BY OSH.ChangedAt DESC) AS OrderStatus "
                + "FROM Orders o "
                + "JOIN Users u ON o.UserID = u.ID "
                + "JOIN OrderDetails d ON o.ID = d.OrderID "
                + "JOIN Products p ON d.ProductID = p.ID "
                + "JOIN Categories c ON p.CategoryID = c.ID "
                + "LEFT JOIN iPhone_Details ip ON ip.DetailID = d.DetailID AND p.CategoryID = 1 "
                + "LEFT JOIN iPad_Details ipad ON ipad.DetailID = d.DetailID AND p.CategoryID = 2 "
                + "LEFT JOIN MacBook_Details mac ON mac.DetailID = d.DetailID AND p.CategoryID = 3 "
                + "ORDER BY o.OrderDate DESC, o.ID DESC";

        try (
                 Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);  ResultSet rs = ps.executeQuery()) {
            if (rs.last()) {
                int rowCount = rs.getRow();
                System.out.println("Số bản ghi lấy được từ getAllOrderHistories: " + rowCount + " at " + new java.util.Date());
                rs.beforeFirst();
            }

            while (rs.next()) {
                String key = rs.getInt("OrderID") + "-" + rs.getInt("ProductID") + "-" + rs.getInt("DetailID");
                ProductDTO dto = orderProductMap.get(key);

                if (dto == null) {
                    dto = new ProductDTO();
                    dto.setOrderId(rs.getInt("OrderID"));
                    dto.setOrderDate(rs.getTimestamp("OrderDate"));
                    dto.setProductId(rs.getInt("ProductID"));
                    dto.setProductName(rs.getString("ProductName"));
                    dto.setImage(rs.getString("ImageURL")); // từ *_Details
                    dto.setCategoryId(rs.getInt("CategoryID"));
                    dto.setCategoryName(rs.getString("CategoryName"));
                    dto.setQuantity(rs.getInt("Quantity"));
                    dto.setPrice(rs.getDouble("Price"));
                    dto.setVersion(rs.getString("Version")); // từ OrderDetails
                    dto.setColor(rs.getString("Color"));
                    dto.setStorage(rs.getString("Storage"));
                    dto.setUsername(rs.getString("Username"));
                    dto.setDetailId(rs.getInt("DetailID"));
                    dto.setSubTotal(dto.getPrice() * dto.getQuantity());
                    dto.setStatus(rs.getString("OrderStatus"));
                    orderProductMap.put(key, dto);
                }
            }
        }

        list.addAll(orderProductMap.values());
        System.out.println("Số bản ghi trả về từ getAllOrderHistories: " + list.size() + " at " + new java.util.Date());
        return list;
    }

    private String getSafeString(ResultSet rs, String columnName) {
        try {
            return rs.getString(columnName);
        } catch (SQLException e) {
            return null;
        }
    }

    public ProductDTO getProductById(int productId, String categoryId, int detailId) throws SQLException {
        ProductDTO dto = new ProductDTO();
        String sql = "";

        switch (categoryId) {
            case "1": // iPhone
                sql = "SELECT p.Name AS ProductName, p.CategoryID, c.Name AS CategoryName, "
                        + "ip.Version, ip.Color, ip.Storage, ip.Price, ip.ImageURL "
                        + "FROM Products p "
                        + "JOIN iPhone_Details ip ON p.ID = ip.ProductID "
                        + "JOIN Categories c ON p.CategoryID = c.ID "
                        + "WHERE ip.DetailID = ?";
                break;
            case "2": // iPad
                sql = "SELECT p.Name AS ProductName, p.CategoryID, c.Name AS CategoryName, "
                        + "ipad.Version, ipad.Color, ipad.Storage, ipad.Price, ipad.ImageURL "
                        + "FROM Products p "
                        + "JOIN iPad_Details ipad ON p.ID = ipad.ProductID "
                        + "JOIN Categories c ON p.CategoryID = c.ID "
                        + "WHERE ipad.DetailID = ?";
                break;
            case "3": // MacBook
                sql = "SELECT p.Name AS ProductName, p.CategoryID, c.Name AS CategoryName, "
                        + "mac.Version, mac.Color, mac.Storage, mac.Price, mac.ImageURL "
                        + "FROM Products p "
                        + "JOIN MacBook_Details mac ON p.ID = mac.ProductID "
                        + "JOIN Categories c ON p.CategoryID = c.ID "
                        + "WHERE mac.DetailID = ?";
                break;
            default:
                return null;
        }

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, detailId);

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    dto.setProductId(productId);
                    dto.setProductName(rs.getString("ProductName"));
                    dto.setCategoryId(rs.getInt("CategoryID"));
                    dto.setCategoryName(rs.getString("CategoryName"));
                    dto.setVersion(rs.getString("Version"));
                    dto.setColor(rs.getString("Color"));
                    dto.setStorage(rs.getString("Storage"));
                    dto.setPrice(rs.getDouble("Price"));
                    dto.setImage(rs.getString("ImageURL")); // để dùng khi hiển thị
                }
            }
        }

        return dto;
    }

    public void markOrderAsReturnRequested(int orderId, int productId, int detailId) throws SQLException {
        String sql = "UPDATE Orders "
                + "SET OrderStatus = 'Return Requested' "
                + "WHERE ID = ? AND ID IN (SELECT OrderID FROM OrderDetails WHERE ProductID = ? AND DetailID = ?)";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ps.setInt(2, productId);
            ps.setInt(3, detailId);

            ps.executeUpdate();
        }
    }

    public boolean updateOrderStatus(int orderId, String status) throws SQLException {
        String sql = "INSERT INTO OrderStatusHistory (OrderID, Status, ChangedAt) VALUES (?, ?, ?)";
        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setString(2, status);
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            return ps.executeUpdate() > 0;
        }
    }

    // ✅ Hàm cập nhật lại số lượng hàng sau khi cancel / return
    public void restoreStockFromOrder(int orderId) throws SQLException {
        String sql = "SELECT d.ProductID, d.DetailID, d.Quantity, p.CategoryID "
                + "FROM OrderDetails d JOIN Products p ON d.ProductID = p.ID "
                + "WHERE d.OrderID = ?";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int productId = rs.getInt("ProductID");
                int detailId = rs.getInt("DetailID");
                int quantity = rs.getInt("Quantity");
                int categoryId = rs.getInt("CategoryID"); // ✅ dùng getInt thay vì getString

                String updateSql = null;
                switch (categoryId) {
                    case 1:
                        updateSql = "UPDATE iPhone_Details SET Quantity = Quantity + ? WHERE DetailID = ?";
                        break;
                    case 2:
                        updateSql = "UPDATE iPad_Details SET Quantity = Quantity + ? WHERE DetailID = ?";
                        break;
                    case 3:
                        updateSql = "UPDATE MacBook_Details SET Quantity = Quantity + ? WHERE DetailID = ?";
                        break;
                }

                if (updateSql != null) {
                    try ( PreparedStatement psUpdate = conn.prepareStatement(updateSql)) {
                        psUpdate.setInt(1, quantity);
                        psUpdate.setInt(2, detailId);
                        psUpdate.executeUpdate();
                    }
                }
            }
        }
    }

}
