<%-- 
    Document   : updatePassword
    Created on : Jun 17, 2025, 5:20:33 PM
    Author     : KhanhDang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="user" value="${sessionScope.loggedUser}" />
<%@include file="/WEB-INF/include/header.jsp" %>
<title>G6Shop - Update Profile</title>

<div class="container py-5" style="margin-top: 120px; margin-bottom: 20px">
    <form action="user?action=change-password" method="post" enctype="multipart/form-data">
        <!-- Avatar upload -->
        <div class="text-center mb-5">
            <!-- Avatar lớn -->
            <div class="rounded-circle mx-auto d-flex align-items-center justify-content-center shadow"
                 style="background-color: #e5e7eb; width: 110px; height: 110px; overflow: hidden; font-weight: 600;">

                <c:choose>
                    <c:when test="${not empty loggedUser.avatar}">
                        <img src="${pageContext.request.contextPath}/${loggedUser.avatar}"
                             alt="Avatar"
                             style="width: 100%; height: 100%; object-fit: cover;" />
                    </c:when>
                    <c:otherwise>
                        <div class="text-dark d-flex align-items-center justify-content-center"
                             style="width: 100%; height: 100%; font-size: 2rem !important;">
                            ${fn:toUpperCase(fn:substring(user.userFullname, 0, 1))}
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="mt-4 position-relative d-inline-block">
                <span class="fw-semibold" style="font-size: 1.25rem; color: #2563eb;">Change Password</span>
                <div style="height: 2px; background-color: #2563eb; width: 100%; position: absolute; left: 0; bottom: -2px;"></div>
            </div>
        </div>

        <!-- Editable fields -->
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label">Full Name</label>
                        <div class="form-control bg-light">${fn:escapeXml(user.userFullname)}</div>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Username</label>
                        <div class="form-control bg-light">${fn:escapeXml(user.userName)}</div>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Email</label>
                        <div class="form-control bg-light">${fn:escapeXml(user.userEmail)}</div>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Phone</label>
                        <div class="form-control bg-light">${fn:escapeXml(user.userPhone)}</div>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Current Password</label>
                        <input type="password" name="currentPassword" value="" class="form-control" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">New Password</label>
                        <input type="password" name="newPassword" value="" class="form-control" required>
                    </div>
                </div>

                <!-- Buttons -->
                <div class="text-end mt-4">
                    <a href="user?view=profile" class="btn btn-secondary fw-semibold px-4 py-2 me-2">
                        <i class="fa fa-times me-1"></i> Cancel
                    </a>
                    <button type="submit" class="btn btn-primary fw-semibold px-4" style="height: 48px;">
                        <i class="fa fa-save me-1"></i> Save Changes
                    </button>
                </div>
            </div>
        </div>
    </form>
</div>

<script>
    function previewAvatar(input) {
        const file = input.files[0];
        const preview = document.getElementById("avatarPreview");
        const letter = document.getElementById("avatarLetter");

        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                preview.src = e.target.result;
                preview.style.display = "block";
                letter.style.display = "none";
            };
            reader.readAsDataURL(file);
        }
    }
</script>
<%@include file="/WEB-INF/include/footer.jsp" %>
