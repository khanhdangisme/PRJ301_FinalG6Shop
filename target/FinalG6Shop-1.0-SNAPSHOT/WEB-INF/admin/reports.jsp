<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@include file="/WEB-INF/include/headerAdmin.jsp" %>
<title>G6Shop - Report</title>

<main class="col-md-9 ms-sm-auto col-lg-10 px-md-4" style="margin-top: 110px;">
    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Revenue Report</h1>
    </div>
    <div class="card shadow-sm p-4">
        <!-- Lọc theo ngày -->
        <form class="row g-3 align-items-end mb-3" action="admin" method="post">
            <div class="col-auto">
                <label for="startDate" class="form-label mb-0">From:</label>
                <input type="date" id="startDate" name="startDate" class="form-control" required value="${startDate}"/>
            </div>
            <div class="col-auto">
                <label for="endDate" class="form-label mb-0">To:</label>
                <input type="date" id="endDate" name="endDate" class="form-control" required value="${endDate}"/>
            </div>
            <div class="col-auto">
                <button type="submit" class="btn btn-outline-primary fw-semibold" name="action" value="revenue">Revenue</button>
            </div>
            <div class="col-auto">
                <button class="btn btn-outline-primary" type="submit" name="action" value="bestselling">Best selling products</button>
            </div>
        </form>

        <!-- Hiển thị bảng sau khi chọn action -->
        <c:if test="${not empty action}">
            <c:choose>
                <c:when test="${action == 'bestselling'}">
                    <!-- Bảng sản phẩm bán chạy -->
                    <h4 class="mb-3 mt-2">Best selling products</h4>
                    <div class="table-responsive">
                        <table class="table table-striped table-bordered align-middle">
                            <thead class="table-light">
                                <tr>
                                    <th class="text-center">STT</th>
                                    <th class="text-center">Image</th>
                                    <th class="text-center">Product Name</th>
                                    <th class="text-center">Version</th>
                                    <th class="text-center">Color</th>
                                    <th class="text-center">Storage</th>
                                    <th class="text-center">Quantity Sold</th>
                                    <th class="text-center">Revenue (VND)</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${topProducts}" varStatus="loop">
                                    <tr>
                                        <td class="text-center">${loop.index + 1}</td>
                                        <td class="text-center">
                                            <img src="${pageContext.request.contextPath}/assets/img/${item.image}" alt="${item.productName}" width="60" height="60" class="rounded shadow-sm border" />
                                        </td>
                                        <td class="text-center">${item.productName}</td>
                                        <td class="text-center">${item.version}</td>
                                        <td class="text-center">${item.color}</td>
                                        <td class="text-center">${item.storage}</td>
                                        <td class="text-center">${item.quantity}</td>
                                        <td class="text-center">
                                            <fmt:formatNumber value="${item.subTotal}" type="number" groupingUsed="true"/>₫
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty topProducts}">
                                    <tr><td colspan="8" class="text-center">No data</td></tr>
                                </c:if>
                            </tbody>

                        </table>
                    </div>
                </c:when>
                <c:when test="${action == 'revenue'}">
                    <!-- Bảng doanh thu -->
                    <h4 class="mb-3 mt-2">Revenue summary</h4>
                    <div class="table-responsive">
                        <table class="table table-striped table-bordered align-middle">
                            <thead class="table-light">
                                <tr>
                                    <th class="text-center">FromDate</th>
                                    <th class="text-center">ToDate</th>
                                    <th class="text-center">Revenue (VND)</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td class="text-center">
                                        <fmt:formatDate value="${fromDate}" pattern="HH:mm:ss dd-MM-yyyy" />
                                    </td>
                                    <td class="text-center">
                                        <fmt:formatDate value="${toDate}" pattern="HH:mm:ss dd-MM-yyyy" />
                                    </td>
                                    <td class="text-center fw-semibold text-danger"> <strong>
                                        <fmt:formatNumber value="${total}" type="number"  groupingUsed="true"/>₫
                                        </strong>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </c:when>
            </c:choose>
        </c:if>
    </div>
</main>
<%@ include file="/WEB-INF/include/footerAdmin.jsp" %>
