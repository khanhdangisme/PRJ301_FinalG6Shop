/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author email
 */
public class ProductDTO {
    private int productId;
    private String productName;
    private String image;
    private String version;
    private String color;
    private String storage;
    private double price;
    private String categoryName;
    private int categoryId;
    private int quantity;

    public ProductDTO() {
    }

    public ProductDTO(int productId, String productName, String image, String version, String color, String storage, double price, String categoryName, int categoryId, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.image = image;
        this.version = version;
        this.color = color;
        this.storage = storage;
        this.price = price;
        this.categoryName = categoryName;
        this.categoryId = categoryId;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    
    
    @Override
    public String toString() {
        return "ProductDTO{" +
           "productId=" + productId +
           ", productName='" + productName + '\'' +
           ", image='" + image + '\'' +
           ", version='" + version + '\'' +
           ", color='" + color + '\'' +
           ", storage='" + storage + '\'' +
           ", price=" + price + '\'' +
           ", categoryName='" + categoryName + '\'' +
           ", categoryId=" + categoryId + '\'' +
           ", quantity=" + quantity +
           '}';
    }

    
    
    
}
