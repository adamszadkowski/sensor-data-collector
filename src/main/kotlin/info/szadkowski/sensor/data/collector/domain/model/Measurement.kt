package info.szadkowski.sensor.data.collector.domain.model

sealed class Measurement

data class TemperatureMeasurement(
    val temperature: Double
) : Measurement()
