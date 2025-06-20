<%-- 
    Document   : updatePassword
    Created on : Jun 17, 2025, 5:20:33 PM
    Author     : KhanhDang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    model.User user = (model.User) session.getAttribute("loggedUser");
%>
<%@include file="/WEB-INF/include/header.jsp" %>
<title>G6Shop - Update Password</title>

<div class="container py-5" style="margin-top: 120px; margin-bottom: 20px">
    <form action="user?action=change-password" method="post">
        <!-- Avatar và tiêu đề -->
        <div class="text-center mb-5">
            <div class="rounded-circle text-dark mx-auto d-flex align-items-center justify-content-center shadow"
                 style="background-color: #e5e7eb; width: 110px; height: 110px; font-size: 2rem !important; font-weight: 700;">
                <span><%= user.getUserFullname().toUpperCase().charAt(0) %></span>
            </div>

            <div class="mt-4 position-relative d-inline-block">
                <span class="fw-semibold" style="font-size: 1.25rem; color: #2563eb;">Change Password</span>
                <div style="height: 2px; background-color: #2563eb; width: 100%; position: absolute; left: 0; bottom: -2px;"></div>
            </div>
        </div>

        <!-- Form -->
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="row g-3">
                    <div class="col-12">
                        <label class="form-label">Username</label>
                        <input type="text" name="username" class="form-control bg-light" readonly value="<%= user.getUserName() %>">
                    </div>

                    <div class="col-12">
                        <label class="form-label">Current Password</label>
                        <input type="password" name="currentPassword" class="form-control" required>
                    </div>

                    <div class="col-12">
                        <label class="form-label">New Password</label>
                        <input type="password" name="newPassword" class="form-control" required>
                    </div>
                </div>

                <!-- Action buttons -->
                <div class="text-end mt-4">
                    <a href="user?view=profile" class="btn btn-secondary fw-semibold px-4 py-2 me-2">
                        <i class="fa fa-times me-1"></i> Cancel
                    </a>
                    <button type="submit" class="btn btn-warning fw-semibold px-4" style="height: 48px;">
                        <i class="fa fa-save me-1"></i> Save Password
                    </button>
                </div>
            </div>
        </div>
    </form>
</div>

<%@include file="/WEB-INF/include/footer.jsp" %>
