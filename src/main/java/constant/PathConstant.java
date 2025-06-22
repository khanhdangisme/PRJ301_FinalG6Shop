/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package constant;

/**
 *
 * @author email
 */
public class PathConstant {
    //JSP (neu request.getRequestDispatcher thi chuyen ve thang URL .jsp) (khong load lai trang)
    public static final String URL_USER_PROFILE = "/WEB-INF/user/profile.jsp";
    public static final String URL_USER_UPDATE_PROFILE = "/WEB-INF/user/update.jsp";
    public static final String URL_USER_UPDATE_PASSWORD = "/WEB-INF/user/updatePassword.jsp";
    
    public static final String URL_ADMIN_DASHBOARD = "/WEB-INF/admin/dashboard.jsp";
    public static final String URL_ADMIN_CUSTOMERS = "/WEB-INF/admin/customer.jsp";
    public static final String URL_ADMIN_PRODUCT = "/WEB-INF/admin/product.jsp";
    public static final String URL_ADMIN_DETAILS = "/WEB-INF/admin/details.jsp";
             
    public static final String URL_LOGIN = "/WEB-INF/common/login.jsp";
    public static final String URL_REGISTER = "/WEB-INF/common/register.jsp";
    
    public static final String URL_INDEX = "index.jsp";
    public static final String URL_SHOP = "/WEB-INF/view/shop.jsp";
    
    //Servlet (neu response.sendRedirect thi them request.getContextPath() + URL servlet)
    public static final String URL_SERVLET_LOGIN = "/login";
    
    //SendRedirect (giup load lai trang)
    public static final String URL_SERVLET_ADMIN_CUSTOMERS = "/admin?view=customer";
    public static final String URL_SERVLET_USER_PROFILE = "/user?view=profile";
  
}
