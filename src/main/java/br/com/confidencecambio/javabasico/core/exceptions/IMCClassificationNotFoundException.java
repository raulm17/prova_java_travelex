package br.com.confidencecambio.javabasico.core.exceptions;

public class IMCClassificationNotFoundException extends BaseException {
    @Override
    public String getCodeKey() {
        return "error.imc.classification.code";
    }

    @Override
    public String getStatusCodeKey() {
        return "error.imc.classification.statuscode";
    }

    @Override
    public String getDescriptionKey() {
        return "error.imc.classification.description";
    }
}
