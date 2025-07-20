<%-- 
    Document   : voucher-modal
    Created on : Jul 20, 2025, 10:46:33 PM
    Author     : Ngo Quoc Hung - CE191184
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:forEach var="v" items="${vouchers}">
    <div class="voucher-item">
        <strong>${v.code}</strong><br>
        Discount ${v.discountPercent}% Max ${v.maxDiscount} (VND) . Expiry: 
        <fmt:formatDate value="${v.expiryDate}" pattern="dd/MM/yyyy"/>
    </div>
</c:forEach>

