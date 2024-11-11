package com.deloitte.demo.user.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {
    private int code;
    private String message;
    private T data;
    private Object error;

    public static<T> ResponseDto success(T data) {
        return new ResponseDto(200, "success", data, null);
    }

    public static<T> ResponseDto empty() {
        return new ResponseDto(200, "success", null, null);
    }

    public static <T> ResponseDto noContent () {
        return new ResponseDto(HttpStatus.NO_CONTENT.value(), "success", null, null);
    }

    public static<T> ResponseDto error(int code, Object error) {
        return new ResponseDto(code, "error", null, error);
    }
}