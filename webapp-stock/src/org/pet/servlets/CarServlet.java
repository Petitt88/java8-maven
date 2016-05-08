package org.pet.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pet.models.Car;

@WebServlet("/car")
public class CarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Car car = new Car();
		car.setName("Opel");
		car.setPlateNumber("XXX-001");

		request.setAttribute("car", car);
		request.getRequestDispatcher("Views/car.jsp").forward(request, response);
	}

}
