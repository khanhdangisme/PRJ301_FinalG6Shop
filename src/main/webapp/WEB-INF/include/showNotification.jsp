<%-- 
    Document   : showNotification
    Created on : Jun 18, 2025, 4:26:47 PM
    Author     : KhanhDang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:if test="${not empty sessionScope.message}">
    <c:set var="type" value="${sessionScope.messageType != null ? sessionScope.messageType : 'success'}" />

    <div id="custom-toast" class="toast border-0 show"
         role="alert" aria-live="assertive" aria-atomic="true"
         style="position: fixed; top: 20px; left: 50%; transform: translateX(-50%);
         z-index: 1055; min-width: 280px; max-width: 400px;
         padding: 0.75rem 1rem; border-radius: 0.5rem;
         box-shadow: 0 0.5rem 1rem rgba(0,0,0,.15);
         background-color: ${type == 'success' ? '#d1e7dd' : '#f8d7da'};
         color: ${type == 'success' ? '#0f5132' : '#842029'};
         display: flex; align-items: center; position: fixed;">

        <!-- Nút X sát mép trái -->
        <button type="button" class="btn-close me-2" onclick="closeToast()" aria-label="Close"
                style="position: absolute; top: 6px; left: 6px; width: 0.75rem; height: 0.75rem; font-size: 0.75rem;"></button>

        <!-- Icon trạng thái -->
        <i class="fa ${type == 'success' ? 'fa-check-circle' : 'fa-exclamation-circle'} me-2"
           style="font-size: 1.1rem; margin-left: 1.5rem;"></i>

        <!-- Nội dung -->
        <div style="flex-grow: 1; font-weight: 600; font-size: 0.95rem;">${sessionScope.message}</div>
    </div>

    <script>
        setTimeout(() => {
            const toast = document.getElementById("custom-toast");
            if (toast)
                toast.style.display = "none";
        }, 2000);

        function closeToast() {
            const toast = document.getElementById("custom-toast");
            if (toast)
                toast.style.display = "none";
        }
    </script>

    <c:remove var="message" scope="session" />
    <c:remove var="messageType" scope="session" />
</c:if>
