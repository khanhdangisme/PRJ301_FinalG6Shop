<%-- 
    Document   : header
    Created on : Jun 16, 2025, 1:13:18 PM
    Author     : KhanhDang
--%>

<%@ page import="model.User" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/include/showNotification.jsp" %>

<% User loggedUser = (User) session.getAttribute("loggedUser");
    boolean loggedIn = loggedUser != null;
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <link rel="apple-touch-icon" href="assets/img/logo_G6.png">
        <link rel="shortcut icon" type="image/x-icon" href="assets/img/mini_logo.png">

        <link rel="stylesheet" href="assets/css/bootstrap.min.css">
        <!--<link rel="stylesheet" href="assets/css/templatemo.css">-->
        <link rel="stylesheet" href="assets/css/custom.css">
        <link rel="stylesheet" href="assets/css/dashboard.css">

        <!-- Load fonts style after rendering the layout styles -->
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Roboto:wght@100;200;300;400;500;700;900&display=swap">
        <link rel="stylesheet" href="assets/css/fontawesome.min.css">
     
    </head>
    <body>
        <!-- Header -->
        <nav class="navbar navbar-expand-lg navbar-light shadow fixed-top bg-white">
            <div class="container d-flex justify-content-between align-items-center" style="margin-top: -7px; margin-bottom: -7px;">
                <a class="navbar-brand align-self-center" href="index.jsp">
                    <img src="assets/img/logo_G6.png" alt="G6Shop Logo" height="70">
                </a>

                <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#templatemo_main_nav">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse flex-fill d-lg-flex justify-content-lg-between" id="templatemo_main_nav">
                    <!-- Main menu -->
                    <div class="flex-fill">
                        <ul class="nav navbar-nav d-flex justify-content-around mx-lg-auto">
                            <li class="nav-item"><a class="nav-link home" href="index.jsp">Home</a></li>
                            <li class="nav-item"><a class="nav-link home" href="<%=request.getContextPath()%>/shop">Shop</a></li>

                        </ul>
                    </div>

                    <!-- Icons: Search / Cart / User -->
                    <div class="navbar align-self-center d-flex">

                        <!-- User account -->
                        <div class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle text-dark d-flex align-items-center" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                <!-- Avatar icon -->
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

                                <!-- Greeting -->
                                <span class="fw-semibold profile">Hi, <%= loggedUser.getUserFullname() %></span>
                            </a>

                            <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0 rounded-3 mt-2 profile" aria-labelledby="userDropdown" style="min-width: 260px;">
                                <!-- User Info -->
                                <li class="px-3 py-2 border-bottom">
                                    <div class="fw-bold text-dark mb-1"><%= loggedUser.getUserName() %></div>
                                    <div class="text-muted small"><%= loggedUser.getUserEmail() %></div>
                                </li>

                                <!-- Profile -->
                                <li>
                                    <a class="dropdown-item d-flex align-items-center py-2" href="user?view=profile">
                                        <i class="fa fa-user me-2" style="color: #343a40;"></i> My Profile
                                    </a>
                                </li>

                                <!-- Admin Dashboard -->
                                <c:if test="${sessionScope.loggedUser.userRole == 0}">
                                    <li>
                                        <a class="dropdown-item d-flex align-items-center py-2" href="${pageContext.request.contextPath}/admin?view=dashboard">
                                            <i class="fa fa-tachometer-alt me-2" style="color: #9c8412;"></i> Dashboard
                                        </a>
                                    </li>
                                </c:if>

                                <!-- Logout -->
                                <li>
                                    <a class="dropdown-item d-flex align-items-center py-2" href="<%=request.getContextPath()%>/logout">
                                        <i class="fa fa-sign-out-alt me-2" style="color: #dc3545;"></i> 
                                        <span class="text-danger">Sign Out</span>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </nav>

        <div class="container-fluid">
            <div class="row">
                <nav id="sidebarMenuAdmin" class="col-md-3 col-lg-2 d-md-block bg-light sidebar collapse sidebarAdmin" style="margin-top: 60px;">
                    <div class="position-sticky pt-3">
                        <ul class="nav flex-column">
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/admin?view=dashboard" style="font-size: 18px !important; font-weight: 500 !important;">
                                    <span data-feather="home"></span>
                                    Dashboard
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/admin?view=product" style="font-size: 18px !important; font-weight: 500 !important;">
                                    <span data-feather="shopping-cart"></span>
                                    Products
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/admin?view=customer" style="font-size: 18px !important; font-weight: 500 !important;">
                                    <span data-feather="users"></span>
                                    Customers
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/admin?view=reports" style="font-size: 18px !important; font-weight: 500 !important;">
                                    <span data-feather="bar-chart-2"></span>
                                    Reports
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/admin?view=orderlist"
                                   style="font-size: 18px !important; font-weight: 500 !important;">
                                    <span data-feather="file"></span>
                                    Order history
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/admin?view=voucher" style="font-size: 18px !important; font-weight: 500 !important;">
                                    <span data-feather="gift"></span>
                                    Vouchers
                                </a>
                            </li>
                        </ul>
                    </div>
                </nav>
            