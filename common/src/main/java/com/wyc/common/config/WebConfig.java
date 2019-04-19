package com.wyc.common.config;

import com.bstek.uflo.console.UfloServlet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.*;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;

@ImportResource(locations = {"classpath:uflo-console-context.xml"})
@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class WebConfig{
    @Bean
    public ServletRegistrationBean servletRegistration() {
        return new ServletRegistrationBean(new UfloServlet(), "/uflo/*");
    }




//    @Qualifier(value="drawTilesConfigurer")
//    @Primary
//    @Bean
//    public TilesConfigurer tilesConfigurer() {
//        final TilesConfigurer configurer = new TilesConfigurer();
//        configurer.setDefinitions(new String[] {"/WEB-INF/tiles.xml"});
//        configurer.setCheckRefresh(true);
//        return configurer;
//    }
//
//
//    @Qualifier(value="drawDispatcherServlet")
//    @Primary
//    @Bean
//    public DispatcherServlet dispatcherServlet() {
//        return new DispatcherServlet();
//    }
//
//
//    @Qualifier(value="drawMultipartResolver")
//    @Primary
//    @Bean
//    public MultipartResolver multipartResolver(){
//        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
//        commonsMultipartResolver.setDefaultEncoding("utf-8");
//        commonsMultipartResolver.setMaxUploadSize(500000);
//        return commonsMultipartResolver;
//    }
//
//    @Override
//    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
//        configurer.enable();
//    }
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/js/**").addResourceLocations("/js/");
//        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
//    }
}
