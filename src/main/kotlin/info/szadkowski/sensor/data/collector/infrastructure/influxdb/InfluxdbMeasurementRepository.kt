package info.szadkowski.sensor.data.collector.infrastructure.influxdb

import info.szadkowski.sensor.data.collector.domain.MeasurementRepository
import info.szadkowski.sensor.data.collector.domain.WriteFailedException
import info.szadkowski.sensor.data.collector.domain.model.AirQualityMeasurement
import info.szadkowski.sensor.data.collector.domain.model.Tags
import info.szadkowski.sensor.data.collector.domain.model.TemperatureMeasurement
import java.time.Instant

class InfluxdbMeasurementRepository(
    private val influxdbClient: InfluxdbClient,
    private val properties: InfluxdbProperties
) : MeasurementRepository {
    override fun write(measurement: TemperatureMeasurement, tags: Tags, timestamp: Instant) {
        writeFormatted(properties.measurements.temperature, measurement.format(), tags, timestamp)
    }

    override fun write(measurement: AirQualityMeasurement, tags: Tags, timestamp: Instant) {
        writeFormatted(properties.measurements.airQuality, measurement.format(), tags, timestamp)
    }

    private fun writeFormatted(name: String, formattedMeasurement: String, tags: Tags, timestamp: Instant) {
        val response = influxdbClient.write(
            db = properties.database,
            username = properties.username,
            password = properties.password,
            content = "$name,location=${tags.location} $formattedMeasurement ${timestamp.epochSecond}"
        ).execute()
        if (!response.isSuccessful) {
            throw WriteFailedException()
        }
    }

    private fun TemperatureMeasurement.format() = "temperature=$temperature,humidity=$humidity"
    private fun AirQualityMeasurement.format() = "pm25=$pm25,pm10=$pm10"
}
