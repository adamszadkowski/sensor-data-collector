package info.szadkowski.sensor.data.collector.infrastructure.property

import info.szadkowski.sensor.data.collector.domain.SensorRepository
import info.szadkowski.sensor.data.collector.domain.model.Sensor

class PropertySensorRepository(
    sensorProperties: SensorProperties
) : SensorRepository {
    private val sensorsByKey = sensorProperties.sensors.groupBy { it.apiKey }.mapValues { (_, v) -> v[0] }

    override fun fetch(apiKey: String): Sensor {
        return Sensor(sensorsByKey[apiKey]!!.location)
    }
}
