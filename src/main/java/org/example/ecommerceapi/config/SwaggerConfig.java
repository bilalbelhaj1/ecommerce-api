package org.example.ecommerceapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author $(bilal belhaj)
 **/
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-commerce Api")
                        .description("E-commerce api for where the admin can manage products, categories orders, and customer can place orders")
                        .version("v1.0")
                );
    }
}
