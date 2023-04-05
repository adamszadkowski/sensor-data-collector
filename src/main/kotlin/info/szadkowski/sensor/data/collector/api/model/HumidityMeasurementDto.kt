package info.szadkowski.sensor.data.collector.api.model

import com.fasterxml.jackson.annotation.JsonCreator
import java.time.Instant

data class HumidityMeasurementDto @JsonCreator constructor(
    val timestamp: Instant,
    val humidity: Double,
)
