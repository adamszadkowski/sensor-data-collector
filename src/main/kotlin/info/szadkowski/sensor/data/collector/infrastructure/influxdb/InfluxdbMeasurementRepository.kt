package info.szadkowski.sensor.data.collector.infrastructure.influxdb

import info.szadkowski.sensor.data.collector.domain.MeasurementRepository
import info.szadkowski.sensor.data.collector.domain.model.Tags
import info.szadkowski.sensor.data.collector.domain.model.TemperatureMeasurement
import java.time.Instant

class InfluxdbMeasurementRepository(
    private val influxdbClient: InfluxdbClient,
    private val properties: InfluxdbProperties
) : MeasurementRepository {
    override fun write(measurement: TemperatureMeasurement, tags: Tags, timestamp: Instant) {
        influxdbClient.write("${properties.measurements.temperature},location=${tags.location} ${measurement.format()} ${timestamp.epochSecond}")
            .execute()
    }

    private fun TemperatureMeasurement.format() = "temperature=$temperature,humidity=$humidity"
}
