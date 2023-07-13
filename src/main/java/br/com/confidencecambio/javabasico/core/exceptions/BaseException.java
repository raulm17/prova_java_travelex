package br.com.confidencecambio.javabasico.core.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class BaseException extends RuntimeException {
    public BaseException(Throwable e) {
        super(e);
    }

    public abstract String getCodeKey();

    public abstract String getStatusCodeKey();

    public abstract String getDescriptionKey();
}
