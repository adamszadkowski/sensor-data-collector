package info.szadkowski.sensor.data.collector.api

import info.szadkowski.sensor.data.collector.domain.WriteFailedException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(WriteFailedException::class)
    @ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
    fun handleWriteException() {
        // do nothing
    }
}
