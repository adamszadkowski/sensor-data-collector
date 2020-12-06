package info.szadkowski.sensor.data.collector.infrastructure.influxdb

import info.szadkowski.sensor.data.collector.domain.MeasurementRepository

class InfluxdbMeasurementRepository(
    private val influxdbClient: InfluxdbClient
) : MeasurementRepository {
    override fun write() {
        influxdbClient.write().execute()
    }
}
