<%-- 
    Document   : shop
    Created on : Jul 2, 2025, 5:50:37 PM
    Author     : KhanhDang
--%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/header.jsp" %>
<title>G6Shop - Shop</title>

<div class="container py-5" style="margin-top: 100px;">
    <div class="row">
        <!-- CATEGORY MENU -->
        <div class="col-lg-3">
            <h1 class="h2 pb-4 font-weight-bold"><strong>Categories</strong></h1>
            <ul class="list-unstyled templatemo-accordion">
                <c:forEach var="category" items="${list}">
                    <li class="pb-3">
                        <a class="collapsed d-flex justify-content-between h3 text-decoration-none"
                           data-bs-toggle="collapse"
                           href="#collapse${category.categoryID}"
                           role="button"
                           aria-expanded="false"
                           aria-controls="collapse${category.categoryID}">
                            ${category.categoryName}
                            <i class="fa fa-fw fa-chevron-circle-down mt-1"></i>
                        </a>
                        <ul id="collapse${category.categoryID}"
                            class="collapse list-unstyled ps-3"
                            data-bs-parent=".templatemo-accordion">
                            <c:forEach var="product" items="${productsMap[category.categoryID]}">
                                <li>
                                    <a class="text-decoration-none"
                                       href="shop?productId=${product.productId}">
                                        ${product.productName}
                                    </a>
                                </li>
                            </c:forEach>
                        </ul>
                    </li>
                </c:forEach>
            </ul>

        </div>

        <!-- PRODUCT DETAIL -->
        <div class="col-lg-9">
            <c:if test="${not empty productDetail}">
                <div class="row">
                    <c:forEach var="p" items="${productDetail}">
                        <div class="col-md-4">
                            <div class="card mb-4 product-wap rounded-0">
                                <div class="card rounded-0">
                                    <img class="card-img rounded-0 img-fluid"
                                         <img class="card-img rounded-0 img-fluid"
                                         src="${not empty p.image ? pageContext.request.contextPath.concat('/assets/img/').concat(p.image) : pageContext.request.contextPath.concat('/assets/img/default.jpg')}"
                                         alt="${p.productName}"
                                         style="height: 250px; object-fit: cover;">
                                    <div class="card-img-overlay rounded-0 product-overlay d-flex align-items-center justify-content-center">
                                        <ul class="list-unstyled">
                                            <li><a class="btn btn-success text-white mt-2"
                                                   href="shop?view=details&id=${p.productId}&color=${p.color}&storage=${p.storage}">
                                                    <i class="far fa-eye"></i></a></li>
                                            <li><a class="btn btn-success text-white mt-2" href="#">
                                                    <i class="fas fa-cart-plus"></i></a></li>
                                        </ul>
                                    </div>
                                </div>
                                <div class="card-body">
                                    <a href="shop?id=${p.productId}&color=${p.color}&storage=${p.storage}"
                                       class="h3 text-decoration-none text-dark">
                                        ${p.productName}
                                    </a>
                                    <div class="product-info mt-2">
                                        <c:if test="${not empty p.version}">
                                            <span class="badge bg-secondary me-1">${p.version}</span>
                                        </c:if>
                                        <c:if test="${not empty p.color}">
                                            <span class="badge bg-primary me-1">${p.color}</span>
                                        </c:if>
                                        <c:if test="${not empty p.storage}">
                                            <span class="badge bg-info">${p.storage}</span>
                                        </c:if>
                                    </div>
                                    <div class="text-center mt-2">
                                        <p class="h5 text-success mb-0">
                                            <fmt:formatNumber value="${p.price}" pattern="#,##0"/>₫
                                        </p>
                                    </div>
                                    <div class="text-center mt-2">
                                        <c:choose>
                                            <c:when test="${p.quantity > 0}">
                                                <button class="btn btn-secondary btn-sm w-100" style="background-color: black; border-color: black;">
                                                    <i class="fas fa-shopping-bag"></i> Buy
                                                </button>
                                            </c:when>
                                            <c:otherwise>
                                                <button class="btn btn-secondary btn-sm w-100" disabled style="background-color: silver; border-color: silver;">
                                                    <i class="fas fa-ban"></i> Out of Stock
                                                </button>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:if>
            <c:if test="${empty productDetail}">
                <p class="text-center text-muted mt-5">Please select a product from the left menu.</p>
            </c:if>

            <nav aria-label="Page navigation example">
                <ul class="pagination justify-content-center">
                    <!-- Previous -->
                    <li class="page-item ${param.page <= 1 || empty param.page ? 'disabled' : ''}">
                        <a class="page-link" href="shop?page=1" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>

                    <!-- Page Numbers -->
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${param.page == i || (empty param.page && i == 1) ? 'active' : ''}">
                            <a class="page-link" href="shop?page=${i}">${i}</a>
                        </li>
                    </c:forEach>

                    <!-- Next -->
                    <li class="page-item ${param.page >= totalPages ? 'disabled' : ''}">
                        <a class="page-link" href="shop?page=${totalPages}" aria-label="Next">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                </ul>
            </nav>
        </div>
    </div>
</div>


<%@include file="/WEB-INF/include/footer.jsp" %> 