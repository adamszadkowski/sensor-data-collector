package info.szadkowski.sensor.data.collector.api

import info.szadkowski.sensor.data.collector.api.model.MeasurementDto
import info.szadkowski.sensor.data.collector.domain.MeasurementRepository
import info.szadkowski.sensor.data.collector.domain.model.Measurement
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/measurement")
class MeasurementEndpoint(
    private val measurementRepository: MeasurementRepository
) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = ["application/vnd.sensor.collector.v1+json"])
    fun writeMeasurement(@RequestBody request: MeasurementDto) {
        measurementRepository.write(request.toDomain())
    }

    private fun MeasurementDto.toDomain() = Measurement(
        temperature = temperature
    )
}
