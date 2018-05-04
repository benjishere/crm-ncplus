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
        setSpacing(false);

        // left-side menu
        CRMMenu crmMenu = new CRMMenu();
        addComponent(crmMenu);

        // main content - client table
        VerticalLayout tableView = new TableView();
        addComponent(tableView);
        this.setExpandRatio(tableView, 1);

    }
}
