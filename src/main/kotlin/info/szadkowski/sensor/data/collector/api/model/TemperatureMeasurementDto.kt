package info.szadkowski.sensor.data.collector.api.model

import com.fasterxml.jackson.annotation.JsonCreator
import java.time.Instant
import javax.validation.constraints.NotNull

data class TemperatureMeasurementDto @JsonCreator constructor(
    @field:NotNull(message = "missing timestamp")
    val timestamp: Instant?,
    @field:NotNull(message = "missing temperature")
    val temperature: Double?,
    @field:NotNull(message = "missing humidity")
    val humidity: Double?
)
