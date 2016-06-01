package com.pet.king.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

// maps to /api/shop
@Path("shop")
public class ShopService {

	@GET
	// @Path("title")
	@Produces(MediaType.TEXT_HTML)
	public String GetTitle() {
		return "<p>Greatest shop service!</p>";
	}

	@GET
	@Path("title")
	@Produces(MediaType.TEXT_HTML)
	public String GetTitleAlternative() {
		return "<p>Super shop service!</p>";
	}
}
