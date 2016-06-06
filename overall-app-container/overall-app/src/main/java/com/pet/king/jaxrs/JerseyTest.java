package com.pet.king.jaxrs;

import java.util.concurrent.Future;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.pet.king.common.jaxrs.models.PersonDto;
import com.pet.king.common.jaxrs.models.YearDto;

public class JerseyTest {

	public void execute() {

		this.getWithString();
		this.getWithJson();
		this.createPersonAsync();
		this.createPersonFail();
	}

	private void getWithString() {
		try {

			String response = ClientBuilder
					.newClient()
					.target("http://localhost:8080/overall-webapp")
					.path("api/shop")
					// .queryParam(name, values)
					.request(MediaType.TEXT_HTML)
					// .header(name, value)
					.get(String.class);

			System.out.println(String.format("Jersey root GET response: %s", response));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void getWithJson() {
		try {

			YearDto response = ClientBuilder
					.newClient()
					.target("http://localhost:8080/overall-webapp")
					.path("api/shop/2016?plusyear=20")
					// .queryParam(name, values)
					.register(JacksonFeature.class)
					.request(MediaType.APPLICATION_JSON)
					// .header(name, value)
					.get(YearDto.class);

			System.out.println(String.format("Jersey root GET response: %s", response));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void createPersonAsync() {

		try {

			PersonDto person = new PersonDto();
			person.setAge(27);
			person.setName("Peter");

			Future<Response> response = ClientBuilder
					.newClient()
					.target("http://localhost:8080/overall-webapp")
					.path("api/shop")
					// .register(LoggingFilter.class)
					// registers json support provided by Jackson
					.register(JacksonFeature.class)
					.request(MediaType.APPLICATION_JSON)
					.async()
					.post(Entity.entity(person, MediaType.APPLICATION_JSON));

			PersonDto responseBody = response.get().readEntity(PersonDto.class);

			System.out.println(String.format("Created person: %s", responseBody));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void createPersonFail() {

		try {

			PersonDto person = new PersonDto();
			person.setAge(27);
			// age is not set, so Bean validation on the server side will send the reponse back without even hitting the Jaxrs service

			Response response = ClientBuilder
					.newClient()
					.target("http://localhost:8080/overall-webapp")
					.path("api/shop")
					.register(JacksonFeature.class)
					.request(MediaType.APPLICATION_JSON, MediaType.TEXT_HTML)
					.post(Entity.entity(person, MediaType.APPLICATION_JSON));

			PersonDto responseBody = response.readEntity(PersonDto.class);

			System.out.println(String.format("Created person: %s", responseBody));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
