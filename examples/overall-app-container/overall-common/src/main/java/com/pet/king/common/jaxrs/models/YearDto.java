package com.pet.king.common.jaxrs.models;

public class YearDto {

	private int year;

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	@Override
	public String toString() {
		return "YearDto [year=" + year + "]";
	}

}