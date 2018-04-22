package com.igorbrodevic.view;

import com.igorbrodevic.controller.CustomerService;
import com.igorbrodevic.data.Customer1;
import com.igorbrodevic.data.HibernateUtil;
import com.vaadin.data.ValueContext;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.converter.StringToBooleanConverter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Grid;
import org.hibernate.Session;

import java.util.Arrays;
import java.util.List;

public class TableView extends VerticalLayout {

    private CustomerService service = CustomerService.getInstance();
    private Grid<Customer1> grid;
    private SingleSelect<Customer1> singleSelect;
    private TextField filterText = new TextField();
    private CustomerForm form = new CustomerForm(this);
    VerticalLayout layout = new VerticalLayout();

    public TableView() {
        CssLayout filtering = new CssLayout();
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        Button clearFilterTextBtn  = new Button(VaadinIcons.CLOSE_SMALL);
        grid = updateList();
        singleSelect = grid.asSingleSelect();

        clearFilterTextBtn.addClickListener(e -> {
            filterText.clear();
            updateList();
        });


        HorizontalLayout main = new HorizontalLayout(grid); //, form);
        main.setSpacing(true);
        main.setSizeFull();
        grid.setSizeFull();
        main.setExpandRatio(grid, 1);

        Button addCustomerBtn = new Button("Dodaj klienta");
       /* addCustomerBtn.addClickListener(e -> {
            grid.select(null);
            form.setCustomer(new Customer());

        });*/

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
                Customer1 customer = (Customer1) event.getFirstSelectedItem().get();//event.getSelected().iterator().next();
                //form.setCustomer(customer);
            }
        });



        layout.setMargin(false);
        layout.setSpacing(false);
        addComponent(layout);
    }

    public Grid<Customer1> updateList() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Grid<Customer1> grid = new Grid<>(Customer1.class);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        List<Customer1> customers1 = session.createQuery("from Customer1").list();

        grid.setItems(customers1);
        grid.removeAllColumns();
        grid.addColumn(Customer1::getFirstName).setCaption("Imię");
        grid.addColumn(Customer1::getLastName).setCaption("Nazwisko");
        grid.addColumn(Customer1::getCity).setCaption("Miasto");
        grid.addColumn(Customer1::getContractSignedDate).setCaption("Data podpisania");
        grid.addColumn(Customer1::getContractEndDate).setCaption("Koniec umowy");
        //
        StringToBooleanConverter converter = new StringToBooleanConverter("", VaadinIcons.CLOSE_CIRCLE.getHtml(),
                VaadinIcons.CHECK_CIRCLE.getHtml());

        grid.addColumn(customer1 -> converter.convertToPresentation(customer1.isDomesticClient(), new ValueContext()),
                new HtmlRenderer()).setCaption("Zagraniczny");
        //
        grid.addColumn(Customer1::getLastContactDate).setCaption("Ostatni kontakt");
        grid.addColumn(Customer1::getCustomerPackage).setCaption("Pakiet");
        grid.addColumn(Customer1::getPotentialPackage).setCaption("Zainteresowany");
        grid.addColumn(Customer1::getPlannedContactDate).setCaption("Planowany kontakt");

        grid.addStyleName("table");
        grid.setHeightByRows(customers1.size());

        session.close();
        return grid;
    }

}
