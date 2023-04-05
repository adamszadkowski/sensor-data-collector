package info.szadkowski.sensor.data.collector.infrastructure.metrics

import io.micrometer.common.KeyValue
import io.micrometer.common.KeyValues
import org.springframework.http.server.observation.ServerRequestObservationContext
import org.springframework.http.server.observation.ServerRequestObservationConvention

class SensorVersionObservationConvention : ServerRequestObservationConvention {
    override fun getName(): String = "http.server.requests"

    override fun getLowCardinalityKeyValues(context: ServerRequestObservationContext): KeyValues =
        KeyValues.of(
            KeyValue.of("sensor-version", context.carrier.getHeader("Sensor-Version") ?: "unknown"),
            KeyValue.of("sensor-type", context.carrier.getHeader("Sensor-Type") ?: "unknown"),
        )
}
