package info.szadkowski.sensor.data.collector.api

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.VerificationException
import com.github.tomakehurst.wiremock.client.WireMock.*
import info.szadkowski.sensor.data.collector.wiremock.WireMockInitializer
import org.junit.jupiter.api.AfterEach
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
    fun `Writes temperature measurement to InfluxDB`() {
        // given
        wireMockServer.stubFor(
            post("/write")
                .withRequestBody(equalTo("temperature=21.3"))
                .willReturn(
                    aResponse()
                        .withStatus(204)
                )
        )

        // when
        mockMvc.post("/measurement") {
            accept = MediaType("application", "vnd.sensor.collector.v1+json")
            contentType = MediaType.APPLICATION_JSON
            content = """{"temperature": 21.3}"""
        }.andExpect {
            status { is2xxSuccessful() }
        }

        // then
        wireMockServer.verify(1, postRequestedFor(urlPathEqualTo("/write")))
    }
}
