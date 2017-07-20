package io.github.isaacbao.scaffold;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * api文档自动生成工具
 * Created by rongyang_lu on 2017/7/17.
 */
@Configuration
@EnableSwagger2
public class Swagger2 {

    @Value("${io.github.isaacbao.scaffold.swagger.enable}")
    private boolean enable;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(enable)
                .select()
                .apis(RequestHandlerSelectors.basePackage("io.github.isaacbao.scaffold"))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("API文档:").append("\n")
                .append("本文档内的接口，如无意外都统一只接受 → application/xml;charset=UTF-8 ← 这个类型的参数").append("\n");

        return new ApiInfoBuilder()
                .title("API文档")
                .description(sb.toString())
                .termsOfServiceUrl("http://localhost/")
                .contact("Lou")
                .version("0.0")
                .build();
    }

}
