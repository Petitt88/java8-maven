<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="org.pet.models.Car"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	<p>Car.jsp</p>

	<%--
	<%
	// Car car = (Car) request.getAttribute("car"); // if (car == null) {
	// car = new Car(); // }
	%>
	--%>

	<jsp:useBean id="car" scope="request" class="org.pet.models.Car">
		<%-- happens to run when the "car" object is not present in the request object and the jsp creates the instance.
			This is kinda like default parameterization.
		 --%>
		<jsp:setProperty property="name" name="car" value="-" />
		<jsp:setProperty property="plateNumber" name="car" value="-" />

		<%-- Would set the "plateNumber" from the request parameter. --%>
		<%-- 		<jsp:setProperty property="plateNumber" name="car" param="request_param_name" /> --%>

		<%-- Sets all of the properties from the request. --%>
		<%-- 		<jsp:setProperty property="*" name="car" />  --%>
	</jsp:useBean>

	<div>
		Car is:
		<%=car.getName()%></div>
	<div>
		The plate number is: (via jstl jsp:getProperty):
		<jsp:getProperty property="plateNumber" name="car" /></div>

</body>
</html>