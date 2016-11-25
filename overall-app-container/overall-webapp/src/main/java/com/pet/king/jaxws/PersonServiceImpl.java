package com.pet.king.jaxws;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.jws.WebService;

// will be available at "/personws" uri, defined in the web.xml and sun-jaxws.xml files
//@WebService(endpointInterface = "com.pet.king.jaxws.PersonService")
public class PersonServiceImpl implements PersonService {

	private static Map<Integer, Person> persons = new HashMap<>();

	public String Hello() {
		return "Hello from jax-ws webservice";
	}

	public boolean addPerson(Person p) {
		if (persons.get(p.getId()) != null)
			return false;

		persons.put(p.getId(), p);
		return true;
	}

	public boolean deletePerson(int id) {
		if (persons.get(id) == null)
			return false;

		persons.remove(id);
		return true;
	}

	public Person getPerson(int id) {
		return persons.get(id);
	}

	public Person[] getAllPersons() {
		return persons
				.entrySet()
				.stream()
				.map(Map.Entry::getValue)
				.collect(Collectors.toList())
				.toArray(new Person[persons.size()]);
	}
}
