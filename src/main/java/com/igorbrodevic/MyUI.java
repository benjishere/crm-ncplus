package com.igorbrodevic;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 *
 */
@Theme("mytheme")
@Widgetset("com.igorbrodevic.MyAppWidgetset")
public class MyUI extends UI {

    private CustomerService service = CustomerService.getInstance();
    private Grid grid = new Grid();
    private TextField filterText = new TextField();
    private CustomerForm form = new CustomerForm(this);

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        VerticalLayout layout = new VerticalLayout();
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

        Button addCusomerBtn = new Button("Add new customer");
        addCusomerBtn.addClickListener(e -> {
            grid.select(null);
            form.setCustomer(new Customer());
        });

        HorizontalLayout toolbar = new HorizontalLayout(filtering, addCusomerBtn);
        toolbar.setSpacing(true);

        grid.setColumns("firstName", "lastName", "email");
        filtering.addComponents(filterText, clearFilterTextBtn);
        layout.addComponents(toolbar, main);

        filterText.setInputPrompt("filter by name...");
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
        setContent(layout);

        form.setVisible(false);
    }

    public void updateList() {
        List<Customer> customers = service.findAll(filterText.getValue());
        grid.setContainerDataSource(new BeanItemContainer<>(Customer.class, customers));
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
