package com.pet.king.jaxrs;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

public class JerseyTest {

	public void execute() {

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
}
