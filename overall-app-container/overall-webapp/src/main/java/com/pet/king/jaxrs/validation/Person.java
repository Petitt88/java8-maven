package com.pet.king.jaxrs.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import com.pet.king.jaxrs.models.PersonDto;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { Person.PersonValidator.class })
public @interface Person {

	String message() default "Person is not valid";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	public static class PersonValidator implements ConstraintValidator<Person, PersonDto> {

		@Override
		public void initialize(Person constraintAnnotation) {
		}

		@Override
		public boolean isValid(PersonDto value, ConstraintValidatorContext context) {
			if (value == null)
				return false;

			if (value.getName() == null || value.getAge() == null)
				return false;

			return true;
		}

	}
}
