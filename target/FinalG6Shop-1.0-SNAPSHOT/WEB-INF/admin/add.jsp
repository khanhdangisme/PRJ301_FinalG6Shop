<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/include/headerAdmin.jsp" %>
<title>G6Shop - Add Product</title>

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
    <form action="product?view=add" method="post" class="card shadow p-4 border-0 bg-white">
        <!-- Image & Basic Info -->
        <div class="row mb-4 align-items-center">
            <div class="col-md-4 text-center">
                <div class="mb-3">
                    <label class="form-label">Choose Product Image</label>
                    <select name="productImage" class="form-control" onchange="previewImage(this)">
                        <c:forEach var="img" items="${images}">
                            <option value="${img}">${img}</option>
                        </c:forEach>
                    </select>
                    <img id="previewImg" src="${pageContext.request.contextPath}/assets/img/${images[0]}" style="width: 200px;" class="mt-3" />
                </div>
            </div>

            <div class="col-md-8">
                <div class="mb-3">
                    <label class="form-label">Product Name</label>
                    <input type="text" name="productName" class="form-control fw-bold fs-5" required />
                </div>
                <div class="mb-3">
                    <label class="form-label">Price</label>
                    <div class="input-group">
                        <input type="number" name="price" class="form-control" min="0" step="1000" required />
                        <span class="input-group-text">₫</span>
                    </div>
                </div>
                <div class="mb-3">
                    <label class="form-label">Quantity</label>
                    <input type="number" name="quantity" class="form-control" min="0" required />
                </div>
            </div>
        </div>

        <!-- Category + Basic Specs -->
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label">Category</label>
                <select name="categoryId" class="form-control" id="categorySelect" onchange="showFields()" required>
                    <option value="1">iPhone</option>
                    <option value="2">iPad</option>
                    <option value="3">MacBook</option>
                </select>
            </div>
            <div class="col-md-6">
                <label class="form-label">Color</label>
                <input type="text" name="color" class="form-control" required />
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label">Version</label>
                <input type="text" name="version" class="form-control" required />
            </div>
            <div class="col-md-6">
                <label class="form-label">Storage</label>
                <input type="text" name="storage" class="form-control" required />
            </div>
        </div>

        <!-- iPhone/iPad Fields -->
        <div id="groupPhoneFields">
            <div class="row mb-3">
                <div class="col-md-6"><label class="form-label">Screen Size</label><input type="text" name="screenSize" class="form-control" /></div>
                <div class="col-md-6"><label class="form-label">Rear Camera</label><input type="text" name="rearCamera" class="form-control" /></div>
            </div>
            <div class="row mb-3">
                <div class="col-md-6"><label class="form-label">Front Camera</label><input type="text" name="frontCamera" class="form-control" /></div>
                <div class="col-md-6"><label class="form-label">Chipset</label><input type="text" name="chipset" class="form-control" /></div>
            </div>
            <div class="row mb-3">
                <div class="col-md-6"><label class="form-label">Battery</label><input type="text" name="battery" class="form-control" /></div>
                <div class="col-md-6"><label class="form-label">SIM Type</label><input type="text" name="simType" class="form-control" /></div>
            </div>
            <div class="row mb-3">
                <div class="col-md-6"><label class="form-label">OS</label><input type="text" name="os" class="form-control" /></div>
                <div class="col-md-6"><label class="form-label">Resolution</label><input type="text" name="resolution" class="form-control" /></div>
            </div>
            <div class="row mb-3">
                <div class="col-md-6"><label class="form-label">Screen Features</label><input type="text" name="screenFeatures" class="form-control" /></div>
                <div class="col-md-6"><label class="form-label">CPU Type</label><input type="text" name="cpuType" class="form-control" /></div>
            </div>
        </div>

        <!-- MacBook Fields -->
        <div id="groupMacFields" style="display: none;">
            <div class="row mb-3">
                <div class="col-md-6"><label class="form-label">RAM</label><input type="text" name="ram" class="form-control" /></div>
                <div class="col-md-6"><label class="form-label">GPU Type</label><input type="text" name="gpuType" class="form-control" /></div>
            </div>
            <div class="row mb-3">
                <div class="col-md-6"><label class="form-label">Screen Size</label><input type="text" name="screenSize" class="form-control" /></div>
                <div class="col-md-6"><label class="form-label">Screen Tech</label><input type="text" name="screenTech" class="form-control" /></div>
            </div>
            <div class="row mb-3">
                <div class="col-md-6"><label class="form-label">Battery</label><input type="text" name="battery" class="form-control" /></div>
                <div class="col-md-6"><label class="form-label">OS</label><input type="text" name="os" class="form-control" /></div>
            </div>
            <div class="row mb-3">
                <div class="col-md-6"><label class="form-label">Resolution</label><input type="text" name="resolution" class="form-control" /></div>
                <div class="col-md-6"><label class="form-label">CPU Type</label><input type="text" name="cpuType" class="form-control" /></div>
            </div>
            <div class="row mb-3">
                <div class="col-md-6"><label class="form-label">Ports</label><input type="text" name="ports" class="form-control" /></div>
            </div>
        </div>

        <!-- Submit -->
        <div class="text-center mt-4">
            <button type="submit" class="btn btn-success me-2">
                <i class="bi bi-plus-circle"></i> Add Product
            </button>
            <a href="product?view=list" class="btn btn-outline-secondary">
                <i class="bi bi-arrow-left-circle"></i> Cancel
            </a>
        </div>
    </form>

    <script>
        function previewImage(select) {
            const fileName = select.value;
            document.getElementById('previewImg').src = '${pageContext.request.contextPath}/assets/img/' + fileName;
        }

        function showFields() {
            const category = document.getElementById("categorySelect").value;
            document.getElementById("groupPhoneFields").style.display = (category === "1" || category === "2") ? "block" : "none";
            document.getElementById("groupMacFields").style.display = (category === "3") ? "block" : "none";
        }

        // Gọi khi load trang để set đúng field
        window.onload = showFields;
    </script>
</main>

<%@ include file="/WEB-INF/include/footerAdmin.jsp" %>
