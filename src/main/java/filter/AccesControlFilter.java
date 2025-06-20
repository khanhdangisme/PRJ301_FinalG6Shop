/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filter;

import constant.AttributeConstant;
import constant.MessageConstant;
import constant.ParamConstant;
import constant.PathConstant;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import model.User;

/**
 *
 * @author email
 */
@WebFilter("/*")
public class AccesControlFilter implements Filter {

    // Danh sách các đường dẫn yêu cầu phải đăng nhập
    private static final List<String> PROTECTED_PATHS = Arrays.asList(
            "/admin",
            "/user"
    );

    @Override
    public void doFilter(ServletRequest sr, ServletResponse srp, FilterChain fc)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) sr;
        HttpServletResponse res = (HttpServletResponse) srp;
        HttpSession session = req.getSession(false);

        String path = req.getRequestURI().substring(req.getContextPath().length());
        User user = (session != null) ? (User) session.getAttribute("loggedUser") : null;

        boolean protectedPath = false;
        for (String prefix : PROTECTED_PATHS) {
            if (path.startsWith(prefix)) {
                protectedPath = true;
                break;
            }
        }

        // 1. Chặn nếu chưa đăng nhập
        if (user == null && protectedPath) {
            HttpSession newSession = req.getSession(true);
            newSession.setAttribute(AttributeConstant.MESSAGE, MessageConstant.FILTER_BLOCK_NOT_LOGIN);
            newSession.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
            res.sendRedirect(req.getContextPath() + PathConstant.URL_SERVLET_LOGIN);
            return;
        }

        // 2. Không cho người đã login quay lại login.jsp
        if ("/login".equals(path) && user != null && session != null) {
            session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.FILTER_BLOCK_LOGIN);
            session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
            res.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        // 3. Chặn nếu status ≠ "Enable"
        if (user != null && session != null && !"Enable".equals(user.getStatus())) {
            session.invalidate();
            HttpSession newSession = req.getSession(true);
            newSession.setAttribute(AttributeConstant.MESSAGE, MessageConstant.FILTER_BLOCK_NOT_ACCESS);
            newSession.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
            req.getRequestDispatcher(PathConstant.URL_SERVLET_LOGIN).forward(req, res);
            return;
        }

        // 4. Chặn theo role
        int role = (user != null) ? user.getUserRole() : -1;

        // 4.1 Role admin
        if (path.startsWith("/admin") && role != 0 && session != null) {
            session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.FILTER_BLOCK_NOT_ADMIN);
            session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
            res.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        // 4.2 Role user
        if (path.startsWith("/user") && role != 0 && role != 1 && session != null) {
            session.setAttribute(AttributeConstant.MESSAGE, MessageConstant.FILTER_BLOCK_NOT_ADMIN);
            session.setAttribute(AttributeConstant.MESSAGETYPE, MessageConstant.DANGER);
            res.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        // Nếu không bị chặn, cho qua
        fc.doFilter(sr, srp);
    }

}
