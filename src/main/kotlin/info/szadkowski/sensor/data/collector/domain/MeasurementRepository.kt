package info.szadkowski.sensor.data.collector.domain

import info.szadkowski.sensor.data.collector.domain.model.TaggedTemperatureMeasurement

interface MeasurementRepository {
    fun write(measurement: TaggedTemperatureMeasurement)
}
