import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val exposedVersion by extra { "0.13.7" }
val gradlewVersion by extra { "5.4.1" }

plugins {
	id("org.springframework.boot") version "2.1.5.RELEASE"
	id("io.spring.dependency-management") version "1.0.7.RELEASE"
	kotlin("jvm") version "1.3.31"
	kotlin("plugin.spring") version "1.3.31"
}


group = "hu.pet"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
	maven("https://dl.bintray.com/kotlin/exposed/")
	//maven { url = uri("https://dl.bintray.com/kotlin/exposed/") }
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	runtimeOnly("mysql:mysql-connector-java")

	implementation("org.jetbrains.exposed:exposed:$exposedVersion")

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude("junit")
	}
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
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