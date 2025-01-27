package com.my_geeks.geeks.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.SneakyThrows;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        String jwt = "Cookie Auth";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.COOKIE)
                .name("accessToken")
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
            return operation;
        });
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
