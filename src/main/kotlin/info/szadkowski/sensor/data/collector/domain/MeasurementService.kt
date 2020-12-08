package info.szadkowski.sensor.data.collector.domain

import info.szadkowski.sensor.data.collector.domain.model.TaggedTemperatureMeasurement
import info.szadkowski.sensor.data.collector.domain.model.TemperatureMeasurement

class MeasurementService(
    private val measurementRepository: MeasurementRepository,
    private val sensorRepository: SensorRepository
) {

    fun write(apiKey: String, measurement: TemperatureMeasurement) {
        val sensor = sensorRepository.fetch(apiKey)
        val taggedMeasurement = TaggedTemperatureMeasurement(
            temperature = measurement.temperature,
            humidity = measurement.humidity,
            location = sensor.location
        )
        measurementRepository.write(taggedMeasurement)
    }
}
