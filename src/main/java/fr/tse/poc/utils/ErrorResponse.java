package fr.tse.poc.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiResponse;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "Error Response Model")
public class ErrorResponse {

    private String errorCode;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

}
