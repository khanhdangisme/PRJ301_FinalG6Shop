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

                <c:set var="discountPercent" value="10"/>
                <c:set var="finalPrice" value="${price * (1 - discountPercent / 100.0)}"/>

                <div class="text-center mt-4">
                    <div class="d-flex justify-content-between align-items-end flex-wrap gap-2">
                        <!-- Nút Back -->
                        <a href="shop" class="btn btn-outline-secondary" style="width: 160px; height: 44px; font-weight: bold; display: flex; align-items: center; justify-content: center; margin-bottom: 5px;">
                            <i class="fas fa-arrow-left"></i> Back to Shop
                        </a>

                        <!-- Ô nhập voucher -->
                        <input type="text" value="G6SALE10"
                               class="form-control text-center fw-bold"
                               style="width: 300px; height: 44px; margin-bottom: 5px;" />

                        <!-- Nút Add + Buy + Total Price -->
                        <div class="d-flex flex-column align-items-center gap-1">
                            <span class="text-danger fw-bold" style="font-size: 1rem; text-align: center; margin-bottom: 5px;">
                                <fmt:formatNumber value="${price * 0.9}" pattern="#,##0"/> ₫ (10% off)
                            </span>
                            <div class="d-flex gap-2">
                                <c:choose>
                                    <c:when test="${getDetail.productQuatity > 0}">
                                        <button type="button"
                                                onclick="location.href = '<c:url value='/cart?action=add&id=${getDetail.productID}&color=${color}&storage=${storage}'/>'"
                                                class="btn btn-dark" style="width: 160px; height: 44px; font-weight: bold; display: flex; align-items: center; justify-content: center;">
                                            <i class="fas fa-cart-plus"></i> Add to Cart
                                        </button>
                                        <button type="button"
                                                onclick="showBuy('${getDetail.productID}:${color}:${storage}')"
                                                class="btn btn-success" style="width: 160px; height: 44px; font-weight: bold; display: flex; align-items: center; justify-content: center;">
                                            <i class="fas fa-shopping-bag"></i> Buy
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <button type="button" class="btn btn-danger" disabled
                                                style="width: 160px; height: 44px; font-weight: bold; display: flex; align-items: center; justify-content: center;">
                                            <i class="fas fa-times-circle"></i> Out of Stock
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>

    </div>
</main>

<!-- ====== ORDER SUCCESS TRIGGER ====== -->
<c:if test="${sessionScope.orderSuccess}">
    <script>
        window.addEventListener("load", () => {
            const modal = new bootstrap.Modal(document.getElementById('orderSuccessModal'));
            modal.show();
        });
    </script>
    <c:remove var="orderSuccess" scope="session"/>
</c:if>

<c:if test="${not empty sessionScope.coSelIds}">
    <script>
        window.addEventListener("load", () => {
            const selId = "${sessionScope.coSelIds}";
            document.getElementById('coSelIds').value = selId;
            new bootstrap.Modal(document.getElementById('checkoutModal')).show();
        });
    </script>
    <c:remove var="coSelIds" scope="session" />
</c:if>

<script>
    function alertOutOfStock() {
        const body = document.getElementById('cartAlertBody');
        body.innerText = "Sản phẩm đã hết hàng. Vui lòng chọn sản phẩm khác.";
        const modal = new bootstrap.Modal(document.getElementById('cartAlertModal'));
        modal.show();
    }
</script>    

<script>
    function showLoginModal(redirectUrl) {
        sessionStorage.setItem("redirectAfterLogin", redirectUrl);
        const loginModal = new bootstrap.Modal(document.getElementById('loginModal'));
        loginModal.show();
    }

    function showCheckoutForm(productId, color, storage) {
        document.getElementById('checkoutProductId').value = productId;
        document.getElementById('checkoutColor').value = color;
        document.getElementById('checkoutStorage').value = storage;

        const modal = new bootstrap.Modal(document.getElementById('checkoutModal'));
        modal.show();
    }
</script>

<script>
    function showBuy(selId) {
        const loggedIn = '${sessionScope.loggedUser != null}';

        if (loggedIn === 'false') {
            const url = '${pageContext.request.contextPath}/checkout'
                    + '?selectedIds=' + encodeURIComponent(selId);
            window.location.href = url;
            return;
        }

        // Đã login → show buyCheckoutModal
        document.getElementById('buySelIds').value = selId;
        new bootstrap.Modal(document.getElementById('buyCheckoutModal')).show();
    }
</script>

<%@include file="/WEB-INF/include/footer.jsp" %> 
<%@include file="/WEB-INF/include/showPopupUser.jsp" %>