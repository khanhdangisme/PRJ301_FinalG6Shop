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

//    public List<ProductDTO> getOrderHistoryByUser(int userId) throws SQLException {
//        List<ProductDTO> list = new ArrayList<>();
//        Map<Integer, ProductDTO> productMap = new HashMap<>(); // Sử dụng Map để tránh trùng lặp ProductID
//
//        String sql = "SELECT o.ID AS OrderID, o.OrderDate, "
//                + "       d.ProductID, d.Quantity, d.Price, "
//                + "       p.Name AS ProductName, p.MainImage, p.CategoryID, "
//                + "       c.Name AS CategoryName, "
//                + "       ip.Version AS iPhoneVersion, ip.Color AS iPhoneColor, ip.Storage AS iPhoneStorage, "
//                + "       ipad.Version AS iPadVersion, ipad.Color AS iPadColor, ipad.Storage AS iPadStorage, "
//                + "       mac.Version AS MacVersion, mac.Color AS MacColor, mac.Storage AS MacStorage "
//                + "FROM Orders o "
//                + "JOIN OrderDetails d ON o.ID = d.OrderID "
//                + "JOIN Products p ON d.ProductID = p.ID "
//                + "JOIN Categories c ON p.CategoryID = c.ID "
//                + "LEFT JOIN iPhone_Details ip ON d.ProductID = ip.ProductID "
//                + "LEFT JOIN iPad_Details ipad ON d.ProductID = ipad.ProductID "
//                + "LEFT JOIN MacBook_Details mac ON d.ProductID = mac.ProductID "
//                + "WHERE o.UserID = ? "
//                + "ORDER BY o.OrderDate DESC, o.ID DESC";
//
//        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, userId);
//            try ( ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    int productId = rs.getInt("ProductID");
//                    ProductDTO dto = productMap.get(productId);
//
//                    if (dto == null) {
//                        dto = new ProductDTO();
//                        dto.setOrderDate(rs.getTimestamp("OrderDate"));
//                        dto.setProductId(productId);
//                        dto.setProductName(rs.getString("ProductName"));
//                        dto.setImage(rs.getString("MainImage"));
//                        dto.setCategoryId(rs.getInt("CategoryID"));
//                        dto.setCategoryName(rs.getString("CategoryName"));
//                        dto.setQuantity(rs.getInt("Quantity"));
//                        dto.setPrice(rs.getDouble("Price"));
//                        productMap.put(productId, dto);
//                    }
//
//                    // Chỉ cập nhật version, color, storage nếu chưa có hoặc ưu tiên bản ghi đầu tiên
//                    if (dto.getVersion() == null) {
//                        String version = getSafeString(rs, "iPhoneVersion");
//                        if (version == null) {
//                            version = getSafeString(rs, "iPadVersion");
//                        }
//                        if (version == null) {
//                            version = getSafeString(rs, "MacVersion");
//                        }
//                        dto.setVersion(version);
//                    }
//                    if (dto.getColor() == null) {
//                        String color = getSafeString(rs, "iPhoneColor");
//                        if (color == null) {
//                            color = getSafeString(rs, "iPadColor");
//                        }
//                        if (color == null) {
//                            color = getSafeString(rs, "MacColor");
//                        }
//                        dto.setColor(color);
//                    }
//                    if (dto.getStorage() == null) {
//                        String storage = getSafeString(rs, "iPhoneStorage");
//                        if (storage == null) {
//                            storage = getSafeString(rs, "iPadStorage");
//                        }
//                        if (storage == null) {
//                            storage = getSafeString(rs, "MacStorage");
//                        }
//                        dto.setStorage(storage);
//                    }
//
//                    dto.setSubTotal(dto.getPrice() * dto.getQuantity());
//                }
//            }
//        }
//
//        list.addAll(productMap.values());
//        return list;
//    }
    public List<ProductDTO> getOrderHistoryByUser(int userId) throws SQLException {
    List<ProductDTO> list = new ArrayList<>();

    // ➊ LinkedHashMap giữ nguyên thứ tự INSERT (SQL đã ORDER BY DESC)
    Map<String, ProductDTO> productMap = new LinkedHashMap<>();

    String sql = "SELECT o.ID AS OrderID, o.OrderDate, "
            + "       d.ProductID, d.Quantity, d.Price, "
            + "       p.Name AS ProductName, "
            + "       COALESCE(ip.ImageURL, ipad.ImageURL, mac.ImageURL) AS ImageURL, "
            + "       p.CategoryID, "
            + "       c.Name AS CategoryName, "
            + "       ip.Version  AS iPhoneVersion, ip.Color  AS iPhoneColor, ip.Storage  AS iPhoneStorage, "
            + "       ipad.Version AS iPadVersion, ipad.Color AS iPadColor, ipad.Storage AS iPadStorage, "
            + "       mac.Version AS MacVersion, mac.Color AS MacColor, mac.Storage AS MacStorage, "
            + "       (SELECT TOP 1 Status FROM OrderStatusHistory OSH WHERE OSH.OrderID = o.ID ORDER BY OSH.ChangedAt DESC) AS OrderStatus "
            + "FROM Orders o "
            + "JOIN OrderDetails d ON o.ID = d.OrderID "
            + "JOIN Products p ON d.ProductID = p.ID "
            + "JOIN Categories c ON p.CategoryID = c.ID "
            + "LEFT JOIN iPhone_Details ip ON d.ProductID = ip.ProductID "
            + "LEFT JOIN iPad_Details ipad ON d.ProductID = ipad.ProductID "
            + "LEFT JOIN MacBook_Details mac ON d.ProductID = mac.ProductID "
            + "WHERE o.UserID = ? "
            + "ORDER BY o.OrderDate DESC, o.ID DESC";

    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, userId);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // ➋ Key duy nhất cho mỗi chi tiết đơn hàng (OrderID‑ProductID)
                String key = rs.getInt("OrderID") + "-" + rs.getInt("ProductID");
                ProductDTO dto = productMap.get(key);

                if (dto == null) {
                    dto = new ProductDTO();
                    dto.setOrderDate(rs.getTimestamp("OrderDate"));
                    dto.setProductId(rs.getInt("ProductID"));
                    dto.setProductName(rs.getString("ProductName"));
                    dto.setImage(rs.getString("ImageURL"));  // Lấy ImageURL từ chi tiết sản phẩm
                    dto.setCategoryId(rs.getInt("CategoryID"));
                    dto.setCategoryName(rs.getString("CategoryName"));
                    dto.setQuantity(rs.getInt("Quantity"));
                    dto.setPrice(rs.getDouble("Price"));
                    dto.setStatus(rs.getString("OrderStatus"));  // Lưu trạng thái đơn hàng
                    productMap.put(key, dto);
                }

                // Cập nhật version / color / storage chỉ 1 lần
                if (dto.getVersion() == null) {
                    dto.setVersion(firstNonNull(rs, "iPhoneVersion", "iPadVersion", "MacVersion"));
                }
                if (dto.getColor() == null) {
                    dto.setColor(firstNonNull(rs, "iPhoneColor", "iPadColor", "MacColor"));
                }
                if (dto.getStorage() == null) {
                    dto.setStorage(firstNonNull(rs, "iPhoneStorage", "iPadStorage", "MacStorage"));
                }

                dto.setSubTotal(dto.getPrice() * dto.getQuantity());
            }
        }
    }

    // ➌ LinkedHashMap → giữ nguyên thứ tự DESC khi addAll
    list.addAll(productMap.values());
    return list;
}



    /* Helper ngắn gọn lấy cột đầu tiên khác null */
    private String firstNonNull(ResultSet rs, String... cols) throws SQLException {
        for (String col : cols) {
            String v = rs.getString(col);
            if (v != null) {
                return v;
            }
        }
        return null;
    }

    public List<ProductDTO> getAllOrderHistories() throws SQLException {
        List<ProductDTO> list = new ArrayList<>();
        Map<String, ProductDTO> orderProductMap = new LinkedHashMap<>();

        String sql = "SELECT o.ID AS OrderID, o.OrderDate, u.Username, d.ProductID, d.Quantity, d.Price, "
                + "p.Name AS ProductName, p.MainImage, p.CategoryID, c.Name AS CategoryName, "
                + "COALESCE(ip.Version, ipad.Version, mac.Version) AS Version, "
                + "COALESCE(ip.Color, ipad.Color, mac.Color) AS Color, "
                + "COALESCE(ip.Storage, ipad.Storage, mac.Storage) AS Storage, "
                + "d.ID AS DetailID "
                + "FROM Orders o "
                + "JOIN Users u ON o.UserID = u.ID "
                + "JOIN OrderDetails d ON o.ID = d.OrderID "
                + "JOIN Products p ON d.ProductID = p.ID "
                + "JOIN Categories c ON p.CategoryID = c.ID "
                + "LEFT JOIN iPhone_Details ip ON d.ProductID = ip.ProductID "
                + "LEFT JOIN iPad_Details ipad ON d.ProductID = ipad.ProductID "
                + "LEFT JOIN MacBook_Details mac ON d.ProductID = mac.ProductID "
                + "ORDER BY o.OrderDate DESC, o.ID DESC";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            conn.setAutoCommit(true); // Đảm bảo commit tự động
            ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();

            if (rs.last()) {
                int rowCount = rs.getRow();
                System.out.println("Số bản ghi lấy được từ getAllOrderHistories: " + rowCount + " at " + new java.util.Date());
                rs.beforeFirst();
            }

            while (rs.next()) {
                String key = rs.getInt("OrderID") + "-" + rs.getInt("ProductID");
                ProductDTO dto = orderProductMap.get(key);

                if (dto == null) {
                    dto = new ProductDTO();
                    dto.setOrderDate(rs.getTimestamp("OrderDate"));
                    dto.setProductId(rs.getInt("ProductID"));
                    dto.setProductName(rs.getString("ProductName"));
                    dto.setImage(rs.getString("MainImage"));
                    dto.setCategoryId(rs.getInt("CategoryID"));
                    dto.setCategoryName(rs.getString("CategoryName"));
                    dto.setQuantity(rs.getInt("Quantity"));
                    dto.setPrice(rs.getDouble("Price"));
                    dto.setVersion(getSafeString(rs, "Version"));
                    dto.setColor(getSafeString(rs, "Color"));
                    dto.setStorage(getSafeString(rs, "Storage"));
                    dto.setUsername(rs.getString("Username"));
                    dto.setDetailId(rs.getInt("DetailID"));
                    dto.setSubTotal(dto.getPrice() * dto.getQuantity());
                    orderProductMap.put(key, dto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy tất cả lịch sử đơn hàng: " + e.getMessage() + " at " + new java.util.Date());
            throw e;
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (SQLException e) {
            }
            if (ps != null) try {
                ps.close();
            } catch (SQLException e) {
            }
            if (conn != null) try {
                conn.close();
            } catch (SQLException e) {
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

    public ProductDTO getProductById(int productId) throws SQLException {
        ProductDTO product = null;
        String sql = "SELECT p.ID AS ProductID, p.Name AS ProductName, p.Price, p.MainImage, p.CategoryID, "
                + "c.Name AS CategoryName, "
                + "ip.Version AS iPhoneVersion, ip.Color AS iPhoneColor, ip.Storage AS iPhoneStorage, "
                + "ipad.Version AS iPadVersion, ipad.Color AS iPadColor, ipad.Storage AS iPadStorage, "
                + "mac.Version AS MacVersion, mac.Color AS MacColor, mac.Storage AS MacStorage "
                + "FROM Products p "
                + "JOIN Categories c ON p.CategoryID = c.ID "
                + "LEFT JOIN iPhone_Details ip ON p.ID = ip.ProductID "
                + "LEFT JOIN iPad_Details ipad ON p.ID = ipad.ProductID "
                + "LEFT JOIN MacBook_Details mac ON p.ID = mac.ProductID "
                + "WHERE p.ID = ?";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    product = new ProductDTO();
                    product.setProductId(rs.getInt("ProductID"));
                    product.setProductName(rs.getString("ProductName"));
                    product.setPrice(rs.getDouble("Price"));
                    product.setImage(rs.getString("MainImage"));
                    product.setCategoryId(rs.getInt("CategoryID"));
                    product.setCategoryName(rs.getString("CategoryName"));
                    product.setVersion(getSafeString(rs, "iPhoneVersion") != null ? getSafeString(rs, "iPhoneVersion")
                            : getSafeString(rs, "iPadVersion") != null ? getSafeString(rs, "iPadVersion")
                            : getSafeString(rs, "MacVersion"));
                    product.setColor(getSafeString(rs, "iPhoneColor") != null ? getSafeString(rs, "iPhoneColor")
                            : getSafeString(rs, "iPadColor") != null ? getSafeString(rs, "iPadColor")
                            : getSafeString(rs, "MacColor"));
                    product.setStorage(getSafeString(rs, "iPhoneStorage") != null ? getSafeString(rs, "iPhoneStorage")
                            : getSafeString(rs, "iPadStorage") != null ? getSafeString(rs, "iPadStorage")
                            : getSafeString(rs, "MacStorage"));
                }
            }
        }
        return product;
    }
}
