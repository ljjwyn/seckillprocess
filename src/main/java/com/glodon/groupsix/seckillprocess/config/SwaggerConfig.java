package com.glodon.groupsix.seckillprocess.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    //配置了Swapperbean实例
    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo());
    }

    //配置Swagger信息，就是一些默认的配置信息
    private ApiInfo apiInfo(){
        //作者信息
        Contact contact =new Contact(
                "李佳洁",
                "https://github.com/ljjwyn",
                "lijj-m@Gloden.com");
        return new ApiInfo(
                "李佳洁的SwaggerApi",
                "秒杀模块API",
                "1.0",
                "https://www.cnblogs.com/yaoyaoo/",
                contact,
                //以下三个配置默认
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());

    }
}
