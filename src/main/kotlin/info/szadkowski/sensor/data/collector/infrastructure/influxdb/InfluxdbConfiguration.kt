package info.szadkowski.sensor.data.collector.infrastructure.influxdb

import info.szadkowski.sensor.data.collector.domain.MeasurementRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

@Configuration
@EnableConfigurationProperties(InfluxdbProperties::class)
class InfluxdbConfiguration(
    @Autowired val influxdbProperties: InfluxdbProperties
) {

    @Bean
    fun influxdbClient(): InfluxdbClient =
        Retrofit.Builder()
            .baseUrl(influxdbProperties.url)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(InfluxdbClient::class.java)

    @Bean
    fun measurementRepository(influxdbClient: InfluxdbClient): MeasurementRepository =
        InfluxdbMeasurementRepository(influxdbClient, influxdbProperties)
}
