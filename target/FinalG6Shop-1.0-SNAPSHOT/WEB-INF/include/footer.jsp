<%-- 
    Document   : footer
    Created on : Jun 16, 2025, 1:13:41 PM
    Author     : KhanhDang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!-- Start Footer -->
<footer class="bg-dark" id="tempaltemo_footer" style="padding-bottom: 40px">
    <div class="container">
        <div class="row">

            <div class="col-md-4 pt-5">
                <h2 class="h2 text-white border-bottom pb-3 border-light logo">G6 Shop</h2>
                <ul class="list-unstyled text-light footer-link-list">
                    <li>
                        <i class="fas fa-map-marker-alt fa-fw"></i>
                        Ninh Kieu - Can Tho - Viet Nam
                    </li>
                    <li>
                        <i class="fa fa-phone fa-fw"></i> 
                        093-934-6993
                    </li>
                    <li>
                        <i class="fa fa-envelope fa-fw"></i>
                        emailcuadang1705@gmail.com
                    </li>
                </ul>
            </div>

            <div class="col-md-4 pt-5">
                <h2 class="h2 text-light border-bottom pb-3 border-light">Products</h2>
                <ul class="list-unstyled text-light footer-link-list">
                    <li><a class="text-decoration-none" href="shop?view=search&query=iPhone">iPhone</a></li>
                    <li><a class="text-decoration-none" href="shop?view=search&query=iPad">iPad</a></li>
                    <li><a class="text-decoration-none" href="shop?view=search&query=Macbook">Macbook</a></li>
                </ul>
            </div>

            <div class="col-md-4 pt-5">
                <h2 class="h2 text-light border-bottom pb-3 border-light">Further Info</h2>
                <ul class="list-unstyled text-light footer-link-list">
                    <li><a class="text-decoration-none" href="index.jsp">Home</a></li>
                    <li><a class="text-decoration-none" href="shop">Shop</a></li>
                </ul>
            </div>
        </div>
    </div>
</footer>
<!-- End Footer -->

<!-- Start Script -->
<script src="${pageContext.request.contextPath}/assets/js/jquery-1.11.0.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/jquery-migrate-1.2.1.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
<!--<script src="${pageContext.request.contextPath}/assets/js/templatemo.js"></script>-->
<script src="${pageContext.request.contextPath}/assets/js/custom.js"></script>

<!-- End Script -->




<%@include file="/WEB-INF/include/showPopupUser.jsp" %>

<!-- nhớ trạng thái accordion bằng localStorage -->
</body>
</html>

