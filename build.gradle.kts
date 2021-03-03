import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.4.2"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.4.21"
	kotlin("kapt") version "1.4.21"
	kotlin("plugin.spring") version "1.4.21"
}

group = "io.github.hlipinski"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

val krush_version: String by project
val exposed_version: String by project

repositories {
	mavenCentral()
	maven(url = "https://dl.bintray.com/kotlin/exposed")
	maven(url = "https://philanthropist.touk.pl/nexus/content/repositories/releases")
	maven(url = "https://philanthropist.touk.pl/nexus/content/repositories/snapshots")
}

dependencies {
	api("pl.touk.krush:krush-annotation-processor:$krush_version")
	kapt("pl.touk.krush:krush-annotation-processor:$krush_version")

	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.jetbrains.exposed:exposed-spring-boot-starter:$exposed_version")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.postgresql:postgresql:42.2.5")
	implementation("com.h2database:h2:1.4.200")

	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
	testImplementation("org.testcontainers:testcontainers:1.15.2")
	testImplementation("org.testcontainers:junit-jupiter:1.15.2")
	testImplementation("org.testcontainers:mongodb:1.15.2")
	testImplementation("org.testcontainers:postgresql:1.15.2")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
	testLogging {
		events(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
	}
}
