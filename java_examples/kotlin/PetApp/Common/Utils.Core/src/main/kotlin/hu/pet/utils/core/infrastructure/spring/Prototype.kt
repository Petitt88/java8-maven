package hu.pet.utils.core.infrastructure.spring

import org.springframework.context.annotation.Scope

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Scope("prototype")
annotation class Prototype