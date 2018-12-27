package com.pet.webfluxthymeleaf

import com.pet.webfluxthymeleaf.infrastructure.MovieConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InjectionPoint
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope
import java.lang.reflect.Member

@SpringBootApplication
@EnableConfigurationProperties(MovieConfig::class)
class WebfluxThymeleafApplication {

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	fun getLogger(ip: InjectionPoint): Logger {
		val member: Member? = ip.member
		return if (member != null) LoggerFactory.getLogger(member.declaringClass) else LoggerFactory.getLogger(ip.declaredType.name)
	}
}

fun main(args: Array<String>) {
	runApplication<WebfluxThymeleafApplication>(*args) {
		addInitializers(Startup.initializer)
	}
}
