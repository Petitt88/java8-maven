package org.pet.servlets;

import java.io.IOException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pet.businesslogic.MyEjb;
import org.pet.models.Car;

@WebServlet(name = "car", urlPatterns = { "/car" })
public class CarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// resource injection vs dependency injection: http://stackoverflow.com/questions/30060876/what-is-difference-between-resource-injection-and-dependency-injection-cdi-in
	// @Resource vs @Inject
	// @Inject - can inject Java pojo classes
	// @Resource is for resources like "UserTransaction"
	@Inject
	private Car car;

	@EJB
	MyEjb myEjb;
	
	//@GET - to specify the method invoked on GET requests (we are no longer tied to the doGet
	//@POST
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Car car = new Car();
		this.car.setName("Opel");
		this.car.setPlateNumber("XXX-001");

		String message = this.myEjb.getMessage();

		request.setAttribute("car", car);
		request.setAttribute("message", message);
		request.getRequestDispatcher("Views/car.jsp").forward(request, response);
	}

}
