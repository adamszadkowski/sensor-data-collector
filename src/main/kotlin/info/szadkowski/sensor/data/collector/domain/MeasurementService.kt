package info.szadkowski.sensor.data.collector.domain

import info.szadkowski.sensor.data.collector.domain.model.Tags
import info.szadkowski.sensor.data.collector.domain.model.TemperatureMeasurement

class MeasurementService(
    private val measurementRepository: MeasurementRepository,
    private val sensorRepository: SensorRepository
) {

    fun write(apiKey: String, measurement: TemperatureMeasurement) {
        val sensor = sensorRepository.fetch(apiKey)
        measurementRepository.write(
            measurement = measurement,
            tags = Tags(location = sensor.location)
        )
    }
}
