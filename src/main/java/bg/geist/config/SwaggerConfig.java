package bg.geist.config;

import bg.geist.GeistApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Component
public class SwaggerConfig {

    /*
    SwaggerConfig.
    The Docket is a builder class that configures the generation of Swagger documentation.
    To access the REST API endpoints, we can visit this URL in our browser: http://host/v2/api-docs
    */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
          .select()
          .apis(RequestHandlerSelectors.basePackage(GeistApplication.class.getPackageName()))
          .paths(PathSelectors.any())
          .build();
    }
}
