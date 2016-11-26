package com.pet.king.jaxws;

// @WebService
// @SOAPBinding(style = Style.RPC)
public interface PersonService {

	// @WebMethod
	String Hello();

	boolean addPerson(Person p);

	boolean deletePerson(int id);

	Person getPerson(int id);

	Person[] getAllPersons();

}
