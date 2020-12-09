package info.szadkowski.sensor.data.collector.api.model

import com.fasterxml.jackson.annotation.JsonCreator
import java.time.Instant

data class AirQualityMeasurementDto @JsonCreator constructor(
    val timestamp: Instant,
    val pm25: Double,
    val pm10: Double
)
