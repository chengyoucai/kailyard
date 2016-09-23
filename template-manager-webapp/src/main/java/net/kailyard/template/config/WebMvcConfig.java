package net.kailyard.template.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.beans.factory.config.FieldRetrievingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Bean
    public WebMvcConfigurerAdapter forwardToIndex() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                // forward requests to /admin and /user to their index.html
                registry.addViewController("/").setViewName(
                        "forward:/index.html");
            }
        };
    }

    @Bean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
        return new FastJsonHttpMessageConverter();
    }

    @Bean
    public FieldRetrievingFactoryBean fieldRetrievingFactoryBean() {
        FieldRetrievingFactoryBean  f = new FieldRetrievingFactoryBean();
        f.setStaticField("com.alibaba.fastjson.serializer.SerializerFeature.DisableCircularReferenceDetect");
        return f;
    }

    //    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new AdminInterceptor()).addPathPatterns("/system/admin/**");
//    }
}
