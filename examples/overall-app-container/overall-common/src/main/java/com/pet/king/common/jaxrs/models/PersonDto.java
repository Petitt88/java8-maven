package com.pet.king.common.jaxrs.models;

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
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/*
	 * Note that in this version, firstName and lastName are fields initialized via injection,
	 * and name and age are a resource class properties. Constraint annotations on properties are specified on their corresponding getters.
	 * Currently validation annotation are commented out because the com.pet.king.common.jaxrs.validation.PersonValidator takes care of validation.
	 */
	// @NotNull
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	// @NotNull
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "PersonDto [age=" + age + ", name=" + name + ", id=" + id + "]";
	}
}
