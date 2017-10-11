<div>Hello from the about page under home section.</div>

<%
	//request.getParameter("sdsd");
	//session.getAttribute(("ss");
	//config.getInitParameter("sdsd");
	//application.getAttribute("sds");

	//pageContext.getAttribute("edsd")

	// equivalent as the application.setAttribute
	//pageContext.setAttribute("param", "value", PageContext.APPLICATION_SCOPE);

	// will check in different scopes (page, request, session, application)
	//pageContext.findAttribute("sdsdd");

	// comes from the web.xml
	//String initParam = getInitParameter("param1");
	String initParam = getServletConfig().getInitParameter("param1");
%>

<%=initParam%>

<%=getServletContext().getAttribute("over")%>


<%!/*
	 tomcat generates such a method when compiling this into a servlet
	 we can override it
	*/
	public void jspInit() {
		getServletContext().setAttribute("over", "overrinding the jspInit method");
	}%>