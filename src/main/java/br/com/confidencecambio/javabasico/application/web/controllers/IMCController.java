package br.com.confidencecambio.javabasico.application.web.controllers;

import br.com.confidencecambio.javabasico.core.models.requests.IMCRequest;
import br.com.confidencecambio.javabasico.core.models.responses.IMCResponse;
import br.com.confidencecambio.javabasico.core.usecases.IMCCalculateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import javax.validation.Valid;

@RestController
@RequestMapping("/imc")
@RequestScope
@RequiredArgsConstructor
@Validated
public class IMCController {

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public IMCResponse imcCalculator(@Valid @RequestBody IMCRequest request) {
        return IMCCalculateUseCase.execute(request);
    }
}
