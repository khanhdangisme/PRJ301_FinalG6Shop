<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@include file="/WEB-INF/include/headerAdmin.jsp" %>
<title>G6Shop - Product</title>

<style>
    .form-label {
        font-weight: 600;
        margin-bottom: 0.3rem;
        text-align: left;
        display: block;
    }

    .form-control {
        border-radius: 0.6rem;
        padding: 0.6rem 0.75rem;
        font-size: 1rem;
        text-align: left;
    }

    .form-control:read-only {
        background-color: #f9f9f9;
        color: #555;
    }

    .card {
        border-radius: 1rem;
    }

    .btn {
        border-radius: 0.5rem;
        padding: 0.5rem 1.2rem;
    }

    .btn i {
        margin-right: 0.4rem;
    }

    .top-section img {
        max-height: 320px;
        max-width: 100%;
        object-fit: contain;
        border-radius: 1rem;
    }
</style>

<main class="ms-auto pe-4" style="margin-top: 120px; max-width: calc(100% - 270px);">
    <form action="product?action=update&id=${getDetail.productID}&detailID=${getDetail.de}" method="post"
          class="card shadow p-4 border-0 bg-white">

        <!-- Top section -->
        <div class="row mb-4 align-items-center">
            <div class="col-md-4 text-center">
                <img src="assets/img/${getDetail.productImage}" alt="Product Image" style="width: 200px" class="img-fluid rounded shadow-sm">
            </div>
            <div class="col-md-8">
                <div class="mb-3">
                    <label class="form-label">Product Name</label>
                    <input type="text" name="productName" class="form-control fw-bold fs-5" value="${getDetail.productName}" />
                </div>
                <c:choose>
                    <c:when test='${getDetail.categoryName eq "iPhone"}'>
                        <c:set var="rawPrice" value="${getDetail.iPhonePrice}" />
                    </c:when>
                    <c:when test='${getDetail.categoryName eq "iPad"}'>
                        <c:set var="rawPrice" value="${getDetail.iPadPrice}" />
                    </c:when>
                    <c:when test='${getDetail.categoryName eq "MacBook"}'>
                        <c:set var="rawPrice" value="${getDetail.macPrice}" />
                    </c:when>
                </c:choose>

                <fmt:formatNumber value="${rawPrice}" type="number" var="price" maxFractionDigits="0" />
                <div class="mb-3">
                    <label class="form-label">Price</label>
                    <div class="input-group">
                        <input type="text" name="price" class="form-control text-start" value="${price}" />
                        <span class="input-group-text">₫</span>
                    </div>
                </div>
            </div>
        </div>

        <c:choose>
            <c:when test='${getDetail.categoryName eq "iPhone"}'>
                <c:set var="color" value="${getDetail.iPhoneColor}" />
                <c:set var="version" value="${getDetail.iPhoneVersion}" />
                <c:set var="storage" value="${getDetail.iPhoneStorage}" />
            </c:when>
            <c:when test='${getDetail.categoryName eq "iPad"}'>
                <c:set var="color" value="${getDetail.iPadColor}" />
                <c:set var="version" value="${getDetail.iPadVersion}" />
                <c:set var="storage" value="${getDetail.iPadStorage}" />
            </c:when>
            <c:when test='${getDetail.categoryName eq "MacBook"}'>
                <c:set var="color" value="${getDetail.macColor}" />
                <c:set var="version" value="${getDetail.macVersion}" />
                <c:set var="storage" value="${getDetail.macStorage}" />
            </c:when>
        </c:choose>


        <!-- Common Fields -->
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label">Category</label>
                <input type="text" name="categoryName" class="form-control" value="${getDetail.categoryName}" readonly />
            </div>
            <div class="col-md-6">
                <label class="form-label">Color</label>
                <input type="text" name="color" class="form-control text-start" value="${color}" />
            </div>
        </div>

        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label">Version</label>
                <input type="text" name="version" class="form-control text-start" value="${version}" />
            </div>
            <div class="col-md-6">
                <label class="form-label">Storage</label>
                <input type="text" name="storage" class="form-control text-start" value="${storage}" />
            </div>
        </div>

        <!-- Dynamic Detail Section -->
        <c:choose>
            <c:when test='${getDetail.categoryName eq "iPhone"}'>
                <div class="row mb-3">
                    <div class="col-md-6"><label class="form-label">Screen Size</label><input type="text" name="screenSize" class="form-control" value="${getDetail.iPhoneScreenSize}" /></div>
                    <div class="col-md-6"><label class="form-label">Rear Camera</label><input type="text" name="rearCamera" class="form-control" value="${getDetail.iPhoneRearCamera}" /></div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-6"><label class="form-label">Front Camera</label><input type="text" name="frontCamera" class="form-control" value="${getDetail.iPhoneFrontCamera}" /></div>
                    <div class="col-md-6"><label class="form-label">Chipset</label><input type="text" name="chipset" class="form-control" value="${getDetail.iPhoneChipset}" /></div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-6"><label class="form-label">Battery</label><input type="text" name="battery" class="form-control" value="${getDetail.iPhoneBattery}" /></div>
                    <div class="col-md-6"><label class="form-label">SIM Type</label><input type="text" name="simType" class="form-control" value="${getDetail.iPhoneSimType}" /></div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-6"><label class="form-label">OS</label><input type="text" name="os" class="form-control" value="${getDetail.iPhoneOs}" /></div>
                    <div class="col-md-6"><label class="form-label">Resolution</label><input type="text" name="resolution" class="form-control" value="${getDetail.iPhoneResolution}" /></div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-6"><label class="form-label">Screen Features</label><input type="text" name="screenFeatures" class="form-control" value="${getDetail.iPhoneScreenFeatures}" /></div>
                    <div class="col-md-6"><label class="form-label">CPU Type</label><input type="text" name="cpuType" class="form-control" value="${getDetail.iPhoneCpuType}" /></div>
                </div>
            </c:when>

            <c:when test='${getDetail.categoryName eq "iPad"}'>
                <div class="row mb-3">
                    <div class="col-md-6"><label class="form-label">Screen Size</label><input type="text" name="screenSize" class="form-control" value="${getDetail.iPadScreenSize}" /></div>
                    <div class="col-md-6"><label class="form-label">Rear Camera</label><input type="text" name="rearCamera" class="form-control" value="${getDetail.iPadRearCamera}" /></div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-6"><label class="form-label">Front Camera</label><input type="text" name="frontCamera" class="form-control" value="${getDetail.iPadFrontCamera}" /></div>
                    <div class="col-md-6"><label class="form-label">Chipset</label><input type="text" name="chipset" class="form-control" value="${getDetail.iPadChipset}" /></div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-6"><label class="form-label">Battery</label><input type="text" name="battery" class="form-control" value="${getDetail.iPadBattery}" /></div>
                    <div class="col-md-6"><label class="form-label">SIM Type</label><input type="text" name="simType" class="form-control" value="${getDetail.iPadSimType}" /></div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-6"><label class="form-label">OS</label><input type="text" name="os" class="form-control" value="${getDetail.iPadOs}" /></div>
                    <div class="col-md-6"><label class="form-label">Resolution</label><input type="text" name="resolution" class="form-control" value="${getDetail.iPadResolution}" /></div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-6"><label class="form-label">Screen Features</label><input type="text" name="screenFeatures" class="form-control" value="${getDetail.iPadScreenFeatures}" /></div>
                    <div class="col-md-6"><label class="form-label">CPU Type</label><input type="text" name="cpuType" class="form-control" value="${getDetail.iPadCpuType}" /></div>
                </div>
            </c:when>

            <c:when test='${getDetail.categoryName eq "MacBook"}'>
                <div class="row mb-3">
                    <div class="col-md-6"><label class="form-label">RAM</label><input type="text" name="ram" class="form-control" value="${getDetail.macRam}" /></div>
                    <div class="col-md-6"><label class="form-label">GPU Type</label><input type="text" name="gpuType" class="form-control" value="${getDetail.macGpuType}" /></div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-6"><label class="form-label">Screen Size</label><input type="text" name="screenSize" class="form-control" value="${getDetail.macSreenSize}" /></div>
                    <div class="col-md-6"><label class="form-label">Screen Tech</label><input type="text" name="screenTech" class="form-control" value="${getDetail.maccreenTech}" /></div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-6"><label class="form-label">Battery</label><input type="text" name="battery" class="form-control" value="${getDetail.macBattery}" /></div>
                    <div class="col-md-6"><label class="form-label">OS</label><input type="text" name="os" class="form-control" value="${getDetail.macOs}" /></div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-6"><label class="form-label">Resolution</label><input type="text" name="resolution" class="form-control" value="${getDetail.macResolution}" /></div>
                    <div class="col-md-6"><label class="form-label">CPU Type</label><input type="text" name="cpuType" class="form-control" value="${getDetail.macCpuType}" /></div>
                </div>
                <div class="row mb-3">
                    <div class="col-md-6"><label class="form-label">Ports</label><input type="text" name="ports" class="form-control" value="${getDetail.macPorts}" /></div>
                </div>
            </c:when>
        </c:choose>

        <!-- Buttons -->
        <div class="text-center mt-4">
            <button type="submit" style="padding-left: 10px;" class="btn btn-primary me-2">
                <i class="bi bi-save"></i> Save Changes
            </button>
            <a href="javascript:history.back()" style="padding-left: 13px;" class="btn btn-outline-secondary me-2">
                <i class="bi bi-arrow-left-circle"></i> Cancel
            </a>
            <a href="ProductServlet?view=delete&id=${getDetail.productID}" style="padding-left: 15px;" class="btn btn-danger"
               onclick="return confirm('Are you sure you want to delete this product?');">
                <i class="bi bi-trash"></i> Delete
            </a>
        </div>
    </form>
</main>

<%@ include file="/WEB-INF/include/footerAdmin.jsp" %>
