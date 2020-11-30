package org.ost.investigate.springboot.demo.service.exceptions;

import lombok.Data;

@Data
public class MyCustomConnectionException extends Exception{
    private String status;
    private String message;
    public MyCustomConnectionException(String status,String message){
        this.status = status;
        this.message = message;
    }
}
