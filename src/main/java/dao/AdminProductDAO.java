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
import model.MacbookDetails;
import model.Product;
import model.ProductDTO;
import model.iPadDetails;
import model.iPhoneDetails;

/**
 *
 * @author email
 */
public class AdminProductDAO extends DBContext {

    public static void main(String[] args) throws SQLException {
        AdminProductDAO dao = new AdminProductDAO();
        System.out.println(dao.getProductDetail(2, "Black", "512GB"));
    }
    public static final String SELECT_CATEGORY_PRODUCT = "{CALL SearchProducts(?, null, null, null, null)}";
    public static final String SELECT_CATEGORY = "select distinct a.ID,  a.Name from Categories a join Products b on a.ID = b.CategoryID";
    public static final String SELECT_PRODUCT_MACBOOK = "select b.ImageURL, a.Name, b.Version, b.Storage, b.Color, b.Price, a.Quantity from Products a join iPhone_Details b on a.ID = b.ProductID";

    public List<Product> getAllCategory() throws SQLException {
        List<Product> list = new ArrayList<>();
        ResultSet rs = executeSelectQuery(SELECT_CATEGORY, null);

        while (rs.next()) {
            Product product = new Product();
            product.setCategoryID(rs.getInt("ID"));
            product.setCategoryName(rs.getString("Name"));
            list.add(product);
        }
        return list;
    }

    public List<ProductDTO> getProduct(int categoryId) throws SQLException {
        List<ProductDTO> list = new ArrayList<>();
        ResultSet rs = executeSelectQuery(SELECT_CATEGORY_PRODUCT, new Object[]{categoryId});
        while (rs.next()) {
            ProductDTO dto = new ProductDTO();
            dto.setProductId(rs.getInt("ProductID"));
            dto.setProductName(rs.getString("ProductName"));
            dto.setVersion(rs.getString("Version"));
            dto.setColor(rs.getString("Color"));
            dto.setStorage(rs.getString("Storage"));
            dto.setPrice(rs.getDouble("Price"));
            dto.setImage(rs.getString("Image"));
            dto.setCategoryId(rs.getInt("CategoryID"));
            dto.setCategoryName(rs.getString("CategoryName"));
            dto.setQuantity(rs.getInt("Quantity"));
            list.add(dto);
        }
        return list;
    }

    public Product getProductDetail(int productId, String color, String storage) throws SQLException {
        String sql = "{CALL GetProductDetail(?, ?, ?)}";
        ResultSet rs = executeSelectQuery(sql, new Object[]{productId, color, storage});

        if (rs.next()) {
            int categoryId = rs.getInt("CategoryID");

            switch (categoryId) {
                case 1: // iPhone
                    iPhoneDetails iphone = new iPhoneDetails();
                    iphone.setProductID(productId);
                    iphone.setProductName(rs.getString("ProductName"));
                    iphone.setProductImage(rs.getString("MainImage"));
                    iphone.setProductQuatity(rs.getInt("Quantity"));
                    iphone.setCategoryID(categoryId);
                    iphone.setCategoryName(rs.getString("CategoryName"));
                    iphone.setiPhoneVersion(rs.getString("Version"));
                    iphone.setiPhoneColor(rs.getString("Color"));
                    iphone.setiPhoneStorage(rs.getString("Storage"));
                    iphone.setiPhonePrice(rs.getDouble("Price"));
                    iphone.setiPhoneScreenSize(rs.getString("ScreenSize"));
                    iphone.setiPhoneRearCamera(rs.getString("RearCamera"));
                    iphone.setiPhoneFrontCamera(rs.getString("FrontCamera"));
                    iphone.setiPhoneChipset(rs.getString("Chipset"));
                    iphone.setiPhoneBattery(rs.getString("Battery"));
                    iphone.setiPhoneSimType(rs.getString("SimType"));
                    iphone.setiPhoneOs(rs.getString("OS"));
                    iphone.setiPhoneResolution(rs.getString("Resolution"));
                    iphone.setiPhoneScreenFeatures(rs.getString("ScreenFeatures"));
                    iphone.setiPhoneCpuType(rs.getString("CPUType"));
                    iphone.setiPhoneImageUrl(rs.getString("ImageURL"));
                    return iphone;

                case 2: // iPad
                    iPadDetails ipad = new iPadDetails();
                    ipad.setProductID(productId);
                    ipad.setProductName(rs.getString("ProductName"));
                    ipad.setProductImage(rs.getString("MainImage"));
                    ipad.setProductQuatity(rs.getInt("Quantity"));
                    ipad.setCategoryID(categoryId); // ✅ FIXED
                    ipad.setCategoryName(rs.getString("CategoryName"));
                    ipad.setiPadVersion(rs.getString("Version"));
                    ipad.setiPadColor(rs.getString("Color"));
                    ipad.setiPadStorage(rs.getString("Storage"));
                    ipad.setiPadPrice(rs.getDouble("Price"));
                    ipad.setiPadScreenSize(rs.getString("ScreenSize"));
                    ipad.setiPadRearCamera(rs.getString("RearCamera"));
                    ipad.setiPadFrontCamera(rs.getString("FrontCamera"));
                    ipad.setiPadChipset(rs.getString("Chipset"));
                    ipad.setiPadBattery(rs.getString("Battery"));
                    ipad.setiPadSimType(rs.getString("SimType"));
                    ipad.setiPadOs(rs.getString("OS"));
                    ipad.setiPadResolution(rs.getString("Resolution"));
                    ipad.setiPadScreenFeatures(rs.getString("ScreenFeatures"));
                    ipad.setiPadCpuType(rs.getString("CPUType"));
                    ipad.setiPadImageUrl(rs.getString("ImageURL"));
                    return ipad;

                case 3: // MacBook
                    MacbookDetails mac = new MacbookDetails();
                    mac.setProductID(productId);
                    mac.setProductName(rs.getString("ProductName"));
                    mac.setProductImage(rs.getString("MainImage"));
                    mac.setProductQuatity(rs.getInt("Quantity"));
                    mac.setCategoryID(categoryId);
                    mac.setCategoryName(rs.getString("CategoryName"));
                    mac.setMacVersion(rs.getString("Version"));
                    mac.setMacColor(rs.getString("Color"));
                    mac.setMacPrice(rs.getDouble("Price"));
                    mac.setMacGpuType(rs.getString("GPUType"));
                    mac.setMacRam(rs.getString("RAM"));
                    mac.setMacStorage(rs.getString("Storage"));
                    mac.setMacSreenSize(rs.getString("ScreenSize"));
                    mac.setMaccreenTech(rs.getString("ScreenTech"));
                    mac.setMacBattery(rs.getString("Battery"));
                    mac.setMacOs(rs.getString("OS"));
                    mac.setMacResolution(rs.getString("Resolution"));
                    mac.setMacCpuType(rs.getString("CPUType"));
                    mac.setMacPorts(rs.getString("Ports"));
                    mac.setMacImageUrl(rs.getString("ImageURL"));
                    return mac;

                default:
                    return null;
            }
        }
        return null;
    }

