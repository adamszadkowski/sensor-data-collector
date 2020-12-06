package info.szadkowski.sensor.data.collector.api

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
@RequestMapping("/measurement")
class MeasurementEndpoint(
    @Value("\${influxdb.url}") val url: String
) {

    @PostMapping(produces = ["application/vnd.sensor.collector.v1+json"])
    fun writeMeasurement() {
        RestTemplate().postForEntity("${url}/write", null, Unit::class.java)
    }
}
