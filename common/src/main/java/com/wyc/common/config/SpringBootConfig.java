package com.wyc.common.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;


@Service
public class SpringBootConfig implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            InputStream is = null;
            try {
                is = new FileInputStream("application.properties");
            }catch (Exception e){
                try {
                    is = new FileInputStream(new ClassPathResource("application.properties").getFile());
                }catch (Exception e2){
                    is =  new FileInputStream("c:/etc/nk/jvip/application.properties");
                }
            }
            Properties properties = new Properties();
            properties.load(is);
            PropertiesPropertySource propertySource = new PropertiesPropertySource("dynamic", properties);
            environment.getPropertySources().addLast(propertySource);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
