import org.gradle.jvm.tasks.Jar
import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	// Jackson datatype to use java 8 time api
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

	implementation(project(":Utils"))

	// annotation processing
	kapt("org.springframework.boot:spring-boot-configuration-processor")

	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.springframework.security:spring-security-test")
}

// make the Spring Boot gradle plugin generate build information consumable during runtime
springBoot {
	buildInfo()
}

tasks.withType<Jar> {
	enabled = false
}
tasks.withType<BootJar> {
	enabled = true
}
