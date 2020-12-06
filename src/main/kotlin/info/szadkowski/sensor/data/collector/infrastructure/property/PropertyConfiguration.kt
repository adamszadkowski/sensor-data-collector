package info.szadkowski.sensor.data.collector.infrastructure.property

import info.szadkowski.sensor.data.collector.domain.SensorRepository
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(SensorProperties::class)
class PropertyConfiguration {

    @Bean
    fun propertySensorRepository(sensorProperties: SensorProperties): SensorRepository =
        PropertySensorRepository(sensorProperties)
}
