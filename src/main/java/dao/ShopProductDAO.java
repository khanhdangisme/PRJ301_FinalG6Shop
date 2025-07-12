/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import db.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
public class ShopProductDAO extends DBContext {
    
    public static final String SELECT_CATEGORY = "select distinct a.ID,  a.Name from Categories a join Products b on a.ID = b.CategoryID";
    public static final String SELECT_CATEGORY_MENU = "{CALL GetProductsByCategory(?)}";
    public static final String SELECT_DETAIL = "{CALL GetProductDetail(?, ?, ?)}";

    // Thêm vào ShopProductDAO
    public ProductDTO getProductDetailDTO(int productId, String color, String storage)
            throws SQLException {

        ResultSet rs = executeSelectQuery(SELECT_DETAIL,
                new Object[]{productId, color, storage});
        if (rs.next()) {
            ProductDTO dto = new ProductDTO();
            dto.setProductId(productId);
            dto.setProductName(rs.getString("ProductName"));
            dto.setVersion(rs.getString("Version"));
            dto.setColor(rs.getString("Color"));
            dto.setStorage(rs.getString("Storage"));
            dto.setPrice(rs.getDouble("Price"));
            dto.setImage(rs.getString("ImageURL"));
            dto.setQuantity(rs.getInt("Quantity"));
            dto.setCategoryId(rs.getInt("CategoryID"));
            dto.setCategoryName(rs.getString("CategoryName"));
            dto.setDetailId(rs.getInt("DetailID"));  // nếu cần
            return dto;
        }
        return null;
    }

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
    

    public List<ProductDTO> getProductsForCategory(int categoryId) throws SQLException {
        List<ProductDTO> list = new ArrayList<>();
        ResultSet rs = executeSelectQuery(SELECT_CATEGORY_MENU, new Object[]{categoryId});
        while (rs.next()) {
            ProductDTO dto = new ProductDTO();
            dto.setProductId(rs.getInt("ProductID"));  // BỔ SUNG
            dto.setProductName(rs.getString("ProductName"));
            list.add(dto);
        }
        return list;
    }

    public List<ProductDTO> getProductItem(int productId) throws SQLException {
        List<ProductDTO> list = new ArrayList<>();
        ResultSet rs = executeSelectQuery("{call GetProductItem(?)}", new Object[]{productId});
        while (rs.next()) {
            ProductDTO dto = new ProductDTO();
            dto.setProductId(rs.getInt("ProductID"));
            dto.setProductName(rs.getString("ProductName"));
            dto.setVersion(rs.getString("Version"));
            dto.setColor(rs.getString("Color"));
            dto.setStorage(rs.getString("Storage"));
            dto.setPrice(rs.getDouble("Price"));
            dto.setImage(rs.getString("ImageURL"));
            dto.setQuantity(rs.getInt("Quantity"));
            list.add(dto);
        }
        return list;
    }

