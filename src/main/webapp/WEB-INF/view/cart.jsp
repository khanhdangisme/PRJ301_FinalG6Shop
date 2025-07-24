<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/header.jsp" %>

<!-- Voucher Floating Icon -->
<title>G6Shop ‑ Cart</title>

<!-- ====== GIAO DIỆN SÁNG, FONT ROBOTO / BOOTSTRAP ====== -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
<style>
    .navbar .fw-semibold {
        font-weight: 300 !important; /* Đảm bảo không bị ghi đè thành 700 */
    }
    body            {
        background:#f8f9fa;
        padding-top:100px;
    }
    h1,h5            {
        font-family: "Roboto", sans-serif;
    }
    .table thead     {
        background:#212529;
        color:#fff;
    }
    .table tbody tr  {
        background:#fff;
    }
    .form-check-input:checked{
        background:#0d6efd;
        border-color:#0d6efd;
    }
    .total-amount    {
        font-weight:700;
        color:#dc3545;
    }
    /* kích thước nhỏ gọn cho nút mũi tên */
    .qty-btn{
        width:2rem;
        padding:0rem 0;
    }

    input[type=number]::-webkit-inner-spin-button,
    input[type=number]::-webkit-outer-spin-button {
        -webkit-appearance: none;
        margin: 0;
    }

    input[type=number] {
        -moz-appearance: textfield;
    }

    /* ẩn nút remove của header để gọn */
    th.remove-col{
        width:1px;
    }
    .product-image {
        width: 60px;  /* Điều chỉnh chiều rộng */
        height: 80px; /* Điều chỉnh chiều cao */
        object-fit: cover; /* Đảm bảo hình ảnh không bị kéo dãn */
        border-radius: 5px; /* Thêm góc bo tròn (tuỳ chọn) */
    }
</style>

<div class="container py-4">
    <h1 class="h2 text-center mb-4"><strong>Your Shopping Cart</strong></h1>

    <c:choose>
        <c:when test="${not empty sessionScope.couponError}">
            <div class="alert alert-danger auto-dismiss text-center">${sessionScope.couponError}</div>
            <c:remove var="couponError" scope="session"/>
        </c:when>
        <c:when test="${not empty sessionScope.couponSuccess}">
            <div class="alert alert-success auto-dismiss text-center">${sessionScope.couponSuccess}</div>
            <c:remove var="couponSuccess" scope="session"/>
        </c:when>
    </c:choose>

    <c:if test="${not empty sessionScope.successMessage}">
        <div class="alert alert-success auto-dismiss text-center my-3">
            <i class="fa fa-check-circle me-2"></i>${sessionScope.successMessage}
        </div>
        <c:remove var="successMessage" scope="session"/>
    </c:if>

    <c:if test="${not empty sessionScope.errorMessage}">
        <div class="alert alert-danger auto-dismiss text-center my-3">
            <i class="fa fa-exclamation-circle me-2"></i>${sessionScope.errorMessage}
        </div>
        <c:remove var="errorMessage" scope="session"/>
    </c:if>

    <!-- ====== GIỎ TRỐNG ====== -->
    <c:if test="${empty cartItems}">
        <p class="text-muted text-center">Your cart is empty.</p>
    </c:if>

    <!-- ====== GIỎ CÓ SẢN PHẨM ====== -->
    <c:if test="${not empty cartItems}">
        <table class="table table-bordered align-middle text-center">
            <thead>
                <tr>
                    <th><input type="checkbox" id="selectAll" class="form-check-input"></th>
                    <th>Image</th>
                    <th>Product</th>
                    <th>Version</th>
                    <th>Color</th>
                    <th>Storage</th>
                    <th>Quantity</th>
                    <th>Price</th>
                    <th>Total</th>
                    <th class="remove-col"></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${cartItems}">
                    <tr>
                        <!-- Checkbox chọn mua -->
                        <td>
                            <input type="checkbox"
                                   class="form-check-input item-checkbox"
                                   data-price="${item.subTotal}"
                                   data-id="${item.productId}:${item.color}:${item.storage}"
                                   checked>
                        </td>
                        <td><img src="${pageContext.request.contextPath}/assets/img/${item.image}" alt="${item.productName}" class="product-image"></td>
                        <!-- Thông tin sản phẩm -->
                        <td class="text-start">${item.productName}</td>
                        <td>${item.version}</td>

                        <td>${item.color}</td>
                        <td>${item.storage}</td>

                        <!-- SỐ LƯỢNG -->
                        <td>
                            <form action="${pageContext.request.contextPath}/cart" method="post" class="d-inline-flex align-items-center">

                                <input type="hidden" name="action"  value="update">
                                <input type="hidden" name="id"      value="${item.productId}">
                                <input type="hidden" name="color"   value="${item.color}">
                                <input type="hidden" name="storage" value="${item.storage}">

                                <div class="input-group input-group-sm qty-group">
                                    <!-- nút ↓ -->
                                    <button type="button" class="btn btn-outline-dark qty-btn"
                                            onclick="changeQty(this, -1)">
                                        <i class="fas fa-minus"></i>
                                    </button>

                                    <!-- ô nhập (ẩn spinner mặc định) -->
                                    <input type="number" name="quantity" min="0"
                                           class="form-control text-center no-spinner qty-input"
                                           value="${item.quantity}">

                                    <!-- nút ↑ -->
                                    <button type="button" class="btn btn-outline-dark qty-btn"
                                            onclick="changeQty(this, 1)">
                                        <i class="fas fa-plus"></i>
                                    </button>
                                </div>
                            </form>
                        </td>

                        <!-- Giá & Thành tiền -->
                        <td class="text-success">
                            <fmt:formatNumber value="${item.price}" pattern="#,##0"/>₫
                        </td>
                        <td class="fw-bold text-danger">
                            <fmt:formatNumber value="${item.subTotal}" pattern="#,##0"/>₫
                        </td>
                        <!-- REMOVE -->
                        <td>
                            <form action="cart" method="post">
                                <input type="hidden" name="action"  value="remove">
                                <input type="hidden" name="id"      value="${item.productId}">
                                <input type="hidden" name="color"   value="${item.color}">
                                <input type="hidden" name="storage" value="${item.storage}">
                                <button type="submit" class="btn btn-sm btn-danger">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>

    <!-- ====== COUPON & TỔNG TIỀN ====== -->
    <div class="row mt-4 align-items-end gy-3">
        <!-- Apply coupon -->
        <div class="col-md-6">
            <form action="cart" method="post" class="input-group" style="margin-top: 20px;">
                <input type="hidden" name="action" value="applyCoupon">
                <input type="text" name="voucherCode" id="couponCode"
                       class="form-control" placeholder="Enter Coupon Code" style="height: 46px;"
                       value="${sessionScope.voucherCode != null ? sessionScope.voucherCode : ''}">
                <button class="btn btn-dark" type="submit" style="height: 46px;">Apply</button>
            </form>
        </div>

        <!-- Tổng cộng -->
        <div class="col-md-6 text-md-end">
            <h5>Subtotal:
                <span><fmt:formatNumber value="${total}" pattern="#,##0"/>₫</span>
            </h5>
            <c:if test="${discount > 0}">
                <h5>Discount:
                    <span><fmt:formatNumber value="${discount}" pattern="#,##0"/>₫</span>
                </h5>
            </c:if>
            <h5 class="total-amount">Final Total:
                <span id="totalAmount"><fmt:formatNumber value="${finalTotal}" pattern="#,##0"/>₫</span>
            </h5>
            <button class="btn btn-dark px-4" onclick="checkout()" style="height: 46px;">Checkout</button>
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

