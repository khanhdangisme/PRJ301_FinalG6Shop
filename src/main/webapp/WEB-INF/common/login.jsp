<%-- 
    Document   : login
    Created on : May 28, 2025, 7:45:36 AM
    Author     : KhanhDang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/header.jsp" %>
<title>G6Shop - Sign in</title>
<link href="/assets/css/signin.css" rel="stylesheet">

<main class="d-flex justify-content-center align-items-center my-5" style="min-height: 80vh; padding-top: 135px; padding-bottom: 40px;">
    <div class="border rounded shadow p-4 bg-white" style="max-width: 400px; width: 100%;">
        <%
            String savedUsername = "";
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if ("username".equals(c.getName())) {
                        savedUsername = c.getValue();
                        break;
                    }
                }
            }
        %>    
        <form action="login" method="POST">
            <h1 class="h3 mb-4 fw-bold text-center text-primary">Please sign in</h1>

            <!-- Username -->
            <div class="form-floating mb-3">
                <input type="text" class="form-control" name="username" id="usernameInput"
                       placeholder="Enter User Name" value="${username != null ? username : ''}" required>
                <label for="usernameInput">Username</label>
            </div>

            <!-- Password -->
            <div class="form-floating mb-3">
                <input type="password" class="form-control" name="password" id="passwordInput" placeholder="Enter Password" required>
                <label for="passwordInput">Password</label>
            </div>

            <!-- Remember Me -->
            <div class="form-check mb-3 d-flex justify-content-center">
                <input class="form-check-input me-2" type="checkbox" name="remember" id="rememberMeCheck" value="remember-me">
                <label class="form-check-label" for="rememberMeCheck">
                    Remember me
                </label>
            </div>

            <!-- Sign in button -->
            <button class="w-100 btn btn-lg btn-primary mb-2" type="submit">Sign in</button>

            <!-- Create account -->
            <p class="text-center small mt-3">
                Don't have an account? <a href="register">Create one</a>
            </p>

        </form>
    </div>
</main>
<%@include file="/WEB-INF/include/footer.jsp" %>