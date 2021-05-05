package info.szadkowski.sensor.data.collector.infrastructure.metrics

import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.Tags
import org.springframework.boot.actuate.metrics.web.servlet.WebMvcTagsContributor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class SensorVersionTagsContributor : WebMvcTagsContributor {
    override fun getTags(
        request: HttpServletRequest,
        response: HttpServletResponse?,
        handler: Any?,
        exception: Throwable?,
    ) = Tags.of(
        Tag.of("sensor-version", request.getHeader("Sensor-Version") ?: "unknown"),
    )

    override fun getLongRequestTags(request: HttpServletRequest?, handler: Any?) = Tags.empty()
}
