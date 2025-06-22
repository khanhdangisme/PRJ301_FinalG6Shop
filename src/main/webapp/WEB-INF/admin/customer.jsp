<%-- 
    Document   : customer
    Created on : Jun 22, 2025, 2:59:18 AM
    Author     : KhanhDang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<title>G6Shop - Customers</title>
<%@include file="/WEB-INF/include/headerAdmin.jsp" %>
<main class="col-md-9 ms-sm-auto col-lg-10 px-md-4" style="margin-top: 110px;">
    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Customers</h1>
        <button type="button" class="btn btn-sm btn-outline-primary" data-bs-toggle="modal" data-bs-target="#createAdminModal">
            Create Admin
        </button>
    </div>

    <div class="table-responsive">
        <table class="table table-striped table-bordered table-sm">
            <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Username</th>
                    <th scope="col">Fullname</th>
                    <th scope="col">Email</th>
                    <th scope="col">Phone</th>
                    <th scope="col">Status</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty list}">
                        <tr>
                            <td colspan="7" class="text-center">No customer found!</td>
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
                                <td>
                                    <button type="button"
                                            class="badge rounded-pill border-0 ${customer.status == 'Enable' ? 'bg-success' : 'bg-secondary'}"
                                            style="min-width: 80px; font-size: 0.85rem; padding: 6px 12px; cursor: pointer; outline: none;"
                                            data-bs-toggle="modal"
                                            data-bs-target="#confirmToggleModal"
                                            data-form-id="toggleForm-${loop.index}"
                                            data-username="${customer.userName}"
                                            data-action="${customer.status == 'Enable' ? 'disable' : 'enable'}">
                                        ${customer.status}
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>

    </div>
</main>
</div>
</div>
<%@include file="/WEB-INF/include/footerAdmin.jsp" %>
