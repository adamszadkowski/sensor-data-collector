package info.szadkowski.sensor.data.collector.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SampleEndpoint {

    @GetMapping("/sample")
    fun sample() = "text"
}
