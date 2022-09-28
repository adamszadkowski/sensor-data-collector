package info.szadkowski.sensor.data.collector.domain

import info.szadkowski.sensor.data.collector.domain.model.AirPressureMeasurement
import info.szadkowski.sensor.data.collector.domain.model.AirQualityMeasurement
import info.szadkowski.sensor.data.collector.domain.model.Tags
import info.szadkowski.sensor.data.collector.domain.model.TemperatureMeasurement
import java.time.Instant

interface MeasurementRepository {
    fun write(measurement: TemperatureMeasurement, tags: Tags, timestamp: Instant)
    fun write(measurement: AirQualityMeasurement, tags: Tags, timestamp: Instant)
    fun write(measurement: AirPressureMeasurement, tags: Tags, timestamp: Instant)
}
