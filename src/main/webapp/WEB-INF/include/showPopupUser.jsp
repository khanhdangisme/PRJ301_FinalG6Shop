<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!-- Search Modal -->
<div class="modal fade" id="templatemo_search" tabindex="-1" aria-labelledby="searchModalLabel" aria-hidden="true">
    <div class="modal-dialog mt-3">
        <div class="modal-content border-0 shadow">
            <div class="modal-header">
                <h5 class="modal-title" id="searchModalLabel">Search</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form action="shop" method="get">
                    <input type="hidden" name="view" value="search">
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

<!-- Cart Modal -->
<div class="modal fade" id="cartModal" tabindex="-1" aria-labelledby="cartModalLabel" aria-hidden="true">
    <div class="modal-dialog mt-3">
        <div class="modal-content border-0 shadow">

            <!-- Header -->
            <div class="modal-header">
                <h5 class="modal-title" id="cartModalLabel">Your Cart</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>

            <!-- Body -->
            <div class="modal-body px-4 py-3">
                <c:choose>
                    <c:when test="${sessionScope.cartCount gt 0}">
                        <p class="mb-0">You have
                            <strong>${sessionScope.cartCount}</strong>
                            item<c:if test="${sessionScope.cartCount gt 1}">s</c:if> in your cart.
                            </p>
                    </c:when>
                    <c:otherwise>
                        <p class="mb-0">Your cart is currently empty.</p>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Footer -->
            <div class="modal-footer px-4 py-3">
                <a href="${pageContext.request.contextPath}/cart?action=view" class="btn btn-success">
                    Go to Cart
                </a>
            </div>

        </div>
    </div>
</div>

<div class="modal fade" id="cartAlertModal" tabindex="-1" aria-labelledby="cartAlertLabel" aria-hidden="true">
    <div class="modal-dialog mt-3">
        <div class="modal-content border-0 shadow">

            <!-- Header -->
            <div class="modal-header">
                <h5 class="modal-title" id="cartAlertLabel">Your Cart</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>

            <!-- Body -->
            <div class="modal-body px-4 py-3" id="cartAlertBody">
                <!-- Nội dung được JS thay đổi -->
            </div>

            <!-- Footer -->
            <div class="modal-footer px-4 py-3">
                <a href="${pageContext.request.contextPath}/cart?action=view" class="btn btn-success">
                    Go to Cart
                </a>
            </div>

        </div>
    </div>
</div>

<!-- Checkout Modal -->
<div class="modal fade" id="checkoutModal" tabindex="-1" aria-labelledby="checkoutLabel" aria-hidden="true">
    <div class="modal-dialog mt-3">
        <div class="modal-content border-0 shadow">
            <div class="modal-header bg-success text-white">
                <h5 class="modal-title" id="checkoutLabel">Checkout Information</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>

            <form action="${pageContext.request.contextPath}/checkout" method="post">
                <div class="modal-body px-4 py-3">
                    <!-- id sản phẩm đã chọn -->
                    <input type="hidden" name="selectedIds" id="coSelIds">

                    <div class="mb-2">
                        <label class="form-label">Fullname</label>
                        <input name="fullname" class="form-control" required>
                    </div>
                    <div class="mb-2">
                        <label class="form-label">Email</label>
                        <input name="email" type="email" class="form-control" required>
                    </div>
                    <div class="mb-2">
                        <label class="form-label">Phone</label>
                        <input name="phone" class="form-control" required>
                    </div>
                    <div class="mb-2">
                        <label class="form-label">Address</label>
                        <input name="address" class="form-control" required>
                    </div>
                    <div class="row">
                        <div class="col-md-6 mb-2">
                            <label class="form-label">City</label>
                            <input name="city" class="form-control" required>
                        </div>
                        <div class="col-md-6 mb-2">
                            <label class="form-label">Province</label>
                            <input name="province" class="form-control" required>
                        </div>
                    </div>
                </div>
                <div class="modal-footer px-4 py-3">
                    <button type="submit" class="btn btn-success px-4">Place Order</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Delete Confirmation Modal -->
