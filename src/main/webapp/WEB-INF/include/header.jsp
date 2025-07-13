<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/include/showNotification.jsp" %>

<%
    User loggedUser = (User) session.getAttribute("loggedUser");
    boolean loggedIn = loggedUser != null;
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <link rel="apple-touch-icon" href="${pageContext.request.contextPath}/assets/img/logo_G6.png">
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/img/mini_logo.png">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/templatemo.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/custom.css">

        <!-- Load fonts style after rendering the layout styles -->
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Roboto:wght@100;200;300;400;500;700;900&display=swap">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/fontawesome.min.css">
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-light shadow fixed-top bg-white">
            <div class="container d-flex justify-content-between align-items-center">

                <!-- Logo -->
                <a class="navbar-brand align-self-center" href="${pageContext.request.contextPath}/index.jsp">
                    <img src="${pageContext.request.contextPath}/assets/img/logo_G6.png" alt="G6Shop Logo" height="70">
                </a>

                <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#templatemo_main_nav">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse flex-fill d-lg-flex justify-content-lg-between" id="templatemo_main_nav">
                    <!-- Main menu -->
                    <div class="flex-fill">
                        <ul class="nav navbar-nav d-flex justify-content-between mx-lg-auto">
                            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/index.jsp">Home</a></li>
                            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/shop">Shop</a></li>
                        </ul>
                    </div>

                    <!-- Icons: Search / Cart / User -->
                    <div class="navbar align-self-center d-flex">

                        <!-- Search icon (mặc định shop) -->
                        <a class="nav-icon d-none d-lg-inline" href="#" data-bs-toggle="modal" data-bs-target="#templatemo_search">
                            <i class="fa fa-fw fa-search text-dark mr-2"></i>
                        </a>

                        <!-- Cart icon -->
                        <a class="nav-icon position-relative text-decoration-none"
                           href="${pageContext.request.contextPath}/cart?action=view">
                            <i class="fa fa-fw fa-cart-arrow-down text-dark mr-1"></i>
                            <c:if test="${sessionScope.cartCount != null && sessionScope.cartCount > 0}">
                                <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-light text-dark">
                                    ${sessionScope.cartCount}
                                </span>
                            </c:if>
                        </a>

                        <!-- User account -->
                        <% if (loggedIn) { %>
                        <div class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle text-dark d-flex align-items-center" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                <div class="avatar-placeholder me-2">
                                    <!-- Kiểm tra nếu có ảnh thì hiển thị ảnh, nếu không sẽ lấy chữ cái đầu tiên của tên người dùng -->
                                    <c:choose>
                                        <c:when test="${not empty loggedUser.avatar}">
                                            <img src="${pageContext.request.contextPath}/${loggedUser.avatar}" alt="Avatar" style="width: 40px; height: 40px; object-fit: cover; border-radius: 50%;" />
                                        </c:when>
                                        <c:otherwise>
                                            <!-- Nếu không có ảnh, hiển thị chữ cái đầu tiên của tên người dùng -->
                                            <div class="text-dark d-flex align-items-center justify-content-center" style="width: 40px; height: 40px; font-size: 20px; background-color: #e5e7eb; border-radius: 50%; font-weight: 700;">
                                                <%= loggedUser.getUserFullname().toUpperCase().charAt(0) %>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <span class="fw-semibold">Hi, <%= loggedUser.getUserFullname() %></span>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0 rounded-3 mt-2" aria-labelledby="userDropdown" style="min-width: 260px;">
                                <li class="px-3 py-2 border-bottom">
                                    <div class="fw-bold text-dark mb-1"><%= loggedUser.getUserName() %></div>
                                    <div class="text-muted small"><%= loggedUser.getUserEmail() %></div>
                                </li>
                                <li>
                                    <a class="dropdown-item d-flex align-items-center py-2" href="${pageContext.request.contextPath}/user?view=profile">
                                        <i class="fa fa-user me-2" style="color: #343a40;"></i> My Profile
                                    </a>
                                </li>
                                <c:if test="${sessionScope.loggedUser.userRole == 0}">
                                    <li>
                                        <a class="dropdown-item d-flex align-items-center py-2" href="${pageContext.request.contextPath}/admin?view=dashboard">
                                            <i class="fa fa-tachometer-alt me-2" style="color: #9c8412;"></i> Dashboard
                                        </a>
                                    </li>
                                </c:if>

                                <li>
                                    <a class="dropdown-item d-flex align-items-center py-2" href="${pageContext.request.contextPath}/history">
                                        <i class="fa fa-history me-2" style="color: #198754;"></i> Order History
                                    </a>
                                </li>

                                <li>
                                    <a class="dropdown-item d-flex align-items-center py-2" href="${pageContext.request.contextPath}/logout">
                                        <i class="fa fa-sign-out-alt me-2" style="color: #dc3545;"></i>
                                        <span class="text-danger">Sign Out</span>
                                    </a>
                                </li>
                            </ul>
                        </div>
                        <% } else { %>
                        <a class="nav-icon position-relative text-decoration-none"
                           href="${pageContext.request.contextPath}/login">
                            <i class="fa fa-fw fa-user text-dark mr-3"></i>
                        </a>
                        <% } %>
                    </div>
                </div>
            </div>
        </nav>

        <!-- Search Modal (dùng cho Shop mặc định) -->
        <div class="modal fade" id="templatemo_search" tabindex="-1" aria-labelledby="searchModalLabel" aria-hidden="true">
            <div class="modal-dialog mt-3">
                <div class="modal-content border-0 shadow">
                    <div class="modal-header">
                        <h5 class="modal-title" id="searchModalLabel">Search</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form action="${pageContext.request.contextPath}/shop" method="get">
                            <input type="hidden" name="view" value="search" />
                            <div class="input-group">
                                <input type="text" name="query" class="form-control" placeholder="Type to search..." required>
                                <button class="btn btn-success" type="submit">
                                    <i class="fa fa-search"></i>
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
