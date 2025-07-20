<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Voucher" %>
<%
    Voucher v = (Voucher) request.getAttribute("voucher");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Voucher</title>
</head>
<body>
<h2>Edit Voucher</h2>
<form action="${pageContext.request.contextPath}/voucher?action=edit" method="post">
    <input type="hidden" name="id" value="<%=v.getId()%>" />
    <label>Code:</label><br>
    <input type="text" name="code" value="<%=v.getCode()%>" required><br><br>
    <label>Discount Percent (%):</label><br>
    <input type="number" name="discountPercent" min="1" max="100" value="<%=v.getDiscountPercent()%>" required><br><br>
    <label>Max Discount (VNĐ):</label><br>
    <input type="number" name="maxDiscount" min="0" step="0.01" value="<%=v.getMaxDiscount()%>" required><br><br>
    <label>Expiry Date (yyyy-mm-dd):</label><br>
    <input type="date" name="expiryDate" value="<%=new java.text.SimpleDateFormat("yyyy-MM-dd").format(v.getExpiryDate())%>" required><br><br>
    <label>Active:</label>
    <input type="checkbox" name="isActive" <%=v.isActive()?"checked":""%>><br><br>
    <button type="submit">Update Voucher</button>
    <a href="${pageContext.request.contextPath}/voucher">Cancel</a>
</form>
</body>
</html> 