/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import db.DBContext;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.User;

/**
 *
 * @author email
 */
public class AdminDAO extends DBContext{
    public static void main(String[] args) throws SQLException {
        AdminDAO dao = new AdminDAO();
        System.out.println(dao.getAll());
    }
    
    public static final String SELECT_CUSTOMERS = "select Username, FullName, Email, Phone, Role, status from Users";
    public static final String UPDATE_STATUS = "UPDATE Users SET status = CASE WHEN status = 'Enable' THEN 'Disable' ELSE 'Enable' END WHERE Username = ?";
            
    public List<User> getAll() throws SQLException {
        List<User> list = new ArrayList<>();
        ResultSet rs = executeSelectQuery(SELECT_CUSTOMERS, null);
        
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
    
    public boolean updateStatus(String username) throws SQLException{
        return this.executeQuery(UPDATE_STATUS, new Object[]{username}) > 0;
    }
    
}
