/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Ha Minh Man - CE190070
 */
import java.util.Date;

public class Voucher {

    private int id;
    private String code;
    private int discountPercent;
    private double maxDiscount;
    private Date expiryDate;
    private boolean isActive;

    // Constructor
    public Voucher() {
    }

    public Voucher(int id, String code, int discountPercent, double maxDiscount, Date expiryDate, boolean isActive) {
        this.id = id;
        this.code = code;
        this.discountPercent = discountPercent;
        this.maxDiscount = maxDiscount;
        this.expiryDate = expiryDate;
        this.isActive = isActive;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public double getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(double maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Voucher{" + "id=" + id + ", code=" + code + ", discountPercent=" + discountPercent + ", maxDiscount=" + maxDiscount + ", expiryDate=" + expiryDate + ", isActive=" + isActive + '}';
    }
    
    
}
