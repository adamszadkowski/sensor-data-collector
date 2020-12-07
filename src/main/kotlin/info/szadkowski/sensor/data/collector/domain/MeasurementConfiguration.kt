package info.szadkowski.sensor.data.collector.domain

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MeasurementConfiguration {

    @Bean
    fun measurementService(
        measurementRepository: MeasurementRepository,
        sensorRepository: SensorRepository
    ): MeasurementService = MeasurementService(measurementRepository, sensorRepository)
}
