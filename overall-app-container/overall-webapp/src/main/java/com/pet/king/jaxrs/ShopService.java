package com.pet.king.jaxrs;

import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.validator.constraints.Email;

import com.pet.king.jaxrs.models.PersonDto;
import com.pet.king.jaxrs.models.YearDto;
import com.pet.king.jaxrs.validation.Person;

// maps to /api/shop
@Path("shop")
public class ShopService {

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getTitle() {
		return "<p>Greatest shop service!</p>";
	}

	@GET
	@Path("{year}")
	@Produces(MediaType.APPLICATION_JSON)
	// NOTE: for some reason PathParam does not work...
	// in case of reference types the request hits but the associated path-param is always null
	public Response getYear(@PathParam("year") int year) {
		YearDto yearDto = new YearDto();
		yearDto.setYear(year);

		return Response
				.status(200)
				.type(MediaType.APPLICATION_JSON)
				.entity(yearDto)
				.build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void createProduct(
			@NotNull @FormParam("name") String name,
			@NotNull @FormParam("price") String age,
			@Email @FormParam("creatorEamil") String email) {

	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createPerson(@NotNull @Person PersonDto person) {

		// manual validation, totally unnecessary since we are using bean-validation
		// which invokes the validator behind the scenes before entering here
		// Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		// validator.validate(person);

		return Response
				.status(200)
				// .type(MediaType.APPLICATION_JSON)
				.entity(person)
				.build();
	}

}
