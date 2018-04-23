package com.igorbrodevic.view;

import com.google.gwt.user.client.ui.ClickListener;
import com.igorbrodevic.controller.AddCustomer;
import com.igorbrodevic.controller.CustomerService;
import com.igorbrodevic.data.Customer1;
import com.igorbrodevic.data.HibernateUtil;
import com.vaadin.data.ValueContext;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.converter.StringToBooleanConverter;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Grid;
import org.hibernate.Session;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TableView extends VerticalLayout {

    private CustomerService service = CustomerService.getInstance();
    private Grid<Customer1> grid;
    private SingleSelect<Customer1> singleSelect;
    private TextField filterText = new TextField();
    private CustomerForm form = new CustomerForm(this);
    VerticalLayout layout = new VerticalLayout();
    private String filterValue = "";

    public TableView() {
        setSizeFull();
        CssLayout filtering = new CssLayout();
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        filterText = buildFilter();
        Button clearFilterTextBtn  = new Button(VaadinIcons.CLOSE_SMALL);
        grid = updateList();
        singleSelect = grid.asSingleSelect();

        clearFilterTextBtn.addClickListener(e -> {
            filterText.clear();
            updateList();
        });

        grid.setSizeFull();

        HorizontalLayout toolbar = new HorizontalLayout(filtering, buildEditButton());
        toolbar.setSpacing(true);

        filtering.addComponents(filterText, clearFilterTextBtn);
        layout.addComponents(toolbar, grid);



        grid.addSelectionListener(event -> {
            if (event.getFirstSelectedItem().equals(null)) {
                //form.setVisible(false);
            } else {
                Customer1 customer = (Customer1) event.getFirstSelectedItem().get();//event.getSelected().iterator().next();
                //form.setCustomer(customer);
            }
        });



        layout.setMargin(false);
        layout.setSpacing(true);
        layout.addStyleName("table-padding");
        //layout.setSizeFull();
        addComponent(layout);
        addStyleName("table-padding");
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
        grid.addColumn(Customer1::getStreet).setCaption("Ulica");
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

    private TextField buildFilter() {
        final TextField filter = new TextField();

        // TODO use new filtering API
        filter.addValueChangeListener(event -> {

            Collection<Customer1> customer1s = getDataFromDB().stream().filter(customer -> {
                        filterValue = filter.getValue().trim().toLowerCase();
                        return passesFilter(customer.getFirstName())
                                || passesFilter(customer.getLastName())
                                || passesFilter(customer.getCity());
                    }).collect(Collectors.toList());

            ListDataProvider<Customer1> dataProvider = com.vaadin.data.provider.DataProvider
                    .ofCollection(customer1s);
            //dataProvider.addSortComparator(Comparator
            //        .comparing(Transaction::getTime).reversed()::compare);
            grid.setDataProvider(dataProvider);
        });

        filter.setPlaceholder("Wyszukaj");
        filter.setIcon(FontAwesome.SEARCH);
        filter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        filter.addShortcutListener(
                new ShortcutListener("Clear", ShortcutAction.KeyCode.ESCAPE, null) {
                    @Override
                    public void handleAction(final Object sender,
                                             final Object target) {
                        filter.setValue("");
                    }
                });
        return filter;
    }

    private boolean passesFilter(String subject) {
        if (subject == null) {
            return false;
        }
        return subject.trim().toLowerCase().contains(filterValue);
    }

    private List<Customer1> getDataFromDB() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Customer1> customers1 = session.createQuery("from Customer1").list();
        session.close();
        return customers1;
    }

    private Component buildEditButton() {
        Button result = new Button();
        /*result.setIcon(FontAwesome.EDIT);
        result.addStyleName("icon-edit");
        result.addStyleName(ValoTheme.BUTTON_ICON_ONLY);*/
        result.setCaption("Dodaj klienta");
        //result.setDescription("Edit Dashboard");

        result.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                getUI().addWindow(
                        new AddCustomer(getDataFromDB().get(1)));
            }
        });
        return result;
    }

}
