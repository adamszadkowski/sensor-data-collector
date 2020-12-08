package info.szadkowski.sensor.data.collector.domain.model

data class TaggedTemperatureMeasurement(
    val temperature: Double,
    val humidity: Double,
    val location: String
)
