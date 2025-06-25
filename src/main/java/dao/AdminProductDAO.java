/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import db.DBContext;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
    public static final String SELECT_ALL_PRODUCT = "{CALL SearchProducts(null, null, null, null, null)}";
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

    // Lấy tất cả sản phẩm
    public List<ProductDTO> getAllProducts() throws SQLException {
        List<ProductDTO> list = new ArrayList<>();
        ResultSet rs = executeSelectQuery(SELECT_ALL_PRODUCT, null);

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
                    iphone.setiPhoneDetailId(rs.getInt("DetailID"));
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
                    ipad.setiPadDetailId(rs.getInt("DetailID"));
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
                    mac.setMacDetailId(rs.getInt("DetailID"));
                    mac.setMacVersion(rs.getString("Version"));
                    mac.setMacColor(rs.getString("Color"));
                    mac.setMacPrice(rs.getDouble("Price"));
                    mac.setMacGpuType(rs.getString("GPUType"));
                    mac.setMacRam(rs.getString("RAM"));
                    mac.setMacStorage(rs.getString("Storage"));
                    mac.setMacScreenSize(rs.getString("ScreenSize"));
                    mac.setMacScreenTech(rs.getString("ScreenTech"));
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

    public boolean updateProductDetail(Product product, Object detail) throws SQLException {
        String sql = "{CALL UpdateProductDetail(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        Object[] params = new Object[25];

        if (product == null || product.getProductID() == 0 || product.getProductName() == null) {
            throw new IllegalArgumentException("Product or required fields are null");
        }
        
        
        

        int cat = product.getCategoryID();

        // iPhone
        if (cat == 1 && detail instanceof iPhoneDetails) {
            iPhoneDetails iphone = (iPhoneDetails) detail;

            if (iphone.getiPhoneDetailId() == 0 || iphone.getiPhoneVersion() == null || iphone.getiPhoneColor() == null
                    || iphone.getiPhoneStorage() == null || iphone.getiPhonePrice() == 0) {
                throw new IllegalArgumentException("Missing required iPhone fields");
            }

            params[0] = iphone.getiPhoneDetailId();
            params[1] = product.getProductID();
            params[2] = product.getCategoryID();
            params[3] = product.getProductName();
            params[4] = iphone.getiPhoneVersion();
            params[5] = iphone.getiPhoneColor();
            params[6] = iphone.getiPhoneStorage();
            params[7] = iphone.getiPhonePrice();
            params[8] = iphone.getiPhoneQuantity();
            params[9] = product.getProductImage();
            params[10] = iphone.getiPhoneImageUrl();
            params[11] = iphone.getiPhoneScreenSize();
            params[12] = iphone.getiPhoneRearCamera();
            params[13] = iphone.getiPhoneFrontCamera();
            params[14] = iphone.getiPhoneChipset();
            params[15] = iphone.getiPhoneBattery();
            params[16] = iphone.getiPhoneSimType();
            params[17] = iphone.getiPhoneOs();
            params[18] = iphone.getiPhoneResolution();
            params[19] = iphone.getiPhoneScreenFeatures();
            params[20] = iphone.getiPhoneCpuType();
            params[21] = null;
            params[22] = null;
            params[23] = null;
            params[24] = null;

        } // iPad
        else if (cat == 2 && detail instanceof iPadDetails) {
            iPadDetails ipad = (iPadDetails) detail;

            if (ipad.getiPadDetailId() == 0 || ipad.getiPadVersion() == null || ipad.getiPadColor() == null
                    || ipad.getiPadStorage() == null || ipad.getiPadPrice() == 0) {
                throw new IllegalArgumentException("Missing required iPad fields");
            }

            params[0] = ipad.getiPadDetailId();
            params[1] = product.getProductID();
            params[2] = product.getCategoryID();
            params[3] = product.getProductName();
            params[4] = ipad.getiPadVersion();
            params[5] = ipad.getiPadColor();
            params[6] = ipad.getiPadStorage();
            params[7] = ipad.getiPadPrice();
            params[8] = ipad.getiPadQuantity();
            params[9] = product.getProductImage();
            params[10] = ipad.getiPadImageUrl();
            params[11] = ipad.getiPadScreenSize();
            params[12] = ipad.getiPadRearCamera();
            params[13] = ipad.getiPadFrontCamera();
            params[14] = ipad.getiPadChipset();
            params[15] = ipad.getiPadBattery();
            params[16] = ipad.getiPadSimType();
            params[17] = ipad.getiPadOs();
            params[18] = ipad.getiPadResolution();
            params[19] = ipad.getiPadScreenFeatures();
            params[20] = ipad.getiPadCpuType();
            params[21] = null;
            params[22] = null;
            params[23] = null;
            params[24] = null;

        } // MacBook
        else if (cat == 3 && detail instanceof MacbookDetails) {
            MacbookDetails mac = (MacbookDetails) detail;

            if (mac.getMacDetailId() == 0 || mac.getMacVersion() == null || mac.getMacColor() == null
                    || mac.getMacStorage() == null || mac.getMacPrice() == 0) {
                throw new IllegalArgumentException("Missing required MacBook fields");
            }

            params[0] = mac.getMacDetailId();
            params[1] = product.getProductID();
            params[2] = product.getCategoryID();
            params[3] = product.getProductName();
            params[4] = mac.getMacVersion();
            params[5] = mac.getMacColor();
            params[6] = mac.getMacStorage();
            params[7] = mac.getMacPrice();
            params[8] = mac.getMacQuantity();         
            params[9] = product.getProductImage();
            params[10] = mac.getMacImageUrl();
            params[11] = mac.getMacScreenSize();
            params[12] = null;
            params[13] = null;
            params[14] = null;
            params[15] = mac.getMacBattery();
            params[16] = null;
            params[17] = mac.getMacOs();
            params[18] = mac.getMacResolution();
            params[19] = null;
            params[20] = mac.getMacCpuType();
            params[21] = mac.getMacRam();
            params[22] = mac.getMacGpuType();
            params[23] = mac.getMacScreenTech();
            params[24] = mac.getMacPorts();
        } else {
            throw new IllegalArgumentException("Invalid category or detail type");
        }

        // Debug log
        System.out.println("SQL: " + sql);
        System.out.println("Params: " + Arrays.toString(params));

        return executeCallableUpdate(sql, params) > 0;
    }

    public boolean insertProductDetail(
            String name, String version, String color, String storage, double price, int quantity,
            int categoryId, String mainImage,
            String screenSize, String rearCamera, String frontCamera, String chipset, String battery,
            String simType, String os, String resolution, String screenFeatures, String cpuType,
            String ram, String gpuType, String screenTech, String ports
    ) throws SQLException {

        String sql = "{CALL InsertProductDetail(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        // Tạo mảng 24 phần tử đúng thứ tự tham số procedure
        Object[] params = new Object[24];

        // Phần chung
        params[0] = name;
        params[1] = version;
        params[2] = color;
        params[3] = storage;
        params[4] = price;
        params[5] = quantity;
        params[6] = categoryId;
        params[7] = mainImage;

        // iPhone & iPad (Category 1 & 2)
        switch (categoryId) {
            case 1:
                params[8] = screenSize;
                params[9] = rearCamera;
                params[10] = frontCamera;
                params[11] = chipset;
                params[12] = battery;
                params[13] = simType;
                params[14] = os;
                params[15] = resolution;
                params[16] = screenFeatures;
                params[17] = cpuType;
                // MacBook fields để null
                params[18] = null; // RAM
                params[19] = null; // GPU
                params[20] = null; // ScreenTech
                params[21] = null; // Ports
                params[22] = null; // reserved
                params[23] = null;
                break;
            case 2:
                params[8] = screenSize;
                params[9] = rearCamera;
                params[10] = frontCamera;
                params[11] = chipset;
                params[12] = battery;
                params[13] = simType;
                params[14] = os;
                params[15] = resolution;
                params[16] = screenFeatures;
                params[17] = cpuType;
                // MacBook fields để null
                params[18] = null; // RAM
                params[19] = null; // GPU
                params[20] = null; // ScreenTech
                params[21] = null; // Ports
                params[22] = null; // reserved
                params[23] = null;
                break;
            case 3:
                // MacBook
                params[8] = screenSize;
                params[9] = null; // Rear Camera
                params[10] = null; // Front Camera
                params[11] = null; // Chipset
                params[12] = battery;
                params[13] = null; // Sim Type
                params[14] = os;
                params[15] = resolution;
                params[16] = null; // Screen Features
                params[17] = cpuType;
                params[18] = ram;
                params[19] = gpuType;
                params[20] = screenTech;
                params[21] = ports;
                params[22] = null;
                params[23] = null;
                break;
            default:
                throw new IllegalArgumentException("Invalid category ID");
        }

        return executeCallableUpdate(sql, params) > 0;
    }

    // Lấy danh sách ảnh trong thư mục
    public List<String> getAvailableImages(String folderPath) {
        List<String> imageList = new ArrayList<>();

        try {
            // Đường dẫn tuyệt đối tới thư mục chứa ảnh
            String realPath = new File("src/main/webapp/" + folderPath).getAbsolutePath();
            File folder = new File(realPath);

            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile() && isImage(file.getName())) {
                            imageList.add(file.getName());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageList;
    }

    // Kiểm tra file có phải là ảnh không
    private boolean isImage(String fileName) {
        String lower = fileName.toLowerCase();
        return lower.endsWith(".jpg") || lower.endsWith(".jpeg")
                || lower.endsWith(".png") || lower.endsWith(".gif")
                || lower.endsWith(".webp");
    }

    public boolean deleteProduct(int categoryId, int id, String color, String storage) throws SQLException {
        String tableName;
        switch (categoryId) {
            case 1:
                tableName = "iPhone_Details";
                break;
            case 2:
                tableName = "iPad_Details";
                break;
            case 3:
                tableName = "MacBook_Details";
                break;
            default:
                throw new IllegalArgumentException("Invalid category ID: " + categoryId);
        }

        String sql = "DELETE FROM " + tableName + " WHERE ProductID = ? AND Color = ? AND Storage = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, color);
            ps.setString(3, storage);
            return ps.executeUpdate() > 0;
        }
    }

}
