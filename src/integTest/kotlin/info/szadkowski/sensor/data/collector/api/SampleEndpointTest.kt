package info.szadkowski.sensor.data.collector.api

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class SampleEndpointTest(
    @Autowired val mockMvc: MockMvc
) {

    @Test
    fun `Get sample text`() {
        mockMvc.get("/sample").andExpect {
            status { is2xxSuccessful() }
            content { string("text") }
        }
    }
}
