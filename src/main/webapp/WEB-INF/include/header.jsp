<%-- 
    Document   : header
    Created on : Jun 16, 2025, 1:13:18 PM
    Author     : KhanhDang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <link rel="stylesheet" href="assets/css/templatemo.css">
        <link rel="stylesheet" href="assets/css/custom.css">

        <!-- Load fonts style after rendering the layout styles -->
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Roboto:wght@100;200;300;400;500;700;900&display=swap">
        <link rel="stylesheet" href="assets/css/fontawesome.min.css">
    </head>
    <body>
        <!-- Header -->
        <nav class="navbar navbar-expand-lg navbar-light shadow fixed-top bg-white">
            <div class="container d-flex justify-content-between align-items-center">

                <!--                <a class="navbar-brand text-success logo h1 align-self-center" href="index.jsp">
                                    G6Shop
                                </a>-->
                <a class="navbar-brand align-self-center" href="index.jsp">
                    <img src="assets/img/logo_G6.png" alt="G6Shop Logo" height="70">
                </a>

                <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#templatemo_main_nav">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse flex-fill d-lg-flex justify-content-lg-between" id="templatemo_main_nav">
                    <!-- Main menu -->
                    <div class="flex-fill">
                        <ul class="nav navbar-nav d-flex justify-content-between mx-lg-auto">
                            <li class="nav-item"><a class="nav-link" href="index.jsp">Home</a></li>
                            <li class="nav-item"><a class="nav-link" href="<%=request.getContextPath()%>/product">Shop</a></li>
                            <li class="nav-item"><a class="nav-link" href="#">Contact</a></li>
                        </ul>
                    </div>

                    <!-- Icons: Search / Cart / User -->
                    <div class="navbar align-self-center d-flex">
                        <!-- Search icon (large screen) -->
                        <a class="nav-icon d-none d-lg-inline" href="#" data-bs-toggle="modal" data-bs-target="#templatemo_search">
                            <i class="fa fa-fw fa-search text-dark mr-2"></i>
                        </a>

                        <!-- Cart icon -->
                        <a class="nav-icon position-relative text-decoration-none" href="#" data-bs-toggle="modal" data-bs-target="#cartModal">
                            <i class="fa fa-fw fa-cart-arrow-down text-dark mr-1"></i>
                            <span class="position-absolute top-0 left-100 translate-middle badge rounded-pill bg-light text-dark">7</span>
                        </a>

                        <!-- User account -->
                        <% if (loggedIn) { %>
                        <div class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle text-dark d-flex align-items-center" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                <!-- Avatar icon -->
                                <!-- Avatar icon -->
                                <div class="avatar-placeholder me-2">
                                    <%= loggedUser.getUserFullname().toUpperCase().charAt(0) %>
                                </div>

                                <!-- Greeting -->
                                <span class="fw-semibold">Hi, <%= loggedUser.getUserFullname() %></span>
                            </a>

                            <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0 rounded-3 mt-2" aria-labelledby="userDropdown" style="min-width: 260px;">
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

                                <!-- Order History -->
                                <li>
                                    <a class="dropdown-item d-flex align-items-center py-2" href="#">
                                        <i class="fa fa-history me-2" style="color: #198754;"></i> Order History
                                    </a>
                                </li>

                                <!-- Logout -->
                                <li>
                                    <a class="dropdown-item d-flex align-items-center py-2" href="<%=request.getContextPath()%>/logout">
                                        <i class="fa fa-sign-out-alt me-2" style="color: #dc3545;"></i> 
                                        <span class="text-danger">Sign Out</span>
                                    </a>
                                </li>
                            </ul>
                        </div>
                        <% } else { %>
                        <a class="nav-icon position-relative text-decoration-none" href="<%=request.getContextPath()%>/login">
                            <i class="fa fa-fw fa-user text-dark mr-3"></i>
                        </a>
                        <% } %>
                    </div>
                </div>

            </div>
        </nav>
