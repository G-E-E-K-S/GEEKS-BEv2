package com.my_geeks.geeks.swagger;

import com.my_geeks.geeks.exception.ErrorCode;
import com.my_geeks.geeks.swagger.annotation.ApiErrorResponse;
import com.my_geeks.geeks.swagger.annotation.ApiErrorResponses;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.SneakyThrows;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        String jwt = "Bearer Token";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")
                //.scheme("cookie")
                //.bearerFormat("JWT")
        );

        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .addSecurityItem(securityRequirement)
                .components(components);
    }
    private Info apiInfo() {
        return new Info()
                .title("GEEKS API") // API의 제목
                .description("Swagger UI") // API에 대한 설명
                .version("1.0.0"); // API의 버전
    }

    @Bean
    public OperationCustomizer operationCustomizer() {
        return ((operation, handlerMethod) -> {
            this.addResponseBodyWrapperSchemaExample(operation, String.class, "data");

            ApiErrorResponses apiErrorResponses = handlerMethod.getMethodAnnotation(ApiErrorResponses.class);

            if(apiErrorResponses != null) {
                generateErrorResponseExample(operation, apiErrorResponses.value());
            } else {
                ApiErrorResponse apiErrorResponse = handlerMethod.getMethodAnnotation(ApiErrorResponse.class);

                if(apiErrorResponse != null) {
                    generateErrorResponseExample(operation, apiErrorResponse.value());
                }
            }
            return operation;
        });
    }

    private void generateErrorResponseExample(Operation operation, ErrorCode[] errorCodes) {
        ApiResponses responses = operation.getResponses();

        Map<Integer, List<ExampleHolder>> statusExampleHolders = Arrays.stream(errorCodes)
                .map(
                        errorCode -> ExampleHolder.builder()
                                .holder(getExample(errorCode))
                                .name(errorCode.toString())
                                .code(errorCode.getCode())
                                .message(errorCode.getMessage())
                                .build()
                )
                .collect(Collectors.groupingBy(ExampleHolder::getCode));

        addExamplesToResponses(responses, statusExampleHolders);
    }

    private void generateErrorResponseExample(Operation operation, ErrorCode errorCode) {
        ApiResponses responses = operation.getResponses();

        ExampleHolder exampleHolder = ExampleHolder.builder()
                .holder(getExample(errorCode))
                .name(errorCode.toString())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        addExamplesToResponses(responses, exampleHolder);
    }

    private void addExamplesToResponses(ApiResponses apiResponses,
                                        Map<Integer, List<ExampleHolder>> statusExampleHolders) {
        statusExampleHolders.forEach(
                (status, v) -> {
                    Content content = new Content();
                    MediaType mediaType = new MediaType();
                    ApiResponse apiResponse = new ApiResponse();

                    v.forEach(
                            exampleHolder -> mediaType.addExamples(
                                    exampleHolder.getName(),
                                    exampleHolder.getHolder()
                            )
                    );

                    content.addMediaType("application/json", mediaType);
                    apiResponse.setContent(content);
                    apiResponses.addApiResponse(String.valueOf(status), apiResponse);
                }
        );
    }

    private void addExamplesToResponses(ApiResponses apiResponses, ExampleHolder exampleHolder) {
        Content content = new Content();
        MediaType mediaType = new MediaType();
        ApiResponse apiResponse = new ApiResponse();

        mediaType.addExamples(exampleHolder.getName(), exampleHolder.getHolder());
        content.addMediaType("application/json", mediaType);
        apiResponse.setContent(content);
        apiResponses.addApiResponse(String.valueOf(exampleHolder.getCode()), apiResponse);
    }


    private Example getExample(ErrorCode errorCode) {
        ErrorDto errorDto = ErrorDto.from(errorCode);
        Example example = new Example();
        example.setValue(errorDto);
        return example;
    }


    private void addResponseBodyWrapperSchemaExample(Operation operation, Class<?> type, String wrapFieldName) {
        final Content content = operation.getResponses().get("200").getContent();

        if (content != null) {
            content.keySet()
                    .forEach(mediaTypeKey -> {
                        final MediaType mediaType = content.get(mediaTypeKey);
                        mediaType.schema(wrapSchema(mediaType.getSchema(), type, wrapFieldName));
                    });
        }
    }

    @SneakyThrows
    private <T> Schema<T> wrapSchema(Schema<?> originalSchema, Class<T> type, String wrapFieldName) {
        final Schema<T> wrapperSchema = new Schema<>();

        wrapperSchema.addProperty("httpstatus", new Schema<>().type("String").example("OK"));
        wrapperSchema.addProperty("success", new Schema<>().type("boolean").example(true));
        wrapperSchema.addProperty("data", originalSchema);
        wrapperSchema.addProperty("error", new Schema<>().type("boolean").example(null));
//        final T instance = type.getDeclaredConstructor().newInstance();
//
//        for (Field field : type.getDeclaredFields()) {
//            field.setAccessible(true);
//            wrapperSchema.addProperty(field.getName(), new Schema<>().example(field.get(instance)));
//            field.setAccessible(false);
//        }
//
//        wrapperSchema.addProperty(wrapFieldName, originalSchema);
        return wrapperSchema;
    }
}
