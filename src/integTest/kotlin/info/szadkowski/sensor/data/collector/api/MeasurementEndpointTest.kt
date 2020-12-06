package info.szadkowski.sensor.data.collector.api

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.client.RestTemplate

@SpringBootTest
@AutoConfigureMockMvc
class MeasurementEndpointTest {

    @Test
    fun `Writes measurement to InfluxDB`() {
        // given
        val wireMockServer = WireMockServer(WireMockConfiguration.options().dynamicPort())
        wireMockServer.start()

        wireMockServer.stubFor(
            post("/write")
                .willReturn(
                    aResponse()
                        .withStatus(204)
                )
        )

        // when
        RestTemplate().postForObject("${wireMockServer.baseUrl()}/write", null, Unit::class.java)

        // then
        wireMockServer.verify(1, postRequestedFor(urlPathEqualTo("/write")))

        wireMockServer.stop()
    }
}
