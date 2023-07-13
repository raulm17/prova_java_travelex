package br.com.confidencecambio.javabasico.core.usecases;

import br.com.confidencecambio.javabasico.core.enums.IMCClassification;
import br.com.confidencecambio.javabasico.core.models.requests.IMCRequest;
import br.com.confidencecambio.javabasico.core.models.responses.IMCResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IMCCalculateUseCase {
    private static final Logger logger = LoggerFactory.getLogger(IMCCalculateUseCase.class);
    public static IMCResponse execute(IMCRequest request) {
        logger.debug("Calculating IMC");
        var imc = request.getWeight() / (request.getHeight() * request.getHeight());
        var classification = IMCClassification.getIMCClassification(imc);
        var message = COMORBIDITY_IS + classification.getRiskOfComorbidity();
        logger.debug("IMC calculated");
        return new IMCResponse(imc, classification, message);
    }

    private static final String COMORBIDITY_IS = "The risk of comorbidity is: ";
}
