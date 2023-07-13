package br.com.confidencecambio.javabasico.core.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public final class DResponse<T> {
    private final boolean hasError;
    private final List<Error> errors;
    private final T data;

    private DResponse(T data) {
        this.hasError = false;
        this.errors = Collections.emptyList();
        this.data = data;
    }

    private DResponse(List<Error> errors, T data) {
        this.hasError = true;
        this.errors = Collections.unmodifiableList(errors);
        this.data = data;
    }

    public static <T> DResponse<T> ok(T data) {
        return new DResponse<>(data);
    }

    public static <T> DResponse<T> error(List<Error> errors) {
        return new DResponse<>(errors, null);
    }

    @AllArgsConstructor
    @Data
    public static class Error {
        private String code;
        private String description;
    }
}
