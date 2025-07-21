<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/header.jsp" %>
<%@include file="/WEB-INF/include/showNotification.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<title>G6Shop - Home</title>

<div style="margin-top: 90px; margin-bottom: 20px">
    <!-- Start Banner Hero -->
    <div id="template-mo-zay-hero-carousel" class="carousel slide" data-bs-ride="carousel">
        <ol class="carousel-indicators">
            <li data-bs-target="#template-mo-zay-hero-carousel" data-bs-slide-to="0" class="active"></li>
            <li data-bs-target="#template-mo-zay-hero-carousel" data-bs-slide-to="1"></li>
            <li data-bs-target="#template-mo-zay-hero-carousel" data-bs-slide-to="2"></li>
        </ol>
        <div class="carousel-inner">
            <div class="carousel-item active">
                <div class="container">
                    <div class="row p-5">
                        <div class="mx-auto col-md-8 col-lg-6 order-lg-last">
                            <img class="img-fluid" src="./assets/img/iphone_banner.webp" alt="">
                        </div>
                        <div class="col-lg-6 mb-0 d-flex align-items-center">
                            <div class="text-align-left align-self-center">
                                <h1 class="h1 text-dark"><b>iPhone 16 Pro Max</b></h1>
                                <h3>Premium performance at just 36 million VND</h3>
                                <h3 class="h2">Power, Precision, and Style. Built for your digital lifestyle</h3>

                                <a href="shop?view=search&query=IPhone+16+Pro+Max" class="btn btn-dark btn-lg mt-3">Shop Now</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="carousel-item">
                <div class="container">
                    <div class="row p-5">
                        <div class="mx-auto col-md-8 col-lg-6 order-lg-last">
                            <img class="img-fluid" src="./assets/img/ipad_banner.png" alt="">
                        </div>
                        <div class="col-lg-6 mb-0 d-flex align-items-center">
                            <div class="text-align-left">
                                <h1 class="h1 text-dark"><b>iPad Pro M4</b></h1>
                                <h3>Premium performance at just 32 million VND</h3>
                                <h3 class="h2">The ultimate creative powerhouse, now within your reach</h3>
                                <a href="shop?view=search&query=Ipad+Pro" class="btn btn-dark btn-lg mt-3">Shop Now</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="carousel-item">
                <div class="container">
                    <div class="row p-5">
                        <div class="mx-auto col-md-8 col-lg-6 order-lg-last">
                            <img class="img-fluid" src="./assets/img/macbook_banner.png" alt="">
                        </div>
                        <div class="col-lg-6 mb-0 d-flex align-items-center">
                            <div class="text-align-left">
                                <h1 class="h1 text-dark"><b>Macbook Pro M4</b></h1>
                                <h3>Premium performance at just 90 million VND</h3>
                                <h3 class="h2">Built for creators. Powered for professionals </h3>
                                <a href="shop?view=search&query=Macbook+Pro" class="btn btn-dark btn-lg mt-3">Shop Now</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <a class="carousel-control-prev text-decoration-none w-auto ps-3" href="#template-mo-zay-hero-carousel" role="button" data-bs-slide="prev">
            <i class="fas fa-chevron-left"  style="color: silver !important;"></i>
        </a>
        <a class="carousel-control-next text-decoration-none w-auto pe-3" href="#template-mo-zay-hero-carousel" role="button" data-bs-slide="next">
            <i class="fas fa-chevron-right" style="color: silver !important;"></i>
        </a>
    </div>
</div> 
<!-- End Banner Hero -->


<!-- Start Categories of The Month -->
<section class="container py-5">
    <div class="row text-center pt-3">
        <div class="col-lg-6 m-auto">
            <h1 class="h1">Categories</h1>
        </div>
    </div>
    <div class="row">
        <div class="col-12 col-md-4 p-5 mt-3">
            <a href="#"><img src="./assets/img/iPhone.webp" class="rounded-circle img-fluid border"></a>
            <h5 class="text-center mt-3 mb-3">iPhone</h5>
            <p class="text-center"><a class="btn btn-dark" href="shop?view=search&query=iPhone">Go Shop</a></p>
        </div>
        <div class="col-12 col-md-4 p-5 mt-3">
            <a href="#"><img src="./assets/img/iPad.webp" class="rounded-circle img-fluid border"></a>
            <h2 class="h5 text-center mt-3 mb-3">iPad</h2>
            <p class="text-center"><a class="btn btn-dark" href="shop?view=search&query=Ipad">Go Shop</a></p>
        </div>
        <div class="col-12 col-md-4 p-5 mt-3">
            <a href="#"><img src="./assets/img/macbook.webp" class="rounded-circle img-fluid border"></a>
            <h2 class="h5 text-center mt-3 mb-3">Macbook</h2>
            <p class="text-center"><a class="btn btn-dark" href="shop?view=search&query=Macbook">Go Shop</a></p>
        </div>
    </div>
