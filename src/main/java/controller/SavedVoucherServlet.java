package controller;

import dao.VoucherDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Voucher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/savedVoucher")
public class SavedVoucherServlet extends HttpServlet {

    // POST: Lưu mã vào session
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String code = request.getParameter("code");
        HttpSession session = request.getSession();
        List<String> savedCodes = (List<String>) session.getAttribute("savedCodes");

        if (savedCodes == null) {
            savedCodes = new ArrayList<>();
        }

        if (code != null && !code.trim().isEmpty() && !savedCodes.contains(code.trim())) {
            savedCodes.add(code.trim());
            session.setAttribute("savedMessage", "🎉 Code saved \"" + code + "\" successfully!");
        } else {
            session.setAttribute("savedMessage", "⚠️ Code \"" + code + "\" previously saved.");
        }

        session.setAttribute("savedCodes", savedCodes);
        response.sendRedirect("voucher");
    }

    // GET: Hiển thị các mã đã lưu
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        List<String> savedCodes = (List<String>) session.getAttribute("savedCodes");
        List<Voucher> savedVouchers = new ArrayList<>();
        VoucherDAO dao = new VoucherDAO();

        if (savedCodes != null) {
            for (String savedCode : savedCodes) {
                Voucher v = dao.getVoucherByCode(savedCode);
                if (v != null) {
                    savedVouchers.add(v);
                }
            }
        }

        request.setAttribute("savedVouchers", savedVouchers);
        request.getRequestDispatcher("savedVoucher.jsp").forward(request, response);
    }
}
