<%-- 
    Document   : product
    Created on : Jun 22, 2025, 6:01:43 AM
    Author     : KhanhDang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<title>G6Shop - Product</title>
<%@include file="/WEB-INF/include/headerAdmin.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<main class="col-md-9 ms-sm-auto col-lg-10 px-md-4" style="margin-top: 110px;">
    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Product</h1>
    </div>
    <!-- Tab nav -->
    <ul class="nav nav-tabs" id="productTabs" role="tablist">
        <c:forEach var="cate" items="${list}" varStatus="status">
            <li class="nav-item" role="presentation">
                <button class="nav-link ${status.index == 0 ? 'active' : ''}"
                        id="tab-${cate.categoryID}"
                        data-bs-toggle="tab"
                        data-bs-target="#content-${cate.categoryID}"
                        type="button"
                        role="tab"
                        aria-controls="content-${cate.categoryID}"
                        aria-selected="${status.index == 0 ? 'true' : 'false'}">
                    ${cate.categoryName}
                </button>
            </li>
        </c:forEach>
    </ul>
    <!-- Tab content -->
    <div class="tab-content mt-3" id="productTabContent">
        <div class="d-flex justify-content-between ...">
            <h1 class="h2">Product</h1>
            <a href="image-list" class="btn btn-success">
                <i class="bi bi-plus-circle"></i> Add Product
            </a>
        </div>
        <c:forEach var="cate" items="${list}" varStatus="status">
            <div class="tab-pane fade ${status.index == 0 ? 'show active' : ''}" 
                 id="content-${cate.categoryID}" 
                 role="tabpanel" 
                 aria-labelledby="tab-${cate.categoryID}">

                <c:set var="prodList" value="${productsMap[cate.categoryID]}" />

                <c:if test="${empty prodList}">
                    <p class="text-muted">There are no products.</p>
                </c:if>

                <c:if test="${not empty prodList}">
                    <div class="table-responsive">
                        <table class="table table-bordered text-center">
                            <thead class="table-light">
                                <tr>
                                    <th>#</th>
                                    <th>Image</th>
                                    <th>Name</th>
                                    <th>Version</th>
                                    <th>Storage</th>
                                    <th>Color</th>
                                    <th>Price</th>
                                    <th>Quantity</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="p" items="${prodList}" varStatus="loop">
                                    <tr>
                                        <td>${loop.index + 1}</td>
                                        <td><img src="assets/img/${p.image}" width="50" alt="${p.productName}"></td>
                                        <td>${p.productName}</td>
                                        <td>${p.version}</td>
                                        <td>${p.storage}</td>
                                        <td>${p.color}</td>
                                        <fmt:formatNumber value="${p.price}" type="number" maxFractionDigits="0" var="priceFormatted"/>
                                        <td>${priceFormatted} ₫</td>
                                        <td>${p.quantity}</td>
                                        <td>
                                            <a href="product?view=details&id=${p.productId}&color=${p.color}&storage=${p.storage}" class="btn btn-success btn-sm rounded-pill px-3 fw-bold">
                                                View
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:if>
            </div>
        </c:forEach>
    </div>
</main>

<%@include file="/WEB-INF/include/footerAdmin.jsp" %>
