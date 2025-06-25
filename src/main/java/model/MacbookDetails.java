/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author email
 */
public class MacbookDetails extends Product {

    private int MacDetailId;     // <-- Bắt buộc nếu cập nhật
    private String MacVersion;
    private String MacColor;
    private double MacPrice;
    private int MacQuantity;
    private String MacGpuType;
    private String MacRam;
    private String MacStorage;
    private String MacScreenSize;
    private String MacScreenTech;
    private String MacBattery;
    private String MacOs;
    private String MacResolution;
    private String MacCpuType;
    private String MacPorts;
    private String MacImageUrl;

    public MacbookDetails() {
    }

    public MacbookDetails(int MacDetailId, String MacVersion, String MacColor, double MacPrice, int MacQuantity, String MacGpuType, String MacRam, String MacStorage, String MacScreenSize, String MacScreenTech, String MacBattery, String MacOs, String MacResolution, String MacCpuType, String MacPorts, String MacImageUrl) {
        this.MacDetailId = MacDetailId;
        this.MacVersion = MacVersion;
        this.MacColor = MacColor;
        this.MacPrice = MacPrice;
        this.MacQuantity = MacQuantity;
        this.MacGpuType = MacGpuType;
        this.MacRam = MacRam;
        this.MacStorage = MacStorage;
        this.MacScreenSize = MacScreenSize;
        this.MacScreenTech = MacScreenTech;
        this.MacBattery = MacBattery;
        this.MacOs = MacOs;
        this.MacResolution = MacResolution;
        this.MacCpuType = MacCpuType;
        this.MacPorts = MacPorts;
        this.MacImageUrl = MacImageUrl;
    }

    public int getMacQuantity() {
        return MacQuantity;
    }

    public void setMacQuantity(int MacQuantity) {
        this.MacQuantity = MacQuantity;
    }



    public int getMacDetailId() {
        return MacDetailId;
    }

    public void setMacDetailId(int MacDetailId) {
        this.MacDetailId = MacDetailId;
    }

    public String getMacVersion() {
        return MacVersion;
    }

    public void setMacVersion(String MacVersion) {
        this.MacVersion = MacVersion;
    }

    public String getMacColor() {
        return MacColor;
    }

    public void setMacColor(String MacColor) {
        this.MacColor = MacColor;
    }

    public double getMacPrice() {
        return MacPrice;
    }

    public void setMacPrice(double MacPrice) {
        this.MacPrice = MacPrice;
    }

    public String getMacGpuType() {
        return MacGpuType;
    }

    public void setMacGpuType(String MacGpuType) {
        this.MacGpuType = MacGpuType;
    }

    public String getMacRam() {
        return MacRam;
    }

    public void setMacRam(String MacRam) {
        this.MacRam = MacRam;
    }

    public String getMacStorage() {
        return MacStorage;
    }

    public void setMacStorage(String Mactorage) {
        this.MacStorage = Mactorage;
    }

    public String getMacScreenSize() {
        return MacScreenSize;
    }

    public void setMacScreenSize(String MacScreenSize) {
        this.MacScreenSize = MacScreenSize;
    }

    public String getMacScreenTech() {
        return MacScreenTech;
    }

    public void setMacScreenTech(String MacScreenTech) {
        this.MacScreenTech = MacScreenTech;
    }

    public String getMacBattery() {
        return MacBattery;
    }

    public void setMacBattery(String MacBattery) {
        this.MacBattery = MacBattery;
    }

    public String getMacOs() {
        return MacOs;
    }

    public void setMacOs(String MacOs) {
        this.MacOs = MacOs;
    }

    public String getMacResolution() {
        return MacResolution;
    }

    public void setMacResolution(String MacResolution) {
        this.MacResolution = MacResolution;
    }

    public String getMacCpuType() {
        return MacCpuType;
    }

    public void setMacCpuType(String MacCpuType) {
        this.MacCpuType = MacCpuType;
    }

    public String getMacPorts() {
        return MacPorts;
    }

    public void setMacPorts(String MacPorts) {
        this.MacPorts = MacPorts;
    }

    public String getMacImageUrl() {
        return MacImageUrl;
    }

    public void setMacImageUrl(String MacImageUrl) {
        this.MacImageUrl = MacImageUrl;
    }

    @Override
    public String toString() {
        return "MacbookDetails{"
                + "MacDetailId=" + MacDetailId
                + ", MacVersion='" + MacVersion + '\''
                + ", MacColor='" + MacColor + '\''
                + ", MacPrice=" + MacPrice
                + ", MacGpuType='" + MacGpuType + '\''
                + ", MacRam='" + MacRam + '\''
                + ", MacStorage='" + MacStorage + '\''
                + ", MacScreenSize='" + MacScreenSize + '\''
                + ", MacScreenTech='" + MacScreenTech + '\''
                + ", MacBattery='" + MacBattery + '\''
                + ", MacOs='" + MacOs + '\''
                + ", MacResolution='" + MacResolution + '\''
                + ", MacCpuType='" + MacCpuType + '\''
                + ", MacPorts='" + MacPorts + '\''
                + ", MacImageUrl='" + MacImageUrl + '\''
                + ", ProductID=" + getProductID()
                + ", ProductName='" + getProductName() + '\''
                + ", ProductQuatity=" + getProductQuatity()
                + ", ProductImage='" + getProductImage() + '\''
                + ", CategoryID=" + getCategoryID()
                + ", CategoryName='" + getCategoryName() + '\''
                + '}';
    }

}
