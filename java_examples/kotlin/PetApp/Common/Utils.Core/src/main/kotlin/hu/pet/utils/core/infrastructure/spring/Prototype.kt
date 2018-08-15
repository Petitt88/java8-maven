package hu.pet.utils.core.infrastructure.spring

import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
annotation class Prototype