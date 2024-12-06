plugins {
	java
	id("org.springframework.boot") version "3.4.0"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.rpl"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	// https://mvnrepository.com/artifact/org.springframework.security/spring-security-core
	implementation("org.springframework.security:spring-security-core")

	// https://mvnrepository.com/artifact/org.springframework.data/spring-data-jpa
	implementation("org.springframework.data:spring-data-jpa")

	// https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")


	// https://mvnrepository.com/artifact/jakarta.persistence/jakarta.persistence-api
	implementation("jakarta.persistence:jakarta.persistence-api")

	// https://mvnrepository.com/artifact/jakarta.validation/jakarta.validation-api
	implementation("jakarta.validation:jakarta.validation-api")
	
	implementation("org.springframework.boot:spring-boot-starter-security")

	// https://mvnrepository.com/artifact/org.hibernate.orm/hibernate-core
	implementation("org.hibernate.orm:hibernate-core")
	// https://mvnrepository.com/artifact/org.springframework/spring-aspects
	implementation("org.springframework:spring-aspects")



	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
