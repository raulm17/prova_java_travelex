package br.com.confidencecambio.javabasico.core.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@AllArgsConstructor
public class InputErrorResponse {
    private List<String> message;
    private String details;
    private HttpStatus status;
}