    public Product getProductDetail(int productId, String color, String storage) throws SQLException {
        ResultSet rs = executeSelectQuery(SELECT_DETAIL, new Object[]{productId, color, storage});

        if (rs.next()) {
            int categoryId = rs.getInt("CategoryID");

            switch (categoryId) {
                case 1: // iPhone
                    iPhoneDetails iphone = new iPhoneDetails();
                    iphone.setProductID(productId);
                    iphone.setProductName(rs.getString("ProductName"));
                    iphone.setProductImage(rs.getString("ImageURL"));
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
                    ipad.setProductImage(rs.getString("ImageURL"));
                    ipad.setProductQuatity(rs.getInt("Quantity"));
                    ipad.setCategoryID(categoryId);
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
                    mac.setProductImage(rs.getString("ImageURL"));
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

    public List<ProductDTO> searchProductsByKeyword(String keyword) throws SQLException {
        String sql = "{CALL SearchProducts(null, ?, null, null, null)}"; // Gọi SP lọc theo tên
        Object[] params = new Object[]{"%" + keyword + "%"};

        List<ProductDTO> list = new ArrayList<>();
        ResultSet rs = executeSelectQuery(sql, params);

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
    // Lấy tất cả sản phẩm
// Cập nhật phương thức getAllProducts() trong AdminProductDAO

    public List<ProductDTO> getAllProducts() throws SQLException {
        List<ProductDTO> list = new ArrayList<>();

        // SQL để lấy tất cả sản phẩm từ các bảng chi tiết
        String sql = "SELECT "
                + "p.ID as ProductID, "
                + "p.Name as ProductName, "
                + "p.MainImage, "
                + "p.CategoryID, "
                + "c.Name as CategoryName, "
                + "p.Quantity, "
                + "iphone.DetailID as iPhoneDetailID, "
                + "iphone.Version as iPhoneVersion, "
                + "iphone.Color as iPhoneColor, "
                + "iphone.Storage as iPhoneStorage, "
                + "iphone.Price as iPhonePrice, "
                + "iphone.ImageURL as iPhoneImageURL, "
                + "ipad.DetailID as iPadDetailID, "
                + "ipad.Version as iPadVersion, "
                + "ipad.Color as iPadColor, "
                + "ipad.Storage as iPadStorage, "
                + "ipad.Price as iPadPrice, "
                + "ipad.ImageURL as iPadImageURL, "
                + "mac.DetailID as MacDetailID, "
                + "mac.Version as MacVersion, "
                + "mac.Color as MacColor, "
                + "mac.Storage as MacStorage, "
                + "mac.Price as MacPrice, "
                + "mac.ImageURL as MacImageURL "
                + "FROM Products p "
                + "JOIN Categories c ON p.CategoryID = c.ID "
                + "LEFT JOIN iPhone_Details iphone ON p.ID = iphone.ProductID "
                + "LEFT JOIN iPad_Details ipad ON p.ID = ipad.ProductID "
                + "LEFT JOIN MacBook_Details mac ON p.ID = mac.ProductID "
                + "WHERE iphone.DetailID IS NOT NULL OR ipad.DetailID IS NOT NULL OR mac.DetailID IS NOT NULL "
                + "ORDER BY p.CategoryID, p.Name";

        try ( Connection con = DBContext.getConnection();  PreparedStatement ps = con.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ProductDTO dto = new ProductDTO();

                // Thông tin cơ bản
                dto.setProductId(rs.getInt("ProductID"));
                dto.setProductName(rs.getString("ProductName"));

                // ===== FIX: Xử lý hình ảnh chính xác =====
                String mainImage = rs.getString("MainImage");
                dto.setImage(mainImage); // Sử dụng helper method

                dto.setCategoryId(rs.getInt("CategoryID"));
                dto.setCategoryName(rs.getString("CategoryName"));
                dto.setQuantity(rs.getInt("Quantity"));

                // Lấy thông tin chi tiết dựa trên CategoryID
                int categoryId = rs.getInt("CategoryID");

                switch (categoryId) {
                    case 1: // iPhone
                        if (rs.getObject("iPhoneDetailID") != null) {
                            dto.setDetailId(rs.getInt("iPhoneDetailID"));
                            dto.setVersion(rs.getString("iPhoneVersion"));
                            dto.setColor(rs.getString("iPhoneColor"));
                            dto.setStorage(rs.getString("iPhoneStorage"));
                            dto.setPrice(rs.getDouble("iPhonePrice"));

                            // ===== FIX: Ưu tiên ảnh chi tiết với đường dẫn đúng =====
                            String iPhoneImg = rs.getString("iPhoneImageURL");
                            if (iPhoneImg != null && !iPhoneImg.trim().isEmpty()) {
                                dto.setImage(iPhoneImg);
                            } else if (mainImage == null || mainImage.trim().isEmpty()) {
                                dto.setImage("assets/images/default-iphone.jpg");
                            }
                        }
                        break;

                    case 2: // iPad
                        if (rs.getObject("iPadDetailID") != null) {
                            dto.setDetailId(rs.getInt("iPadDetailID"));
                            dto.setVersion(rs.getString("iPadVersion"));
                            dto.setColor(rs.getString("iPadColor"));
                            dto.setStorage(rs.getString("iPadStorage"));
                            dto.setPrice(rs.getDouble("iPadPrice"));

                            // ===== FIX: Ưu tiên ảnh chi tiết với đường dẫn đúng =====
                            String iPadImg = rs.getString("iPadImageURL");
                            if (iPadImg != null && !iPadImg.trim().isEmpty()) {
                                dto.setImage(iPadImg);
                            } else if (mainImage == null || mainImage.trim().isEmpty()) {
                                dto.setImage("assets/images/default-ipad.jpg");
                            }
                        }
                        break;

                    case 3: // MacBook
                        if (rs.getObject("MacDetailID") != null) {
                            dto.setDetailId(rs.getInt("MacDetailID"));
                            dto.setVersion(rs.getString("MacVersion"));
                            dto.setColor(rs.getString("MacColor"));
                            dto.setStorage(rs.getString("MacStorage"));
                            dto.setPrice(rs.getDouble("MacPrice"));

                            // ===== FIX: Ưu tiên ảnh chi tiết với đường dẫn đúng =====
                            String macImg = rs.getString("MacImageURL");
                            if (macImg != null && !macImg.trim().isEmpty()) {
                                dto.setImage(macImg);
                            } else if (mainImage == null || mainImage.trim().isEmpty()) {
                                dto.setImage("assets/images/default-macbook.jpg");
                            }
                        }
                        break;
                }

                // ===== DEBUG: Log để kiểm tra =====
                System.out.println("Product: " + dto.getProductName() + " - Image: " + dto.getImage());

                list.add(dto);
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllProducts: " + e.getMessage());
            throw e;
        }

        return list;
    }

    public List<ProductDTO> getProductsPaging(String keyword, int page, int pageSize) throws SQLException {
        List<ProductDTO> list = new ArrayList<>();

        String sql = "{CALL PhanTrang(null, null, null, null, ?, ?, ?)}";
        Object[] params = new Object[]{
            keyword == null ? null : keyword,
            page,
            pageSize
        };

        ResultSet rs = executeSelectQuery(sql, params);

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
            dto.setDetailId(rs.getInt("DetailID"));
            list.add(dto);
        }

        return list;
    }

    public int countProductByFilter(String keyword, Integer categoryId) throws SQLException {
        String sql = "{CALL CountProductsByFilter(?, NULL, NULL, NULL, ?)}";
        Object[] params = new Object[]{categoryId, keyword};

        ResultSet rs = executeSelectQuery(sql, params);
        if (rs.next()) {
            return rs.getInt("Total");
        }
        return 0;
    }

}
