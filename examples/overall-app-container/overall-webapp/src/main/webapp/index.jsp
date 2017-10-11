<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="kp" uri="java_taghandlers" %>
<%@ taglib prefix="my_tagfiles" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Test Custom Tag - Hello</title>
</head>
<body>

<%
    String nameNonEL = "Peter";
%>
<c:set var="name" value="Peter"/>

<h2>Tag handlers</h2>

<kp:hello name="${param['name']}"/>

<kp:hello-body>Peter</kp:hello-body>

<div>Function example: ${kp:getSum(25,30) }</div>

<p>
    Hello,
    <c:out default="everyone - this is the default value"
           value="${param['name']}"/>
    !
</p>

${name}
<br/>
<c:out value="This is how you format in EL, ${pageScope.name} (pageScope.variable) or just ${name} (variable)!"/>
<!-- Won't work since nameNonEl is not on any of the page scope -->
<br/> ${nameNonEL}

<c:set var="str"
       value="${empty param.str ? 'Hello, world!' : param.str}"/>
<c:if test="${fn:contains(str, 'Hello')}">
    <c:out value="'${str}' contains 'Hello'"/>
    <br/>
</c:if>
<c:if test="${!fn:contains(str, 'Hello')}">
    <c:out value="'${str}' does NOT contain 'Hello'"/>
    <br/>
</c:if>

<h2>Tag files (they get compiled into tag-handlers - just like
    jsps to servlets)</h2>
<my_tagfiles:banana size="big" taste="delicious"></my_tagfiles:banana>
<my_tagfiles:banana size="extra-big" taste="tasty">
    <span style="color: lightgreen;">Extra banana here</span>
</my_tagfiles:banana>

</body>
</html>