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
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;

/**
 *
 * @author email
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

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
            out.println("<title>Servlet RegisterServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegisterServlet at " + request.getContextPath() + "</h1>");
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
        request.getRequestDispatcher(PathConstant.URL_REGISTER).forward(request, response);
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
        String fullname = request.getParameter(ParamConstant.FULLNAME);
        String email = request.getParameter(ParamConstant.EMAIL);
        String phone = request.getParameter(ParamConstant.PHONE);

        UserDAO dao = new UserDAO();
        HttpSession session = request.getSession();
        // Kiểm tra người dùng đã tồn tại chưa
        if (dao.checkUserExists(username)) {
            session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.REGISTER_ERROR_EXISTS);
            session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
            request.setAttribute(AttributeConstant.USERNAME, username);
            request.setAttribute(AttributeConstant.FULLNAME, fullname);
            request.setAttribute(AttributeConstant.EMAIL, email);
            request.setAttribute(AttributeConstant.PHONE, phone);
            request.getRequestDispatcher(PathConstant.URL_REGISTER).forward(request, response);
            return;
        }
        User newUser = new User(0, username, password, fullname, email, phone, 1, null, "Enable"); // Role = 1 là customer

        boolean inserted = false;
        try {
            inserted = dao.insertUser(newUser);
        } catch (SQLException ex) {
            Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (inserted) {
            session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.REGISTER_SUCCESSFULLY);
            session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.SUCCESS);
            response.sendRedirect(request.getContextPath() + PathConstant.URL_SERVLET_LOGIN);
        } else {
            session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.REGISTER_ERROR);
            session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
            request.setAttribute(AttributeConstant.USERNAME, username);
            request.setAttribute(AttributeConstant.FULLNAME, fullname);
            request.setAttribute(AttributeConstant.EMAIL, email);
            request.setAttribute(AttributeConstant.PHONE, phone);
            request.getRequestDispatcher(PathConstant.URL_REGISTER).forward(request, response);
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
