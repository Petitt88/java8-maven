package com.pet.king.app;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Inherited
public @interface Trait {

	String name();

	int order() default 0;

	String[] names();
}

@interface TraitDefault {
	// this is the default value. We can use the annotation
	// - either: TraitDefault(5)
	// - or: TraitDefault(value = 5)
	int value();
}
