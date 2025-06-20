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
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;

/**
 *
 * @author email
 */
@WebServlet(name = "UserServlet", urlPatterns = {"/user"})
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class UserServlet extends HttpServlet {

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
            out.println("<title>Servlet UserServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UserServlet at " + request.getContextPath() + "</h1>");
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

        switch (view) {
            case "profile":
                request.getRequestDispatcher(PathConstant.URL_USER_PROFILE).forward(request, response);
                break;
            case "update-profile":
                request.getRequestDispatcher(PathConstant.URL_USER_UPDATE_PROFILE).forward(request, response);
                break;
            case "update-password":
                request.getRequestDispatcher(PathConstant.URL_USER_UPDATE_PASSWORD).forward(request, response);
                break;
            default:
                request.getRequestDispatcher(PathConstant.URL_USER_PROFILE).forward(request, response);
                break;
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

        String action = request.getParameter(ParamConstant.ACTION);
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("loggedUser"); // KHÔNG cần check null nữa
        UserDAO dao = new UserDAO();

        if ("save-profile".equals(action)) {

            // Lấy dữ liệu từ form
            String username = request.getParameter(ParamConstant.USERNAME);
            String fullname = request.getParameter(ParamConstant.FULLNAME);
            String email = request.getParameter(ParamConstant.EMAIL);
            String phone = request.getParameter(ParamConstant.PHONE);

            // Giữ nguyên avatar hiện tại
            String avatarUrl = user.getAvatarUrl();

            // Xử lý ảnh mới nếu có
            Part avatarPart = request.getPart("avatar");

            if (avatarPart != null && avatarPart.getSize() > 0) {
                String uploadPath = request.getServletContext().getRealPath("/assets/uploads");
                File dir = new File(uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                String filename = UUID.randomUUID() + "_" + Paths.get(avatarPart.getSubmittedFileName()).getFileName().toString();
                avatarPart.write(uploadPath + File.separator + filename);
                avatarUrl = "assets/uploads/" + filename;
            }

            // Gán lại thông tin cho user hiện tại
            user.setUserName(username);
            user.setUserFullname(fullname);
            user.setUserEmail(email);
            user.setUserPhone(phone);
            user.setAvatarUrl(avatarUrl);

            // Gọi DAO để cập nhật
            boolean updated = false;
            try {
                updated = dao.updateUser(user);
            } catch (SQLException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }

            if (updated) {
                session.setAttribute(AttributeConstant.LOGGEDUSER, user);
                session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.UPDATE_SUCCESSFULLY);
                session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.SUCCESS);
                response.sendRedirect(request.getContextPath() + PathConstant.URL_SERVLET_USER_PROFILE);
            } else {
                session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.UPDATE_UNSUCCESSFULLY);
                session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                request.getRequestDispatcher(PathConstant.URL_USER_UPDATE_PROFILE).forward(request, response);
            }
        } else if ("change-password".equals(action)) {

            String username = request.getParameter(AttributeConstant.USERNAME);
            String currentPassword = request.getParameter(AttributeConstant.CURRENT_PASSWORD);
            String newPassword = request.getParameter(AttributeConstant.NEW_PASSWORD);

            boolean correct = dao.checkExistsPassword(username, currentPassword);

            if (!correct) {
                session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.UPDATE_CURRENTPASSWORD_ERROR);
                session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                request.getRequestDispatcher(PathConstant.URL_USER_UPDATE_PASSWORD).forward(request, response);
            } else {
                boolean updated = false;
                try {
                    updated = dao.updateUserPassword(username, newPassword);
                } catch (SQLException ex) {
                    Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (updated) {
                    session.setAttribute(AttributeConstant.LOGGEDUSER, user);
                    session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.UPDATE_PASSWORD);
                    session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.SUCCESS);
                    response.sendRedirect(request.getContextPath() + PathConstant.URL_SERVLET_USER_PROFILE);
                } else {
                    session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.UPDATE_PASSWORD_ERROR);
                    session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                    request.getRequestDispatcher(PathConstant.URL_USER_UPDATE_PASSWORD).forward(request, response);
                }

            }
        } else if ("delete".equals(action)) {
            String username = request.getParameter(AttributeConstant.USERNAME);
            String password = request.getParameter(AttributeConstant.CURRENT_PASSWORD);

            boolean correct = dao.checkExistsPassword(username, password);
            if (!correct) {
                session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.DELETE_PASSWORD_ERROR);
                session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                request.getRequestDispatcher(PathConstant.URL_USER_PROFILE).forward(request, response);
            } else {
                boolean updated = false;
                try {
                    updated = dao.deleteUser(username, password);
                } catch (SQLException ex) {
                    Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (updated) {
                    session.invalidate();

                    // Tạo session mới để chứa thông báo
                    HttpSession newSession = request.getSession(true);
                    newSession.setAttribute(AttributeConstant.MESSAGE, MessageConstant.DELETE_PASSWORD);
                    newSession.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.SUCCESS);
                    response.sendRedirect(request.getContextPath() + PathConstant.URL_SERVLET_LOGIN);
                } else {
                    session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.DELETE_PASSWORD_ERROR);
                    session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
                    request.getRequestDispatcher(PathConstant.URL_USER_UPDATE_PASSWORD).forward(request, response);
                }
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

}
