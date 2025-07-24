/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import db.DBContext;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;

/**
 *
 * @author email
 */
public class UserDAO extends DBContext {

    public static void main(String[] args) {
        UserDAO user = new UserDAO();
        System.out.println(user.hashMd5("123"));
    }

    public static final String SELECT_PASSWORD = "SELECT ID, Username, Password, FullName, Email, Phone, Role, avatar, status FROM Users WHERE Username = ? AND Password = ?";
    public static final String INSERT_USER = "INSERT INTO Users (Username, Password, FullName, Email, Phone, Role, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
    public static final String CHECK_EXIST = "SELECT 1 FROM Users WHERE Username = ?";
    public static final String UPDATE_USER = "UPDATE users SET fullname=?, email=?, phone=?, avatar =? WHERE username=?";
    public static final String UPDATE_PASSWORD_USER = "UPDATE Users SET password = ? WHERE username = ?";
    public static final String CHECK_EXIST_BEFORE_UPDATE_PASSWORD = "SELECT 1 FROM Users WHERE username = ? AND password = ?";
    public static final String DELETE_USER = "DELETE FROM Users WHERE Username = ? and password = ?";

    private String hashMd5(String raw) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] mess = md.digest(raw.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : mess) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    //ma hoa password
    public User login(String username, String password) {
        String hasPwd = hashMd5(password);
        Object[] params = {username, hasPwd};
        try {
            ResultSet rs = this.executeSelectQuery(SELECT_PASSWORD, params);
            if (rs.next()) {
                return new User(
                        rs.getInt("ID"),
                        rs.getString("Username"),
                        null, // Không trả password (vì bảo mật)
                        rs.getString("FullName"),
                        rs.getString("Email"),
                        rs.getString("Phone"),
                        rs.getInt("Role"),
                        rs.getString("Avatar"),
                        rs.getString("status")
                );
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean insertUser(User user) throws SQLException {
        String hashedPwd = hashMd5(user.getUserPassword());
        Object[] params = {
            user.getUserName(),
            hashedPwd,
            user.getUserFullname(),
            user.getUserEmail(),
            user.getUserPhone(),
            user.getUserRole(),
            user.getStatus()
        };
return this.executeQuery(INSERT_USER, params) > 0;
    }

    public boolean updateUser(User user) throws SQLException {

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM Users WHERE Email = ? AND Username != ?")) {
            stmt.setString(1, user.getUserEmail());
            stmt.setString(2, user.getUserName());
            try ( ResultSet rs = stmt.executeQuery()) {
                rs.next();
                if (rs.getInt(1) > 0) {
                    throw new SQLException("Email is already registered by another user.");
                }
            }
        }

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM Users WHERE Phone = ? AND Username != ?")) {
            stmt.setString(1, user.getUserPhone());
            stmt.setString(2, user.getUserName());
            try ( ResultSet rs = stmt.executeQuery()) {
                rs.next();
                if (rs.getInt(1) > 0) {
                    throw new SQLException("Phone number is already registered by another user.");
                }
            }
        }

        Object[] params = {
            user.getUserFullname(),
            user.getUserEmail(),
            user.getUserPhone(),
            user.getAvatar(),
            user.getUserName()
        };
        return this.executeQuery(UPDATE_USER, params) > 0;
    }

    public boolean checkExistsPassword(String username, String password) {
        String hashedPwd = hashMd5(password);
        try ( ResultSet rs = this.executeSelectQuery(CHECK_EXIST_BEFORE_UPDATE_PASSWORD, new Object[]{username, hashedPwd})) {
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean updateUserPassword(String username, String password) throws SQLException {
        String hashedPwd = hashMd5(password);
        try {
            return this.executeQuery(UPDATE_PASSWORD_USER, new Object[]{hashedPwd, username}) > 0;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean deleteUser(String username, String password) throws SQLException {
        String hashedPwd = hashMd5(password);
        return this.executeQuery(DELETE_USER, new Object[]{username, hashedPwd}) > 0;
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null) {
            return false;
        }
        return phone.matches("^0\\d{9}$");
    }

    public static boolean isValidGmail(String email) {
        if (email == null) {
            return false;
        }

        return email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$");
    }

    public static boolean isValidPassword(String password) {
if (password == null || password.length() < 6) {
            return false;
        }

        return password.matches("^[A-Z].*\\d+.*$");
    }

    public boolean containsScript(String input) {
        if (input == null) {
            return false;
        }
        String lower = input.toLowerCase();
        return lower.contains("<script") || lower.contains("</script>")
                || lower.contains("javascript:") || lower.contains("onerror=")
                || lower.contains("onload=") || lower.contains("alert(");
    }

    public boolean checkUserExists(String username) throws SQLException {
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM Users WHERE Username = ?")) {
            stmt.setString(1, username);
            try ( ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    public boolean checkEmailExists(String email) throws SQLException {
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM Users WHERE Email = ?")) {
            stmt.setString(1, email);
            try ( ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    public boolean checkPhoneExists(String phone) throws SQLException {
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM Users WHERE Phone = ?")) {
            stmt.setString(1, phone);
            try ( ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }
}
