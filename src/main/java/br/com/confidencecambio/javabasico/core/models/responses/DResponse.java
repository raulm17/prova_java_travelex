package br.com.confidencecambio.javabasico.core.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
public final class DResponse<T> {
    private final boolean hasError;
    private final List<Error> errors;
    private final T data;

    public DResponse(T data) {
        this.hasError = false;
        this.errors = Collections.emptyList();
        this.data = data;
    }

    public DResponse(List<Error> errors, T data) {
        this.hasError = true;
        this.errors = Collections.unmodifiableList(errors);
        this.data = data;
    }

    public DResponse() {
        this.hasError = false;
        this.errors = Collections.emptyList();
        this.data = null;
    }

    public static <T> DResponse<T> ok(T data) {
        return new DResponse<>(data);
    }

    public static <T> DResponse<T> error(List<Error> errors) {
        return new DResponse<>(errors, null);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Error {
        private String code;
        private String description;
    }
}
