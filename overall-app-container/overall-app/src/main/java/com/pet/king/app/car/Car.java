package com.pet.king.app.car;

import com.pet.king.app.InterfaceMindBlowUp;
import com.pet.king.app.Person;
import com.pet.king.app.PersonExtended;

import java.util.Optional;


public class Car extends Person {

	<T> void something(final T param) {
		super.bossProt();

		PersonExtended p = new PersonExtended();
		// cannot invoke protected or package-private methods.
		// however if this class was in the same package, it could invoke them

		InterfaceMindBlowUp mindBU = () -> System.out.println(param.toString());
	}

	public <T extends Integer> void Calc(T param1, T param2) {

	}

	public <T extends String> void Calc(T param1, T param2) {

	}

	// <T extends String> void sort(Collection<T> strings) {
	// // Some implementation over strings heres
	// }
	//
	// <T extends Number> void sort(Collection<T> numbers) {
	// // Some implementation over numbers here
	// }

	/**
	 * The method parses the string argument as a signed decimal integer. The
	 * characters in the string must all be decimal digits, except that the
	 * first character may be a minus sign {@code '-'} or plus sign {@code '+'}.
	 * <p>
	 * <p>
	 * An exception of type {@code NumberFormatException} is thrown if string is
	 * {@code null} or has length of zero.
	 * <p>
	 * <p>
	 * Examples: <blockquote>
	 * <p>
	 * <pre>
	 * parse( "0" ) returns 0
	 * parse( "+42") returns 42
	 * parse( "-2" ) returns -2
	 * parse( "string" ) throws a NumberFormatException
	 * </pre>
	 * <p>
	 * </blockquote>
	 *
	 * @param str a {@code String} containing the {@code int} representation to
	 *            be parsed
	 * @return the integer value represented by the string
	 * @throws NumberFormatException if the string does not contain a valid integer value
	 */
	public int parse(String str) throws NumberFormatException {
		if (str == null) {
			throw new IllegalArgumentException("String should not be null");
		}

		return Integer.parseInt(str);
	}

	public Optional<Integer> parseOptional(String str) {
		if (str == null) {
			return Optional.empty();
		}
		return Optional.of(Integer.parseInt(str));
	}
}
