package com.igorbrodevic.view;

import com.igorbrodevic.event.CRMEvent.UserLoginRequestedEvent;
import com.igorbrodevic.event.CRMEventBus;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@UIScope
@SuppressWarnings("serial")
public class LoginView extends VerticalLayout {

    private CRMEventBus crmEventBus;

    public LoginView() {
        setSizeFull();
        setMargin(false);
        setSpacing(false);


        Component loginForm = buildLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);

        /*Notification notification = new Notification(
                "Welcome to Dashboard Demo");
        notification
                .setDescription("<span>This application is not real, it only demonstrates an application built with the <a href=\"https://vaadin.com\">Vaadin framework</a>.</span> <span>No username or password is required, just click the <b>Sign In</b> button to continue.</span>");
        notification.setHtmlContentAllowed(true);
        notification.setStyleName("tray dark small closable login-help");
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.setDelayMsec(20000);
        notification.show(Page.getCurrent());*/
    }

    private Component buildLoginForm() {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        //loginPanel.setMargin(false);
        loginPanel.setSpacing(true);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");

        //loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields());
        CheckBox rememberMe = new CheckBox("Pamiętaj hasło", true);
        loginPanel.addComponent(rememberMe);
        return loginPanel;
    }

    private Component buildFields() {
        HorizontalLayout fields = new HorizontalLayout();
        fields.addStyleName("fields");
        fields.setSpacing(true);

        final TextField username = new TextField("Użytkownik");
        username.setIcon(FontAwesome.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final PasswordField password = new PasswordField("Hasło");
        password.setIcon(FontAwesome.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final Button signin = new Button("Zaloguj");
        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(KeyCode.ENTER);
        signin.focus();

        fields.addComponents(username, password, signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        //signin.addClickListener()

        signin.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                CRMEventBus.post(new UserLoginRequestedEvent(username
                  .getValue(), password.getValue()));
            }
        });
        return fields;
    }

    private Component buildLabels() {
        CssLayout labels = new CssLayout();
        labels.addStyleName("labels");

        Label welcome = new Label("Witam");
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);

        Label title = new Label("PAWEX");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_LIGHT);
        labels.addComponent(title);
        return labels;

        /*Image image = new Image(null,  new ThemeResource("img/Inappi_logo.png"));
        image.setWidth("30%");
        image.setHeight("30%");
        addComponent(image);
        image.setSizeUndefined();
        //setComponentAlignment(image, Alignment.TOP_CENTER);
        return image;*/
    }
}
