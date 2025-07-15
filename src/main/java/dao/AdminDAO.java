/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import db.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.User;

/**
 *
 * @author email
 */
public class AdminDAO extends DBContext {

    public static void main(String[] args) throws SQLException {
        AdminDAO dao = new AdminDAO();
        System.out.println(dao.getAll(1, 10));
    }

    public static final String SELECT_CUSTOMERS = "SELECT Username, FullName, Email, Phone, Role, status FROM Users \n"
            + "ORDER BY ID\n"
            + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
    public static final String UPDATE_STATUS = "UPDATE Users SET status = CASE WHEN status = 'Enable' THEN 'Disable' ELSE 'Enable' END WHERE Username = ?";

    public List<User> getAll(int page, int pagesize) throws SQLException {
        List<User> list = new ArrayList<>();

        // Tính toán OFFSET dựa trên trang hiện tại và kích thước trang
        int offset = (page - 1) * pagesize;

        // Thực hiện truy vấn với tham số OFFSET và FETCH NEXT
        ResultSet rs = executeSelectQuery(SELECT_CUSTOMERS, new Object[]{offset, pagesize});

        while (rs.next()) {
            User user = new User();
            user.setUserName(rs.getString("Username"));
            user.setUserFullname(rs.getString("Fullname"));
            user.setUserEmail(rs.getString("Email"));
            user.setUserPhone(rs.getString("Phone"));
            user.setUserRole(rs.getInt("Role"));
            user.setStatus(rs.getString("status"));
            list.add(user);
        }

        return list;
    }

    // Phương thức để đếm tổng số khách hàng
    public int getTotalCustomers() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Users";
        ResultSet rs = executeSelectQuery(sql, null);
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public boolean updateStatus(String username) throws SQLException {
        return this.executeQuery(UPDATE_STATUS, new Object[]{username}) > 0;
    }

    public User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM Users WHERE Username = ?";
        Object[] params = {username};

        ResultSet rs = executeSelectQuery(sql, params);
        if (rs.next()) {
            return new User(
                    rs.getInt("ID"),
                    rs.getString("Username"),
                    null,
                    rs.getString("FullName"),
                    rs.getString("Email"),
                    rs.getString("Phone"),
                    rs.getInt("Role"),
                    rs.getString("Avatar"),
                    rs.getString("Status")
            );
        }
        return null;
    }

}
