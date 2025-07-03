<%-- 
    Document   : shopDetials
    Created on : Jul 3, 2025, 10:23:31 AM
    Author     : KhanhDang
--%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/header.jsp" %>
<title>G6Shop - Shop Details</title>

<main class="container" style="margin-top: 120px;">
    <div class="row">
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
        <div class="col-lg-9">
            <div class="card shadow p-4 border-0 bg-white">

                <!-- Top section -->
                <div class="row mb-4 align-items-center">
                    <div class="col-md-4 text-center">
                        <img src="assets/img/${getDetail.productImage}" alt="Product Image"
                             style="width: 200px" class="img-fluid rounded shadow-sm" />
                    </div>
                    <div class="col-md-8">
                        <p><strong>Product Name:</strong> <span class="text-dark fw-bold fs-5">${getDetail.productName}</span></p>
                            <c:choose>
                                <c:when test='${getDetail.categoryName eq "iPhone"}'>
                                    <c:set var="price" value="${getDetail.iPhonePrice}" />
                                </c:when>
                                <c:when test='${getDetail.categoryName eq "iPad"}'>
                                    <c:set var="price" value="${getDetail.iPadPrice}" />
                                </c:when>
                                <c:when test='${getDetail.categoryName eq "MacBook"}'>
                                    <c:set var="price" value="${getDetail.macPrice}" />
                                </c:when>
                            </c:choose>
                        <p>
                            <strong>Price:</strong>
                            <span class="text-success fw-bold fs-5">
                                <fmt:formatNumber value="${price}" pattern="#,##0"/> ₫
                            </span>
                        </p>
                        <p><strong>Quantity:</strong> <span class="text-dark fw-bold">${getDetail.productQuatity}</span></p>
                    </div>
                </div>

                <!-- Set color, version, storage -->
                <c:choose>
                    <c:when test='${getDetail.categoryName eq "iPhone"}'>
                        <c:set var="color" value="${getDetail.iPhoneColor}" />
                        <c:set var="version" value="${getDetail.iPhoneVersion}" />
                        <c:set var="storage" value="${getDetail.iPhoneStorage}" />
                    </c:when>
                    <c:when test='${getDetail.categoryName eq "iPad"}'>
                        <c:set var="color" value="${getDetail.iPadColor}" />
                        <c:set var="version" value="${getDetail.iPadVersion}" />
                        <c:set var="storage" value="${getDetail.iPadStorage}" />
                    </c:when>
                    <c:when test='${getDetail.categoryName eq "MacBook"}'>
                        <c:set var="color" value="${getDetail.macColor}" />
                        <c:set var="version" value="${getDetail.macVersion}" />
                        <c:set var="storage" value="${getDetail.macStorage}" />
                    </c:when>
                </c:choose>

                <!-- Common fields -->
                <div class="row mb-2">
                    <div class="col-md-6">
                        <p><strong>Category:</strong> <span class="text-dark fw-bold">${getDetail.categoryName}</span></p>
                    </div>
                    <div class="col-md-6">
                        <p><strong>Color:</strong> <span class="text-dark fw-bold">${color}</span></p>
                    </div>
                </div>
                <div class="row mb-2">
                    <div class="col-md-6">
                        <p><strong>Version:</strong> <span class="text-dark fw-bold">${version}</span></p>
                    </div>
                    <div class="col-md-6">
                        <p><strong>Storage:</strong> <span class="text-dark fw-bold">${storage}</span></p>
                    </div>
                </div>

                <!-- Dynamic fields -->
                <c:choose>
                    <c:when test='${getDetail.categoryName eq "iPhone"}'>
                        <div class="row mb-2">
                            <div class="col-md-6"><p><strong>Screen Size:</strong> <span class="text-dark fw-bold">${getDetail.iPhoneScreenSize}</span></p></div>
                            <div class="col-md-6"><p><strong>Rear Camera:</strong> <span class="text-dark fw-bold">${getDetail.iPhoneRearCamera}</span></p></div>
                        </div>
                        <div class="row mb-2">
                            <div class="col-md-6"><p><strong>Front Camera:</strong> <span class="text-dark fw-bold">${getDetail.iPhoneFrontCamera}</span></p></div>
                            <div class="col-md-6"><p><strong>Chipset:</strong> <span class="text-dark fw-bold">${getDetail.iPhoneChipset}</span></p></div>
                        </div>
                        <div class="row mb-2">
                            <div class="col-md-6"><p><strong>Battery:</strong> <span class="text-dark fw-bold">${getDetail.iPhoneBattery}</span></p></div>
                            <div class="col-md-6"><p><strong>SIM Type:</strong> <span class="text-dark fw-bold">${getDetail.iPhoneSimType}</span></p></div>
                        </div>
                        <div class="row mb-2">
                            <div class="col-md-6"><p><strong>OS:</strong> <span class="text-dark fw-bold">${getDetail.iPhoneOs}</span></p></div>
                            <div class="col-md-6"><p><strong>Resolution:</strong> <span class="text-dark fw-bold">${getDetail.iPhoneResolution}</span></p></div>
                        </div>
                    </c:when>
                    <c:when test='${getDetail.categoryName eq "iPad"}'>
                        <div class="row mb-2">
                            <div class="col-md-6"><p><strong>Screen Size:</strong> <span class="text-dark fw-bold">${getDetail.iPadScreenSize}</span></p></div>
                            <div class="col-md-6"><p><strong>Rear Camera:</strong> <span class="text-dark fw-bold">${getDetail.iPadRearCamera}</span></p></div>
                        </div>
                        <div class="row mb-2">
                            <div class="col-md-6"><p><strong>Front Camera:</strong> <span class="text-dark fw-bold">${getDetail.iPadFrontCamera}</span></p></div>
                            <div class="col-md-6"><p><strong>Chipset:</strong> <span class="text-dark fw-bold">${getDetail.iPadChipset}</span></p></div>
                        </div>
                        <div class="row mb-2">
                            <div class="col-md-6"><p><strong>Battery:</strong> <span class="text-dark fw-bold">${getDetail.iPadBattery}</span></p></div>
                            <div class="col-md-6"><p><strong>SIM Type:</strong> <span class="text-dark fw-bold">${getDetail.iPadSimType}</span></p></div>
                        </div>
                        <div class="row mb-2">
                            <div class="col-md-6"><p><strong>OS:</strong> <span class="text-dark fw-bold">${getDetail.iPadOs}</span></p></div>
                            <div class="col-md-6"><p><strong>Resolution:</strong> <span class="text-dark fw-bold">${getDetail.iPadResolution}</span></p></div>
                        </div>
                    </c:when>
                    <c:when test='${getDetail.categoryName eq "MacBook"}'>
                        <div class="row mb-2">
                            <div class="col-md-6"><p><strong>RAM:</strong> <span class="text-dark fw-bold">${getDetail.macRam}</span></p></div>
                            <div class="col-md-6"><p><strong>GPU Type:</strong> <span class="text-dark fw-bold">${getDetail.macGpuType}</span></p></div>
                        </div>
                        <div class="row mb-2">
                            <div class="col-md-6"><p><strong>Screen Size:</strong> <span class="text-dark fw-bold">${getDetail.macScreenSize}</span></p></div>
                            <div class="col-md-6"><p><strong>Screen Tech:</strong> <span class="text-dark fw-bold">${getDetail.macScreenTech}</span></p></div>
                        </div>
                        <div class="row mb-2">
                            <div class="col-md-6"><p><strong>Battery:</strong> <span class="text-dark fw-bold">${getDetail.macBattery}</span></p></div>
                            <div class="col-md-6"><p><strong>OS:</strong> <span class="text-dark fw-bold">${getDetail.macOs}</span></p></div>
                        </div>
                        <div class="row mb-2">
                            <div class="col-md-6"><p><strong>Resolution:</strong> <span class="text-dark fw-bold">${getDetail.macResolution}</span></p></div>
                            <div class="col-md-6"><p><strong>CPU Type:</strong> <span class="text-dark fw-bold">${getDetail.macCpuType}</span></p></div>
                        </div>
                        <div class="row mb-2">
                            <div class="col-md-6"><p><strong>Ports:</strong> <span class="text-dark fw-bold">${getDetail.macPorts}</span></p></div>
                        </div>
                    </c:when>
                </c:choose>

                <!-- Back button -->
                <div class="text-center mt-4">
                    <a href="shop" class="btn btn-outline-secondary btn-sm me-2">
                        <i class="bi bi-arrow-left-circle"></i> Back to Shop
                    </a>
                    <button type="button" class="btn btn-dark me-2">
                        <i class="fas fa-cart-plus"></i> Add to Cart
                    </button>
                    <button type="button" class="btn btn-success">
                        <i class="fas fa-shopping-bag"></i> Buy
                    </button>
                </div>
            </div>
        </div>

    </div>
</main>

<%@include file="/WEB-INF/include/footer.jsp" %> 
