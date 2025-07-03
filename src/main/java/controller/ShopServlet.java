/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import constant.AttributeConstant;
import constant.ParamConstant;
import constant.PathConstant;
import dao.ShopProductDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Product;
import model.ProductDTO;

/**
 *
 * @author email
 */
@WebServlet(name = "ShopServlet", urlPatterns = {"/shop"})
public class ShopServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ShopServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ShopServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String view = request.getParameter(ParamConstant.VIEW);
        ShopProductDAO dao = new ShopProductDAO();
        if ("details".equals(view)) {
            try {
                int id = Integer.parseInt(request.getParameter(ParamConstant.ID));
                String color = request.getParameter(ParamConstant.COLOR);
                String storage = request.getParameter(ParamConstant.STORAGE);

                List<Product> cate = dao.getAllCategory();
                request.setAttribute(AttributeConstant.LIST, cate);

                // 2. Lấy product của từng category
                Map<Integer, List<ProductDTO>> productsMap = new HashMap<>();
                for (Product c : cate) {
                    List<ProductDTO> prods = dao.getProductsForCategory(c.getCategoryID());
                    productsMap.put(c.getCategoryID(), prods);
                }
                request.setAttribute("productsMap", productsMap);

                Product productDao = dao.getProductDetail(id, color, storage);

                if (productDao == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                    return;
                }

                request.setAttribute("getDetail", productDao);
                request.getRequestDispatcher(PathConstant.URL_SHOP_DETAILS).forward(request, response);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID format");
            } catch (SQLException ex) {
                Logger.getLogger(ProductAdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
            }
            return;
        } else if ("search".equals(view)) {
            try {
                String keyword = request.getParameter("query").toLowerCase();  // Lấy từ khóa tìm kiếm
                
                List<Product> cate = dao.getAllCategory();
                request.setAttribute(AttributeConstant.LIST, cate);
                // 2. Lấy product của từng category
                Map<Integer, List<ProductDTO>> productsMap = new HashMap<>();
                for (Product c : cate) {
                    List<ProductDTO> prods = dao.getProductsForCategory(c.getCategoryID());
                    productsMap.put(c.getCategoryID(), prods);
                }
                request.setAttribute("productsMap", productsMap);

                // Lấy tất cả sản phẩm
                List<ProductDTO> allProducts = dao.getAllProducts();
                List<ProductDTO> searchResults = new ArrayList<>();

                // Duyệt qua tất cả sản phẩm và kiểm tra xem tên, version, hay storage có chứa từ khóa tìm kiếm không
                for (ProductDTO dto : allProducts) {
                    if (dto.getProductName().toLowerCase().contains(keyword)
                        || (dto.getVersion() != null && dto.getVersion().toLowerCase().contains(keyword))
                        || (dto.getStorage() != null && dto.getStorage().toLowerCase().contains(keyword))) {
                        searchResults.add(dto);
                    }
                }

                // Gửi kết quả tìm kiếm về view
                request.setAttribute("productDetail", searchResults);
                request.setAttribute("searchKeyword", keyword);  // Thêm từ khóa tìm kiếm vào để hiển thị
                request.getRequestDispatcher("/WEB-INF/view/shop.jsp").forward(request, response);
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Search failed");
            }
            return;
        }
        try {
            // 1. Lấy danh sách category
            List<Product> cate = dao.getAllCategory();
            request.setAttribute(AttributeConstant.LIST, cate);

            // 2. Lấy product của từng category
            Map<Integer, List<ProductDTO>> productsMap = new HashMap<>();
            for (Product c : cate) {
                List<ProductDTO> prods = dao.getProductsForCategory(c.getCategoryID());
                productsMap.put(c.getCategoryID(), prods);
            }
            request.setAttribute("productsMap", productsMap);

            // 3. nếu có productId thì show chi tiết
            String pidRaw = request.getParameter("productId");
            if (pidRaw != null) {
                int pid = Integer.parseInt(pidRaw);
                List<ProductDTO> detail = dao.getProductItem(pid);
                request.setAttribute("productDetail", detail);
            } else {
                // nếu chưa chọn productId, load toàn bộ sản phẩm
                List<ProductDTO> allProducts = dao.getProduct(null);
                request.setAttribute("productDetail", allProducts);
            }

            request.getRequestDispatcher(PathConstant.URL_SHOP).forward(request, response);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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

}
