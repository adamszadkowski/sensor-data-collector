package info.szadkowski.sensor.data.collector.api

import info.szadkowski.sensor.data.collector.api.model.AirQualityMeasurementDto
import info.szadkowski.sensor.data.collector.api.model.TemperatureMeasurementDto
import info.szadkowski.sensor.data.collector.domain.MeasurementService
import info.szadkowski.sensor.data.collector.domain.model.AirQualityMeasurement
import info.szadkowski.sensor.data.collector.domain.model.TemperatureMeasurement
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(
    path = ["/measurement"],
    produces = ["application/vnd.sensor.collector.v1+json"]
)
class MeasurementEndpoint(
    private val measurementService: MeasurementService
) {

    @PostMapping(
        path = ["/temperature"],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun writeTemperatureMeasurement(
        @RequestHeader("X-API-KEY") apiKey: String,
        @RequestBody measurement: TemperatureMeasurementDto
    ) {
        measurementService.write(apiKey, measurement.toDomain(), measurement.timestamp)
    }

    @PostMapping(
        path = ["/air-quality"],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun writeAirQualityMeasurement(
        @RequestHeader("X-API-KEY") apiKey: String,
        @RequestBody measurement: AirQualityMeasurementDto
    ) {
        measurementService.write(apiKey, measurement.toDomain(), measurement.timestamp)
    }

    private fun TemperatureMeasurementDto.toDomain() = TemperatureMeasurement(
        temperature = temperature,
        humidity = humidity
    )

    private fun AirQualityMeasurementDto.toDomain() = AirQualityMeasurement(
        pm25 = pm25,
        pm10 = pm10
    )
}
