package info.szadkowski.sensor.data.collector.domain

import info.szadkowski.sensor.data.collector.domain.model.*
import java.time.Instant

interface MeasurementRepository {
    fun write(measurement: TemperatureMeasurement, tags: Tags, timestamp: Instant)
    fun write(measurement: HumidityMeasurement, tags: Tags, timestamp: Instant)
    fun write(measurement: AirQualityMeasurement, tags: Tags, timestamp: Instant)
    fun write(measurement: AirPressureMeasurement, tags: Tags, timestamp: Instant)
}
