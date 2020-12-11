package info.szadkowski.sensor.data.collector.api.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class AirQualityMeasurementDto @JsonCreator constructor(
    val timestamp: Instant,
    @JsonProperty(required = true) val pm25: Double,
    @JsonProperty(required = true) val pm10: Double
)
