package dao;

import db.DBContext;
import java.math.BigDecimal;
import model.RevenueReport;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

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
}
