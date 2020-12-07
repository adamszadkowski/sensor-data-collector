package info.szadkowski.sensor.data.collector.domain

import info.szadkowski.sensor.data.collector.domain.model.TaggedMeasurement

interface MeasurementRepository {
    fun write(measurement: TaggedMeasurement)
}
