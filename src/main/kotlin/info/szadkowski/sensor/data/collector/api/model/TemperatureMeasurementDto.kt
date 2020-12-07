package info.szadkowski.sensor.data.collector.api.model

import com.fasterxml.jackson.annotation.JsonCreator

data class TemperatureMeasurementDto @JsonCreator constructor(
    val temperature: Double
)
