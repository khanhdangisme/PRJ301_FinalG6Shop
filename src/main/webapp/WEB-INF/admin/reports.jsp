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
        <form class="row g-3 align-items-end mb-3" action="admin?action=report" method="post">
            <div class="col-auto">
                <label for="startDate" class="form-label mb-0">From:</label>
                <input type="date" id="startDate" name="startDate" class="form-control" required/>
            </div>
            <div class="col-auto">
                <label for="endDate" class="form-label mb-0">To:</label>
                <input type="date" id="endDate" name="endDate" class="form-control" required=""/>
            </div>
            <div class="col-auto">
                <button type="submit" class="btn btn-outline-primary fw-semibold">Revenue</button>
            </div>
        </form>

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
                        <td class="text-center fw-semibold">
                            <fmt:formatNumber value="${total}" type="number"  groupingUsed="true"/>₫
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</main>
<%@ include file="/WEB-INF/include/footerAdmin.jsp" %>