package info.szadkowski.sensor.data.collector.infrastructure.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties
data class SensorProperties @ConstructorBinding constructor(
    val sensors: List<Sensor>
) {
    data class Sensor(
        val apiKey: String,
        val location: String
    )
}
