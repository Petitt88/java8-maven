package webshop.app;

import webshop.app.entities.Person;
import webshop.common.Session;

public class App {

	public static void main(String[] args) {
		Session<Person> session = new Session<>();
		Person p1 = new Person("Peter Kongyik");

		session.store("KP", p1);
		System.out.println(
				String.format("Number of items in session: %s, name of the 1st person is: %s",
						session.size(),
						session.get("KP").getName()
				));
	}
}
