package com.pet.king.jaxrs;

import com.pet.king.common.jaxrs.models.PersonDto;
import com.pet.king.common.jaxrs.models.YearDto;
import com.pet.king.common.jaxrs.validation.Person;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;

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
	public Response getYear(@PathParam("year") Integer year, @QueryParam("plusyear") int plusyear) {
		YearDto yearDto = new YearDto();

		if (year == null)
			year = LocalDate.now().getYear();

		yearDto.setYear(year + plusyear);

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

		person.setId(LocalDate.now().getDayOfYear());
		return Response
				.status(201)
				// .type(MediaType.APPLICATION_JSON)
				.entity(person)
				.build();
	}

}
