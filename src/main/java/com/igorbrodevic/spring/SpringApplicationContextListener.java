package com.igorbrodevic.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class SpringApplicationContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(CustomConfiguration.class);
        sce.getServletContext().setAttribute("applicationContext", applicationContext);     
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }

}