package info.szadkowski.sensor.data.collector.api

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.VerificationException
import com.github.tomakehurst.wiremock.client.WireMock.*
import info.szadkowski.sensor.data.collector.DependenciesInitializer
import org.influxdb.InfluxDB
import org.influxdb.InfluxDBFactory
import org.influxdb.dto.Query
import org.influxdb.dto.QueryResult
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import strikt.api.expectThat
import strikt.assertions.containsExactly

@SpringBootTest
@ContextConfiguration(initializers = [DependenciesInitializer::class])
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MeasurementEndpointTest(
    @Autowired val wireMockServer: WireMockServer,
    @Autowired val mockMvc: MockMvc
) {

    @AfterEach
    fun validateWireMock() {
        val unmatchedRequests = wireMockServer.findAllUnmatchedRequests()
        if (unmatchedRequests.isNotEmpty()) {
            val nearMisses = wireMockServer.findNearMissesForAllUnmatchedRequests()
            throw when {
                nearMisses.isNotEmpty() -> VerificationException.forUnmatchedNearMisses(nearMisses)
                else -> VerificationException.forUnmatchedRequests(unmatchedRequests)
            }
        }
    }

    @Nested
    inner class RealInfluxDBScenarios(
        @Autowired val influxDB: InfluxDB
    ) {

        @BeforeEach
        fun delegateToInfluxDB(@Value("\${influxdb.dbUrl}") url: String) {
            wireMockServer.stubFor(post(urlMatching("/write(.*)")).willReturn(aResponse().proxiedFrom(url)))
        }

        @AfterEach
        fun clearDatabase(@Value("\${influxdb.dbUrl}") url: String, @Value("\${influxdb.database}") db: String) {
            InfluxDBFactory.connect(url, "admin", "admin").use {
                it.setDatabase(db)
                it.query(Query("DROP MEASUREMENT temp"))
                it.query(Query("DROP MEASUREMENT aqi"))
            }
        }

        @Test
        fun `Writes temperature measurement to InfluxDB`() {
            mockMvc.post("/measurement/temperature") {
                accept = MediaType("application", "vnd.sensor.collector.v1+json")
                contentType = MediaType.APPLICATION_JSON
                header("X-API-KEY", "abc")
                content = """{"timestamp": "2020-12-08T21:24:25Z", "temperature": 21.3, "humidity": 55.3}"""
            }.andExpect {
                status { isNoContent() }
            }

            val results = influxDB.query(Query("""SELECT * FROM temp""")).convertResult()
            expectThat(results).containsExactly(
                mapOf(
                    "time" to "2020-12-08T21:24:25Z",
                    "location" to "location1",
                    "temperature" to 21.3,
                    "humidity" to 55.3
                )
            )
        }

        @Test
        fun `Writes air quality measurement to InfluxDB`() {
            mockMvc.post("/measurement/air-quality") {
                accept = MediaType("application", "vnd.sensor.collector.v1+json")
                contentType = MediaType.APPLICATION_JSON
                header("X-API-KEY", "def")
                content = """{"timestamp": "2020-12-09T21:47:32Z", "pm25": 5.5, "pm10": 10.2}"""
            }.andExpect {
                status { isNoContent() }
            }

            val results = influxDB.query(Query("""SELECT * FROM aqi""")).convertResult()
            expectThat(results).containsExactly(
                mapOf(
                    "time" to "2020-12-09T21:47:32Z",
                    "location" to "location2",
                    "pm25" to 5.5,
                    "pm10" to 10.2
                )
            )
        }

        private fun QueryResult.convertResult() = results.flatMap {
            it.series.flatMap { series ->
                series.values.map { value ->
                    series.columns.zip(value).toMap()
                }
            }
        }
    }
}
