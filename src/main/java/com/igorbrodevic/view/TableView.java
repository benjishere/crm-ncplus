package com.igorbrodevic.view;

import com.google.common.eventbus.Subscribe;
import com.google.gwt.user.client.ui.ClickListener;
import com.igorbrodevic.controller.AddCustomer;
import com.igorbrodevic.controller.CustomerService;
import com.igorbrodevic.data.Customer1;
import com.igorbrodevic.data.HibernateUtil;
import com.igorbrodevic.event.CRMEvent;
import com.igorbrodevic.event.CRMEventBus;
import com.vaadin.data.ValueContext;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.converter.StringToBooleanConverter;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
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
    private Button editButton;
    private CRMEventBus crmEventBus;

    public TableView() {
        crmEventBus.register(this);
        setSizeFull();
        CssLayout filtering = new CssLayout();
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        filterText = buildFilter();
        Button clearFilterTextBtn  = new Button(VaadinIcons.CLOSE_SMALL);

        grid = new Grid<>(Customer1.class);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        initGrid();

        editButton = buildEditButton();
        singleSelect = grid.asSingleSelect();

        clearFilterTextBtn.addClickListener(e -> {
            filterText.clear();
            updateList();
        });

        grid.setSizeFull();

        HorizontalLayout toolbar = new HorizontalLayout(filtering, buildAddButton(), editButton, buildDeleteButton());
        toolbar.setSpacing(true);

        filtering.addComponents(filterText, clearFilterTextBtn);
        layout.addComponents(toolbar, grid);


/*
        grid.addSelectionListener(event -> {
            if (event.getFirstSelectedItem().equals(null)) {
                editButton.setEnabled(false);
            } else {
                editButton.setEnabled(true);
                //Customer1 customer = (Customer1) event.getFirstSelectedItem().get();//event.getSelected().iterator().next();
                //form.setCustomer(customer);
            }
        });*/



        layout.setMargin(false);
        layout.setSpacing(true);
        layout.addStyleName("table-padding");
        //layout.setSizeFull();
        addComponent(layout);
        addStyleName("table-padding");
    }

    public void updateList() {
        Collection<Customer1> customer1s = getDataFromDB();
        System.out.println("UPDATE LIST!!!");
        ListDataProvider<Customer1> dataProvider = DataProvider.ofCollection(customer1s);
        grid.setDataProvider(dataProvider);
    }

    public void initGrid() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

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

        session.getTransaction().commit();
        session.close();
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
        session.getTransaction().commit();
        session.close();
        return customers1;
    }

    private Component buildAddButton() {
        Button result = new Button();
        result.setCaption("Dodaj");

        result.addClickListener(e -> {
            getUI().addWindow(new AddCustomer(new Customer1()));
        });
        return result;
    }

    private Button buildEditButton() {
        Button result = new Button();
        result.setCaption("Edytuj");

        result.addClickListener(e -> {
            getUI().addWindow(new AddCustomer(singleSelect.getValue()));
        });

        return result;
    }

    private Button buildDeleteButton() {
        Button result = new Button();
        result.setCaption("Usuń");

        result.addClickListener(e -> {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.delete(singleSelect.getValue());
            session.getTransaction().commit();
            session.close();

            updateList();
        });

        return result;
    }

    @Subscribe
    public void tableUpdate(final CRMEvent.UpdatedTableContentEvent event) {
        updateList();
    }

}
