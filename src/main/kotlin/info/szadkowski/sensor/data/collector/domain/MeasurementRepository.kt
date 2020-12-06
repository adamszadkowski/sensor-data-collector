package info.szadkowski.sensor.data.collector.domain

import info.szadkowski.sensor.data.collector.domain.model.Measurement

interface MeasurementRepository {
    fun write(measurement: Measurement)
}
