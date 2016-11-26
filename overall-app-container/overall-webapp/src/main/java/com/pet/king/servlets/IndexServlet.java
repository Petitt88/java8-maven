package com.pet.king.servlets;

import com.pet.king.services.MagicService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "index", urlPatterns = {"/haha"})
public class IndexServlet extends HttpServlet {

	@Inject
	private MagicService magicService;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		if (this.magicService == null)
			throw new IllegalStateException("magicService is null, CDI is not enabled!");

		req.getRequestDispatcher("index.jsp").forward(req, resp);
	}
}