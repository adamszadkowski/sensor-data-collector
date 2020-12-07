package info.szadkowski.sensor.data.collector.domain

import info.szadkowski.sensor.data.collector.domain.model.Measurement
import info.szadkowski.sensor.data.collector.domain.model.TaggedMeasurement

class MeasurementService(
    private val measurementRepository: MeasurementRepository,
    private val sensorRepository: SensorRepository
) {

    fun write(apiKey: String, measurement: Measurement) {
        val sensor = sensorRepository.fetch(apiKey)
        val taggedMeasurement = TaggedMeasurement(
            measurement = measurement,
            location = sensor.location
        )
        measurementRepository.write(taggedMeasurement)
    }
}
