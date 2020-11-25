plugins {
    kotlin("jvm") version "1.4.20"

    application
}

repositories {
    jcenter()
}

dependencies {
    implementation(platform(kotlin("bom")))
    implementation(platform("org.junit:junit-bom:5.7.0"))
    implementation(kotlin("stdlib"))

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

application {
    mainClassName = "info.szadkowski.sensor.data.collector.AppKt"
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}
