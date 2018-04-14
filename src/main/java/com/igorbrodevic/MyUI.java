package com.igorbrodevic;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;

/**
 *
 */
@Theme("mytheme")
@Widgetset("com.igorbrodevic.MyAppWidgetset")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        /*final VerticalLayout layout = new VerticalLayout();

        final TextField name = new TextField();
        name.setCaption("Type your name here!:");

        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);

        Button button = new Button("Click Me!");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                layout.addComponent(new Label("Thank you, " + name.getValue() + ", for clicking"));
            }
        });
        layout.addComponent(name);
        layout.addComponent(button);*/

        TextField textField = new TextField("Name:");
        Button button = new Button("Greet");
        button.addClickListener(e -> Notification.show("It worked, " + textField.getValue()));

        VerticalLayout layout = new VerticalLayout();
        layout.addComponents(textField, button);

        setContent(layout);

    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
