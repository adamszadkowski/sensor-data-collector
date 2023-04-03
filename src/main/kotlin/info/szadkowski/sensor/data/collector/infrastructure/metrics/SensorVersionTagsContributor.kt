package info.szadkowski.sensor.data.collector.infrastructure.metrics

import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.Tags
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.actuate.metrics.web.servlet.WebMvcTagsContributor

class SensorVersionTagsContributor : WebMvcTagsContributor {
    override fun getTags(
        request: HttpServletRequest,
        response: HttpServletResponse?,
        handler: Any?,
        exception: Throwable?,
    ) = Tags.of(
        Tag.of("sensor-version", request.getHeader("Sensor-Version") ?: "unknown"),
        Tag.of("sensor-type", request.getHeader("Sensor-Type") ?: "unknown"),
    )

    override fun getLongRequestTags(request: HttpServletRequest?, handler: Any?) = Tags.empty()
}
