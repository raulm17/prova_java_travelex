package br.com.confidencecambio.javabasico.application.web.handlers;

import br.com.confidencecambio.javabasico.core.exceptions.BaseException;
import br.com.confidencecambio.javabasico.core.models.responses.DResponse;
import br.com.confidencecambio.javabasico.core.models.responses.InputErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
@RequiredArgsConstructor
public class GlobalErrorHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    private ResponseEntity<Object> handleGeneral(Exception ex, WebRequest request) {
        int statusCode =
                Integer.parseInt(
                        messageSource.getMessage("error.server.statuscode", new Object[]{}, Locale.getDefault())
                );
        String code = messageSource.getMessage("error.server.code", new Object[]{ex.getMessage()}, Locale.getDefault());
        String description = messageSource.getMessage(
                "error.server.description",
                new Object[]{ex.getMessage()},
                Locale.getDefault()
        );
        return handleException(code, description, ex, request, HttpStatus.valueOf(statusCode));
    }

    @ExceptionHandler(BaseException.class)
    private ResponseEntity<Object> handleBaseException(BaseException ex, WebRequest request) {
        int statusCode =
                Integer.parseInt(messageSource.getMessage(ex.getStatusCodeKey(), new Object[]{}, Locale.getDefault()));
        String code = messageSource.getMessage(ex.getCodeKey(), new Object[]{ex.getMessage()}, Locale.getDefault());
        String description =
                messageSource.getMessage(ex.getDescriptionKey(), new Object[]{ex.getMessage()}, Locale.getDefault());
        return handleException(code, description, ex, request, HttpStatus.valueOf(statusCode));
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        logger.error(ex.getMessage(), ex);
        List<String> errorList = new ArrayList<>();
        List<ObjectError> exceptionMessage = ex.getBindingResult().getAllErrors();

        for (ObjectError error : exceptionMessage) {
            errorList.add(error.getDefaultMessage());
        }
        String code = messageSource.getMessage("error.input.code", new Object[]{ex.getMessage()}, Locale.getDefault());
        int statusCode =
                Integer.parseInt(
                        messageSource.getMessage("error.input.statuscode", new Object[]{}, Locale.getDefault())
                );
        DResponse<InputErrorResponse> response =
                DResponse.error(Collections.singletonList(new DResponse.Error(code, errorList.toString())));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return handleExceptionInternal(ex, response, headers, HttpStatus.valueOf(statusCode), request);
    }

    private ResponseEntity<Object> handleException(
            String code, String message, Exception e, WebRequest request, HttpStatus status
    ) {
        return handleException(Collections.singletonList(new DResponse.Error(code, message)), e, request, status);
    }

    private ResponseEntity<Object> handleException(List<DResponse.Error> errors, Exception e, WebRequest request, HttpStatus status) {
        DResponse errorResponse = DResponse.error(errors);
        logger.error(errors, e);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return handleExceptionInternal(e, errorResponse, headers, status, request);
    }
}
