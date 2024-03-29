package info.szadkowski.sensor.data.collector.api

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import org.influxdb.InfluxDB
import org.influxdb.InfluxDBFactory
import org.influxdb.dto.Query
import org.influxdb.dto.QueryResult
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import strikt.api.expectThat
import strikt.assertions.containsExactly

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MeasurementEndpointTest(
    @Autowired val wireMockServer: WireMockServer,
    @Autowired val mockMvc: MockMvc,
) {

    @Nested
    inner class `Real InfluxDB scenarios`(
        @Autowired val influxDB: InfluxDB,
    ) {

        @BeforeEach
        fun `Delegate to InfluxDB`(@Value("\${influxdb.dbUrl}") url: String) {
            wireMockServer.stubFor(post(urlMatching("/write(.*)")).willReturn(aResponse().proxiedFrom(url)))
        }

        @AfterEach
        fun `Clear InfluxDB`(@Value("\${influxdb.dbUrl}") url: String, @Value("\${influxdb.database}") db: String) {
            InfluxDBFactory.connect(url, "admin", "admin").use {
                it.setDatabase(db)
                it.query(Query("DROP MEASUREMENT temp"))
                it.query(Query("DROP MEASUREMENT aqi"))
            }
        }

        @Test
        fun `Writes deprecated temperature measurement to InfluxDB`() {
            postTemperature(
                apiKey = "abc",
                body = """{"timestamp": "2020-12-08T21:24:25Z", "temperature": 21.3, "humidity": 55.3}""",
            ).andExpect {
                status { isNoContent() }
            }

            val results = influxDB.query(Query("""SELECT * FROM temp""")).convertResult()
            expectThat(results).containsExactly(
                mapOf(
                    "time" to "2020-12-08T21:24:25Z",
                    "location" to "location1",
                    "temperature" to 21.3,
                    "humidity" to 55.3,
                )
            )
        }

        @Test
        fun `Writes temperature measurement to InfluxDB`() {
            postTemperature(
                apiKey = "abc",
                body = """{"timestamp": "2020-12-08T21:24:25Z", "temperature": 21.3}""",
            ).andExpect {
                status { isNoContent() }
            }

            val results = influxDB.query(Query("""SELECT * FROM temp""")).convertResult()
            expectThat(results).containsExactly(
                mapOf(
                    "time" to "2020-12-08T21:24:25Z",
                    "location" to "location1",
                    "temperature" to 21.3,
                )
            )
        }

        @Test
        fun `Writes deprecated humidity measurement to InfluxDB`() {
            postTemperature(
                apiKey = "abc",
                body = """{"timestamp": "2020-12-08T21:24:25Z", "humidity": 55.3}""",
            ).andExpect {
                status { isNoContent() }
            }

            val results = influxDB.query(Query("""SELECT * FROM temp""")).convertResult()
            expectThat(results).containsExactly(
                mapOf(
                    "time" to "2020-12-08T21:24:25Z",
                    "location" to "location1",
                    "humidity" to 55.3,
                )
            )
        }

        @Test
        fun `Writes humidity measurement to InfluxDB`() {
            postHumidity(
                apiKey = "abc",
                body = """{"timestamp": "2020-12-08T21:24:25Z", "humidity": 55.3}""",
            ).andExpect {
                status { isNoContent() }
            }

            val results = influxDB.query(Query("""SELECT * FROM temp""")).convertResult()
            expectThat(results).containsExactly(
                mapOf(
                    "time" to "2020-12-08T21:24:25Z",
                    "location" to "location1",
                    "humidity" to 55.3,
                )
            )
        }

        @Test
        fun `Writes air quality measurement to InfluxDB`() {
            postAirQuality(
                apiKey = "def",
                body = """{"timestamp": "2020-12-09T21:47:32Z", "pm25": 5.5, "pm10": 10.2}""",
            ).andExpect {
                status { isNoContent() }
            }

            val results = influxDB.query(Query("""SELECT * FROM aqi""")).convertResult()
            expectThat(results).containsExactly(
                mapOf(
                    "time" to "2020-12-09T21:47:32Z",
                    "location" to "location2",
                    "pm25" to 5.5,
                    "pm10" to 10.2,
                )
            )
        }

        @Test
        fun `Writes air pressure measurement to InfluxDB`() {
            postAirPressure(
                apiKey = "def",
                body = """{"timestamp": "2022-09-28T16:52:34Z", "airPressure": 100000.0}""",
            ).andExpect {
                status { isNoContent() }
            }

            val results = influxDB.query(Query("""SELECT * FROM airpressure""")).convertResult()
            expectThat(results).containsExactly(
                mapOf(
                    "time" to "2022-09-28T16:52:34Z",
                    "location" to "location2",
                    "airPressure" to 100000.0,
                )
            )
        }

        @ParameterizedTest
        @ValueSource(
            strings = [
                """{"timestamp": "2020-12-09T21:47:32Z", "pm25": 5.5}""",
                """{"timestamp": "2020-12-09T21:47:32Z", "pm10": 10.2}""",
                """{"pm25": 5.5, "pm10": 10.2}""",
            ]
        )
        fun `Fail on missing property in air quality measurement`(body: String) {
            postAirQuality(
                apiKey = "abc",
                body = body,
            ).andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `Fail on incorrect api key in temperature measurement`() {
            postTemperature(
                apiKey = "missing",
                body = """{"timestamp": "2020-12-08T21:24:25Z", "temperature": 21.3, "humidity": 55.3}""",
            ).andExpect {
                status {
                    isBadRequest()
                    reason("Incorrect request header 'X-API-KEY'")
                }
            }
        }

        @Test
        fun `Fail on incorrect api key in air quality measurement`() {
            postAirQuality(
                apiKey = "missing",
                body = """{"timestamp": "2020-12-09T21:47:32Z", "pm25": 5.5, "pm10": 10.2}""",
            ).andExpect {
                status {
                    isBadRequest()
                    reason("Incorrect request header 'X-API-KEY'")
                }
            }
        }

        @Test
        fun `Fail on missing api key in temperature measurement`() {
            mockMvc.post("/measurement/${"temperature"}") {
                accept = mediaType
                contentType = mediaType
                content = """{}"""
            }.andExpect {
                status {
                    isBadRequest()
                    reason("Required header 'X-API-KEY' is not present.")
                }
            }
        }

        @Test
        fun `Fail on missing api key in air quality measurement`() {
            mockMvc.post("/measurement/${"air-quality"}") {
                accept = mediaType
                contentType = mediaType
                content = """{}"""
            }.andExpect {
                status {
                    isBadRequest()
                    reason("Required header 'X-API-KEY' is not present.")
                }
            }
        }

        private fun QueryResult.convertResult() = results.flatMap {
            it.series.flatMap { series ->
                series.values.map { value ->
                    series.columns.zip(value).toMap()
                }
            }
        }
    }

    @Nested
    inner class `Error cases` {

        @ParameterizedTest
        @ValueSource(
            ints = [
                500,
                404,
            ]
        )
        fun `Handle failed dependency on temperature measurement`(status: Int) {
            wireMockServer.stubFor(
                post(urlMatching("/write(.*)"))
                    .willReturn(aResponse().withStatus(status))
            )

            postTemperature(
                apiKey = "abc",
                body = """{"timestamp": "2020-12-08T21:24:25Z", "temperature": 21.3, "humidity": 55.3}""",
            ).andExpect {
                status { isFailedDependency() }
            }
        }

        @ParameterizedTest
        @ValueSource(
            ints = [
                500,
                404,
            ]
        )
        fun `Handle failed dependency on air quality measurement`(status: Int) {
            wireMockServer.stubFor(
                post(urlMatching("/write(.*)"))
                    .willReturn(aResponse().withStatus(status))
            )

            postAirQuality(
                apiKey = "abc",
                body = """{"timestamp": "2020-12-09T21:47:32Z", "pm25": 5.5, "pm10": 10.2}""",
            ).andExpect {
                status { isFailedDependency() }
            }
        }
    }

    private fun postHumidity(apiKey: String, body: String) = postMeasurement("humidity", apiKey, body)
    private fun postTemperature(apiKey: String, body: String) = postMeasurement("temperature", apiKey, body)
    private fun postAirQuality(apiKey: String, body: String) = postMeasurement("air-quality", apiKey, body)
    private fun postAirPressure(apiKey: String, body: String) = postMeasurement("air-pressure", apiKey, body)
    private fun postMeasurement(measurement: String, apiKey: String, body: String) =
        mockMvc.post("/measurement/$measurement") {
            accept = mediaType
            contentType = mediaType
            header("X-API-KEY", apiKey)
            content = body
        }

    private val mediaType = MediaType("application", "vnd.sensor.collector.v1+json")
}
