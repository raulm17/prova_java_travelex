package br.com.confidencecambio.javabasico.core.usecases;

import br.com.confidencecambio.javabasico.core.enums.IMCClassification;
import br.com.confidencecambio.javabasico.core.exceptions.IMCClassificationNotFoundException;
import br.com.confidencecambio.javabasico.core.models.requests.IMCRequest;
import br.com.confidencecambio.javabasico.core.models.responses.IMCResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class IMCCalculateUseCaseTest {

    @ParameterizedTest
    @MethodSource("provideValidIMCRequest")
    public void givenValidIMCRequest_whenCalculateIMC_thenValidIMCResponseIsReturned(IMCRequest request, IMCResponse expected) {
        // Given
        // When
        IMCResponse actual = IMCCalculateUseCase.execute(request);
        // Then
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidIMCRequest")
    public void givenInvalidIMCRequest_whenCalculateIMC_thenThrowsIMCClassificationNotFoundException(IMCRequest request) {
        // Given
        // When
        // Then
        Assertions.assertThrows(IMCClassificationNotFoundException.class, () -> IMCCalculateUseCase.execute(request));
    }

    public static Stream<Arguments> provideValidIMCRequest() {
        return Stream.of(
                Arguments.of(
                        new IMCRequest(1.80, 80.3),
                        new IMCResponse(24.78395061728395, IMCClassification.HEALTHY, "The risk of comorbidity is: Average")
                ),
                Arguments.of(
                        new IMCRequest(1.73, 95.0),
                        new IMCResponse(31.741788900397605, IMCClassification.OBESE_I, "The risk of comorbidity is: Moderate")
                ),
                Arguments.of(
                        new IMCRequest(1.71, 110.9),
                        new IMCResponse(37.9261995143805, IMCClassification.OBESE_II, "The risk of comorbidity is: Severe")
                ),
                Arguments.of(
                        new IMCRequest(1.65, 130.0),
                        new IMCResponse(47.75022956841139, IMCClassification.OBESE_III, "The risk of comorbidity is: Very severe")
                ),
                Arguments.of(
                        new IMCRequest(1.75, 50.0),
                        new IMCResponse(16.3265306122449, IMCClassification.UNDERWEIGHT, "The risk of comorbidity is: Low (but increased risk of other clinical problems)")
                ),
                Arguments.of(
                        new IMCRequest(1.68, 75.3),
                        new IMCResponse(26.679421768707485, IMCClassification.OVERWEIGHT, "The risk of comorbidity is: Increased")
                )
        );
    }

    public static Stream<Arguments> provideInvalidIMCRequest() {
        return Stream.of(
                Arguments.of(
                        new IMCRequest(1.80, 80000.3)
                ),
                Arguments.of(
                        new IMCRequest(1.73, 91115.0)
                ),
                Arguments.of(
                        new IMCRequest(1.71, 110777.9)
                ),
                Arguments.of(
                        new IMCRequest(1.65, -130.0)
                ),
                Arguments.of(
                        new IMCRequest(1.75, -50.0)
                ),
                Arguments.of(
                        new IMCRequest(1.68, -75.3)
                )
        );
    }
}