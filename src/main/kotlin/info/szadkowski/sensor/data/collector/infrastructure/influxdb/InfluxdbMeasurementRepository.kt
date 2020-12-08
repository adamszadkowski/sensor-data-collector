package info.szadkowski.sensor.data.collector.infrastructure.influxdb

import info.szadkowski.sensor.data.collector.domain.MeasurementRepository
import info.szadkowski.sensor.data.collector.domain.model.TaggedTemperatureMeasurement

class InfluxdbMeasurementRepository(
    private val influxdbClient: InfluxdbClient,
    private val properties: InfluxdbProperties
) : MeasurementRepository {
    override fun write(measurement: TaggedTemperatureMeasurement) {
        influxdbClient.write("${properties.measurements.temperature},location=${measurement.location} ${measurement.format()}")
            .execute()
    }

    private fun TaggedTemperatureMeasurement.format() = "temperature=$temperature,humidity=$humidity"
}
