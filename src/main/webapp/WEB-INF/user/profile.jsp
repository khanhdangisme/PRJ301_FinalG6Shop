<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    model.User user = (model.User) session.getAttribute("loggedUser");
%>
<%@include file="/WEB-INF/include/header.jsp" %>
<title>G6Shop - Profile</title>

<div class="page-content-wrapper">
    <div class="container py-5">
        <div class="container" style="margin-top: 120px; margin-bottom: 20px">
            <!-- Avatar + My Profile -->
            <div class="text-center mb-5">
                <!-- Avatar lớn -->
                <div class="rounded-circle mx-auto d-flex align-items-center justify-content-center shadow"
                     style="background-color: #e5e7eb; width: 110px; height: 110px; overflow: hidden; font-weight: 600;">

                    <c:choose>
                        <c:when test="${not empty user.avatarUrl}">
                            <img src="${pageContext.request.contextPath}/${user.avatarUrl}"
                                 alt="Avatar"
                                 style="width: 100%; height: 100%; object-fit: cover;" />
                        </c:when>
                        <c:otherwise>
                            <div class="text-dark d-flex align-items-center justify-content-center"
                                 style="width: 100%; height: 100%; font-size: 2rem !important;">
                                <%= user.getUserFullname().toUpperCase().charAt(0) %>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Label "My Profile" -->
                <div class="mt-4 position-relative d-inline-block">
                    <span class="fw-semibold" style="font-size: 1.25rem; color: #2563eb;">My Profile</span>
                    <div style="height: 2px; background-color: #2563eb; width: 100%; position: absolute; left: 0; bottom: -2px;"></div>
                </div>
            </div>

            <!-- User Info -->
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <div class="row g-3">
                        <div class="col-md-6">
                            <label class="form-label">Full Name</label>
                            <div class="form-control bg-light"><%= user.getUserFullname() %></div>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Username</label>
                            <div class="form-control bg-light"><%= user.getUserName() %></div>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Email</label>
                            <div class="form-control bg-light"><%= user.getUserEmail() %></div>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Phone</label>
                            <div class="form-control bg-light"><%= user.getUserPhone() %></div>
                        </div>
                    </div>

                    <!-- Action buttons -->
                    <div class="text-end mt-4">
                        <a href="user?view=update-profile" class="btn btn-edit-profile">
                            <i class="fa fa-edit me-1"></i> Edit Profile
                        </a>
                        <a href="user?view=update-password" class="btn btn-change-password">
                            <i class="fa fa-key me-1"></i> Change Password
                        </a>
                        <a href="#" class="btn btn-delete-account" data-bs-toggle="modal" data-bs-target="#confirmDeleteModal">
                            <i class="fa fa-trash me-1"></i> Delete Account
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="/WEB-INF/include/footer.jsp" %>
