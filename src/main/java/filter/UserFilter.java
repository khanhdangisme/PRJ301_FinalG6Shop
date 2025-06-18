/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filter;

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
import model.User;

/**
 *
 * @author email
 */
@WebFilter("/user/*")
public class UserFilter implements Filter{

    @Override
    public void doFilter(ServletRequest sr, ServletResponse srp, FilterChain fc) 
        throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) sr;
        HttpServletResponse res = (HttpServletResponse) srp;
        HttpSession session = req.getSession(false);

        User user = (session != null) ? (User) session.getAttribute("loggedUser") : null;

        if (user == null) {
            // chưa đăng nhập
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (user.getUserRole() != 1 && user.getUserRole() != 0) {
            // không phải admin
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied. Admins only.");
            return;
        }

        fc.doFilter(sr, srp); 
    }
    
}
