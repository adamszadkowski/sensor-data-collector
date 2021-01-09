package info.szadkowski.sensor.data.collector.infrastructure

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T> T.logger(): Logger {
    val unwrapped = when {
        T::class.isCompanion -> T::class.java.enclosingClass
        else -> T::class.java
    }
    return LoggerFactory.getLogger(unwrapped)
}
