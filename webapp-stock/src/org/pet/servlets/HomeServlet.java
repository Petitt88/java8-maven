package org.pet.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.pet.helpers.Calculator;

/**
 * Servlet implementation class HomeServlet
 */
// @WebServlet("/home")
@WebServlet(urlPatterns = { "/home" }, initParams = { @WebInitParam(name = "car", value = "Peugeot") })
public class HomeServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// response.setContentLength(arg0);
		PrintWriter writer = response.getWriter();

		String par1 = request.getParameter("user");

		// session
		HttpSession session = request.getSession();
		// session.setAttribute(arg0, arg1);

		String contentPath = request.getContextPath();
		writer.append("Served at: ").append(request.getContextPath());
		writer.println(String.format("Content path: %s", contentPath));

		// global store (app-level)
		ServletContext context = request.getServletContext();
		// context.setAttribute(arg0, arg1);

		String car = this.getServletConfig().getInitParameter("car");
		writer.println("Car from ServletConfig is: " + car);
		
		// will send 3XX redirect back to the browser
		// will make the browser send a 2nd call, targeting the about.jsp resource
		//response.sendRedirect("Views/about.jsp");

		this.calculateAndWrite(writer);
	}

	private void calculateAndWrite(PrintWriter writer) {
		String calced = new Calculator().calc();
		writer.println(String.format("calculated: %s", calced));
	}

}
