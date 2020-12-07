package info.szadkowski.sensor.data.collector.api

import info.szadkowski.sensor.data.collector.api.model.TemperatureMeasurementDto
import info.szadkowski.sensor.data.collector.domain.MeasurementService
import info.szadkowski.sensor.data.collector.domain.model.Measurement
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/measurement")
class MeasurementEndpoint(
    private val measurementService: MeasurementService
) {

    @PostMapping(
        path = ["/temperature"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = ["application/vnd.sensor.collector.v1+json"]
    )
    fun writeMeasurement(
        @RequestHeader("X-API-KEY") apiKey: String,
        @RequestBody measurement: TemperatureMeasurementDto
    ) {
        measurementService.write(apiKey, measurement.toDomain())
    }

    private fun TemperatureMeasurementDto.toDomain() = Measurement(
        temperature = temperature
    )
}
