package info.szadkowski.sensor.data.collector.api.model

import com.fasterxml.jackson.annotation.JsonCreator
import java.time.Instant

data class TemperatureMeasurementDto @JsonCreator constructor(
    val timestamp: Instant,
    val temperature: Double?,
    @Deprecated(message = "use other endpoint") val humidity: Double?,
)
