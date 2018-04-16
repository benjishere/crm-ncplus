package com.igorbrodevic.view;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/*
 * Dashboard MainView is a simple HorizontalLayout that wraps the menu on the
 * left and creates a simple container for the navigator on the right.
 */
@SuppressWarnings("serial")
public class MainView extends HorizontalLayout {

    public MainView() {
        setSizeFull();
        //addStyleName("mainview");
        setSpacing(false);

        //
        CRMMenu crmMenu = new CRMMenu();
        addComponent(crmMenu);

        /*ComponentContainer content = new CssLayout();
        content.addStyleName("view-content");
        content.setSizeFull();
        addComponent(content);
        setExpandRatio(content, 1.0f);*/

        VerticalLayout tableView = new TableView();
        tableView.setSizeFull();
        //setExpandRatio(tableView, 1);
        addComponent(tableView);
        this.setExpandRatio(tableView, 0.85f);
        this.setExpandRatio(crmMenu, 0.15f);

    }
}
