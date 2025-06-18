/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import constant.AttributeConstant;
import constant.MessageConstant;
import constant.ParamConstant;
import constant.PathConstant;
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
import model.User;

/**
 *
 * @author email
 */
@WebServlet(name = "LoginController", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

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
        String remember = request.getParameter(ParamConstant.REMEMBER_ME); // null nếu không tích

        UserDAO dao = new UserDAO();
        User loggedUser = dao.login(username, password);
        HttpSession session = request.getSession();

        if (loggedUser != null) {
            // Tạo session
            session.setAttribute(AttributeConstant.LOGGEDUSER, loggedUser);
//            request.getSession().setMaxInactiveInterval(1 * 60);

            // Xử lý Remember Me
            if ("remember-me".equals(remember)) {
                // Người dùng đã tích checkbox -> tạo cookie
                Cookie usernameCookie = new Cookie("username", username);
                usernameCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
                response.addCookie(usernameCookie);
            } else {
                // Không tích -> xóa cookie (nếu từng có)
                Cookie usernameCookie = new Cookie("username", "");
                usernameCookie.setMaxAge(0); // xóa cookie
                response.addCookie(usernameCookie);
            }

            // (Tùy chọn) Cookie theme
            Cookie themeCookie = new Cookie("theme", "dark");
            themeCookie.setMaxAge(24 * 60 * 60); // 1 ngày
            response.addCookie(themeCookie);

            session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.LOGIN_SUCCESSFULLY);
            session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.SUCCESS); 
            // Chuyển về trang chủ
            response.sendRedirect(request.getContextPath());
        } else {
            // Đăng nhập thất bại
            request.setAttribute(AttributeConstant.USERNAME, username);
            session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.LOGIN_ERROR);
            session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
            request.getRequestDispatcher(PathConstant.URL_LOGIN).forward(request, response);
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

}
