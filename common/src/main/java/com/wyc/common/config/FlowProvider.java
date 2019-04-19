package com.wyc.common.config;

import com.bstek.uflo.env.EnvironmentProvider;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;

@Configuration
public class FlowProvider implements EnvironmentProvider {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public PlatformTransactionManager getPlatformTransactionManager() {
        return new JpaTransactionManager(entityManagerFactory);
    }

    public String getCategoryId() {
        return null;
    }
    public String getLoginUser() {
        return "anonymous";
    }

}
