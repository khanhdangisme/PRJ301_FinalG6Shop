/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author email
 */
public class Product {
    private int categoryID;
    private String categoryName;
    private int productID;
    private String productName;
    private int productQuatity;
    private String productImage;

    public Product() {
    }

    public Product(int categoryID, String categoryName, int productID, String productName, int productQuatity, String productImage) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.productID = productID;
        this.productName = productName;
        this.productQuatity = productQuatity;
        this.productImage = productImage;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductQuatity() {
        return productQuatity;
    }

    public void setProductQuatity(int productQuatity) {
        this.productQuatity = productQuatity;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
    
}
