package info.szadkowski.sensor.data.collector.api.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class TemperatureMeasurementDto @JsonCreator constructor(
    val timestamp: Instant,
    @JsonProperty(required = true) val temperature: Double,
    @JsonProperty(required = true) val humidity: Double
)
