package com.slandow.cycleganmanager.controller

import com.slandow.cycleganmanager.util.ResponseUtil
import groovy.util.logging.Slf4j
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

import org.springframework.security.access.AccessDeniedException

@Slf4j
@ControllerAdvice
class ResponseExceptionHandler {

    @ExceptionHandler(value = AccessDeniedException)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    ResponseEntity handleUnauthorizedRequest(RuntimeException re) {
        log.warn(re.message, re)
        return ResponseUtil.unauthorized(re.localizedMessage)
    }

    @ExceptionHandler(value = [IllegalArgumentException, IllegalStateException])
    @Order(Ordered.HIGHEST_PRECEDENCE)
    ResponseEntity handleBadRequest(RuntimeException re) {
        log.warn(re.message, re)
        return ResponseUtil.badRequest(re.localizedMessage)
    }

    @ExceptionHandler(value = NoSuchElementException)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    ResponseEntity handleNotFoundRequest(RuntimeException re) {
        log.warn(re.message, re)
        return ResponseUtil.notFound(re.localizedMessage)
    }

    @ExceptionHandler(value = RuntimeException)
    @Order(Ordered.LOWEST_PRECEDENCE)
    ResponseEntity handleOtherExceptions(RuntimeException re) {
        log.error(re.message, re)
        return ResponseUtil.internalError()
    }
}