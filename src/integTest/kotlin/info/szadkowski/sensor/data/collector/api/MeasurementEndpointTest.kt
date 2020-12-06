package info.szadkowski.sensor.data.collector.api

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import info.szadkowski.sensor.data.collector.wiremock.WireMockInitializer
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.client.RestTemplate
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@SpringBootTest
@ContextConfiguration(initializers = [WireMockInitializer::class])
@AutoConfigureMockMvc
class MeasurementEndpointTest(
    @Autowired val wireMockServer: WireMockServer
) {

    @Test
    fun `Writes measurement to InfluxDB`(@Value("\${influxdb.url}") url: String) {
        // given
        wireMockServer.stubFor(
            post("/write")
                .willReturn(
                    aResponse()
                        .withStatus(204)
                )
        )

        // when
        val response = RestTemplate().postForEntity("${wireMockServer.baseUrl()}/write", null, Unit::class.java)

        // then
        expectThat(response.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
        wireMockServer.verify(1, postRequestedFor(urlPathEqualTo("/write")))
    }
}
