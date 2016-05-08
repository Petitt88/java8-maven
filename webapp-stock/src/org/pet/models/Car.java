package org.pet.models;

import javax.enterprise.context.RequestScoped;

@RequestScoped
//@ApplicationScoped
public class Car {
	private String name;
	private String plateNumber;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

}
