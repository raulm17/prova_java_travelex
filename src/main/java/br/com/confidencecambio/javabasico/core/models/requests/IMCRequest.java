package br.com.confidencecambio.javabasico.core.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IMCRequest {
    @NotNull(message = "Height is required")
    @JsonProperty
    private Double height;
    @NotNull(message = "Weight is required")
    @JsonProperty
    private Double weight;
}
