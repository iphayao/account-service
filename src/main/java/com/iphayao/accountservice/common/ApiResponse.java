package com.iphayao.accountservice.common;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiResponse<T> {
    private String status;
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(String status, T data) {
        this.status = status;
        this.data = data;
    }

    public static <T> ApiResponse<T> created(T data) {
        DataBuilder builder = created();
        return builder.data(data);
    }

    public static <T> ApiResponse<T> ok(T data) {
        DataBuilder builder = ok();
        return builder.data(data);
    }

    private static DataBuilder ok() {
        return new SimpleDataBuilder(HttpStatus.OK);
    }

    private static DataBuilder created() {
        return new SimpleDataBuilder(HttpStatus.CREATED);
    }

    public interface DataBuilder {
        <T> ApiResponse<T> data(T data);
    }

    private static class SimpleDataBuilder implements DataBuilder {
        String status;

        SimpleDataBuilder(HttpStatus status) {
            this.status = status.getReasonPhrase();
        }

        @Override
        public <T> ApiResponse<T> data(T data) {
            return new ApiResponse<>(status, data);
        }
    }


}
