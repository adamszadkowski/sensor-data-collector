package info.szadkowski.sensor.data.collector.api

import info.szadkowski.sensor.data.collector.domain.MeasurementRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/measurement")
class MeasurementEndpoint(
    private val measurementRepository: MeasurementRepository
) {

    @PostMapping(produces = ["application/vnd.sensor.collector.v1+json"])
    fun writeMeasurement() {
        measurementRepository.write()
    }
}
