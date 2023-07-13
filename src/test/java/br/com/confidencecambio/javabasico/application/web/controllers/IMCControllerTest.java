package br.com.confidencecambio.javabasico.application.web.controllers;

import br.com.confidencecambio.javabasico.JavaBasicoApplication;
import br.com.confidencecambio.javabasico.core.enums.IMCClassification;
import br.com.confidencecambio.javabasico.core.models.requests.IMCRequest;
import br.com.confidencecambio.javabasico.core.models.responses.DResponse;
import br.com.confidencecambio.javabasico.core.models.responses.IMCResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JavaBasicoApplication.class)
@EnableAutoConfiguration(exclude = {
        ErrorMvcAutoConfiguration.class
})
public class IMCControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @ParameterizedTest
    @MethodSource("provideValidIMCRequest")
    public void shouldReturnOkWhenValidIMCRequest(IMCRequest request, DResponse<IMCResponse> expectedResponse) throws Exception {
        var post = MockMvcRequestBuilders.post("/imc")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request));
        var result = mockMvc.perform(post).andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());
        Assertions.assertEquals(objectMapper.writeValueAsString(expectedResponse), result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidIMCRequest")
    public void shouldReturnUnprocessableEntityWhenInvalidIMCRequest(IMCRequest request) throws Exception {
        var post = MockMvcRequestBuilders.post("/imc")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request));
        var result = mockMvc.perform(post).andReturn();

        Assertions.assertEquals(422, result.getResponse().getStatus());
        Assertions.assertEquals(objectMapper.writeValueAsString(DResponse.error(INVALID_CLASSIFICATION_ERROR)), result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidInputsIMCRequest")
    public void shouldReturnBadRequestWhenInvalidInputsIMCRequest(IMCRequest request, DResponse expectedResponse) throws Exception {
        var post = MockMvcRequestBuilders.post("/imc")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request));
        var result = mockMvc.perform(post).andReturn();
        var response =
                objectMapper.readValue(result.getResponse().getContentAsString(), DResponse.class);
        Assertions.assertEquals(400, result.getResponse().getStatus());
        Assertions.assertTrue(response.getErrors().containsAll(expectedResponse.getErrors()));
    }

    public static Stream<Arguments> provideValidIMCRequest() {
        return Stream.of(
                Arguments.of(
                        new IMCRequest(1.80, 80.3),
                        DResponse.ok(new IMCResponse(24.78395061728395, IMCClassification.HEALTHY, "The risk of comorbidity is: Average"))
                ),
                Arguments.of(
                        new IMCRequest(1.73, 95.0),
                        DResponse.ok(new IMCResponse(31.741788900397605, IMCClassification.OBESE_I, "The risk of comorbidity is: Moderate"))
                ),
                Arguments.of(
                        new IMCRequest(1.71, 110.9),
                        DResponse.ok(new IMCResponse(37.9261995143805, IMCClassification.OBESE_II, "The risk of comorbidity is: Severe"))
                ),
                Arguments.of(
                        new IMCRequest(1.65, 130.0),
                        DResponse.ok(new IMCResponse(47.75022956841139, IMCClassification.OBESE_III, "The risk of comorbidity is: Very severe"))
                ),
                Arguments.of(
                        new IMCRequest(1.75, 50.0),
                        DResponse.ok(new IMCResponse(16.3265306122449, IMCClassification.UNDERWEIGHT, "The risk of comorbidity is: Low (but increased risk of other clinical problems)"))
                ),
                Arguments.of(
                        new IMCRequest(1.68, 75.3),
                        DResponse.ok(new IMCResponse(26.679421768707485, IMCClassification.OVERWEIGHT, "The risk of comorbidity is: Increased"))
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

    public static Stream<Arguments> provideInvalidInputsIMCRequest() {
        return Stream.of(
                Arguments.of(
                        new IMCRequest(null, 80000.3),
                        DResponse.error(INVALID_HEIGHT_ERROR_LIST)
                ),
                Arguments.of(
                        new IMCRequest(1.73, null),
                        DResponse.error(INVALID_WEIGHT_ERROR_LIST)
                ),
                Arguments.of(
                        new IMCRequest(null, 110777.9),
                        DResponse.error(INVALID_HEIGHT_ERROR_LIST)
                ),
                Arguments.of(
                        new IMCRequest(1.65, null),
                        DResponse.error(INVALID_WEIGHT_ERROR_LIST)
                ),
                Arguments.of(
                        new IMCRequest(null, null),
                        DResponse.error(INVALID_HEIGHT_AND_WEIGHT_ERROR)
                ),
                Arguments.of(
                        new IMCRequest(null, null),
                        DResponse.error(INVALID_HEIGHT_AND_WEIGHT_ERROR)
                )
        );
    }

    private static final DResponse.Error INVALID_HEIGHT_ERROR = new DResponse.Error("ERR-0002", "Height is required");
    private static final DResponse.Error INVALID_WEIGHT_ERROR = new DResponse.Error("ERR-0002", "Weight is required");
    private static final List<DResponse.Error> INVALID_HEIGHT_ERROR_LIST = Collections.singletonList(INVALID_HEIGHT_ERROR);
    private static final List<DResponse.Error> INVALID_WEIGHT_ERROR_LIST = Collections.singletonList(INVALID_WEIGHT_ERROR);
    private static final List<DResponse.Error> INVALID_HEIGHT_AND_WEIGHT_ERROR = List.of(
            INVALID_HEIGHT_ERROR,
            INVALID_WEIGHT_ERROR
    );
    private static final List<DResponse.Error> INVALID_CLASSIFICATION_ERROR = Collections.singletonList(new DResponse.Error("ERR-0003", "Found IMC value is invalid, no classification is applicable"));
}