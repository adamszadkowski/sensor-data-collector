package info.szadkowski.sensor.data.collector.domain

import info.szadkowski.sensor.data.collector.domain.model.AirPressureMeasurement
import info.szadkowski.sensor.data.collector.domain.model.AirQualityMeasurement
import info.szadkowski.sensor.data.collector.domain.model.Tags
import info.szadkowski.sensor.data.collector.domain.model.TemperatureMeasurement
import java.time.Instant

class MeasurementService(
    private val measurementRepository: MeasurementRepository,
    private val sensorRepository: SensorRepository
) {

    fun write(apiKey: String, measurement: TemperatureMeasurement, timestamp: Instant) {
        measurementRepository.write(
            measurement = measurement,
            tags = loadTags(apiKey),
            timestamp = timestamp
        )
    }

    fun write(apiKey: String, measurement: AirQualityMeasurement, timestamp: Instant) {
        measurementRepository.write(
            measurement = measurement,
            tags = loadTags(apiKey),
            timestamp = timestamp
        )
    }

    fun write(apiKey: String, measurement: AirPressureMeasurement, timestamp: Instant) {
        measurementRepository.write(
            measurement = measurement,
            tags = loadTags(apiKey),
            timestamp = timestamp
        )
    }

    private fun loadTags(apiKey: String) = Tags(location = sensorRepository.fetch(apiKey).location)
}