<div class="modal fade" id="confirmDeleteModal" tabindex="-1" aria-labelledby="confirmDeleteLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 rounded-3">

            <!-- Header -->
            <div class="modal-header" style="background-color: #E43B3B; color: white;">
                <h5 class="modal-title" id="confirmDeleteLabel">Confirm Deletion</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>

            <!-- FORM -->
            <form action="user?action=delete" method="post">
                <input type="hidden" name="action" value="delete">

                <!-- Body -->
                <div class="modal-body text-dark">
                    <div class="mb-3">
                        <label for="confirmUsername" class="form-label">Confirm Username</label>
                        <input type="text" class="form-control" id="confirmUsername" name="username" 
                               value="${sessionScope.loggedUser.userName}" readonly>
                    </div>

                    <div class="mb-3">
                        <label for="confirmPassword" class="form-label">Confirm Password</label>
                        <input type="password" class="form-control" id="confirmPassword" name="currentPassword"
                               style="border-radius: 0.375rem;" required>
                    </div>

                    <p class="mb-0">Are you sure you want to delete your account? This action cannot be undone.</p>
                </div>

                <!-- Footer -->
                <div class="modal-footer justify-content-end">
                    <a href="user?view=profile" class="btn btn-secondary fw-semibold py-1 me-2">
                        <i class="fa fa-times me-1"></i> Cancel
                    </a>
                    <button type="submit" class="btn btn-warning fw-semibold px-4" style="background-color: #E43B3B; color: white;">
                        <i class="fa fa-trash me-1"></i> Delete
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Cart Alert Modal (Cảnh báo giỏ hàng) -->
<div class="modal fade" id="cartAlertModal" tabindex="-1" aria-labelledby="cartAlertLabel" aria-hidden="true">
    <div class="modal-dialog mt-3"> <%-- giống #cartModal --%>
        <div class="modal-content border-0 shadow"> <%-- giống #cartModal --%>

            <!-- Header -->
            <div class="modal-header">
                <h5 class="modal-title" id="cartAlertLabel">Your Cart</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>

            <!-- Nội dung động -->
            <div class="modal-body" id="cartAlertBody">
                <!-- JS sẽ chèn nội dung như “You have not selected any products.” -->
            </div>

            <!-- Footer -->
            <div class="modal-footer">
                <a href="${pageContext.request.contextPath}/cart?action=view"
                   class="btn btn-success">Go to Cart</a>
            </div>

        </div>
    </div>
</div>

<!-- Checkout Modal -->
<div class="modal fade" id="checkoutModal" tabindex="-1" aria-labelledby="checkoutLabel" aria-hidden="true">
    <div class="modal-dialog mt-3">
        <div class="modal-content border-0 shadow">
            <div class="modal-header bg-success text-white">
                <h5 class="modal-title" id="checkoutLabel">Checkout Information</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>

            <form action="${pageContext.request.contextPath}/checkout" method="post">
                <div class="modal-body px-4 py-3">
                    <input type="hidden" name="selectedIds" id="coSelIds">
                    <div class="mb-2">
                        <label class="form-label">Fullname</label>
                        <input name="fullname" class="form-control" required>
                    </div>
                    <div class="mb-2">
                        <label class="form-label">Email</label>
                        <input name="email" type="email" class="form-control" required>
                    </div>
                    <div class="mb-2">
                        <label class="form-label">Phone</label>
                        <input name="phone" class="form-control" required>
                    </div>
                    <div class="mb-2">
                        <label class="form-label">Address</label>
                        <input name="address" class="form-control" required>
                    </div>
                    <div class="row">
                        <div class="col-md-6 mb-2">
                            <label class="form-label">City</label>
                            <input name="city" class="form-control" required>
                        </div>
                        <div class="col-md-6 mb-2">
                            <label class="form-label">Province</label>
                            <input name="province" class="form-control" required>
                        </div>
                    </div>
                </div>
                <div class="modal-footer px-4 py-3">
                    <button type="submit" class="btn btn-success px-4">Place Order</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Modal: Order Success -->
<div class="modal fade" id="orderSuccessModal" tabindex="-1" aria-labelledby="orderSuccessLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow">
            <div class="modal-header">
                <h5 class="modal-title text-success" id="orderSuccessLabel">
                    <i class="fas fa-check-circle me-2"></i> Order placed successfully!
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body text-center">
                <p>Thank you for shopping at <strong>G6Shop</strong>!</p>
                <p>A confirmation email has been sent to you.</p>
                <p>You can check your order status in <a href="${pageContext.request.contextPath}/history">My Orders</a>.</p>
            </div>
            <div class="modal-footer justify-content-center">
                <a href="${pageContext.request.contextPath}/shop" class="btn btn-dark">
                    <i class="fas fa-shopping-bag"></i> Continue shopping
                </a>
            </div>
        </div>
    </div>
</div>

