package br.com.confidencecambio.javabasico.core.enums;

import br.com.confidencecambio.javabasico.core.exceptions.IMCClassificationNotFoundException;

public enum IMCClassification {
    UNDERWEIGHT("Low (but increased risk of other clinical problems)", 0.0, 18.5),
    HEALTHY("Average", 18.5, 25.0),
    OVERWEIGHT("Increased", 25.0, 30.0),
    OBESE_I("Moderate", 30.0, 35.0),
    OBESE_II("Severe", 35.0, 40.0),
    OBESE_III("Very severe", 40.0, 999.0);

    private final String riskOfComorbidity;
    private final Double minIMC;
    private final Double maxIMC;

    IMCClassification(String riskOfComorbidity, Double minIMC, Double maxIMC) {
        this.riskOfComorbidity = riskOfComorbidity;
        this.minIMC = minIMC;
        this.maxIMC = maxIMC;
    }

    public static IMCClassification getIMCClassification(Double imc) {
        for (IMCClassification imcClassification : IMCClassification.values()) {
            if (imc >= imcClassification.minIMC && imc < imcClassification.maxIMC) {
                return imcClassification;
            }
        }
        throw new IMCClassificationNotFoundException();
    }

    public String getRiskOfComorbidity() {
        return riskOfComorbidity;
    }
}
