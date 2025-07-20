package dao;

import db.DBContext;
import java.math.BigDecimal;
import model.RevenueReport;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import model.ProductDTO;

public class ReportDAO extends DBContext {

    public static void main(String[] args) throws SQLException {
        ReportDAO dao = new ReportDAO();
        LocalDate fromDate = LocalDate.of(2025, 7, 16);
        LocalDate toDate = LocalDate.of(2025, 7, 16);
        System.out.println(dao.getReport(fromDate, toDate));
    }

    public BigDecimal getReport(LocalDate fromDate, LocalDate toDate) throws SQLException {
        String sql = "SELECT SUM(o.TotalPrice) AS TotalRevenue\n"
                + "FROM Orders o\n"
                + "JOIN (\n"
                + "    SELECT OSH.OrderID, OSH.Status\n"
                + "    FROM OrderStatusHistory OSH\n"
                + "    JOIN (\n"
                + "        SELECT OrderID, MAX(ChangedAt) AS LatestChange\n"
                + "        FROM OrderStatusHistory\n"
                + "        GROUP BY OrderID\n"
                + "    ) latest ON OSH.OrderID = latest.OrderID AND OSH.ChangedAt = latest.LatestChange\n"
                + ") LatestStatus ON o.ID = LatestStatus.OrderID\n"
                + "WHERE \n"
                + "    o.OrderDate >= ?\n"
                + "    AND o.OrderDate <= ?\n"
                + "    AND LatestStatus.Status = 'Completed'";

        Object[] params = {
            Timestamp.valueOf(fromDate.atStartOfDay()),
            Timestamp.valueOf(toDate.atTime(23, 59, 59))
        };

        ResultSet rs = executeSelectQuery(sql, params);

        if (rs.next()) {
            BigDecimal total = rs.getBigDecimal("TotalRevenue");
            return total != null ? total : BigDecimal.ZERO;
        }

        return BigDecimal.ZERO;
    }

    public List<ProductDTO> getTop5BestSellingProductsFullInfo(LocalDate fromDate, LocalDate toDate) throws SQLException {
        List<ProductDTO> list = new ArrayList<>();
        String sql
                = "SELECT TOP 5 "
                + "    p.ID AS ProductID, p.Name AS ProductName, p.CategoryID, c.Name AS CategoryName, "
                + "    SUM(d.Quantity) AS Quantity, SUM(d.Quantity * d.Price) AS SubTotal, "
                + "    CASE "
                + "        WHEN p.CategoryID = 1 THEN ip.ImageURL "
                + "        WHEN p.CategoryID = 2 THEN ipad.ImageURL "
                + "        WHEN p.CategoryID = 3 THEN mac.ImageURL "
                + "        ELSE NULL END AS ImageURL, "
                + "    d.Version, d.Color, d.Storage "
                + "FROM OrderDetails d "
                + "JOIN Orders o ON d.OrderID = o.ID "
                + "JOIN Products p ON d.ProductID = p.ID "
                + "JOIN Categories c ON p.CategoryID = c.ID "
                + "LEFT JOIN iPhone_Details ip ON ip.DetailID = d.DetailID AND p.CategoryID = 1 "
                + "LEFT JOIN iPad_Details ipad ON ipad.DetailID = d.DetailID AND p.CategoryID = 2 "
                + "LEFT JOIN MacBook_Details mac ON mac.DetailID = d.DetailID AND p.CategoryID = 3 "
                + "JOIN ( "
                + "    SELECT OSH.OrderID "
                + "    FROM OrderStatusHistory OSH "
                + "    JOIN ( "
                + "        SELECT OrderID, MAX(ChangedAt) AS LatestChange "
                + "        FROM OrderStatusHistory "
                + "        GROUP BY OrderID "
                + "    ) latest ON OSH.OrderID = latest.OrderID AND OSH.ChangedAt = latest.LatestChange "
                + "    WHERE OSH.Status = 'Completed' "
                + ") CompletedOrders ON o.ID = CompletedOrders.OrderID "
                + "WHERE o.OrderDate BETWEEN ? AND ? "
                + "GROUP BY p.ID, p.Name, p.CategoryID, c.Name, d.Version, d.Color, d.Storage, ip.ImageURL, ipad.ImageURL, mac.ImageURL "
                + "ORDER BY Quantity DESC, SubTotal DESC";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(fromDate.atStartOfDay()));
            ps.setTimestamp(2, Timestamp.valueOf(toDate.atTime(23, 59, 59)));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProductDTO dto = new ProductDTO();
                dto.setProductId(rs.getInt("ProductID"));
                dto.setProductName(rs.getString("ProductName"));
                dto.setCategoryId(rs.getInt("CategoryID"));
                dto.setCategoryName(rs.getString("CategoryName"));
                dto.setQuantity(rs.getInt("Quantity"));
                dto.setSubTotal(rs.getDouble("SubTotal"));
                dto.setVersion(rs.getString("Version"));
                dto.setColor(rs.getString("Color"));
                dto.setStorage(rs.getString("Storage"));
                dto.setImage(rs.getString("ImageURL"));
                list.add(dto);
            }
        }
        return list;
    }

}
