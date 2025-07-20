<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/headerAdmin.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<title>G6Shop - Vouchers</title>
<main class="col-md-9 ms-sm-auto col-lg-10 px-md-4" style="margin-top: 110px;">
    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Vouchers</h1>
        <button type="button" class="btn btn-sm btn-outline-primary" data-bs-toggle="modal" data-bs-target="#addVoucherModal">
            <i class="fa fa-plus"></i> Add Voucher
        </button>
    </div>
    <div class="table-responsive">
        <table class="table table-striped table-bordered table-sm">
            <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Code</th>
                    <th scope="col">Discount (%)</th>
                    <th scope="col">Max Discount (VNĐ)</th>
                    <th scope="col">Expiry Date</th>
                    <th scope="col">Status</th>
                    <th scope="col">Action</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty vouchers}">
                        <tr>
                            <td colspan="7" class="text-center">No voucher found!</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="v" items="${vouchers}" varStatus="loop">
                            <tr>
                                <td>${loop.index + 1}</td>
                                <td>${v.code}</td>
                                <td>${v.discountPercent}</td>
                                <td><fmt:formatNumber value="${v.maxDiscount}" type="number" maxFractionDigits="0"/> ₫</td>
                                <td><fmt:formatDate value="${v.expiryDate}" pattern="dd/MM/yyyy" /></td>
                                <td>
                                    <span class="badge rounded-pill ${v.active ? 'bg-success' : 'bg-secondary'}" style="min-width: 80px; font-size: 0.85rem; padding: 6px 12px;">${v.active ? 'Active' : 'Inactive'}</span>
                                </td>
                                <td>
                                    <button type="button" class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#editVoucherModal${v.id}">Edit</button>
                                    <form action="${pageContext.request.contextPath}/voucher" method="post" style="display:inline;" onsubmit="return confirm('Are you sure you want to delete this voucher?');">
                                        <input type="hidden" name="action" value="delete" />
                                        <input type="hidden" name="id" value="${v.id}" />
                                        <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
    <!-- Add Modal -->
    <div class="modal fade" id="addVoucherModal" tabindex="-1" aria-labelledby="addVoucherModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <form action="${pageContext.request.contextPath}/voucher?action=add" method="post">
                    <div class="modal-header bg-primary text-white py-2 px-3 align-items-center">
                        <h5 class="modal-title mb-0" id="addVoucherModalLabel" style="font-size: 1.2rem;">Add Voucher</h5>
                        <button type="button" class="btn-close btn-close-white ms-auto" data-bs-dismiss="modal" aria-label="Close" style="margin-left: 0;"></button>
                    </div>
                    <div class="modal-body pt-3 pb-2 px-3">
                        <div class="mb-2">
                            <label>Code:</label>
                            <input type="text" class="form-control" name="code" required />
                        </div>
                        <div class="mb-2">
                            <label>Discount Percent (%):</label>
                            <input type="number" class="form-control" name="discountPercent" min="1" max="100" required />
                        </div>
                        <div class="mb-2">
                            <label>Max Discount (VNĐ):</label>
                            <input type="number" class="form-control" name="maxDiscount" min="0" step="0.01" required />
                        </div>
                        <div class="mb-2">
                            <label>Expiry Date (yyyy-mm-dd):</label>
                            <input type="date" class="form-control" name="expiryDate" required />
                        </div>
                        <div class="mb-2">
                            <label>Active:</label>
                            <input type="checkbox" name="isActive" checked />
                        </div>
                    </div>
                    <div class="modal-footer py-2 px-3">
                        <button type="submit" class="btn btn-primary">Add</button>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!-- Các modal Edit voucher đặt ở cuối file, ngoài table -->
    <c:forEach var="v" items="${vouchers}">
        <div class="modal fade" id="editVoucherModal${v.id}" tabindex="-1" aria-labelledby="editVoucherModalLabel${v.id}" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="${pageContext.request.contextPath}/voucher?action=edit" method="post">
                        <div class="modal-header bg-primary text-white py-2 px-3 align-items-center">
                            <h5 class="modal-title mb-0" id="editVoucherModalLabel${v.id}" style="font-size: 1.2rem;">Edit Voucher</h5>
                            <button type="button" class="btn-close btn-close-white ms-auto" data-bs-dismiss="modal" aria-label="Close" style="margin-left: 0;"></button>
                        </div>
                        <div class="modal-body pt-3 pb-2 px-3">
                            <input type="hidden" name="id" value="${v.id}" />
                            <div class="mb-2">
                                <label>Code:</label>
                                <input type="text" class="form-control" name="code" value="${v.code}" required />
                            </div>
                            <div class="mb-2">
                                <label>Discount Percent (%):</label>
                                <input type="number" class="form-control" name="discountPercent" min="1" max="100" value="${v.discountPercent}" required />
                            </div>
                            <div class="mb-2">
                                <label>Max Discount (VNĐ):</label>
                                <input type="number" class="form-control" name="maxDiscount" min="0" step="0.01" value="<fmt:formatNumber value='${v.maxDiscount}' type='number' maxFractionDigits='2' groupingUsed='false'/>" required />
                            </div>
                            <div class="mb-2">
                                <label>Expiry Date (yyyy-mm-dd):</label>
                                <input type="date" class="form-control" name="expiryDate" value="<fmt:formatDate value='${v.expiryDate}' pattern='yyyy-MM-dd'/>" required />
                            </div>
                            <div class="mb-2">
                                <label>Active:</label>
                                <input type="checkbox" name="isActive" ${v.active ? 'checked' : ''} />
                            </div>
                        </div>
                        <div class="modal-footer py-2 px-3">
                            <button type="submit" class="btn btn-primary">Update</button>
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </c:forEach>
</main>
<%@include file="/WEB-INF/include/footerAdmin.jsp" %>
