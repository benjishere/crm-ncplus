package com.igorbrodevic.view;

import com.igorbrodevic.controller.CustomerService;
import com.igorbrodevic.data.Customer;
import com.igorbrodevic.data.Customer1;
import com.igorbrodevic.data.HibernateUtil;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.hibernate.Session;

import java.util.List;

public class TableView extends VerticalLayout {

    private CustomerService service = CustomerService.getInstance();
    private Grid grid = new Grid(Customer1.class);
    private TextField filterText = new TextField();
    private CustomerForm form = new CustomerForm(this);
    VerticalLayout layout = new VerticalLayout();

    public TableView() {

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

        Button addCustomerBtn = new Button("Dodaj klienta");
        addCustomerBtn.addClickListener(e -> {
            grid.select(null);
            form.setCustomer(new Customer());

        });

        HorizontalLayout toolbar = new HorizontalLayout(filtering, addCustomerBtn);
        toolbar.setSpacing(true);

        /*grid.setColumns("firstName", "lastName", "street", "city", "contractSignedDate",
                "contractEndDate", "isDomesticClient", "lastContactDate", "customerPackage",
                "potentialPackage", "plannedContactDate");*/
        filtering.addComponents(filterText, clearFilterTextBtn);
        layout.addComponents(toolbar, main);

        filterText.setPlaceholder("wyszukaj...");
        filterText.addValueChangeListener(e -> {
            //grid.setContainerDataSource(new BeanItemContainer<>(Customer.class, service.findAll(e.getText())));
        });


        grid.addSelectionListener(event -> {
            if (event.getFirstSelectedItem().equals(null)) {
                //form.setVisible(false);
            } else {
                Customer customer = (Customer) event.getFirstSelectedItem().get();//event.getSelected().iterator().next();
                form.setCustomer(customer);
            }
        });

        updateList();

        layout.setMargin(true);
        layout.setSpacing(true);
        addComponent(layout);

        //form.setVisible(false);
    }

    public void updateList() {
        //List<Customer> customers = service.findAll(filterText.getValue());
        //grid.setContainerDataSource(new BeanItemContainer<>(Customer.class, customers));

        grid.removeAllColumns();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Customer1> customers1 = session.createQuery("from Customer1").list();
        grid.setItems(customers1);
        //grid.addColumn(Customer1::getFirstName); //.setCaption("Name");
        //grid.addColumn(Person::getBirthYear).setCaption("Year of birth");


        //grid.setContainerDataSource(new BeanItemContainer<>(Customer1.class, customers1));
        session.close();
    }

}
