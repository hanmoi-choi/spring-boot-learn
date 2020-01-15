import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("org.springframework.boot") version "2.2.2.RELEASE"

    id("org.jetbrains.kotlin.plugin.noarg") version "1.3.61"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.3.61"
    id("org.jetbrains.kotlin.plugin.spring").version("1.3.61")
    kotlin("jvm") version "1.3.61"
}
apply(plugin = "io.spring.dependency-management")
apply(plugin = "kotlin")
apply(plugin = "kotlin-spring")
apply(plugin = "kotlin-jpa")

group = "com.kotlin-spring-boot"
version = "1.0.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

val developmentOnly by configurations.creating
configurations {
    runtimeClasspath {
        extendsFrom(developmentOnly)
    }
}

repositories {
    mavenCentral()
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

dependencies {
    // Monitoring
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-actuator", version = "2.2.2.RELEASE")

    // REST API
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-web", version = "2.2.2.RELEASE") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
    }
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-jetty", version = "2.2.2.RELEASE")

    // Development Tool
    developmentOnly(group = "org.springframework.boot", name = "spring-boot-devtools", version = "2.2.2.RELEASE")

    // Database
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-data-jdbc", version = "2.2.2.RELEASE")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-data-jpa", version = "2.2.2.RELEASE")
    implementation(group = "org.postgresql", name = "postgresql", version = "42.2.8")
    api(group = "org.flywaydb", name = "flyway-core", version = "6.0.8")

    // DateTime
    implementation(group = "joda-time", name = "joda-time", version = "2.10.5")

    // Kotlin
    implementation(group = "com.fasterxml.jackson.module", name = "jackson-module-kotlin")

    // Security
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-security", version = "2.2.2.RELEASE")
    implementation(group = "io.jsonwebtoken", name = "jjwt", version = "0.9.1")
//    testImplementation(group = "org.springframework.securty", name = "spring-security-test")

    // API DOC
    implementation(group = "io.springfox", name = "springfox-swagger-ui", version = "2.8.0")
    implementation(group = "io.springfox", name = "springfox-swagger2", version = "2.8.0")

    // Test
    testImplementation(group = "io.zonky.test", name = "embedded-database-spring-test", version = "1.5.2")
    testImplementation(group = "org.assertj", name = "assertj-core", version = "3.13.2")

    testImplementation(group = "io.kotlintest", name = "kotlintest-runner-junit5", version = "3.3.3")
    testImplementation(group = "io.kotlintest", name = "kotlintest-extensions-spring", version = "3.3.3")

    testImplementation(group = "org.springframework.boot", name = "spring-boot-starter-test", version = "2.2.2.RELEASE") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }

    implementation(group = "org.jetbrains.kotlin", name = "kotlin-reflect")
    implementation(group = "org.jetbrains.kotlin", name = "kotlin-stdlib-jdk8")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}
