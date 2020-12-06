package info.szadkowski.sensor.data.collector.api

import info.szadkowski.sensor.data.collector.api.model.MeasurementDto
import info.szadkowski.sensor.data.collector.domain.MeasurementRepository
import info.szadkowski.sensor.data.collector.domain.SensorRepository
import info.szadkowski.sensor.data.collector.domain.model.Measurement
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/measurement")
class MeasurementEndpoint(
    private val measurementRepository: MeasurementRepository,
    private val sensorRepository: SensorRepository
) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = ["application/vnd.sensor.collector.v1+json"])
    fun writeMeasurement(@RequestBody request: MeasurementDto) {
        val sensor = sensorRepository.fetch()
        measurementRepository.write(request.toDomain(sensor.location))
    }

    private fun MeasurementDto.toDomain(location: String) = Measurement(
        temperature = temperature,
        location = location
    )
}
