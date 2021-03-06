package info.szadkowski.sensor.data.collector.api

import info.szadkowski.sensor.data.collector.api.model.AirQualityMeasurementDto
import info.szadkowski.sensor.data.collector.api.model.TemperatureMeasurementDto
import info.szadkowski.sensor.data.collector.domain.MeasurementService
import info.szadkowski.sensor.data.collector.domain.model.AirQualityMeasurement
import info.szadkowski.sensor.data.collector.domain.model.TemperatureMeasurement
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping(
    path = ["/measurement"],
    produces = ["application/vnd.sensor.collector.v1+json"],
    consumes = ["application/vnd.sensor.collector.v1+json"]
)
@Validated
class MeasurementEndpoint(
    private val measurementService: MeasurementService
) {

    @PostMapping(path = ["/temperature"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun writeTemperatureMeasurement(
        @RequestHeader("X-API-KEY") apiKey: String,
        @Valid @RequestBody measurement: TemperatureMeasurementDto
    ) {
        measurementService.write(apiKey, measurement.toDomain(), measurement.timestamp!!)
    }

    @PostMapping(path = ["/air-quality"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun writeAirQualityMeasurement(
        @RequestHeader("X-API-KEY") apiKey: String,
        @RequestBody measurement: AirQualityMeasurementDto
    ) {
        measurementService.write(apiKey, measurement.toDomain(), measurement.timestamp)
    }

    private fun TemperatureMeasurementDto.toDomain() = TemperatureMeasurement(
        temperature = temperature!!,
        humidity = humidity!!
    )

    private fun AirQualityMeasurementDto.toDomain() = AirQualityMeasurement(
        pm25 = pm25,
        pm10 = pm10
    )
}
