package info.szadkowski.sensor.data.collector

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.VerificationException
import org.springframework.test.context.TestContext
import org.springframework.test.context.TestExecutionListener

class WireMockTestExecutionListener : TestExecutionListener {
    override fun afterTestMethod(testContext: TestContext) =
        testContext.applicationContext.getBean(WireMockServer::class.java)
            .validateWireMock()

    private fun WireMockServer.validateWireMock() {
        val unmatchedRequests = findAllUnmatchedRequests()
        if (unmatchedRequests.isNotEmpty()) {
            val nearMisses = findNearMissesForAllUnmatchedRequests()
            throw when {
                nearMisses.isNotEmpty() -> VerificationException.forUnmatchedNearMisses(nearMisses)
                else -> VerificationException.forUnmatchedRequests(unmatchedRequests)
            }
        }
    }
}
