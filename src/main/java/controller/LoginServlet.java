/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import constant.AttributeConstant;
import constant.MessageConstant;
import constant.ParamConstant;
import constant.PathConstant;
import dao.ShopProductDAO;
import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ProductDTO;
import model.User;

/**
 *
 * @author email
 */
@WebServlet(name = "LoginController", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private static final String REDIRECT_ATTR = "redirectAfterLogin";

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
            out.println("<title>Servlet LoginController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginController at " + request.getContextPath() + "</h1>");
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
        // Đọc cookie username
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("username".equals(c.getName()) && !c.getValue().isEmpty()) {
                    request.setAttribute("savedUsername", c.getValue());
                    break;
                }
            }
        }

        request.getRequestDispatcher(PathConstant.URL_LOGIN).forward(request, response);
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

        String username = request.getParameter(ParamConstant.USERNAME);
        String password = request.getParameter(ParamConstant.PASSWORD);
        String remember = request.getParameter(ParamConstant.REMEMBER_ME);
        HttpSession session = request.getSession();

        if (username != null && !username.trim().isEmpty()
                && password != null && !password.trim().isEmpty()) {
            UserDAO dao = new UserDAO();
            User loggedUser = dao.login(username, password);

            if (loggedUser != null) {
                session.setAttribute(AttributeConstant.LOGGEDUSER, loggedUser);
                session.setMaxInactiveInterval(15 * 60);

                // Xóa chỉ cookie guest cart
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie c : cookies) {
                        if ("cart_guest".equals(c.getName())) {
                            Cookie guestCookie = new Cookie("cart_guest", "");
                            guestCookie.setMaxAge(0);
                            guestCookie.setPath("/");
                            response.addCookie(guestCookie);
                            break;
                        }
                    }
                }

                // Khôi phục giỏ của tài khoản hiện tại
                @SuppressWarnings("unchecked")
                Map<String, Integer> cart = new HashMap<String, Integer>();
                String cartCookieName = "cart_" + username;
                if (cookies != null) {
                    for (Cookie c : cookies) {
                        if (cartCookieName.equals(c.getName())) {
                            try {
                                String cartRaw = URLDecoder.decode(c.getValue(), "UTF-8");
                                String[] items = cartRaw.split(",");
                                for (String item : items) {
                                    String[] parts = item.split("=");
                                    if (parts.length == 2) {
                                        cart.put(parts[0], Integer.parseInt(parts[1]));
                                    }
                                }
                            } catch (Exception e) {
                                Logger.getLogger(LoginServlet.class.getName()).log(Level.WARNING, "Invalid cart cookie", e);
                            }
                            break;
                        }
                    }
                }
                session.setAttribute(AttributeConstant.CART, cart);

                // Lưu guest cart riêng biệt
                @SuppressWarnings("unchecked")
                Map<String, Integer> guestCart = new HashMap<String, Integer>();
                if (cookies != null) {
                    for (Cookie c : cookies) {
                        if ("cart_guest".equals(c.getName())) {
                            try {
                                String cartRaw = URLDecoder.decode(c.getValue(), "UTF-8");
                                String[] items = cartRaw.split(",");
                                for (String item : items) {
                                    String[] parts = item.split("=");
                                    if (parts.length == 2) {
                                        guestCart.put(parts[0], Integer.parseInt(parts[1]));
                                    }
                                }
                            } catch (Exception e) {
                                Logger.getLogger(LoginServlet.class.getName()).log(Level.WARNING, "Invalid guest cart cookie", e);
                            }
                            break;
                        }
                    }
                }
                session.setAttribute("GUEST_CART", guestCart);

                // Đếm tổng số lượng item trong cart
                int cartCount = 0;
                for (Integer qty : cart.values()) {
                    cartCount += qty;
                }
                session.setAttribute(AttributeConstant.CART_COUNT, cartCount);

                // Ghi cookie remember-me
                Cookie usernameCookie = new Cookie("username", "remember-me".equals(remember) ? username : "");
                usernameCookie.setMaxAge("remember-me".equals(remember) ? 7 * 24 * 60 * 60 : 0);
                usernameCookie.setPath("/");
                response.addCookie(usernameCookie);

                // Ghi cookie theme
                Cookie themeCookie = new Cookie("theme", "dark");
                themeCookie.setMaxAge(24 * 60 * 60);
                themeCookie.setPath("/");
                response.addCookie(themeCookie);

                // Gửi thông báo thành công
                session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.LOGIN_SUCCESSFULLY);
                session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.SUCCESS);

                // Chuyển đến checkout với guest cart
                String redirect = (String) session.getAttribute("redirectAfterLogin");
                if (redirect != null && redirect.contains("checkout")) {
                    session.removeAttribute("redirectAfterLogin");
                    response.sendRedirect(redirect);
                } else {
                    response.sendRedirect(request.getContextPath() + "/index.jsp");
                }
                return;
            }
        }

        // Login thất bại
        request.setAttribute(AttributeConstant.USERNAME, username);
        session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.LOGIN_ERROR);
        session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
        request.getRequestDispatcher(PathConstant.URL_LOGIN).forward(request, response);
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
