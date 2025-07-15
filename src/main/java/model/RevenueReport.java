/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;

/**
 *
 * @author Tran Gia Huy - CE190210
 */
public class RevenueReport {

    private String fromDate;
    private String toDate;
    private BigDecimal revenue;

    public RevenueReport() {
    }

    public RevenueReport(String fromDate, String toDate, BigDecimal revenue) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.revenue = revenue;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    @Override
    public String toString() {
        return "RevenueReport{" + "fromDate=" + fromDate + ", toDate=" + toDate + ", revenue=" + revenue + '}';
    }

    

}
