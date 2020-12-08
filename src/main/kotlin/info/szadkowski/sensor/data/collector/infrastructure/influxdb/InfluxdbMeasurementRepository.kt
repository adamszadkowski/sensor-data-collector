package info.szadkowski.sensor.data.collector.infrastructure.influxdb

import info.szadkowski.sensor.data.collector.domain.MeasurementRepository
import info.szadkowski.sensor.data.collector.domain.model.Tags
import info.szadkowski.sensor.data.collector.domain.model.TemperatureMeasurement

class InfluxdbMeasurementRepository(
    private val influxdbClient: InfluxdbClient,
    private val properties: InfluxdbProperties
) : MeasurementRepository {
    override fun write(measurement: TemperatureMeasurement, tags: Tags) {
        influxdbClient.write("${properties.measurements.temperature},location=${tags.location} ${measurement.format()}")
            .execute()
    }

    private fun TemperatureMeasurement.format() = "temperature=$temperature,humidity=$humidity"
}
