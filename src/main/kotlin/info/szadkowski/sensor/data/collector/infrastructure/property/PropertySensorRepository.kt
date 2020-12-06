package info.szadkowski.sensor.data.collector.infrastructure.property

import info.szadkowski.sensor.data.collector.domain.SensorRepository
import info.szadkowski.sensor.data.collector.domain.model.Sensor

class PropertySensorRepository : SensorRepository {
    override fun fetch(): Sensor {
        return Sensor("livingroom")
    }
}
