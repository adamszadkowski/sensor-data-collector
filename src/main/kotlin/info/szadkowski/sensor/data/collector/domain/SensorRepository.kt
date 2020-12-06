package info.szadkowski.sensor.data.collector.domain

import info.szadkowski.sensor.data.collector.domain.model.Sensor

interface SensorRepository {
    fun fetch(): Sensor
}
