package info.szadkowski.sensor.data.collector.domain

import info.szadkowski.sensor.data.collector.domain.model.AirQualityMeasurement
import info.szadkowski.sensor.data.collector.domain.model.Tags
import info.szadkowski.sensor.data.collector.domain.model.TemperatureMeasurement
import java.time.Instant

class MeasurementService(
    private val measurementRepository: MeasurementRepository,
    private val sensorRepository: SensorRepository
) {

    fun write(apiKey: String, measurement: TemperatureMeasurement, timestamp: Instant) {
        val sensor = sensorRepository.fetch(apiKey)
        measurementRepository.write(
            measurement = measurement,
            tags = Tags(location = sensor.location),
            timestamp = timestamp
        )
    }

    fun write(apiKey: String, measurement: AirQualityMeasurement, timestamp: Instant) {
        val sensor = sensorRepository.fetch(apiKey)
        measurementRepository.write(
            measurement = measurement,
            tags = Tags(location = sensor.location),
            timestamp = timestamp
        )
    }
}
