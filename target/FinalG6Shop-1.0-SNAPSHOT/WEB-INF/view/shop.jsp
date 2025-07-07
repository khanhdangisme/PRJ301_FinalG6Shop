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
                            <!--làm mờ-->
                            <div class="card mb-4 product-wap rounded-0 ${p.quantity == 0 ? 'out-of-stock' : ''}">

                                <div class="card rounded-0">
                                    <img class="card-img rounded-0 img-fluid"
                                         src="${not empty p.image ? pageContext.request.contextPath.concat('/assets/img/').concat(p.image) : pageContext.request.contextPath.concat('/assets/img/default.jpg')}"
                                         alt="${p.productName}"
                                         style="height: 250px; object-fit: cover;">

                                    <div class="card-img-overlay rounded-0 product-overlay d-flex align-items-center justify-content-center">
                                        <ul class="list-unstyled">
                                            <!--lớp phủ: đã hết hàng-->
                                            <c:if test="${p.quantity == 0}">
                                                <div class="position-absolute top-50 start-50 translate-middle text-center text-white bg-dark bg-opacity-75 px-3 py-2 rounded">
                                                    <strong>Đã hết hàng</strong>
                                                </div>
                                            </c:if>

                                            <!--View button-->
                                            <li>
                                                <a class="btn btn-success text-white mt-2"
                                                   href="shop?view=details&id=${p.productId}&color=${p.color}&storage=${p.storage}">
                                                    <i class="far fa-eye"></i>
                                                </a>
                                            </li>

                                            <!--Add to Cart button-->
                                            <li>
                                                <c:choose>
                                                    <c:when test="${p.quantity > 0}">
                                                        <a class="btn btn-success text-white mt-2"
                                                           href="<c:url value='/cart?action=add&id=${p.productId}&color=${p.color}&storage=${p.storage}'/>">
                                                            <i class="fas fa-cart-plus"></i>
                                                        </a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <button class="btn btn-secondary text-white mt-2"
                                                                onclick="alertOutOfStock()" disabled>
                                                            <i class="fas fa-cart-plus"></i>
                                                        </button>
                                                    </c:otherwise>
                                                </c:choose>
                                            </li>
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

                                    <!--Buy button-->    
                                    <c:choose>
                                        <c:when test="${p.quantity > 0}">
                                            <button type="button"
                                                    class="btn btn-secondary btn-sm w-100"
                                                    style="background-color: black; border-color: black;"
                                                    onclick="showBuy('${p.productId}:${p.color}:${p.storage}')">
                                                <i class="fas fa-shopping-bag"></i> Buy
                                            </button>
                                        </c:when>
                                        <c:otherwise>
                                            <button type="button"
                                                    class="btn btn-secondary btn-sm w-100"
                                                    onclick="alertOutOfStock()"
                                                    style="background-color: gray; border-color: gray;">
                                                <i class="fas fa-ban"></i> Đã hết hàng
                                            </button>
                                        </c:otherwise>
                                    </c:choose>

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

<c:if test="${not empty requestScope.voucherError}">
    <script>
        const modal = new bootstrap.Modal(document.getElementById('checkoutModal'));
        modal.show();
    </script>
    <div class="alert alert-danger mt-2">${requestScope.voucherError}</div>
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
            const url = '${pageContext.request.contextPath}/checkout?selectedIds=' + encodeURIComponent(selId) + '&source=direct';
            window.location.href = url;
            return;
        }
        document.getElementById('buySelIds').value = selId;
        document.getElementById('buySource').value = 'direct';
        new bootstrap.Modal(document.getElementById('buyCheckoutModal')).show();
    }
</script>

<%@include file="/WEB-INF/include/footer.jsp" %>
<%@include file="/WEB-INF/include/showPopupUser.jsp" %>