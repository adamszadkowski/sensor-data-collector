package info.szadkowski.sensor.data.collector.infrastructure.influxdb

import info.szadkowski.sensor.data.collector.domain.MeasurementRepository
import info.szadkowski.sensor.data.collector.domain.model.TaggedMeasurement
import info.szadkowski.sensor.data.collector.domain.model.TemperatureMeasurement

class InfluxdbMeasurementRepository(
    private val influxdbClient: InfluxdbClient,
    private val properties: InfluxdbProperties
) : MeasurementRepository {
    override fun write(measurement: TaggedMeasurement) {
        when (measurement.measurement) {
            is TemperatureMeasurement -> {
                influxdbClient.write("${properties.measurements.temperature},location=${measurement.location} ${measurement.measurement.format()}")
                    .execute()
            }
        }
    }

    private fun TemperatureMeasurement.format() = "temperature=$temperature,humidity=$humidity"
}
