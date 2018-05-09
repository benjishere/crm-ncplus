package com.igorbrodevic.spring;

import com.igorbrodevic.CRMUI;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

@SpringComponent
@SuppressWarnings("serial")
public class VaadinUIProvider extends UIProvider {

    @Autowired
    ApplicationContext applicationContext;

    @Override
    public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        return CRMUI.class;
    }

    @Override
    public UI createInstance(UICreateEvent event) {
        UI instance = new CRMUI();
        System.out.println("applicationContext is null? " + applicationContext);
        applicationContext.getAutowireCapableBeanFactory().autowireBean(instance);
        return instance;
    }

}