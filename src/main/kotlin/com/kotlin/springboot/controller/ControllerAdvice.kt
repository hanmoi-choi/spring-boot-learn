package com.kotlin.refactoring.controller

import com.kotlin.refactoring.model.error.ErrorDetails
import com.kotlin.refactoring.model.error.ProductNotFoundError
import org.joda.time.DateTime
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class ControllerAdvice {

    @ExceptionHandler(ProductNotFoundError::class)
    fun productNotFoundHandler(ex: ProductNotFoundError, request: WebRequest): ResponseEntity<Any> {
        val errorDetails = ErrorDetails(DateTime.now().toString(), ex.message, request.getDescription(false))
        return ResponseEntity(errorDetails, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun badCredentialsExceptionHandler(ex: BadCredentialsException, request: WebRequest): ResponseEntity<Any> {
        val errorDetails = ErrorDetails(DateTime.now().toString(), ex.message, request.getDescription(false))
        return ResponseEntity(errorDetails, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun accessDeniedExceptionHandler(ex: AccessDeniedException, request: WebRequest): ResponseEntity<Any> {
        val errorDetails = ErrorDetails(DateTime.now().toString(), ex.message, request.getDescription(false))
        return ResponseEntity(errorDetails, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalArgumentHandler(ex: IllegalArgumentException, request: WebRequest): ResponseEntity<Any> {
        val errorDetails = ErrorDetails(DateTime.now().toString(), ex.message, request.getDescription(false))
        return ResponseEntity(errorDetails, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(Exception::class)
    fun rootExceptionHandler(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        val errorDetails = ErrorDetails(DateTime.now().toString(), ex.message, request.getDescription(false))
        return ResponseEntity(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
