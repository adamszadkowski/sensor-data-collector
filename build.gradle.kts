import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    val kotlinVersion = "1.8.20"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion

    id("org.springframework.boot") version "3.0.5"

    id("nebula.integtest") version "9.6.3"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform(kotlin("bom")))
    implementation(platform("org.junit:junit-bom:5.9.1"))
    implementation(platform("org.springframework.boot:spring-boot-dependencies:3.0.5"))
    implementation(platform("org.testcontainers:testcontainers-bom:1.17.3"))
    val retrofitVersion = "2.9.0"
    api("com.squareup.retrofit2:retrofit:$retrofitVersion")
    api("com.squareup.retrofit2:converter-scalars:$retrofitVersion")
    api("com.github.tomakehurst:wiremock-jre8-standalone:2.34.0")
    api("io.strikt:strikt-core:0.34.1")
    api("org.influxdb:influxdb-java:2.23")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("org.springframework:spring-aspects")

    implementation("com.squareup.retrofit2:retrofit")
    implementation("com.squareup.retrofit2:converter-scalars")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.influxdb:influxdb-java")
    testImplementation("org.testcontainers:influxdb")
    testImplementation("com.github.tomakehurst:wiremock-jre8-standalone")
    testImplementation("io.strikt:strikt-core")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks {
    getByName<BootJar>("bootJar") {
        archiveFileName.set("sensor-data-collector.jar")
    }
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }
    withType<Test> {
        useJUnitPlatform()
    }

    wrapper {
        version = "8.0.2"
        distributionType = Wrapper.DistributionType.BIN
    }
}
