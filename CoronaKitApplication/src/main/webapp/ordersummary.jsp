<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>  
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Corona Kit-Order Summary(user)</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<p></p>
<h4>Here is your order summary!</h4>
<p></p>
<button type="button" name="back" onclick="history.go(-2);">Change Order</button>
<hr/>
<h5>Hello ${userset.get(0) }</h5>
<p>Total price of your order is:<strong> ${userset.get(4) } </strong></p>
<p>Invoice will be sent to your email-ID:<strong> ${userset.get(1) } </strong></p>
<p>Shipment tracking will be updated to you on contact:<strong> ${userset.get(2) } </strong></p>
<p>Products will be delivered at your address:<strong> ${userset.get(3) } </strong></p>
<hr/>	
<form action="saveorder?action=saveorder" method = "POST">
	<button name="saveorder" value="saveorder">Place Order >> </button>
</form>
	<jsp:include page="footer.jsp"/>
</body>
</html>