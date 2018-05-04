package com.igorbrodevic;

import javax.servlet.annotation.WebServlet;

import com.google.common.eventbus.Subscribe;
import com.igorbrodevic.controller.CustomerService;
import com.igorbrodevic.data.Customer;
import com.igorbrodevic.data.Customer1;
import com.igorbrodevic.data.CustomerPackage;
import com.igorbrodevic.data.HibernateUtil;
import com.igorbrodevic.domain.User;
import com.igorbrodevic.event.CRMEvent.UserLoginRequestedEvent;
import com.igorbrodevic.event.CRMEventBus;
import com.igorbrodevic.view.CustomerForm;
import com.igorbrodevic.view.LoginView;
import com.igorbrodevic.view.MainView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import com.igorbrodevic.event.CRMEvent.UserLoggedOutEvent;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 */
@Theme("mytheme")
@Widgetset("com.igorbrodevic.MyAppWidgetset")
public class CRMUI extends UI {

    // event bus
    private final CRMEventBus crmEventBus = new CRMEventBus();

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        // register UI to eventbus
        crmEventBus.register(this);

        // make UI responsive
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        // set Polish time
        setLocale(new Locale("pl", "PL"));

        updateContent();
    }

    // present LoginView if not logged in or MainView
    private void updateContent() {
        User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
        if (user != null && "admin".equals(user.getRole())) {
            setContent(new MainView());
            removeStyleName("loginview");
        } else {
            setContent(new LoginView());
            addStyleName("loginview");
        }
    }

    @Subscribe
    public void userLoginRequested(final UserLoginRequestedEvent event) {
        User user = authenticate(event.getUserName(),
                event.getPassword());
        VaadinSession.getCurrent().setAttribute(User.class.getName(), user);
        updateContent();
    }

    @Subscribe
    public void userLoggedOut(final UserLoggedOutEvent event) {
        // When the user logs out, current VaadinSession gets closed and the
        // page gets reloaded on the login screen. Do notice the this doesn't
        // invalidate the current HttpSession.
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
    }

    public User authenticate(String userName, String password) {
        User user = new User();
        user.setFirstName("Paweł");
        user.setLastName("Łataś");
        user.setRole("admin");
        String email = user.getFirstName().toLowerCase() + "."
                + user.getLastName().toLowerCase() + "@"
                + "pawex" + ".com";
        user.setEmail(email.replaceAll(" ", ""));
        user.setLocation("Warsaw");
        user.setBio("Quis aute iure reprehenderit in voluptate velit esse."
                + "Cras mattis iudicium purus sit amet fermentum.");
        return user;
    }

    public static CRMEventBus getDashboardEventbus() {
        return ((CRMUI) getCurrent()).crmEventBus;
    }
    /*public static DataProvider getDataProvider() {
        return ((CRMUI) getCurrent()).dataProvider;
    }*/

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = CRMUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
