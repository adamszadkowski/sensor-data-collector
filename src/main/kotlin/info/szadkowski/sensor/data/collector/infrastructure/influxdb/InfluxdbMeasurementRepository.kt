package info.szadkowski.sensor.data.collector.infrastructure.influxdb

import info.szadkowski.sensor.data.collector.domain.MeasurementRepository
import info.szadkowski.sensor.data.collector.domain.WriteFailedException
import info.szadkowski.sensor.data.collector.domain.model.*
import io.micrometer.core.annotation.Timed
import java.time.Instant

open class InfluxdbMeasurementRepository(
    private val influxdbClient: InfluxdbClient,
    private val properties: InfluxdbProperties
) : MeasurementRepository {

    @Timed(value = "clients.influxdb.write", extraTags = ["measurement", "temperature"])
    override fun write(measurement: TemperatureMeasurement, tags: Tags, timestamp: Instant) {
        writeFormatted(properties.measurements.temperature, measurement.format(), tags, timestamp)
    }

    @Timed(value = "clients.influxdb.write", extraTags = ["measurement", "humidity"])
    override fun write(measurement: HumidityMeasurement, tags: Tags, timestamp: Instant) {
        writeFormatted(properties.measurements.temperature, measurement.format(), tags, timestamp)
    }

    @Timed(value = "clients.influxdb.write", extraTags = ["measurement", "air-quality"])
    override fun write(measurement: AirQualityMeasurement, tags: Tags, timestamp: Instant) {
        writeFormatted(properties.measurements.airQuality, measurement.format(), tags, timestamp)
    }

    @Timed(value = "clients.influxdb.write", extraTags = ["measurement", "air-pressure"])
    override fun write(measurement: AirPressureMeasurement, tags: Tags, timestamp: Instant) {
        writeFormatted(properties.measurements.airPressure, measurement.format(), tags, timestamp)
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

    private fun TemperatureMeasurement.format() = "temperature=$temperature"
    private fun HumidityMeasurement.format() = "humidity=$humidity"
    private fun AirQualityMeasurement.format() = "pm25=$pm25,pm10=$pm10"
    private fun AirPressureMeasurement.format() = "airPressure=$airPressure"
}
