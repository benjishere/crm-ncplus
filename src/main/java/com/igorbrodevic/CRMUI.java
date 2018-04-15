package com.igorbrodevic;

import javax.servlet.annotation.WebServlet;

import com.google.common.eventbus.Subscribe;
import com.igorbrodevic.controller.CustomerService;
import com.igorbrodevic.data.Customer;
import com.igorbrodevic.domain.User;
import com.igorbrodevic.event.CRMEvent.UserLoginRequestedEvent;
import com.igorbrodevic.event.CRMEventBus;
import com.igorbrodevic.view.CustomerForm;
import com.igorbrodevic.view.LoginView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

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



    private CustomerService service = CustomerService.getInstance();
    private Grid grid = new Grid();
    private TextField filterText = new TextField();
    private CustomerForm form = new CustomerForm(this);
    VerticalLayout layout = new VerticalLayout();

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        // register UI to eventbus
        crmEventBus.register(this);

        // make UI responsive
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        setLocale(new Locale("pl", "PL"));

        CssLayout filtering = new CssLayout();
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        Button clearFilterTextBtn  = new Button(FontAwesome.TIMES);

        clearFilterTextBtn.addClickListener(e -> {
            filterText.clear();
            updateList();
        });


        HorizontalLayout main = new HorizontalLayout(grid, form);
        main.setSpacing(true);
        main.setSizeFull();
        grid.setSizeFull();
        main.setExpandRatio(grid, 1);

        Button addCusomerBtn = new Button("Dodaj klienta");
        addCusomerBtn.addClickListener(e -> {
            grid.select(null);
            form.setCustomer(new Customer());

        });

        HorizontalLayout toolbar = new HorizontalLayout(filtering, addCusomerBtn);
        toolbar.setSpacing(true);

        grid.setColumns("firstName", "lastName", "email");
        filtering.addComponents(filterText, clearFilterTextBtn);
        layout.addComponents(toolbar, main);

        filterText.setInputPrompt("wyszukaj...");
        filterText.addTextChangeListener(e -> {
            grid.setContainerDataSource(new BeanItemContainer<>(Customer.class, service.findAll(e.getText())));
        });


        grid.addSelectionListener(e -> {
            if (e.getSelected().isEmpty()) {
                form.setVisible(false);
            } else {
                Customer customer = (Customer) e.getSelected().iterator().next();
                form.setCustomer(customer);
            }
        });

        updateList();

        layout.setMargin(true);
        layout.setSpacing(true);
        //setContent(layout);

        form.setVisible(false);

        updateContent();
    }

    private void updateContent() {
        User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
        if (user != null && "admin".equals(user.getRole())) {
            setContent(layout);
            removeStyleName("loginview");
            //getNavigator().navigateTo(getNavigator().getState());
        } else {
            setContent(new LoginView());
            addStyleName("loginview");
        }
    }

    public void updateList() {
        List<Customer> customers = service.findAll(filterText.getValue());
        grid.setContainerDataSource(new BeanItemContainer<>(Customer.class, customers));
    }

    @Subscribe
    public void userLoginRequested(final UserLoginRequestedEvent event) {
        User user = authenticate(event.getUserName(),
                event.getPassword());
        VaadinSession.getCurrent().setAttribute(User.class.getName(), user);
        updateContent();
    }


    public User authenticate(String userName, String password) {
        User user = new User();
        user.setFirstName("Malwina");
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

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = CRMUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