<!-- ====== SCRIPT ====== -->
<!--<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>-->
<!--<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>-->

<script>
    /* ---- ĐỊNH DẠNG TIỀN ---- */
    const formatVND = v => new Intl.NumberFormat('vi-VN').format(v) + '₫';

    /* ---- CẬP NHẬT TỔNG ---- */
    // function updateTotal() {
    //     let total = 0;
    //     document.querySelectorAll('.item-checkbox:checked').forEach(cb => {
    //         total += parseFloat(cb.dataset.price);
    //     });
    //     document.getElementById('totalAmount').innerText = formatVND(total);
    // }
    // document.querySelectorAll('.item-checkbox').forEach(cb => cb.addEventListener('change', updateTotal));
    // document.getElementById('selectAll')?.addEventListener('change', function () {
    //     document.querySelectorAll('.item-checkbox').forEach(cb => cb.checked = this.checked);
    //     updateTotal();
    // });
    // updateTotal();        // chạy lần đầu

    /* ---- NÚT ↑ / ↓ ---- */
    function changeQty(btn, delta) {
        const form = btn.closest('form');
        const input = form.querySelector('input[name="quantity"]');
        let val = parseInt(input.value) || 0;
        val += delta;
        if (val < 0)
            val = 0;
        input.value = val;
        form.submit();    // POST về cart?action=update
    }
    function submitClosest(el) {
        el.closest('form').submit();
    }

    /* ---- GÕ ENTER HOẶC RỜI Ô ---- */
    document.querySelectorAll('.qty-input').forEach(inp => {
        // enter
        inp.addEventListener('keydown', e => {
            if (e.key === 'Enter') {
                e.preventDefault();          // khỏi reload trang
                e.stopPropagation(); // Ngăn sự kiện lan truyền
                inp.closest('form').submit();
            }
        });
        // blur
        inp.addEventListener('blur', () => {
            inp.closest('form').submit();
        });
    });

    /* ---- CHECKOUT ---- */
    function checkout() {
        const ids = [...document.querySelectorAll('.item-checkbox:checked')]
                .map(cb => cb.dataset.id);
        const voucherCode = document.getElementById('couponCode')?.value || "";

        if (ids.length === 0) {
            // dùng modal cảnh báo đã có (cartAlertModal)
            document.getElementById('cartAlertBody').textContent =
                    "You have not selected any products.";
            new bootstrap.Modal(document.getElementById('cartAlertModal')).show();
            return;
        }

        /* → nếu ĐÃ đăng nhập */
        const loggedIn = '${sessionScope.loggedUser != null}' === 'true';
        if (loggedIn) {
            document.getElementById('coSelIds').value = ids.join(',');
            new bootstrap.Modal(document.getElementById('checkoutModal')).show();
            return;
        }

        /* → chưa đăng nhập : chuyển về /checkout để Filter báo “You need to log in!” */
        const url = '${pageContext.request.contextPath}/checkout'
                + '?selectedIds=' + encodeURIComponent(ids.join(','))
                + '&voucherCode=' + encodeURIComponent(voucherCode);
        window.location.href = url;
    }
</script>

<%@include file="/WEB-INF/include/footer.jsp"%>
<%@include file="/WEB-INF/include/showPopupUser.jsp" %>

<script>
    window.addEventListener('DOMContentLoaded', function () {
        setTimeout(function () {
            document.querySelectorAll('.auto-dismiss').forEach(function (el) {
                el.style.transition = 'opacity 0.5s';
                el.style.opacity = 0;
                setTimeout(function () {
                    el.remove();
                }, 500);
            });
        }, 3000);
    });
</script>

</body>
</html>