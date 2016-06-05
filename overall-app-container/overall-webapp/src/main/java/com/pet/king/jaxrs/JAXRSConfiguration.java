package com.pet.king.jaxrs;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("api")
public class JAXRSConfiguration extends Application {

	public JAXRSConfiguration() {
		// https://jersey.java.net/documentation/latest/bean-validation.html
		// Configuring Jersey specific properties for Bean Validation.

		// new ResourceConfig()
		// // Now you can expect validation errors to be sent to the client.
		// .property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true)
		// // @ValidateOnExecution annotations on subclasses won't cause errors.
		// .property(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true)
		// // Further configuration of ResourceConfig.
		// .register(...)
		
		// Create JAX-RS application.
		final Application application = new ResourceConfig()
		        .packages("org.glassfish.jersey.examples.jackson")
		        .register(JacksonFeature.class);
	}
}
