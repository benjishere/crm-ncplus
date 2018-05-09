package com.igorbrodevic.spring;

import com.igorbrodevic.CRMUI;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

@WebServlet(value = "/*", asyncSupported = true)
@VaadinServletConfiguration(productionMode = false, ui = CRMUI.class)
@SuppressWarnings("serial")
public class SpringVaadinServlet extends VaadinServlet implements SessionInitListener {

    @Autowired
    protected VaadinUIProvider applicationProvider;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        AutowireCapableBeanFactory ctx = ((ApplicationContext) getServletContext().getAttribute("applicationContext")).getAutowireCapableBeanFactory();
        ctx.autowireBean(this);
    }

    @Override
    protected void servletInitialized() {
        getService().addSessionInitListener(this);
    }

    @Override
    public void sessionInit(SessionInitEvent event) throws ServiceException {
        event.getSession().addUIProvider(applicationProvider);
    }
}