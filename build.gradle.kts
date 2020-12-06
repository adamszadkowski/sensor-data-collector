import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.4.20"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion

    id("org.springframework.boot") version "2.4.0"

    id("nebula.integtest") version "7.0.9"
}

repositories {
    jcenter()
}

dependencies {
    implementation(platform(kotlin("bom")))
    implementation(platform("org.junit:junit-bom:5.7.0"))
    implementation(platform("org.springframework.boot:spring-boot-dependencies:2.4.0"))
    api("com.github.tomakehurst:wiremock:2.27.2")
    api("io.strikt:strikt-core:0.22.1")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.springframework.boot:spring-boot-starter-web")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.github.tomakehurst:wiremock")
    testImplementation("io.strikt:strikt-core")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "14"
    }
    withType<Test> {
        useJUnitPlatform()
    }
}
