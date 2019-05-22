import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val exposedVersion by extra { "0.13.7" }
val gradlewVersion by extra { "5.4.1" }
val kotlinCoroutineVersion by extra { "1.2.1" }
val swaggerVersion by extra { "2.9.2" }

plugins {
	id("org.springframework.boot") version "2.1.5.RELEASE"
	id("io.spring.dependency-management") version "1.0.7.RELEASE"
	kotlin("jvm") version "1.3.31"
	kotlin("kapt") version "1.3.31"
	kotlin("plugin.spring") version "1.3.31"
}

group = "com.pet"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
	maven { url = uri("https://dl.bintray.com/kotlin/exposed/") }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
	// embedded mongodb to avoid having it as a docker container
	implementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo")
	implementation("io.springfox:springfox-swagger2:$swaggerVersion")
	implementation("io.springfox:springfox-swagger-ui:$swaggerVersion")
	implementation("org.jetbrains.exposed:exposed:$exposedVersion")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	//implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:${kotlinCoroutineVersion}"
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$kotlinCoroutineVersion")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$kotlinCoroutineVersion")

	kapt("org.springframework.boot:spring-boot-configuration-processor")

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude("junit")
	}
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
	testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<Wrapper> {
	gradleVersion = gradlewVersion
	distributionType = Wrapper.DistributionType.ALL
}