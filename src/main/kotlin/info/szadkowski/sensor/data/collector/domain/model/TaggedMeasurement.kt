package info.szadkowski.sensor.data.collector.domain.model

data class TaggedMeasurement(
    val measurement: Measurement,
    val location: String
)