package info.szadkowski.sensor.data.collector.infrastructure.influxdb

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit

@Configuration
class InfluxdbConfiguration {

    @Bean
    fun influxdbClient(@Value("\${influxdb.url}") url: String): InfluxdbClient =
        Retrofit.Builder()
            .baseUrl(url)
            .build()
            .create(InfluxdbClient::class.java)
}
