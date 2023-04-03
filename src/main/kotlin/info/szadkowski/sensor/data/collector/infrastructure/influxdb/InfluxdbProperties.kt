package info.szadkowski.sensor.data.collector.infrastructure.influxdb

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties("influxdb")
data class InfluxdbProperties @ConstructorBinding constructor(
    val url: String,
    val database: String,
    val username: String,
    val password: String,
    val measurements: Measurements
) {
    data class Measurements(
        val temperature: String,
        val airQuality: String,
        val airPressure: String,
    )
}
