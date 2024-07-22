package com.project.innoutburger.config;

import com.itheima.reggie.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    /*
     * 設置靜態資源映射
     * @param registry
     * */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("開始進行靜態資源映射...");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    /*
    * 擴展 mvc 框架的消息轉換器
    * */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        log.info("擴展消息轉換器...");

        // 創建消息轉換器對象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

        // 設置對象轉換器, 底層使用 Jackson 將 Java 對象轉為 JSON
        messageConverter.setObjectMapper(new JacksonObjectMapper());

        // 將上面的消息轉換器對象追加到 mvc 框架的轉換器集合中
        converters.add(0, messageConverter);

    }
}
