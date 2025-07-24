<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<title>G6Shop ‑ Order History</title>
<%@ include file="/WEB-INF/include/header.jsp" %>

<main class="container py-5 mt-5">
    <div class="row justify-content-center" style="margin-top: 45px;">
        <div class="col-lg-12">
            <div class="card shadow border-0">
                <div class="card-header bg-dark text-white">
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
                                            <th>#</th>
                                            <th>Image</th>
                                            <th>Product</th>
                                            <th>Version</th>
                                            <th>Color</th>
                                            <th>Storage</th>
                                            <th>Category</th>
                                            <th>Qty</th>
                                            <th>Price</th>
                                            <th>Subtotal</th>
                                            <th>Date</th>
                                            <th>Status</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:set var="displayedOrderIds" value="" />
                                        <c:set var="currentOrderId" value="" />
                                        <c:set var="rowColor" value="bg-white" />
                                        <c:forEach var="item" items="${orderHistories}">
                                            <c:if test="${item.orderId != currentOrderId}">
                                                <c:set var="currentOrderId" value="${item.orderId}" />
                                                <c:choose>
                                                    <c:when test="${rowColor == 'bg-white'}">
                                                        <c:set var="rowColor" value="bg-light" />
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:set var="rowColor" value="bg-white" />
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:if>
                                            <tr class="${rowColor}">
                                                <td>${item.orderId}</td>
                                                <td><img src="${pageContext.request.contextPath}/assets/img/${item.image}" alt="${item.productName}" width="60" height="60" class="rounded shadow-sm border" /></td>
                                                <td class="fw-semibold">${item.productName}</td>
                                                <td>${item.version}</td>
                                                <td>${item.color}</td>
                                                <td>${item.storage}</td>
                                                <td><span class="badge bg-secondary">${item.categoryName}</span></td>
                                                <td>${item.quantity}</td>
                                                <td><fmt:formatNumber value="${item.price}" pattern="#,##0"/> ₫</td>
                                                <c:choose>
                                                    <c:when test="${not fn:contains(displayedOrderIds, item.orderId)}">
                                                        <td class="fw-bold text-danger">
                                                            <fmt:formatNumber value="${item.subTotal}" pattern="#,##0"/> ₫
                                                            <c:set var="displayedOrderIds" value="${displayedOrderIds}${item.orderId}," />
                                                        </td>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <td class="text-muted">–</td>
                                                    </c:otherwise>
                                                </c:choose>
                                                <td>
                                                    <fmt:formatDate value="${item.orderDate}" pattern="HH:mm:ss" /><br/>
                                                    <fmt:formatDate value="${item.orderDate}" pattern="dd/MM/yyyy" />
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${item.status == 'Pending'}">
                                                            <span class="badge bg-warning text-dark">Pending</span>
                                                        </c:when>
                                                        <c:when test="${item.status == 'Confirmed'}">
                                                            <span class="badge bg-primary">Confirmed</span>
                                                        </c:when>
                                                        <c:when test="${item.status == 'Shipping'}">
                                                            <span class="badge bg-info text-dark">Shipping</span>
                                                        </c:when>
                                                        <c:when test="${item.status == 'Completed'}">
                                                            <span class="badge bg-success">Completed</span>
                                                        </c:when>
                                                        <c:when test="${item.status == 'Cancelled'}">
                                                            <span class="badge bg-danger">Cancelled</span>
                                                        </c:when>
                                                        <c:when test="${item.status == 'Cancel Requested'}">
                                                            <span class="badge bg-warning text-dark">Cancel Requested</span>
                                                        </c:when>
                                                        <c:when test="${item.status == 'Return Requested'}">
                                                            <span class="badge bg-secondary">Return Requested</span>
                                                        </c:when>
                                                        <c:when test="${item.status == 'Returned'}">
                                                            <span class="badge bg-dark">Returned</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-secondary">Unknown</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${item.status == 'Pending'}">
                                                            <form action="${pageContext.request.contextPath}/history" method="post" style="display:inline;">
                                                                <input type="hidden" name="action" value="cancel-order" />
                                                                <input type="hidden" name="orderId" value="${item.orderId}" />
                                                                <button type="submit" class="btn btn-sm btn-outline-danger"
                                                                        onclick="return confirm('Are you sure you want to cancel this order?');">
                                                                    Cancel
                                                                </button>
                                                            </form>
                                                        </c:when>
                                                        <c:when test="${item.status == 'Confirmed'}">
                                                            <form action="${pageContext.request.contextPath}/history" method="post" style="display:inline;">
                                                                <input type="hidden" name="action" value="cancel-order" />
                                                                <input type="hidden" name="orderId" value="${item.orderId}" />
                                                                <button type="submit" class="btn btn-sm btn-outline-danger"
                                                                        onclick="return confirm('Are you sure you want to cancel this order?');">
                                                                    Cancel
                                                                </button>
                                                            </form>
                                                        </c:when>
                                                        <c:when test="${item.status == 'Completed'}">
                                                            <form action="${pageContext.request.contextPath}/history" method="post" style="display:inline;">
                                                                <input type="hidden" name="action" value="request-return" />
                                                                <input type="hidden" name="orderId" value="${item.orderId}" />
                                                                <button type="submit" class="btn btn-sm btn-outline-secondary">
                                                                    Request Return
                                                                </button>
                                                            </form>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-muted">–</span>
                                                        </c:otherwise>
                                                    </c:choose>
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
