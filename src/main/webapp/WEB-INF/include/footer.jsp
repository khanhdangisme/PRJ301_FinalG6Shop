<%-- 
    Document   : footer
    Created on : Jun 16, 2025, 1:13:41 PM
    Author     : KhanhDang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!-- Start Footer -->
<footer class="bg-dark" id="tempaltemo_footer">
    <div class="container">
        <div class="row">

            <div class="col-md-4 pt-5">
                <h2 class="h2 text-success border-bottom pb-3 border-light logo">Zay Shop</h2>
                <ul class="list-unstyled text-light footer-link-list">
                    <li>
                        <i class="fas fa-map-marker-alt fa-fw"></i>
                        123 Consectetur at ligula 10660
                    </li>
                    <li>
                        <i class="fa fa-phone fa-fw"></i>
                        <a class="text-decoration-none" href="tel:010-020-0340">010-020-0340</a>
                    </li>
                    <li>
                        <i class="fa fa-envelope fa-fw"></i>
                        <a class="text-decoration-none" href="mailto:info@company.com">info@company.com</a>
                    </li>
                </ul>
            </div>

            <div class="col-md-4 pt-5">
                <h2 class="h2 text-light border-bottom pb-3 border-light">Products</h2>
                <ul class="list-unstyled text-light footer-link-list">
                    <li><a class="text-decoration-none" href="#">Luxury</a></li>
                    <li><a class="text-decoration-none" href="#">Sport Wear</a></li>
                    <li><a class="text-decoration-none" href="#">Men's Shoes</a></li>
                    <li><a class="text-decoration-none" href="#">Women's Shoes</a></li>
                    <li><a class="text-decoration-none" href="#">Popular Dress</a></li>
                    <li><a class="text-decoration-none" href="#">Gym Accessories</a></li>
                    <li><a class="text-decoration-none" href="#">Sport Shoes</a></li>
                </ul>
            </div>

            <div class="col-md-4 pt-5">
                <h2 class="h2 text-light border-bottom pb-3 border-light">Further Info</h2>
                <ul class="list-unstyled text-light footer-link-list">
                    <li><a class="text-decoration-none" href="#">Home</a></li>
                    <li><a class="text-decoration-none" href="#">About Us</a></li>
                    <li><a class="text-decoration-none" href="#">Shop Locations</a></li>
                    <li><a class="text-decoration-none" href="#">FAQs</a></li>
                    <li><a class="text-decoration-none" href="#">Contact</a></li>
                </ul>
            </div>

        </div>

        <div class="row text-light mt-4 align-items-center">
            <div class="col-md-6 mb-3 text-center text-md-start">
                <ul class="list-inline footer-icons mb-0">
                    <li class="list-inline-item border border-light rounded-circle text-center me-2">
                        <a class="text-light text-decoration-none" target="_blank" href="http://facebook.com/">
                            <i class="fab fa-facebook-f fa-lg fa-fw"></i>
                        </a>
                    </li>
                    <li class="list-inline-item border border-light rounded-circle text-center me-2">
                        <a class="text-light text-decoration-none" target="_blank" href="https://www.instagram.com/">
                            <i class="fab fa-instagram fa-lg fa-fw"></i>
                        </a>
                    </li>
                    <li class="list-inline-item border border-light rounded-circle text-center me-2">
                        <a class="text-light text-decoration-none" target="_blank" href="https://twitter.com/">
                            <i class="fab fa-twitter fa-lg fa-fw"></i>
                        </a>
                    </li>
                    <li class="list-inline-item border border-light rounded-circle text-center">
                        <a class="text-light text-decoration-none" target="_blank" href="https://www.linkedin.com/">
                            <i class="fab fa-linkedin fa-lg fa-fw"></i>
                        </a>
                    </li>
                </ul>
            </div>

            <div class="col-md-6 text-center text-md-end">
                <form class="d-inline-flex w-100 justify-content-center justify-content-md-end">
                    <input type="email" class="form-control bg-dark border-light text-light me-2" placeholder="Email address" style="max-width: 250px;">
                    <button type="submit" class="btn btn-success">Subscribe</button>
                </form>
            </div>
        </div>
    </div>
</footer>
<!-- End Footer -->

<!-- Start Script -->
<script src="assets/js/jquery-1.11.0.min.js"></script>
<script src="assets/js/jquery-migrate-1.2.1.min.js"></script>
<script src="assets/js/bootstrap.bundle.min.js"></script>
<script src="assets/js/templatemo.js"></script>
<script src="assets/js/custom.js"></script>
<!-- End Script -->

<!-- Search Modal -->
<div class="modal fade" id="templatemo_search" tabindex="-1" aria-labelledby="searchModalLabel" aria-hidden="true">
    <div class="modal-dialog mt-3">
        <div class="modal-content border-0 shadow">
            <div class="modal-header">
                <h5 class="modal-title" id="searchModalLabel">Search</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form action="search" method="get">
                    <div class="input-group">
                        <input type="text" name="query" class="form-control" placeholder="Type to search..." required>
                        <button class="btn btn-success" type="submit">
                            <i class="fa fa-search"></i>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Cart Modal -->
<div class="modal fade" id="cartModal" tabindex="-1" aria-labelledby="cartModalLabel" aria-hidden="true">
    <div class="modal-dialog mt-3">
        <div class="modal-content border-0 shadow">
            <div class="modal-header">
                <h5 class="modal-title" id="cartModalLabel">Your Cart</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <!-- Nội dung giỏ hàng tại đây -->
                <p>Your cart is currently empty.</p>
                <!-- Có thể thay bằng table sản phẩm -->
            </div>
            <div class="modal-footer">
                <a href="cart.jsp" class="btn btn-success">Go to Cart</a>
            </div>
        </div>
    </div>
</div>
</body>

</html>

