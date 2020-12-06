package info.szadkowski.sensor.data.collector.infrastructure.property

import info.szadkowski.sensor.data.collector.domain.SensorRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PropertyConfiguration {

    @Bean
    fun propertySensorRepository(): SensorRepository =
        PropertySensorRepository()
}
