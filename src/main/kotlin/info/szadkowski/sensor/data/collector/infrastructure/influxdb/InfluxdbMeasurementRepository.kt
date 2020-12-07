package info.szadkowski.sensor.data.collector.infrastructure.influxdb

import info.szadkowski.sensor.data.collector.domain.MeasurementRepository
import info.szadkowski.sensor.data.collector.domain.model.TaggedMeasurement
import info.szadkowski.sensor.data.collector.domain.model.TemperatureMeasurement

class InfluxdbMeasurementRepository(
    private val influxdbClient: InfluxdbClient
) : MeasurementRepository {
    override fun write(measurement: TaggedMeasurement) {
        when (measurement.measurement) {
            is TemperatureMeasurement -> {
                influxdbClient.write("""location=${measurement.location} temperature=${measurement.measurement.temperature}""")
                    .execute()
            }
        }
    }
}
