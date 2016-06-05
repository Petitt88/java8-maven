package com.pet.king.jaxrs.models;

import javax.validation.constraints.NotNull;

public class PersonDto {

	// @NotNull
	// @FormParam("firstName")
	// private String firstName;
	//
	// @NotNull
	// @FormParam("lastName")
	// private String lastName;

	private Integer age;
	private String name;

	/*
	 * Note that in this version, firstName and lastName are fields initialized via injection
	 * and name and age are a resource class property. Constraint annotations on properties are specified in their corresponding getters.
	 */
	@NotNull
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@NotNull
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
