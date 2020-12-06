package info.szadkowski.sensor.data.collector.api.model

import com.fasterxml.jackson.annotation.JsonCreator

data class MeasurementDto @JsonCreator constructor(
    val temperature: Double
)
