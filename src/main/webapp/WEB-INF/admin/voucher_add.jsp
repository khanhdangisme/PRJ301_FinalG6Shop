<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Add Voucher</title>
</head>
<body>
<h2>Add New Voucher</h2>
<form action="${pageContext.request.contextPath}/voucher?action=add" method="post">
    <label>Code:</label><br>
    <input type="text" name="code" required><br><br>
    <label>Discount Percent (%):</label><br>
    <input type="number" name="discountPercent" min="1" max="100" required><br><br>
    <label>Max Discount (VNĐ):</label><br>
    <input type="number" name="maxDiscount" min="0" step="0.01" required><br><br>
    <label>Expiry Date (yyyy-mm-dd):</label><br>
    <input type="date" name="expiryDate" required><br><br>
    <label>Active:</label>
    <input type="checkbox" name="isActive" checked><br><br>
    <button type="submit">Add Voucher</button>
    <a href="${pageContext.request.contextPath}/voucher">Cancel</a>
</form>
</body>
</html> 