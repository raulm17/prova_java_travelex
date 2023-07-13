package br.com.confidencecambio.javabasico.application.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    @Bean(name = {"objectMapper", "mapper", "jsonMapper"})
    public ObjectMapper mapper() {
        return Jackson2ObjectMapperBuilder.json()
                .modules(new JavaTimeModule())
                .featuresToDisable(
                        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
                )
                .featuresToEnable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .simpleDateFormat(SIMPLE_DATE_FORMAT)
                .serializers(
                        new LocalDateSerializer(DateTimeFormatter.ISO_DATE),
                        new LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME)
                )
                .deserializers(
                        new LocalDateDeserializer(DateTimeFormatter.ISO_DATE),
                        new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME)
                )
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .build();

    }

    private static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd";
}
