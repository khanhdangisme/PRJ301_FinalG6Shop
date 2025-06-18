<%-- 
    Document   : register.jsp
    Created on : Jun 17, 2025, 2:43:26 AM
    Author     : KhanhDang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/header.jsp" %>
<%@include file="/WEB-INF/include/showNotification.jsp" %>
<title>G6Shop - Register</title>

<link href="/assets/css/signin.css" rel="stylesheet">

<main class="d-flex justify-content-center align-items-center" style="min-height: calc(100vh - 160px); padding-top: 135px; padding-bottom: 40px;">
    <div class="border rounded shadow p-4 bg-white" style="max-width: 450px; width: 100%;">
        <form action="register" method="POST">
            <h2 class="h3 mb-4 fw-bold text-center text-primary">Create Your Account</h2>

            <!-- Username -->
            <div class="form-floating mb-3">
                <input type="text" class="form-control" name="username" id="usernameInput" placeholder="Enter Username" required>
                <label for="usernameInput">Username</label>
            </div>

            <!-- Password -->
            <div class="form-floating mb-3">
                <input type="password" class="form-control" name="password" id="passwordInput" placeholder="Enter Password" required>
                <label for="passwordInput">Password</label>
            </div>

            <!-- Full name -->
            <div class="form-floating mb-3">
                <input type="text" class="form-control" name="fullname" id="fullnameInput" placeholder="Enter Fullname" required>
                <label for="fullnameInput">Full Name</label>
            </div>

            <!-- Email -->
            <div class="form-floating mb-3">
                <input type="email" class="form-control" name="email" id="emailInput" placeholder="name@example.com" required>
                <label for="emailInput">Email</label>
            </div>

            <!-- Phone -->
            <div class="form-floating mb-3">
                <input type="text" class="form-control" name="phone" id="phoneInput" placeholder="Enter Phone" required>
                <label for="phoneInput">Phone Number</label>
            </div>

            <!-- Submit -->
            <button class="w-100 btn btn-lg btn-primary mb-2" type="submit">Sign Up</button>

            <!-- Link to login -->
            <p class="text-center small">
                Already have an account?
                <a href="login" class="text-decoration-none">Sign in</a>
            </p>
        </form>
    </div>
</main>

<%@include file="/WEB-INF/include/footer.jsp" %>