</section>
<!-- End Categories of The Month -->

<a href="javascript:void(0);" class="voucher-icon" onclick="openVoucherModal()">
    <img src="${pageContext.request.contextPath}/assets/img/voucher-icon.png" alt="Voucher Icon">
</a>

<!-- Voucher Modal -->
<div id="voucherModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeVoucherModal()">&times;</span>
        <h2>Available Vouchers</h2>
        <div class="voucher-list" id="voucherModalBody">
            <!-- Nội dung voucher sẽ được load bằng AJAX -->
        </div>
    </div>
</div>

<!-- Styles (giữ nguyên như ông đã viết) -->
<style>
    .voucher-icon {
        position: fixed;
        bottom: 20px;
        right: 20px;
        z-index: 99999;
        background-color: white;
        border: 1px solid #ddd;
        border-radius: 50%;
        padding: 10px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
        text-align: center;
        text-decoration: none;
        transition: 0.3s;
    }

    .voucher-icon img {
        width: 50px;
        height: 50px;
        display: block;
        margin: 0 auto;
        pointer-events: none;
    }

    .voucher-icon:hover {
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.25);
        transform: scale(1.1);
    }

    .modal {
        display: none;
        position: fixed;
        z-index: 100000;
        left: 0;
        top: 0;
        width: 100%;
        height: 100%;
        overflow: auto;
        background-color: rgba(0, 0, 0, 0.4);
    }

    .modal-content {
        background-color: #fff;
        margin: 10% auto;
        padding: 20px;
        border-radius: 10px;
        width: 80%;
        max-width: 500px;
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
        position: relative;
    }

    .close {
        color: #aaa;
        float: right;
        font-size: 24px;
        font-weight: bold;
        cursor: pointer;
        position: absolute;
        right: 15px;
        top: 10px;
    }

    .close:hover {
        color: #333;
    }

    .voucher-list {
        margin-top: 10px;
        max-height: 300px;
        overflow-y: auto;
        text-align: left;
    }

    .voucher-item {
        border: 1px solid #ddd;
        padding: 10px;
        margin-bottom: 10px;
        border-radius: 6px;
        background-color: #f9f9f9;
    }
</style>

<!-- Script -->
<script>
    function openVoucherModal() {
        const modal = document.getElementById("voucherModal");
        const body = document.getElementById("voucherModalBody");

        // Gọi servlet để lấy nội dung voucher
        fetch("${pageContext.request.contextPath}/voucher-list")
            .then(response => {
                if (!response.ok) {
                    throw new Error("Network error");
                }
                return response.text();
            })
            .then(data => {
                body.innerHTML = data;
                modal.style.display = "block";
            })
            .catch(error => {
                body.innerHTML = "<p style='color:red;'>Không thể tải danh sách voucher.</p>";
                modal.style.display = "block";
                console.error(error);
            });
    }

    function closeVoucherModal() {
        document.getElementById("voucherModal").style.display = "none";
    }

    window.onclick = function (event) {
        const modal = document.getElementById("voucherModal");
        if (event.target === modal) {
            closeVoucherModal();
        }
    }
</script>

<!-- Script -->
<script>
    function openVoucherModal() {
        const modal = document.getElementById("voucherModal");
        const body = document.getElementById("voucherModalBody");

        // Gọi servlet để lấy nội dung voucher
        fetch("${pageContext.request.contextPath}/voucher-list")
            .then(response => {
                if (!response.ok) {
                    throw new Error("Network error");
                }
                return response.text();
            })
            .then(data => {
                body.innerHTML = data;
                modal.style.display = "block";
            })
            .catch(error => {
                body.innerHTML = "<p style='color:red;'>Không thể tải danh sách voucher.</p>";
                modal.style.display = "block";
                console.error(error);
            });
    }

    function closeVoucherModal() {
        document.getElementById("voucherModal").style.display = "none";
    }

    window.onclick = function (event) {
        const modal = document.getElementById("voucherModal");
        if (event.target === modal) {
            closeVoucherModal();
        }
    }
</script>


<%@include file="/WEB-INF/include/footer.jsp" %>