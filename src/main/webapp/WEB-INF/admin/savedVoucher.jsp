<%@ page import="java.util.*, model.Voucher" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Saved Vouchers</title>
        <style>
            body {
                font-family: Arial;
            }
            .voucher-container {
                max-width: 900px;
                margin: auto;
                padding: 20px;
                display: flex;
                flex-wrap: wrap;
                gap: 20px;
            }
            .voucher-card {
                border: 1px solid #ccc;
                border-radius: 8px;
                padding: 15px;
                width: calc(50% - 20px);
                background: #fefefe;
                box-shadow: 2px 2px 8px rgba(0,0,0,0.1);
            }
            .voucher-card p {
                margin: 6px 0;
            }
            .back-link {
                text-align: center;
                margin-top: 20px;
            }
            .back-link a {
                color: blue;
                text-decoration: none;
            }
        </style>
    </head>
    <body>

        <h2 style="text-align:center;">📌 Saved Voucher</h2>

        <c:choose>
            <c:when test="${empty savedVouchers}">
                <p style="text-align:center;">You have not saved any codes.</p>
            </c:when>
            <c:otherwise>
                <div class="voucher-container">
                    <c:forEach var="v" items="${savedVouchers}">
                        <div class="voucher-card">
                            <p><strong>Code:</strong> ${v.code}</p>
                            <p><strong>Discount:</strong> ${v.discountPercent}%</p>
                            <p><strong>Max:</strong> ${v.maxDiscount}đ</p>
                            <p><strong>Expiry:</strong>
                                <fmt:formatDate value="${v.expiryDate}" pattern="dd/MM/yyyy HH:mm"/>
                            </p>
                            <p><strong>Status</strong>
                                <c:choose>
                                    <c:when test="${v.active}">
                                        <span style="color:green;">valid</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color:red;">expired</span>
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
        <div class="back-link">
            <a href="${pageContext.request.contextPath}/voucher">← Back to voucher list.</a
        </div>
    </body>
</html>
