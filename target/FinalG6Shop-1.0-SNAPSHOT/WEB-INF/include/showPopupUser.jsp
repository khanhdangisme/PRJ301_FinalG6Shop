<%-- 
    Document   : showPopup
    Created on : Jun 19, 2025, 10:44:42 PM
    Author     : KhanhDang
--%>

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
                <form action="search" method="get">
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
            <div class="modal-header">
                <h5 class="modal-title" id="cartModalLabel">Your Cart</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <!-- Nội dung giỏ hàng tại đây -->
                <p>Your cart is currently empty.</p>
                <!-- Có thể thay bằng table sản phẩm -->
            </div>
            <div class="modal-footer">
                <a href="cart.jsp" class="btn btn-success">Go to Cart</a>
            </div>
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



