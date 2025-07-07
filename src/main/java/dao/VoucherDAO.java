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
import java.util.Date;

/**
 *
 * @author Ha Minh Man - CE190070
 */
public class VoucherDAO {

    public Voucher getVoucherByCode(String code) throws SQLException {
        String sql = "SELECT ID, Code, DiscountPercent, MaxDiscount, ExpiryDate, isActive FROM Coupons WHERE Code = ? AND isActive = 1";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, code);
            try ( ResultSet rs = stmt.executeQuery()) {
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
                return null;
            }
        }
    }
}
