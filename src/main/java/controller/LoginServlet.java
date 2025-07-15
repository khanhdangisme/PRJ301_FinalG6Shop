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

        // 1. Lấy dữ liệu
        String username = request.getParameter(ParamConstant.USERNAME);
        String password = request.getParameter(ParamConstant.PASSWORD);
        String remember = request.getParameter(ParamConstant.REMEMBER_ME);

        HttpSession session = request.getSession();
        
        if (username != null && !username.trim().isEmpty() && password != null && !password.trim().isEmpty()) {
            UserDAO dao = new UserDAO();
            User loggedUser = dao.login(username, password);

            /* ====================  ĐĂNG NHẬP THÀNH CÔNG  ==================== */
            if (loggedUser != null) {
                // Lưu session
                session.setAttribute(AttributeConstant.LOGGEDUSER, loggedUser);
                session.setMaxInactiveInterval(15 * 60);

                /* ---- Remember‑me ---- */
                Cookie usernameCookie = new Cookie("username", "remember-me".equals(remember) ? username : "");
                usernameCookie.setMaxAge("remember-me".equals(remember) ? 7 * 24 * 60 * 60 : 0);
                response.addCookie(usernameCookie);

                /* ---- (tuỳ chọn) Giao diện dark ---- */
                Cookie themeCookie = new Cookie("theme", "dark");
                themeCookie.setMaxAge(24 * 60 * 60);
                response.addCookie(themeCookie);

                /* ---- Thông báo ---- */
                session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.LOGIN_SUCCESSFULLY);
                session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.SUCCESS);

                /* ---- Chuyển hướng ---- */
                String redirect = (String) session.getAttribute(REDIRECT_ATTR);
                if (redirect != null) {
                    session.removeAttribute(REDIRECT_ATTR);
                    response.sendRedirect(redirect);
                } else {
                    response.sendRedirect(request.getContextPath() + "/index.jsp");
                }
                return;
            }
        }

        /* ====================  ĐĂNG NHẬP THẤT BẠI  ==================== */
        request.setAttribute(AttributeConstant.USERNAME, username);
        session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.LOGIN_ERROR);
        session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);

        // forward về trang login; KHÔNG redirect nữa
        request.getRequestDispatcher(PathConstant.URL_LOGIN).forward(request, response);
        // không cần return – forward đã kết thúc luồng
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
