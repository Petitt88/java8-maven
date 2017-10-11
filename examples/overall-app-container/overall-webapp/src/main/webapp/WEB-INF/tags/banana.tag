<%@ tag language="java" pageEncoding="ISO-8859-1" %>
<%@ attribute name="size" required="true" %>
<%@ attribute name="taste" required="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="kp" uri="java_taghandlers" %>

<!-- Tag files go into /WEB-INF/tags/ folder -->

<div>

    <span>Banana size is: ${size}!</span>

    <c:if test="${empty taste}">
        <div>Taste is... unfortunately empty.</div>
    </c:if>
    <c:if test="${not empty taste}">
        <div>Taste is: ${taste}</div>
    </c:if>


    <kp:hello-body>Invoking kp:hello-body tag handler from banana.tag</kp:hello-body>


    <!--
	To get body of tag we need to use:
	Here tagBody is a variable jsp:doBody get tag body and store it in tagBody variable.
	And we can use this tagBody variable in our tag file like this : ${pageScope.tagBody} 
-->
    <jsp:doBody var="tagBody"/>

    <c:choose>
        <c:when test="${not empty pageScope.tagBody}">
            <strong> Body is: ${pageScope.tagBody} </strong>
        </c:when>
        <c:otherwise>
            <i>Body is not set.</i>
        </c:otherwise>
    </c:choose>

</div>