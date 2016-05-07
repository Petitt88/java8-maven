package org.pet.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class HomeServlet
 */
@WebServlet("/home")
// @WebServlet(urlPatterns = { "/home" })
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public HomeServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
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
	}

}
