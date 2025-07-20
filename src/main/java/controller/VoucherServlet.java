package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Voucher;

import java.io.IOException;
import java.util.*;
import dao.VoucherDAO;

@WebServlet(name = "VoucherServlet", urlPatterns = {"/voucher"})

public class VoucherServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        VoucherDAO dao = new VoucherDAO();
        try {
            if (action == null) {
                // Hiển thị danh sách voucher
                request.setAttribute("vouchers", dao.getAllVouchers());
                request.getRequestDispatcher("voucherList.jsp").forward(request, response);
            } else if (action.equals("add")) {
                // Hiển thị form thêm mới
                request.getRequestDispatcher("/WEB-INF/admin/voucher_add.jsp").forward(request, response);
            } else if (action.equals("edit")) {
                // Hiển thị form sửa
                String idRaw = request.getParameter("id");
                if (idRaw != null) {
                    int id = Integer.parseInt(idRaw);
                    Voucher v = dao.getVoucherById(id);
                    request.setAttribute("voucher", v);
                    request.getRequestDispatcher("/WEB-INF/admin/voucher_edit.jsp").forward(request, response);
                } else {
                    response.sendRedirect("voucher");
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        VoucherDAO dao = new VoucherDAO();
        try {
            if (action == null) {
                response.sendRedirect("admin?view=voucher");
                return;
            }
            if (action.equals("add")) {
                // Thêm mới voucher
                String code = request.getParameter("code");
                int discountPercent = Integer.parseInt(request.getParameter("discountPercent"));
                double maxDiscount = Double.parseDouble(request.getParameter("maxDiscount"));
                java.sql.Date expiryDate = java.sql.Date.valueOf(request.getParameter("expiryDate"));
                boolean isActive = "on".equals(request.getParameter("isActive"));
                Voucher v = new Voucher(0, code, discountPercent, maxDiscount, expiryDate, isActive);
                dao.insertVoucher(v);
                response.sendRedirect("admin?view=voucher");
            } else if (action.equals("edit")) {
                // Sửa voucher
                int id = Integer.parseInt(request.getParameter("id"));
                String code = request.getParameter("code");
                int discountPercent = Integer.parseInt(request.getParameter("discountPercent"));
                double maxDiscount = Double.parseDouble(request.getParameter("maxDiscount"));
                java.sql.Date expiryDate = java.sql.Date.valueOf(request.getParameter("expiryDate"));
                boolean isActive = "on".equals(request.getParameter("isActive"));
                Voucher v = new Voucher(id, code, discountPercent, maxDiscount, expiryDate, isActive);
                dao.updateVoucher(v);
                response.sendRedirect("admin?view=voucher");
            } else if (action.equals("delete")) {
                // Xóa voucher
                int id = Integer.parseInt(request.getParameter("id"));
                dao.deleteVoucher(id);
                response.sendRedirect("admin?view=voucher");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    // Hàm tạo ngày hết hạn trong tương lai
    private Date getFutureDate(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }
}
