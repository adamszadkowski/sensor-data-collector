package info.szadkowski.sensor.data.collector.infrastructure.property

import info.szadkowski.sensor.data.collector.domain.MissingApiKeyException
import info.szadkowski.sensor.data.collector.domain.SensorRepository
import info.szadkowski.sensor.data.collector.domain.model.Sensor
import io.micrometer.core.annotation.Counted

class PropertySensorRepository(
    sensorProperties: SensorProperties
) : SensorRepository {
    private val sensorsByKey = sensorProperties.sensors
        .groupBy { it.apiKey }
        .mapValues { (_, v) -> v[0] }
        .mapValues { (_, v) -> Sensor(v.location) }

    @Counted(value = "exceptions", recordFailuresOnly = true, extraTags = ["action", "fetch-sensor"])
    override fun fetch(apiKey: String): Sensor =
        sensorsByKey[apiKey] ?: throw MissingApiKeyException("Cannot find API key $apiKey")
}
