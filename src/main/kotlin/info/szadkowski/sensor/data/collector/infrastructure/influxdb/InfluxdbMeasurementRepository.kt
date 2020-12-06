package info.szadkowski.sensor.data.collector.infrastructure.influxdb

import info.szadkowski.sensor.data.collector.domain.MeasurementRepository
import info.szadkowski.sensor.data.collector.domain.model.Measurement

class InfluxdbMeasurementRepository(
    private val influxdbClient: InfluxdbClient
) : MeasurementRepository {
    override fun write(measurement: Measurement) {
        influxdbClient.write("""temperature=${measurement.temperature}""").execute()
    }
}
