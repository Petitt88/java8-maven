package com.pet.king.jaxws;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

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
