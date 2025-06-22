<%-- 
    Document   : footerAdmin
    Created on : Jun 18, 2025, 10:42:54 PM
    Author     : KhanhDang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>




<!-- Modal Create Admin -->
<div class="modal fade" id="createAdminModal" tabindex="-1" aria-labelledby="createAdminModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form action="${pageContext.request.contextPath}/admin?action=register" method="post">
                <div class="modal-header bg-primary text-white">
                    <h5 class="modal-title" id="createAdminModalLabel">Create Admin</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="form-floating mb-2">
                        <input type="text" class="form-control" name="username" id="usernameInput" placeholder="Enter Username" required>
                        <label for="usernameInput">Username</label>
                    </div>

                    <div class="form-floating mb-2">
                        <input type="password" class="form-control" name="password" id="passwordInput" placeholder="Enter Password" required>
                        <label for="passwordInput">Password</label>
                    </div>

                    <div class="form-floating mb-2">
                        <input type="text" class="form-control" name="fullname" id="fullnameInput" placeholder="Enter Fullname" required>
                        <label for="fullnameInput">Full Name</label>
                    </div>

                    <div class="form-floating mb-2">
                        <input type="email" class="form-control" name="email" id="emailInput" placeholder="name@example.com" required>
                        <label for="emailInput">Email</label>
                    </div>

                    <div class="form-floating mb-2">
                        <input type="text" class="form-control" name="phone" id="phoneInput" placeholder="Enter Phone" required>
                        <label for="phoneInput">Phone Number</label>
                    </div>

                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary">Sign up</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Confirm Status Change -->
<div class="modal fade" id="confirmToggleModal" tabindex="-1" aria-labelledby="confirmToggleLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 rounded-3">

            <!-- Header -->
            <div class="modal-header bg-warning text-dark">
                <h5 class="modal-title" id="confirmToggleLabel">Confirm Status Change</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>

            <!-- Body -->
            <div class="modal-body">
                <!-- Confirm Username -->
                <div class="mb-3">
                    <label for="confirmUsername" class="form-label">Confirm Username</label>
                    <input type="text" class="form-control" id="confirmUsername" name="username" readonly>
                </div>

                <!-- Confirm Status -->
                <div class="mb-3">
                    <label for="confirmStatus" class="form-label">Target Status</label>
                    <input type="text" class="form-control" id="confirmStatus" name="targetStatus" readonly>
                </div>

                <p id="modalConfirmText">Are you sure you want to change this user’s status?</p>
            </div>

            <!-- Footer -->
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-warning text-white" id="confirmToggleBtn">Yes, Change</button>
            </div>
        </div>
    </div>
</div>

<form id="statusForm" action="admin?action=update-status" method="post" class="d-none">
    <input type="hidden" name="username" id="formUsername">
    <input type="hidden" name="targetStatus" id="formStatus">
</form>



<script>
    document.addEventListener("DOMContentLoaded", function () {
        const confirmBtn = document.getElementById("confirmToggleBtn");
        let usernameToUpdate = null;

        const modal = document.getElementById("confirmToggleModal");
        modal.addEventListener("show.bs.modal", function (event) {
            const button = event.relatedTarget;
            usernameToUpdate = button.getAttribute("data-username");
            const action = button.getAttribute("data-action");

            document.getElementById("confirmUsername").value = usernameToUpdate;
            document.getElementById("confirmStatus").value = action.charAt(0).toUpperCase() + action.slice(1);
            document.getElementById("modalConfirmText").textContent =
                    `Are you sure you want to ${action} this user?`;
        });

        confirmBtn.addEventListener("click", function () {
            if (usernameToUpdate) {
                document.getElementById("formUsername").value = usernameToUpdate;
                document.getElementById("formStatus").value = document.getElementById("confirmStatus").value;
                document.getElementById("statusForm").submit();
            }
        });
    });
</script>


<script src="https://cdn.jsdelivr.net/npm/feather-icons@4.28.0/dist/feather.min.js" integrity="sha384-uO3SXW5IuS1ZpFPKugNNWqTZRRglnUJK6UAZ/gxOX80nxEkN9NcGZTftn6RzhGWE" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js@2.9.4/dist/Chart.min.js" integrity="sha384-zNy6FEbO50N+Cg5wap8IKA4M/ZnLJgzc6w2NqACZaK0u0FXfOWRRJOnQtpZun8ha" crossorigin="anonymous"></script>
<script src="assets/js/dashboard.js"></script>
<script src="assets/js/bootstrap.bundle.min.js"></script>
<script src="assets/js/jquery-1.11.0.min.js"></script>
<script src="assets/js/jquery-migrate-1.2.1.min.js"></script>
<script src="assets/js/templatemo.js"></script>
<script src="assets/js/custom.js"></script>
</body>
</html>
