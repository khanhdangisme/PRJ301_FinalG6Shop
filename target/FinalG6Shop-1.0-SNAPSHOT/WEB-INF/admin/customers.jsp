<%-- 
    Document   : dashboard
    Created on : Jun 18, 2025, 8:05:42 PM
    Author     : KhanhDang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/headerAdmin.jsp" %>
<title>G6Shop - Customers</title>

<main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Customers</h1>
        <div class="btn-toolbar mb-2 mb-md-0">
            <div class="btn-group me-2">
                <button type="button" class="btn btn-sm btn-outline-secondary">Share</button>
                <button type="button" class="btn btn-sm btn-outline-secondary">Export</button>
            </div>
        </div>
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
                        <tr>
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

<%@include file="/WEB-INF/include/footerAdmin.jsp" %>
