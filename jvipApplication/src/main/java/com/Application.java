package com;
//import com.file.server.socket.SocketApplication;
//import com.file.server.socket.ConnectException;
import ir.nymph.lang.Snowflake;
import org.hibernate.SessionFactory;
import org.hibernate.dialect.Dialect;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@EnableCaching @SpringBootApplication @EnableScheduling
public class Application{
	@Autowired
	private AutowireCapableBeanFactory autowireCapableBeanFactory;
	public static void main(String[] args) throws Exception{
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
	}

	@Bean Snowflake idWorker() {
		return new Snowflake(0, 0);
	}

	/**
	/**
	 * @return
	 */
	@Bean
	CustomExchange delayedExchange() {
		Map<String,Object> args = new HashMap<>();
		args.put("x-delayed-type", "direct");
		return new CustomExchange("delayedExchange","x-delayed-message",true,false,args);
	}

	@Bean
	public HibernateJpaSessionFactoryBean sessionFactory() {
		return new HibernateJpaSessionFactoryBean();
	}

	@Bean
	//@ConditionalOnMissingBean(PlatformTransactionManager.class)
	public PlatformTransactionManager transactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory);
		return transactionManager;
	}


//	@Bean
//	public SocketApplication socketApplication(){
//		SocketApplication socketApplication = new SocketApplication();
//		autowireCapableBeanFactory.autowireBean(socketApplication);
//		try {
//			socketApplication.start();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (ConnectException e) {
//			e.printStackTrace();
//		}
//		return socketApplication;
//	}

}
