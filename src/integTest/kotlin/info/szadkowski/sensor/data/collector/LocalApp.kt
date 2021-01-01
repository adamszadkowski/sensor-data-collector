package info.szadkowski.sensor.data.collector

import org.springframework.boot.SpringApplication

fun main(args: Array<String>) {
    val app = SpringApplication(Application::class.java)
    app.addInitializers(DependenciesInitializer(shouldStartWiremock = false))
    app.setAdditionalProfiles("local")
    app.run(*args)
}
