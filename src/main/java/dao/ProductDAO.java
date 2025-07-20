/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import db.DBContext;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import model.ProductDTO;

/**
 *
 * @author email
 */
public class ProductDAO extends DBContext {

    public static void main(String[] args) throws SQLException {
        ProductDAO dao = new ProductDAO();
        System.out.println(dao.getOnePerCategory());
    }
    public static final String SELECT_ALL_PRODUCTS = "EXEC SearchProducts";

    //lay all
    public List<ProductDTO> getAll() throws SQLException {
        List<ProductDTO> list = new ArrayList<>();
        ResultSet rs = executeSelectQuery(SELECT_ALL_PRODUCTS, null);

        while (rs.next()) {
            ProductDTO product = new ProductDTO();

            product.setProductId(rs.getInt("ProductID"));
            product.setName(rs.getString("ProductName"));
            product.setMainImage(rs.getString("Image"));
            product.setVersion(rs.getString("Version"));
            product.setColor(rs.getString("Color"));
            product.setPrice(rs.getDouble("Price"));
            product.setCategoryId(rs.getInt("CategoryID"));
            product.setCategory(rs.getString("CategoryName"));

            list.add(product);
        }

        return list;
    }

    //lay 1 san pham
    public List<ProductDTO> getOnePerCategory() throws SQLException {
        List<ProductDTO> result = new ArrayList<>();
        Set<String> seenCategories = new HashSet<>();

        ResultSet rs = executeSelectQuery(SELECT_ALL_PRODUCTS, null);

        while (rs.next()) {
            String category = rs.getString("CategoryName");

            if (!seenCategories.contains(category)) {
                ProductDTO product = new ProductDTO();
                product.setProductId(rs.getInt("ProductID"));
                product.setName(rs.getString("Name"));
                product.setMainImage(rs.getString("Image"));
                product.setVersion(rs.getString("Version"));
                product.setColor(rs.getString("Color"));
                product.setPrice(rs.getDouble("Price"));
                product.setCategoryId(rs.getInt("CategoryID"));
                product.setCategory(category);

                result.add(product);
                seenCategories.add(category); // đánh dấu đã có
            }
        }

        return result;
    }

    //lay all ten category
    public List<String> getAllCategoryNames() throws SQLException {
        Set<String> categories = new HashSet<>();
        ResultSet rs = executeSelectQuery(SELECT_ALL_PRODUCTS, null);

        while (rs.next()) {
            categories.add(rs.getString("CategoryName"));
        }

        return new ArrayList<>(categories);
    }

    //lay all ten product
    public List<String> getProductNames() throws SQLException {
        Set<String> productNames = new HashSet<>();
        ResultSet rs = executeSelectQuery(SELECT_ALL_PRODUCTS, null);

        while (rs.next()) {
            productNames.add(rs.getString("Name"));
        }

        return new ArrayList<>(productNames);
    }

}
