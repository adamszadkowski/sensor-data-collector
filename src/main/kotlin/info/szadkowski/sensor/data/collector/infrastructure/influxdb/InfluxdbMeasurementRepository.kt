package info.szadkowski.sensor.data.collector.infrastructure.influxdb

import info.szadkowski.sensor.data.collector.domain.MeasurementRepository
import info.szadkowski.sensor.data.collector.domain.model.TaggedMeasurement

class InfluxdbMeasurementRepository(
    private val influxdbClient: InfluxdbClient
) : MeasurementRepository {
    override fun write(measurement: TaggedMeasurement) {
        influxdbClient.write("""location=${measurement.location} temperature=${measurement.temperature}""").execute()
    }
}
