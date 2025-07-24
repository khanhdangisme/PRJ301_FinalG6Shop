/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import constant.ParamConstant;
import constant.PathConstant;
import dao.AdminProductDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Product;
import model.ProductDTO;

@WebServlet(name = "ProductServlet", urlPatterns = {"/product"})
public class ProductAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String view = request.getParameter(ParamConstant.VIEW);

        if (view == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing view parameter");
            return;
        }

        switch (view) {
            case "details": {
                try {
                    int id = Integer.parseInt(request.getParameter(ParamConstant.ID));
                    String color = request.getParameter(ParamConstant.COLOR);
                    String storage = request.getParameter(ParamConstant.STORAGE);

                    AdminProductDAO dao = new AdminProductDAO();
                    Product productDao = dao.getProductDetail(id, color, storage);

                    if (productDao == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                        return;
                    }

                    request.setAttribute("getDetail", productDao);
                    request.getRequestDispatcher(PathConstant.URL_ADMIN_DETAILS).forward(request, response);
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID format");
                } catch (SQLException ex) {
                    Logger.getLogger(ProductAdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
                }
                break;
            }
            case "delete": {
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    int categoryId = Integer.parseInt(request.getParameter("categoryId"));
                    String color = request.getParameter("color");
                    String storage = request.getParameter("storage");

                    AdminProductDAO dao = new AdminProductDAO();
                    boolean deleted = dao.deleteProduct(categoryId, id, color, storage);

                    if (deleted) {
                        response.sendRedirect(request.getContextPath() + "/admin?view=product");
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found or already deleted.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error deleting product.");
                }
                break;
            }

            case "add": {
                try {
                    AdminProductDAO dao = new AdminProductDAO();

                    // Lấy danh sách loại sản phẩm
                    List<Product> categories = dao.getAllCategory();

                    // Lấy danh sách ảnh từ thư mục assets/img
                    List<String> images = dao.getAvailableImages("assets/img");

                    request.setAttribute("list", categories);
                    request.setAttribute("images", images);

                    // Chuyển tới trang add.jsp
                    request.getRequestDispatcher("/WEB-INF/admin/add.jsp").forward(request, response);
                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading add product page.");
                }
                break;
            }

            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid view parameter");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String view = request.getParameter("view");
        if ("add".equals(view)) {
            try {
                String name = escapeHtml(request.getParameter("productName"));
                String version = escapeHtml(request.getParameter("version"));
                String color = escapeHtml(request.getParameter("color"));
                String storage = escapeHtml(request.getParameter("storage"));
                String priceParam = request.getParameter("price");    // số, không cần escape
                String quantityParam = request.getParameter("quantity"); // số, không cần escape
                int categoryId = Integer.parseInt(request.getParameter("categoryId"));
                String image = escapeHtml(request.getParameter("productImage"));

                double price = Double.parseDouble(priceParam);
                int quantity = Integer.parseInt(quantityParam);

                String screenSize = escapeHtml(request.getParameter("screenSize"));
                String rearCamera = escapeHtml(request.getParameter("rearCamera"));
                String frontCamera = escapeHtml(request.getParameter("frontCamera"));
                String chipset = escapeHtml(request.getParameter("chipset"));
                String battery = escapeHtml(request.getParameter("battery"));
                String simType = escapeHtml(request.getParameter("simType"));
                String os = escapeHtml(request.getParameter("os"));
                String resolution = escapeHtml(request.getParameter("resolution"));
                String screenFeatures = escapeHtml(request.getParameter("screenFeatures"));
                String cpuType = escapeHtml(request.getParameter("cpuType"));
                String ram = escapeHtml(request.getParameter("ram"));
                String gpuType = escapeHtml(request.getParameter("gpuType"));
                String screenTech = escapeHtml(request.getParameter("screenTech"));
                String ports = escapeHtml(request.getParameter("ports"));

                AdminProductDAO dao = new AdminProductDAO();

                System.out.println("==== FORM INPUT ====");
                System.out.println("Name: " + name);
                System.out.println("Version: " + version);
                System.out.println("Color: " + color);
                System.out.println("Storage: " + storage);
                System.out.println("Price: " + price);
                System.out.println("Quantity: " + quantity);
                System.out.println("CategoryID: " + categoryId);
                System.out.println("MainImage: " + image);
                System.out.println("ScreenSize: " + screenSize);
                System.out.println("RearCamera: " + rearCamera);
                System.out.println("FrontCamera: " + frontCamera);
                System.out.println("Chipset: " + chipset);
                System.out.println("Battery: " + battery);
                System.out.println("SimType: " + simType);
                System.out.println("OS: " + os);
                System.out.println("Resolution: " + resolution);
                System.out.println("ScreenFeatures: " + screenFeatures);
                System.out.println("CPUType: " + cpuType);
                System.out.println("RAM: " + ram);
                System.out.println("GPUType: " + gpuType);
                System.out.println("ScreenTech: " + screenTech);
                System.out.println("Ports: " + ports);
                System.out.println("=====================");

                boolean inserted = dao.insertProductDetail(
                        name, version, color, storage, price, quantity, categoryId, image,
                        screenSize, rearCamera, frontCamera, chipset, battery, simType,
                        os, resolution, screenFeatures, cpuType, ram, gpuType, screenTech, ports
                );

                if (inserted) {
                    response.sendRedirect(request.getContextPath() + "/admin?view=product");
                } else {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Insert failed");
                }

            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error inserting product: " + e.getMessage());
            }
        } else if ("edit".equals(view)) {
            try {
                int productId = Integer.parseInt(request.getParameter("id"));
                int categoryId = Integer.parseInt(request.getParameter("categoryId"));

                // NEW: Lấy giá trị cũ
                String oldColor = escapeHtml(request.getParameter("oldColor"));
                String oldStorage = escapeHtml(request.getParameter("oldStorage"));

                // NEW: Lấy giá trị mới
                String name = escapeHtml(request.getParameter("productName"));
                String version = escapeHtml(request.getParameter("version"));
                String color = escapeHtml(request.getParameter("color"));
                String storage = escapeHtml(request.getParameter("storage"));
                double price = Double.parseDouble(request.getParameter("price"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));

                // Các thuộc tính đặc thù
                String screenSize = escapeHtml(request.getParameter("screenSize"));
                String rearCamera = escapeHtml(request.getParameter("rearCamera"));
                String frontCamera = escapeHtml(request.getParameter("frontCamera"));
                String chipset = escapeHtml(request.getParameter("chipset"));
                String battery = escapeHtml(request.getParameter("battery"));
                String simType = escapeHtml(request.getParameter("simType"));
                String os = escapeHtml(request.getParameter("os"));
                String resolution = escapeHtml(request.getParameter("resolution"));
                String screenFeatures = escapeHtml(request.getParameter("screenFeatures"));
                String cpuType = escapeHtml(request.getParameter("cpuType"));
                String ram = escapeHtml(request.getParameter("ram"));
                String gpuType = escapeHtml(request.getParameter("gpuType"));
                String screenTech = escapeHtml(request.getParameter("screenTech"));
                String ports = escapeHtml(request.getParameter("ports"));

                AdminProductDAO dao = new AdminProductDAO();

                // ✅ Lấy product cũ theo oldColor + oldStorage
                Product existing = dao.getProductDetail(productId, oldColor, oldStorage);
                if (existing == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Original product not found.");
                    return;
                }

                existing.setProductName(name);
                existing.setProductQuatity(quantity);

                Object detail = null;

                // Gán lại dữ liệu tuỳ loại sản phẩm
                if (categoryId == 1 && existing instanceof model.iPhoneDetails) {
                    model.iPhoneDetails iphone = (model.iPhoneDetails) existing;
                    iphone.setiPhoneVersion(version);
                    iphone.setiPhoneColor(color);
                    iphone.setiPhoneStorage(storage);
                    iphone.setiPhonePrice(price);
                    iphone.setiPhoneQuantity(quantity);
                    iphone.setiPhoneScreenSize(screenSize);
                    iphone.setiPhoneRearCamera(rearCamera);
                    iphone.setiPhoneFrontCamera(frontCamera);
                    iphone.setiPhoneChipset(chipset);
                    iphone.setiPhoneBattery(battery);
                    iphone.setiPhoneSimType(simType);
                    iphone.setiPhoneOs(os);
                    iphone.setiPhoneResolution(resolution);
                    iphone.setiPhoneScreenFeatures(screenFeatures);
                    iphone.setiPhoneCpuType(cpuType);
                    detail = iphone;

                } else if (categoryId == 2 && existing instanceof model.iPadDetails) {
                    model.iPadDetails ipad = (model.iPadDetails) existing;
                    ipad.setiPadVersion(version);
                    ipad.setiPadColor(color);
                    ipad.setiPadStorage(storage);
                    ipad.setiPadPrice(price);
                    ipad.setiPadQuantity(quantity);
                    ipad.setiPadScreenSize(screenSize);
                    ipad.setiPadRearCamera(rearCamera);
                    ipad.setiPadFrontCamera(frontCamera);
                    ipad.setiPadChipset(chipset);
                    ipad.setiPadBattery(battery);
                    ipad.setiPadSimType(simType);
                    ipad.setiPadOs(os);
                    ipad.setiPadResolution(resolution);
                    ipad.setiPadScreenFeatures(screenFeatures);
                    ipad.setiPadCpuType(cpuType);
                    detail = ipad;

                } else if (categoryId == 3 && existing instanceof model.MacbookDetails) {
                    model.MacbookDetails mac = (model.MacbookDetails) existing;
                    mac.setMacVersion(version);
                    mac.setMacColor(color);
                    mac.setMacStorage(storage);
                    mac.setMacPrice(price);
                    mac.setMacQuantity(quantity);
                    mac.setMacScreenSize(screenSize);
                    mac.setMacBattery(battery);
                    mac.setMacOs(os);
                    mac.setMacResolution(resolution);
                    mac.setMacCpuType(cpuType);
                    mac.setMacRam(ram);
                    mac.setMacGpuType(gpuType);
                    mac.setMacScreenTech(screenTech);
                    mac.setMacPorts(ports);
                    detail = mac;

                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product category or mismatch class");
                    return;
                }

                // Gọi DAO cập nhật
                boolean updated = dao.updateProductDetail(existing, detail);
                if (updated) {
                    response.sendRedirect(request.getContextPath() + "/admin?view=product");
                } else {
                    request.setAttribute("error", "Update failed");
                    request.setAttribute("getDetail", existing);
                    request.getRequestDispatcher(PathConstant.URL_ADMIN_DETAILS).forward(request, response);
                }

            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Update failed: " + e.getMessage());
            }
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private String escapeHtml(String input) {
        if (input == null) {
            return null;
        }
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }

}
