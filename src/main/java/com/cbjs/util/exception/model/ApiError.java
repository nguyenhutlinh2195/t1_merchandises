package com.cbjs.util.exception.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ApiError {
    private Integer status;
    private HttpStatusCode title;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;

    private List<String> errors;

    public ApiError(){
        this.timestamp = LocalDateTime.now();
        this.debugMessage = "";
        this.errors = new ArrayList<>();
    }

    public ApiError(Integer status, HttpStatusCode title){
        this.status = status;
        this.title = title;
        this.timestamp = LocalDateTime.now();
        this.debugMessage = "";
        this.errors = new ArrayList<>();
    }

    public ApiError(Integer status, HttpStatusCode title, String message) {
        this.status = status;
        this.title = title;
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.debugMessage = "";
        this.errors = new ArrayList<>();
    }

    public ApiError(Integer status, HttpStatusCode title, Throwable ex) {
        this.status = status;
        this.title = title;
        this.timestamp = LocalDateTime.now();
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
        this.errors = new ArrayList<>();
    }

    public ApiError(Integer status, HttpStatusCode title, String message, Throwable ex) {
        this.status = status;
        this.title = title;
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
        this.errors = new ArrayList<>();
    }
}
