package org.ost.investigate.springboot.demo.web;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.ost.investigate.springboot.demo.service.exceptions.MyCustomConnectionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    public class CustomErrorResponse {

        String errorCode;
        String errorMsg;
        int status;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
        LocalDateTime timestamp;

        public CustomErrorResponse(String errorCode, String errorMsg) {
            super();
            this.errorCode = errorCode;
            this.errorMsg = errorMsg;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }


    }

    @ExceptionHandler({Exception.class, MyCustomConnectionException.class})
    public ResponseEntity<CustomErrorResponse> handleException(Exception e) {
        System.out.println("Exception=" + e);
        System.out.println("Exception=" + e.getCause());
        if(e instanceof MyCustomConnectionException){
            System.out.println("----------MyCustomConnectionException-----------");

            CustomErrorResponse error = new CustomErrorResponse(((MyCustomConnectionException) e).getStatus(), e.getMessage());
            error.setTimestamp(LocalDateTime.now());
            error.setStatus((HttpStatus.NOT_FOUND.value()));
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

            System.out.println("----------Exception-----------");
            CustomErrorResponse error = new CustomErrorResponse("Exception", e.getMessage());
            error.setTimestamp(LocalDateTime.now());
            error.setStatus((HttpStatus.NOT_FOUND.value()));
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

    }
}