    public void updateProductDetail(Product product, Object detail) throws SQLException {
        String sql = "{CALL UpdateProductDetail(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        Object[] params = new Object[24];

        // Đúng thứ tự với stored procedure:
        // 1-3: ProductID, CategoryID, ProductName
        params[0] = product.getProductID();
        params[1] = product.getCategoryID();
        params[2] = product.getProductName();

        // 4-6: Version, Color, Storage
        params[3] = null;
        params[4] = null;
        params[5] = null;

        // 7: Price
        params[6] = null;

        // 8-9: Quantity, MainImage
        params[7] = product.getProductQuatity();
        params[8] = product.getProductImage();

        // 10-20: ImageURL → CPUType
        for (int i = 9; i <= 19; i++) {
            params[i] = null;
        }

        // 21-24: RAM, GPUType, ScreenTech, Ports
        for (int i = 20; i <= 23; i++) {
            params[i] = null;
        }

        int cat = product.getCategoryID();

        if (cat == 1 && detail instanceof iPhoneDetails) {
            iPhoneDetails iphone = (iPhoneDetails) detail;
            params[3] = iphone.getiPhoneVersion();
            params[4] = iphone.getiPhoneColor();
            params[5] = iphone.getiPhoneStorage();
            params[6] = iphone.getiPhonePrice();

            params[9] = iphone.getiPhoneImageUrl();
            params[10] = iphone.getiPhoneScreenSize();
            params[11] = iphone.getiPhoneRearCamera();
            params[12] = iphone.getiPhoneFrontCamera();
            params[13] = iphone.getiPhoneChipset();
            params[14] = iphone.getiPhoneBattery();
            params[15] = iphone.getiPhoneSimType();
            params[16] = iphone.getiPhoneOs();
            params[17] = iphone.getiPhoneResolution();
            params[18] = iphone.getiPhoneScreenFeatures();
            params[19] = iphone.getiPhoneCpuType();
        } else if (cat == 2 && detail instanceof iPadDetails) {
            iPadDetails ipad = (iPadDetails) detail;
            params[3] = ipad.getiPadVersion();
            params[4] = ipad.getiPadColor();
            params[5] = ipad.getiPadStorage();
            params[6] = ipad.getiPadPrice();

            params[9] = ipad.getiPadImageUrl();
            params[10] = ipad.getiPadScreenSize();
            params[11] = ipad.getiPadRearCamera();
            params[12] = ipad.getiPadFrontCamera();
            params[13] = ipad.getiPadChipset();
            params[14] = ipad.getiPadBattery();
            params[15] = ipad.getiPadSimType();
            params[16] = ipad.getiPadOs();
            params[17] = ipad.getiPadResolution();
            params[18] = ipad.getiPadScreenFeatures();
            params[19] = ipad.getiPadCpuType();
        } else if (cat == 3 && detail instanceof MacbookDetails) {
            MacbookDetails mac = (MacbookDetails) detail;
            params[3] = mac.getMacVersion();
            params[4] = mac.getMacColor();
            params[5] = mac.getMacStorage();
            params[6] = mac.getMacPrice();

            params[9] = mac.getMacImageUrl();
            params[10] = mac.getMacSreenSize();
            params[14] = mac.getMacBattery(); // dùng lại vị trí Battery
            params[16] = mac.getMacOs();
            params[17] = mac.getMacResolution();
            params[19] = mac.getMacCpuType();

            params[20] = mac.getMacRam();
            params[21] = mac.getMacGpuType();
            params[22] = mac.getMaccreenTech();
            params[23] = mac.getMacPorts();
        }

        executeQuery(sql, params);
    }

}
