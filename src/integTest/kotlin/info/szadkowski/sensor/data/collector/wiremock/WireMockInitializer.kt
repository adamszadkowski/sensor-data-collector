package info.szadkowski.sensor.data.collector.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.event.ContextClosedEvent
import org.testcontainers.containers.InfluxDBContainer
import org.testcontainers.utility.DockerImageName

class WireMockInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        val wireMockServer = WireMockServer(WireMockConfiguration.options().dynamicPort())
        val influxDBContainer = KInfluxDBContainer(DockerImageName.parse("influxdb").withTag("1.8"))
            .withDatabase(database)
            .withUsername(username)
            .withPassword(password)

        wireMockServer.start()
        influxDBContainer.start()

        applicationContext.beanFactory.registerSingleton("wireMockServer", wireMockServer)
        applicationContext.beanFactory.registerSingleton("influxDB", influxDBContainer.newInfluxDB)

        applicationContext.addApplicationListener {
            if (it is ContextClosedEvent) {
                influxDBContainer.stop()
                wireMockServer.stop()
            }
        }

        TestPropertyValues.of(
            "influxdb.url=${wireMockServer.baseUrl()}",
            "influxdb.dbUrl=${influxDBContainer.url}",
            "influxdb.database=$database",
            "influxdb.username=$username",
            "influxdb.password=$password"
        ).applyTo(applicationContext)
    }

    class KInfluxDBContainer(dockerImageName: DockerImageName) : InfluxDBContainer<KInfluxDBContainer>(dockerImageName)

    private companion object {
        const val database = "dbName"
        const val username = "user"
        const val password = "pass"
    }
}

