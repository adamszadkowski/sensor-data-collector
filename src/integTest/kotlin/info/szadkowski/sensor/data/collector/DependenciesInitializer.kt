package info.szadkowski.sensor.data.collector

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.event.ContextClosedEvent
import org.testcontainers.containers.InfluxDBContainer
import org.testcontainers.utility.DockerImageName

class DependenciesInitializer(
    private val shouldStartWiremock: Boolean = true
) : ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        val influxDBContainer = startInfluxdb(applicationContext)

        val influxdbUrl = when (shouldStartWiremock) {
            true -> startWiremock(applicationContext).baseUrl()
            else -> influxDBContainer.url
        }
        TestPropertyValues.of(
            "influxdb.url=$influxdbUrl",
            "influxdb.dbUrl=${influxDBContainer.url}",
            "influxdb.database=$database",
            "influxdb.username=$username",
            "influxdb.password=$password"
        ).applyTo(applicationContext)
    }

    private fun startInfluxdb(applicationContext: ConfigurableApplicationContext) =
        KInfluxDBContainer(DockerImageName.parse("influxdb").withTag("1.8"))
            .withDatabase(database)
            .withUsername(username)
            .withPassword(password)
            .withAdmin("admin")
            .withAdminPassword("admin")
            .apply { start() }
            .also { applicationContext.beanFactory.registerSingleton("influxDB", it.newInfluxDB) }
            .also { applicationContext.onClose { it.stop() } }

    private fun startWiremock(applicationContext: ConfigurableApplicationContext) =
        WireMockServer(WireMockConfiguration.options().dynamicPort())
            .apply { start() }
            .also { applicationContext.beanFactory.registerSingleton("wireMockServer", it) }
            .also { applicationContext.onClose { it.stop() } }

    private fun ConfigurableApplicationContext.onClose(action: () -> Unit) =
        addApplicationListener { if (it is ContextClosedEvent) action() }

    class KInfluxDBContainer(dockerImageName: DockerImageName) : InfluxDBContainer<KInfluxDBContainer>(dockerImageName)

    private companion object {
        const val database = "dbName"
        const val username = "user"
        const val password = "pass"
    }
}

