<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!-- Voucher Floating Icon -->
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