<!-- Buy Checkout Modal -->
<div class="modal fade" id="buyCheckoutModal" tabindex="-1" aria-labelledby="buyCheckoutLabel" aria-hidden="true">
    <div class="modal-dialog mt-3">
        <div class="modal-content border-0 shadow">
            <div class="modal-header bg-success text-white">
                <h5 class="modal-title" id="buyCheckoutLabel">Checkout Information (Buy Now)</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>

            <form action="${pageContext.request.contextPath}/checkout" method="post">
                <div class="modal-body px-4 py-3">
                    <!-- ID sản phẩm đã chọn -->
                    <input type="hidden" name="selectedIds" id="buySelIds">

                    <input type="hidden" name="source" id="buySource" value="direct">

                    <!-- Thông tin người dùng -->
                    <div class="mb-2">
                        <label class="form-label">Fullname</label>
                        <input name="fullname" class="form-control" required>
                    </div>
                    <div class="mb-2">
                        <label class="form-label">Email</label>
                        <input name="email" type="email" class="form-control" required>
                    </div>
                    <div class="mb-2">
                        <label class="form-label">Phone</label>
                        <input name="phone" class="form-control" required>
                    </div>
                    <div class="mb-2">
                        <label class="form-label">Address</label>
                        <input name="address" class="form-control" required>
                    </div>
                    <div class="row">
                        <div class="col-md-6 mb-2">
                            <label class="form-label">City</label>
                            <input name="city" class="form-control" required>
                        </div>
                        <div class="col-md-6 mb-2">
                            <label class="form-label">Province</label>
                            <input name="province" class="form-control" required>
                        </div>
                    </div>

                    <!-- Thông báo lỗi voucher -->
                    <div id="voucherErrorAlert" class="alert alert-danger d-none mt-2 text-center fw-semibold">
                        <!-- JS sẽ chèn lỗi voucher vào đây -->
                    </div>
                </div>

                <!-- Footer có thêm ô nhập mã giảm giá -->
                <div class="modal-footer px-4 py-3 d-flex justify-content-between align-items-center flex-wrap gap-2">
                    <!-- Ô nhập mã giảm giá -->
                    <input type="text" name="voucherCode" class="form-control"
                           placeholder="Enter voucher code (optional)" style="max-width: 260px;" />

                    <!-- Nút đặt hàng -->
                    <button type="submit" class="btn btn-success px-4">
                        Place Order
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<c:if test="${not empty sessionScope.voucherError}">
    <script>
        window.addEventListener("load", () => {
            const modal = new bootstrap.Modal(document.getElementById('voucherErrorModal'));
            modal.show();
        });
    </script>
    <c:remove var="voucherError" scope="session"/>
</c:if>                    

<c:if test="${sessionScope.orderSuccess}">
    <script>
        window.addEventListener("load", () => {
            const modal = new bootstrap.Modal(document.getElementById('orderSuccessModal'));
            modal.show();
        });
    </script>
    <c:remove var="orderSuccess" scope="session"/>
</c:if>

<c:if test="${not empty requestScope.voucherError}">
    <script>
        window.addEventListener("load", () => {
            const modal = new bootstrap.Modal(document.getElementById('buyCheckoutModal'));
            modal.show();

            const alertContainer = document.getElementById("voucherErrorAlert");
            if (alertContainer) {
                alertContainer.innerText = "${requestScope.voucherError}";
                alertContainer.classList.remove("d-none");
            }
        });
    </script>
</c:if>

<script>
    function isValidText(value) {
        return /^[a-zA-Z0-9\s]+$/.test(value);
    }

    function isValidPhone(phone) {
        return /^0\d{9}$/.test(phone);
    }

    function isValidEmail(email) {
        return /^[\w.-]+@[\w.-]+\.[a-zA-Z]{2,6}$/.test(email);
    }

    function validateCheckoutForm(form) {
        const fullname = form.fullname.value.trim();
        const email = form.email.value.trim();
        const phone = form.phone.value.trim();
        const address = form.address.value.trim();
        const city = form.city.value.trim();
        const province = form.province.value.trim();

        if (!isValidText(fullname)) {
            alert("Fullname must contain only letters, numbers, and spaces.");
            form.fullname.focus();
            return false;
        }
        if (!isValidEmail(email)) {
            alert("Please enter a valid email (e.g. example@gmail.com).");
            form.email.focus();
            return false;
        }
        if (!isValidPhone(phone)) {
            alert("Phone number must start with 0 and contain exactly 10 digits.");
            form.phone.focus();
            return false;
        }
        if (!isValidText(address)) {
            alert("Address must contain only letters, numbers, and spaces.");
            form.address.focus();
            return false;
        }
        if (!isValidText(city)) {
            alert("City must contain only letters, numbers, and spaces.");
            form.city.focus();
            return false;
        }
        if (!isValidText(province)) {
            alert("Province must contain only letters, numbers, and spaces.");
            form.province.focus();
            return false;
        }

        return true; // allow submission
    }

// Gán xử lý cho các form Checkout
    document.addEventListener("DOMContentLoaded", function () {
        const checkoutForm = document.querySelector('#checkoutModal form');
        const buyCheckoutForm = document.querySelector('#buyCheckoutModal form');

        if (checkoutForm) {
            checkoutForm.onsubmit = function (e) {
                if (!validateCheckoutForm(this))
                    e.preventDefault();
            }
        }

        if (buyCheckoutForm) {
            buyCheckoutForm.onsubmit = function (e) {
                if (!validateCheckoutForm(this))
                    e.preventDefault();
            }
        }
    });
</script>
