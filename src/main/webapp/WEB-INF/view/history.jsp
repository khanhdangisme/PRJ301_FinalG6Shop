<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/include/header.jsp" %>

<main class="container py-5 mt-5">
    <div class="row justify-content-center" style="margin-top: 45px;">
        <div class="col-lg-12">
            <div class="card shadow border-0">
                <div class="card-header bg-primary text-white">
                    <h4 class="mb-0"><i class="fa fa-history me-2"></i> Your Order History</h4>
                </div>
                <div class="card-body">

                    <c:choose>
                        <c:when test="${empty orderHistories}">
                            <div class="alert alert-info text-center">
                                <i class="fa fa-info-circle me-2"></i> You have no past orders.
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">

                                <table class="table table-hover align-middle">
                                    <thead class="table-light">
                                        <tr>
                                            <th scope="col">Image</th>
                                            <th scope="col">Product</th>
                                            <th scope="col">Version</th>
                                            <th scope="col">Color</th>
                                            <th scope="col">Storage</th>
                                            <th scope="col">Category</th>
                                            <th scope="col">Qty</th>
                                            <th scope="col">Price</th>
                                            <th scope="col">Subtotal</th>
                                            <th scope="col">Date</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="item" items="${orderHistories}">
                                            <tr>
                                                <td>
                                                    <img src="${pageContext.request.contextPath}/assets/img/${item.image}"
                                                         alt="${item.productName}" width="60" height="60"
                                                         class="rounded shadow-sm border" />
                                                </td>
                                                <td class="fw-semibold">${item.productName}</td>
                                                <td>${item.version}</td>
                                                <td>${item.color}</td>
                                                <td>${item.storage}</td>
                                                <td><span class="badge bg-secondary">${item.categoryName}</span></td>
                                                <td>${item.quantity}</td>
                                                <td><fmt:formatNumber value="${item.price}" pattern="#,##0"/> ₫</td>
                                                <td class="fw-bold text-danger">
                                                    <fmt:formatNumber value="${item.subTotal}" pattern="#,##0"/> ₫
                                                </td>
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
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/include/footer.jsp" %>
