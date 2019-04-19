//package com.wyc.common.config;//package com.file.server.config;
//import com.rabbitmq.client.ExceptionHandler;
//import com.rabbitmq.client.impl.DefaultExceptionHandler;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
//import org.springframework.oxm.xstream.XStreamMarshaller;
//import org.springframework.web.multipart.MultipartResolver;
//import org.springframework.web.multipart.commons.CommonsMultipartResolver;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
//
//import java.nio.charset.Charset;
//import java.util.ArrayList;
//import java.util.List;
//
//@EnableWebMvc
//@Configuration
//public class WebConfiguration extends WebMvcConfigurerAdapter {
//
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.add(marshallingHttpMessageConverter());
//    }
//
//    public MarshallingHttpMessageConverter marshallingHttpMessageConverter(){
//        MarshallingHttpMessageConverter marshallingHttpMessageConverter = new MarshallingHttpMessageConverter();
//        List<MediaType> mediaTypes = new ArrayList<MediaType>();
//        mediaTypes.add(MediaType.TEXT_XML);
//        mediaTypes.add(MediaType.APPLICATION_XML);
//        XStreamMarshaller xStreamMarshaller=new XStreamMarshaller();
//        marshallingHttpMessageConverter.setSupportedMediaTypes(mediaTypes);
//        marshallingHttpMessageConverter.setMarshaller(xStreamMarshaller);
//        marshallingHttpMessageConverter.setUnmarshaller(xStreamMarshaller);
//        return marshallingHttpMessageConverter;
//    }
//    //配置文件上传
//    @Bean(name = {"multipartResolver"})
//    public MultipartResolver multipartResolver(){
//        CommonsMultipartResolver commonsMultipartResolver=new CommonsMultipartResolver();
//        commonsMultipartResolver.setDefaultEncoding("utf-8");
//        commonsMultipartResolver.setMaxUploadSize(10485760000L);
//        commonsMultipartResolver.setMaxInMemorySize(40960);
//        return commonsMultipartResolver;
//    }
//    //异常处理
//    @Bean
//    public ExceptionHandler exceptionResolver(){
//        ExceptionHandler exceptionHandler = new DefaultExceptionHandler();
//        return exceptionHandler;
//    }
//    //拦截器
//    @Override
//    public void addInterceptors(InterceptorRegistry registry){
//
//        super.addInterceptors(registry);
//    }
//}