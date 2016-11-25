package com.pet.king.app;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import com.pet.king.app.xml.Address;
import com.pet.king.app.xml.Customer;
import com.pet.king.app.xml.ObjectFactory;
import com.pet.king.app.xml.Order;
import com.pet.king.app.xml.Root;
import com.pet.king.app.xml.ShipInfo;

// @XmlRootElement
// @XmlAccessorType(XmlAccessType.FIELD)
// class Test {
// private int age;
// }

public class XmlExample {

	public void execute() {

		Optional<Root> root = this.createRoot();

		root.ifPresent(r -> {
			try {
				// JAXBContext context = JAXBContext.newInstance(Root.class);

				// JAXB needs the context path specified which happens to be the package name of the generated java classes from the xsd
				JAXBContext context = JAXBContext.newInstance("com.pet.king.app.xml");
				Marshaller marshaller = context.createMarshaller();
				marshaller.marshal(root.get(), new File("root.xml"));

				System.out.println("root.xml successfully create via JAXB");

				Unmarshaller unmarshaller = context.createUnmarshaller();
				Root root2 = (Root) unmarshaller.unmarshal(new File("root.xml"));
				System.out.println(String.format("Unmarshalled %s", Objects.toString(root2)));
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		});
	}

	private Optional<Root> createRoot() {

		ObjectFactory factory = new ObjectFactory();
		DatatypeFactory datatypeFactory;
		try {
			datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
			return Optional.empty();
		}

		Root root = factory.createRoot();
		Root.Customers customers = new Root.Customers();
		root.setCustomers(customers);
		Root.Orders orders = new Root.Orders();
		root.setOrders(orders);

		// IntStream.range(1, 10).map(a -> {
		Stream.iterate(0, a -> a + 1)
				.limit(10)
				.map(a -> {
					// Customer customer = new Customer();
					Customer customer = factory.createCustomer();
					customer.setCompanyName("Comp" + a);
					customer.setContactName("Contact" + a);
					customer.setContactTitle("Title" + a);
					customer.setCustomerID(a.toString());
					customer.setFax("Fax" + a);
					customer.setPhone("Phone" + a);

					Address address = factory.createAddress();
					customer.setFullAddress(address);

					address.setAddress(String.format("Address %s", customer.getCustomerID()));
					address.setCity(String.format("City %s", customer.getCustomerID()));
					address.setCountry(String.format("Country %s", customer.getCustomerID()));
					address.setCustomerID(customer.getCustomerID());
					address.setPostalCode(String.format("PostalCode %s", customer.getCustomerID()));
					address.setRegion(String.format("Region %s", customer.getCustomerID()));

					return customer;
				})
				.forEach(a -> customers.getCustomer().add(a));

		Stream.iterate(0, a -> a + 1)
				.limit(30)
				.map(a -> {
					Order order = factory.createOrder();
					order.setCustomerID(Integer.toString(a % 10));
					order.setEmployeeID(a.toString());
					order.setOrderDate(datatypeFactory.newXMLGregorianCalendar("2012-02-01"));
					order.setRequiredDate(datatypeFactory.newXMLGregorianCalendar("2012-02-02"));

					ShipInfo shipInfo = factory.createShipInfo();
					order.setShipInfo(shipInfo);

					shipInfo.setFreight(new BigDecimal(100 * a));
					shipInfo.setShipAddress("address" + a);
					shipInfo.setShipCity("city" + a);
					shipInfo.setShipCountry("country" + a);
					shipInfo.setShipName("name" + a);
					shipInfo.setShippedDate(datatypeFactory.newXMLGregorianCalendar("2012-02-03"));
					shipInfo.setShipPostalCode("pc" + a);
					shipInfo.setShipRegion("region" + a);
					shipInfo.setShipVia(new BigInteger(Integer.toString(a * a)));

					return order;
				})
				.forEach(a -> orders.getOrder().add(a));

		return Optional.of(root);
	}
}