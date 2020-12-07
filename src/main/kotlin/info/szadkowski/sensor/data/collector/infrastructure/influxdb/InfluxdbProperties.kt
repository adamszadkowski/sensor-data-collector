package info.szadkowski.sensor.data.collector.infrastructure.influxdb

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("influxdb")
data class InfluxdbProperties(
    val url: String
)
