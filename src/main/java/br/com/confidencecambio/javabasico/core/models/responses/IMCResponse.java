package br.com.confidencecambio.javabasico.core.models.responses;

import br.com.confidencecambio.javabasico.core.enums.IMCClassification;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IMCResponse {
    @JsonProperty
    private Double imc;
    @JsonProperty
    private IMCClassification classification;
    @JsonProperty
    private String message;
}
