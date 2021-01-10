package info.szadkowski.sensor.data.collector.infrastructure.metrics

import io.micrometer.core.aop.TimedAspect
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MetricsConfiguration {

    @Bean
    fun timedAspect(registry: MeterRegistry) = TimedAspect(registry)

    @Bean
    fun sensorVersionTagsContributor() = SensorVersionTagsContributor()
}
