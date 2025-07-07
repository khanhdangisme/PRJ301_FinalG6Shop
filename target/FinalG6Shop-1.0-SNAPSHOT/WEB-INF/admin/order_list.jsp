<%-- 
    Document   : customer
    Created on : Jun 22, 2025, 2:59:18 AM
    Author     : KhanhDang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<title>G6Shop - Order History</title>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="/WEB-INF/include/headerAdmin.jsp" %>
<main class="col-md-9 ms-sm-auto col-lg-10 px-md-4" style="margin-top: 110px;">
    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">All Order History</h1>
    </div>
    <div class="card shadow border-0">
    <div class="card-body">
        <c:choose>
            <c:when test="${empty orders}">
                <div class="alert alert-warning text-center">
                    <i class="fa fa-exclamation-circle me-2"></i> No orders found.
                </div>
            </c:when>
            <c:otherwise>
                <div class="table-responsive">
                    <table class="table table-bordered table-hover align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>Image</th>
                                <th>Product</th>
                                <th>Version</th>
                                <th>Color</th>
                                <th>Storage</th>
                                <th>Category</th>
                                <th>Quantity</th>
                                <th>Price</th>
                                <th>Subtotal</th>
                                <th>Customer</th>
                                <th>Date</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${orders}">
                                <tr>
                                    <td>
                                        <img src="${pageContext.request.contextPath}/assets/img/${item.image}"
                                             alt="" width="60" height="60" class="rounded shadow-sm" />
                                    </td>
                                    <td>${item.productName}</td>
                                    <td>${item.version}</td>
                                    <td>${item.color}</td>
                                    <td>${item.storage}</td>
                                    <td>${item.categoryName}</td>
                                    <td>${item.quantity}</td>
                                    <td><fmt:formatNumber value="${item.price}" pattern="#,##0"/> ₫</td>
                            <td><fmt:formatNumber value="${item.subTotal}" pattern="#,##0"/> ₫</td>
                            <td>${item.username}</td>
                            <td>
                            <fmt:formatDate value="${item.orderDate}" pattern="HH:mm:ss" /><br/>
                            <fmt:formatDate value="${item.orderDate}" pattern="dd/MM/yyyy" />
                            </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
        </div>
</main>
</div>
</div>
<%@include file="/WEB-INF/include/footerAdmin.jsp" %>
