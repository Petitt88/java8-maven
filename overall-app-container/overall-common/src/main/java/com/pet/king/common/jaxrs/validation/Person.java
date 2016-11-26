package com.pet.king.common.jaxrs.validation;

import com.pet.king.common.jaxrs.models.PersonDto;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {Person.PersonValidator.class})
public @interface Person {

	String message() default "Person is not valid";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	class PersonValidator implements ConstraintValidator<Person, PersonDto> {

		@Override
		public void initialize(Person constraintAnnotation) {
		}

		@Override
		public boolean isValid(PersonDto value, ConstraintValidatorContext context) {
			if (value == null)
				return false;

			return !(value.getName() == null || value.getAge() == null);
		}

	}
}
