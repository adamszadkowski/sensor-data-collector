package info.szadkowski.sensor.data.collector.api

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.VerificationException
import com.github.tomakehurst.wiremock.client.WireMock.*
import info.szadkowski.sensor.data.collector.wiremock.WireMockInitializer
import org.influxdb.InfluxDB
import org.influxdb.dto.Query
import org.influxdb.dto.QueryResult
import org.junit.jupiter.api.AfterEach
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
@ContextConfiguration(initializers = [WireMockInitializer::class])
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MeasurementEndpointTest(
    @Autowired val wireMockServer: WireMockServer,
    @Autowired val influxDB: InfluxDB,
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

    @Test
    fun `Writes temperature measurement to InfluxDB`(@Value("\${influxdb.dbUrl}") url: String) {
        // given
        wireMockServer.stubFor(post(urlMatching("/write(.*)")).willReturn(aResponse().proxiedFrom(url)))

        // when
        mockMvc.post("/measurement/temperature") {
            accept = MediaType("application", "vnd.sensor.collector.v1+json")
            contentType = MediaType.APPLICATION_JSON
            header("X-API-KEY", "abc")
            content = """{"timestamp": "2020-12-08T21:24:25Z", "temperature": 21.3, "humidity":55.3}"""
        }.andExpect {
            status { is2xxSuccessful() }
        }

        // then
        val results = influxDB.query(Query("""SELECT * FROM temp""")).convertResult()
        expectThat(results).containsExactly(
            entry("2020-12-08T21:24:25Z", "location1", 21.3, 55.3)
        )
    }

    private fun QueryResult.convertResult() = results.flatMap {
        it.series.flatMap { series ->
            series.values.map { value ->
                series.columns.zip(value).toMap()
            }
        }
    }

    private fun entry(timestamp: String, location: String, temperature: Double, humidity: Double) = mapOf(
        "time" to timestamp,
        "location" to location,
        "temperature" to temperature,
        "humidity" to humidity
    )
}
