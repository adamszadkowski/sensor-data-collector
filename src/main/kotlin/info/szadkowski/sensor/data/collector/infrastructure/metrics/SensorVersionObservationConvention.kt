package info.szadkowski.sensor.data.collector.infrastructure.metrics

import io.micrometer.common.KeyValue
import io.micrometer.common.KeyValues
import org.springframework.http.server.observation.DefaultServerRequestObservationConvention
import org.springframework.http.server.observation.ServerRequestObservationContext

class SensorVersionObservationConvention : DefaultServerRequestObservationConvention() {
    override fun getLowCardinalityKeyValues(context: ServerRequestObservationContext): KeyValues =
        super.getLowCardinalityKeyValues(context).and(
            KeyValues.of(
                KeyValue.of("sensor-version", context.carrier.getHeader("Sensor-Version") ?: "unknown"),
                KeyValue.of("sensor-type", context.carrier.getHeader("Sensor-Type") ?: "unknown"),
            )
        )
}
