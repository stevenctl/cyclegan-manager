package com.slandow.cycleganmanager.util

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class ResponseUtil {

    static ResponseEntity responseEntity(HttpStatus status, String message) {
        def body = [
                timestamp: new Date().toString(),
                status   : status.value(),
                message  : message
        ]

        if(status.isError()){
            body.put("error", humanReadable(status.name()))
        }

        return ResponseEntity.status(status).body(body)
    }

    static ResponseEntity notFound(String message) {
        return responseEntity(HttpStatus.NOT_FOUND, message)
    }

    static ResponseEntity badRequest(String message) {
        return responseEntity(HttpStatus.BAD_REQUEST, message)
    }

    static ResponseEntity ok(String message) {
        return responseEntity(HttpStatus.OK, message)
    }

    private static String humanReadable(String errorName){
        return errorName
                .split("_")
                .collect({word -> word.toLowerCase()})
                .collect({word -> word.substring(0,1).toUpperCase() + word.substring(1)})
                .join(" ")
    }

}
