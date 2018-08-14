package hu.pet.utils.core.infrastructure

import hu.pet.utils.core.infrastructure.spring.Prototype
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InjectionPoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.lang.reflect.Member

@Configuration
class CoreConfiguration {

	@Bean
	@Prototype
	fun getLogger(ip: InjectionPoint): Logger {
		val member: Member? = ip.member
		return if (member != null) LoggerFactory.getLogger(member.declaringClass) else LoggerFactory.getLogger(ip.declaredType.name)
	}
}