package com.deloitte.demo.user.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResponseDto<T> extends ResponseDto<List<T>> {
    private int size;
    private int number;
    private long totalElements;
    private int totalPages;

    public PageResponseDto(int code, String message, List<T> data, Object error, int size, int number, long totalElements, int totalPages) {
        super(code, message, data, error);
        this.size = size;
        this.number = number;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public static<T> PageResponseDto success(List<T> data, int size, int number, long totalElements, int totalPages) {
        return new PageResponseDto(200, "success", data, null, size, number, totalElements, totalPages);
    }

    public static PageResponseDto error(int code, String message, Object error) {
        return new PageResponseDto(code, message, null, error, 0, 0, 0, 0);
    }
}