/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import db.DBContext;
import model.Voucher;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Ha Minh Man - CE190070
 */
public class VoucherDAO {

    public Voucher getVoucherByCode(String code) {
    String sql = "SELECT ID, Code, DiscountPercent, MaxDiscount, ExpiryDate, isActive FROM Voucher WHERE Code = ? AND isActive = 1";
    try (Connection conn = DBContext.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, code);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                Voucher voucher = new Voucher();
                voucher.setId(rs.getInt("ID"));
                voucher.setCode(rs.getString("Code"));
                voucher.setDiscountPercent(rs.getInt("DiscountPercent"));
                voucher.setMaxDiscount(rs.getDouble("MaxDiscount"));
                voucher.setExpiryDate(rs.getDate("ExpiryDate"));
                voucher.setActive(rs.getBoolean("isActive"));
                // Kiểm tra ngày hết hạn
                if (voucher.getExpiryDate().after(new Date())) {
                    return voucher;
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}


    // Lấy tất cả voucher (Admin xem danh sách)
    public List<Voucher> getAvailableVouchers() throws SQLException {
        List<Voucher> list = new ArrayList<>();
        String sql = "SELECT ID, Code, DiscountPercent, MaxDiscount, ExpiryDate, isActive "
                + "FROM Voucher WHERE isActive = 1 AND ExpiryDate >= GETDATE()";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql);  ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Voucher v = new Voucher();
                v.setId(rs.getInt("ID"));
                v.setCode(rs.getString("Code"));
                v.setDiscountPercent(rs.getInt("DiscountPercent"));
                v.setMaxDiscount(rs.getDouble("MaxDiscount"));
                v.setExpiryDate(rs.getDate("ExpiryDate"));
                v.setActive(rs.getBoolean("isActive"));
                list.add(v);
            }
        }

        return list;
    }

    // Lấy tất cả voucher (Admin quản lý - bao gồm cả hết hạn)
    public List<Voucher> getAllVouchers() throws SQLException {
        List<Voucher> list = new ArrayList<>();
        String sql = "SELECT ID, Code, DiscountPercent, MaxDiscount, ExpiryDate, isActive "
                + "FROM Voucher ORDER BY ID DESC";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql);  ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Voucher v = new Voucher();
                v.setId(rs.getInt("ID"));
                v.setCode(rs.getString("Code"));
                v.setDiscountPercent(rs.getInt("DiscountPercent"));
                v.setMaxDiscount(rs.getDouble("MaxDiscount"));
                v.setExpiryDate(rs.getDate("ExpiryDate"));
                v.setActive(rs.getBoolean("isActive"));
                list.add(v);
            }
        }

        return list;
    }

    // Lấy voucher theo ID (cho Admin sửa)
    public Voucher getVoucherById(int id) throws SQLException {
        String sql = "SELECT ID, Code, DiscountPercent, MaxDiscount, ExpiryDate, isActive FROM Voucher WHERE ID = ?";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Voucher(
                            rs.getInt("ID"),
                            rs.getString("Code"),
                            rs.getInt("DiscountPercent"),
                            rs.getDouble("MaxDiscount"),
                            rs.getDate("ExpiryDate"),
                            rs.getBoolean("isActive")
                    );
                }
            }
        }
        return null;
    }

    // Thêm voucher mới
    public void insertVoucher(Voucher voucher) throws SQLException {
        String sql = "INSERT INTO Voucher (Code, DiscountPercent, MaxDiscount, ExpiryDate, isActive) VALUES (?, ?, ?, ?, ?)";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, voucher.getCode());
            stmt.setInt(2, voucher.getDiscountPercent());
            stmt.setDouble(3, voucher.getMaxDiscount());
            stmt.setDate(4, new java.sql.Date(voucher.getExpiryDate().getTime()));
            stmt.setBoolean(5, voucher.isActive());

            stmt.executeUpdate();
        }
    }

    // Cập nhật voucher
    public void updateVoucher(Voucher voucher) throws SQLException {
        String sql = "UPDATE Voucher SET Code = ?, DiscountPercent = ?, MaxDiscount = ?, ExpiryDate = ?, isActive = ? WHERE ID = ?";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, voucher.getCode());
            stmt.setInt(2, voucher.getDiscountPercent());
            stmt.setDouble(3, voucher.getMaxDiscount());
            stmt.setDate(4, new java.sql.Date(voucher.getExpiryDate().getTime()));
            stmt.setBoolean(5, voucher.isActive());
            stmt.setInt(6, voucher.getId());

            stmt.executeUpdate();
        }
    }

    // Xóa voucher
    public void deleteVoucher(int id) throws SQLException {
        String sql = "DELETE FROM Voucher WHERE ID = ?";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
