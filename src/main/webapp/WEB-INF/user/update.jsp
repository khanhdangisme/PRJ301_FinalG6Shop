<%-- 
    Document   : update
    Created on : Jun 17, 2025, 3:14:26 PM
    Author     : KhanhDang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    model.User user = (model.User) session.getAttribute("loggedUser");
%>
<%@include file="/WEB-INF/include/header.jsp" %>
<title>G6Shop - Update Profile</title>

<div class="container py-5" style="margin-top: 120px; margin-bottom: 20px">
    <form action="user?action=save-profile" method="post" enctype="multipart/form-data">
        <!-- Avatar upload -->
        <div class="text-center mb-5">
            <input type="file" name="avatar" id="avatarInput" accept="image/*" hidden onchange="previewAvatar(this)">
            <div onclick="document.getElementById('avatarInput').click()" 
                 class="rounded-circle text-dark mx-auto d-flex align-items-center justify-content-center shadow"
                 style="cursor: pointer; background-color: #e5e7eb; width: 110px; height: 110px; font-size: 2rem !important; font-weight: 700; overflow: hidden;">
                <img id="avatarPreview" src="${user.avatarUrl != null ? user.avatarUrl : ''}" 
                     style="width: 100%; height: 100%; object-fit: cover; display: ${user.avatarUrl != null ? 'block' : 'none'};" />
                <span id="avatarLetter" style="display: ${user.avatarUrl == null ? 'block' : 'none'};">
                    <%= user.getUserFullname().toUpperCase().charAt(0) %>
                </span>
            </div>

            <div class="mt-4 position-relative d-inline-block">
                <span class="fw-semibold" style="font-size: 1.25rem; color: #2563eb;">Edit Profile</span>
                <div style="height: 2px; background-color: #2563eb; width: 100%; position: absolute; left: 0; bottom: -2px;"></div>
            </div>
        </div>

        <!-- Editable fields -->
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label">Full Name</label>
                        <input type="text" name="fullname" value="<%= user.getUserFullname() %>" class="form-control" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Username</label>
                        <input type="text" name="username" value="<%= user.getUserName() %>" class="form-control" readonly>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Email</label>
                        <input type="email" name="email" value="<%= user.getUserEmail() %>" class="form-control" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Phone</label>
                        <input type="text" name="phone" value="<%= user.getUserPhone() %>" class="form-control" required>
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

