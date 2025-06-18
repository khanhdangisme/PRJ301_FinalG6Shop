<%-- 
    Document   : dashboard
    Created on : Jun 18, 2025, 8:05:42 PM
    Author     : KhanhDang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/headerAdmin.jsp" %>
<%@include file="/WEB-INF/include/showNotification.jsp" %>
<title>G6Shop - Customers</title>
<link href="/assets/css/signin.css" rel="stylesheet">

<main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Customers</h1>
        <button type="button" class="btn btn-sm btn-outline-primary" data-bs-toggle="modal" data-bs-target="#createAdminModal">
            Create Admin
        </button>
    </div>

    <div class="table-responsive">
        <table class="table table-striped table-sm">
            <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Username</th>
                    <th scope="col">Fullname</th>
                    <th scope="col">Email</th>
                    <th scope="col">Phone</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty list}">
                        <tr>
                            <td colspan="5" class="text-center">No customer found!</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="customer" items="${list}" varStatus="loop">
                            <tr class="${customer.userRole == 0 ? 'table-danger' : ''}">
                                <td>${loop.index + 1}</td>
                                <td>${customer.userName}</td>
                                <td>${customer.userFullname}</td>
                                <td>${customer.userEmail}</td>
                                <td>${customer.userPhone}</td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</main>
<!-- Modal Create Admin -->
<div class="modal fade" id="createAdminModal" tabindex="-1" aria-labelledby="createAdminModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form action="${pageContext.request.contextPath}/admin?action=register" method="post">
                <div class="modal-header">
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


<%@include file="/WEB-INF/include/footerAdmin.jsp" %>
