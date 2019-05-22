import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

val appVersion by extra { "1.0.0-SNAPSHOT" }
val gradlewVersion by extra { "5.4.1" }

plugins {
	id("org.springframework.boot") version "2.1.5.RELEASE"
	id("io.spring.dependency-management") version "1.0.7.RELEASE"
	kotlin("jvm") version "1.3.31"
	kotlin("kapt") version "1.3.31"
	kotlin("plugin.spring") version "1.3.31"
}

// subprojects {
allprojects {
	apply {
		plugin("org.springframework.boot")
		plugin("io.spring.dependency-management")
		plugin("org.jetbrains.kotlin.jvm")
		plugin("org.jetbrains.kotlin.kapt")
		plugin("kotlin-spring")
	}

	group = "hu.pet"
	version = appVersion

	repositories {
		mavenCentral()
	}

	dependencies {
		implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
		implementation("org.jetbrains.kotlin:kotlin-reflect")

		implementation("org.springframework.boot:spring-boot-starter")

		testImplementation("org.springframework.boot:spring-boot-starter-test") {
			exclude("junit")
		}
		testImplementation("org.junit.jupiter:junit-jupiter-api")
		testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
	}

	java.sourceCompatibility = JavaVersion.VERSION_11

	// it is necessary to disable the bootJar task. It works only with Spring Boot projects!
	// however with library ones, it does not.
	// so assuming there will be more library projects than actual Spring Boot projects, disable here the bootJar,
	// reenable the jar task and in each Spring Boot app do the opposite: disable jar and enable bootJar

	tasks.withType<Jar> {
		enabled = true
	}
	tasks.withType<BootJar> {
		enabled = false
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
}

dependencies {
	implementation(project(":EVModule"))
	implementation(project(":SADModule"))
	implementation(project(":VNextSpringModule"))
}

tasks.withType<Wrapper> {
	gradleVersion = gradlewVersion
	distributionType = Wrapper.DistributionType.ALL
}