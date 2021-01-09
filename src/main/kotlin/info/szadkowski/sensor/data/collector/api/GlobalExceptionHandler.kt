package info.szadkowski.sensor.data.collector.api

import info.szadkowski.sensor.data.collector.domain.MissingApiKeyException
import info.szadkowski.sensor.data.collector.domain.WriteFailedException
import info.szadkowski.sensor.data.collector.infrastructure.logger
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import javax.servlet.http.HttpServletResponse

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(WriteFailedException::class)
    @ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
    fun handleWriteException() {
        // do nothing
    }

    @ExceptionHandler(MissingApiKeyException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST, reason = "Incorrect request header 'X-API-KEY'")
    fun handleMissingApiKeyException(e: MissingApiKeyException) {
        log.error("Cannot find api key", e)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        e: MethodArgumentNotValidException,
        httpServletResponse: HttpServletResponse
    ) {
        httpServletResponse.sendError(400, e.fieldErrors.joinToString(separator = ", ") { it.defaultMessage ?: "" })
    }

    companion object {
        val log = logger()
    }
}
