package info.szadkowski.sensor.data.collector.api

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import info.szadkowski.sensor.data.collector.wiremock.WireMockInitializer
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@ContextConfiguration(initializers = [WireMockInitializer::class])
@AutoConfigureMockMvc
class MeasurementEndpointTest(
    @Autowired val wireMockServer: WireMockServer,
    @Autowired val mockMvc: MockMvc
) {

    @Test
    fun `Writes measurement to InfluxDB`() {
        // given
        wireMockServer.stubFor(
            post("/write")
                .willReturn(
                    aResponse()
                        .withStatus(204)
                )
        )

        // when
        mockMvc.post("/measurement") {
            accept = MediaType("application", "vnd.sensor.collector.v1+json")
        }.andExpect {
            status { is2xxSuccessful() }
        }

        // then
        wireMockServer.verify(1, postRequestedFor(urlPathEqualTo("/write")))
    }
}
