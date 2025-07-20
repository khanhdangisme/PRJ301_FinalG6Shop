<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@include file="/WEB-INF/include/headerAdmin.jsp" %>
<title>G6Shop - Report</title>

<main class="col-md-9 ms-sm-auto col-lg-10 px-md-4" style="margin-top: 40px;">
    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-4 border-bottom">
        <h1 class="h2 fw-bold" style="letter-spacing:1px;">Revenue Report</h1>
    </div>
    <div class="card shadow-lg rounded-4 p-4 bg-light border-0">

        <!-- Lọc theo ngày -->
        <form class="row g-3 align-items-end mb-4" action="admin" method="post">
            <div class="col-auto">
                <label for="startDate" class="form-label mb-1 fw-semibold">From:</label>
                <input type="date" id="startDate" name="startDate" class="form-control rounded-3" required value="${startDate}"/>
            </div>
            <div class="col-auto">
                <label for="endDate" class="form-label mb-1 fw-semibold">To:</label>
                <input type="date" id="endDate" name="endDate" class="form-control rounded-3" required value="${endDate}"/>
            </div>
            <div class="col-auto d-flex gap-2">
                <button type="submit" class="btn btn-primary px-4 fw-semibold rounded-3 shadow-sm" name="action" value="revenue">
                    <i class="bi bi-cash-stack"></i> Revenue
                </button>
                <button class="btn btn-success px-4 fw-semibold rounded-3 shadow-sm" type="submit" name="action" value="bestselling">
                    <i class="bi bi-star-fill"></i> Best selling products
                </button>
            </div>
        </form>

        <!-- Hiển thị bảng sau khi chọn action -->
        <c:if test="${not empty action}">
            <c:choose>
                <c:when test="${action == 'bestselling'}">
                    <h4 class="mb-3 mt-2 fw-semibold" style="letter-spacing:0.5px;">Best selling products</h4>
                    <div class="table-responsive rounded-4 shadow-sm">
                        <table class="table table-striped table-hover bg-white rounded-4 align-middle">
                            <thead class="table-dark">
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
                                            <img src="${pageContext.request.contextPath}/assets/img/${item.image}" alt="${item.productName}"
                                                 width="60" height="60" class="rounded-3 shadow-sm border"
                                                 style="transition:transform 0.2s;" onmouseover="this.style.transform = 'scale(1.08)'" onmouseout="this.style.transform = 'scale(1)'" />
                                        </td>
                                        <td class="text-center fw-medium">${item.productName}</td>
                                        <td class="text-center">${item.version}</td>
                                        <td class="text-center">${item.color}</td>
                                        <td class="text-center">${item.storage}</td>
                                        <td class="text-center">${item.quantity}</td>
                                        <td class="text-center text-success fw-semibold">
                                            <fmt:formatNumber value="${item.subTotal}" type="number" groupingUsed="true"/>₫
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty topProducts}">
                                    <tr>
                                        <td colspan="8" class="text-center text-secondary" style="font-size:1.2rem;">
                                            <i class="bi bi-emoji-frown" style="font-size:2rem;"></i><br>
                                            No data 
                                        </td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:when test="${action == 'revenue'}">
                    <h4 class="mb-3 mt-2 fw-semibold">Revenue summary</h4>
                    <div class="table-responsive rounded-4 shadow-sm">
                        <table class="table table-striped table-hover bg-white rounded-4 align-middle">
                            <thead class="table-dark">
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
                                    <td class="text-center fw-bold text-danger" style="font-size:1.3rem;">
                                        <fmt:formatNumber value="${total}" type="number"  groupingUsed="true"/>₫
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

<!-- Thêm link Bootstrap Icons nếu chưa có -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">

<%@ include file="/WEB-INF/include/footerAdmin.jsp" %>