package com.igorbrodevic.view;

import com.google.common.eventbus.Subscribe;
import com.google.gwt.user.client.ui.ClickListener;
import com.igorbrodevic.controller.AddCustomer;
import com.igorbrodevic.data.Customer1;
import com.igorbrodevic.data.HibernateUtil;
import com.igorbrodevic.event.CRMEvent;
import com.igorbrodevic.event.CRMEventBus;
import com.vaadin.data.ValueContext;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.converter.StringToBooleanConverter;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Grid;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TableView extends VerticalLayout {

    // table
    private Grid<Customer1> grid;
    private SingleSelect<Customer1> singleSelect;

    // filter textfield
    private TextField filterText = new TextField();
    private String filterValue = "";

    // buttons
    private Button editButton = new Button();
    private Button deleteButton = new Button();
    private Button clearButton = new Button();

    public TableView() {

        // register tableview to eventbus
        CRMEventBus.register(this);

        setSizeFull();
        CssLayout filtering = new CssLayout();
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        filterText = buildFilter();

        // init and configure grid
        grid = new Grid<>(Customer1.class);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setSizeFull();
        singleSelect = grid.asSingleSelect();
        initGrid();

        // create buttons
        buildEditButton();
        buildDeleteButton();
        buildClearButton();

        filtering.addComponents(filterText, clearButton);

        HorizontalLayout toolbar = new HorizontalLayout(filtering, buildAddButton(), editButton, deleteButton);
        toolbar.setSpacing(true);
        toolbar.addStyleName("toolbar-margin");

        grid.addSelectionListener(event -> {
            if (event.getFirstSelectedItem().equals(null)) {
                editButton.setEnabled(false);
                deleteButton.setEnabled(false);
            } else {
                editButton.setEnabled(true);
                deleteButton.setEnabled(true);
            }
        });

        addComponents(toolbar, grid);
        setExpandRatio(grid, 0.9f);
        addStyleName("table-padding");
    }

    public void updateList() {
        Collection<Customer1> customer1s = getDataFromDB();
        ListDataProvider<Customer1> dataProvider = DataProvider.ofCollection(customer1s);
        dataProvider.setSortOrder(Customer1::getId, SortDirection.ASCENDING);
        grid.setDataProvider(dataProvider);

    }

    public void initGrid() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        List<Customer1> customers1 = session.createQuery("from Customer1 ORDER BY id").list();

        grid.setItems(customers1);
        grid.removeAllColumns();
        grid.addColumn(Customer1::getFirstName).setCaption("Imię");
        grid.addColumn(Customer1::getLastName).setCaption("Nazwisko");
        grid.addColumn(Customer1::getMobile).setCaption("Nr tel");
        grid.addColumn(Customer1::getCity).setCaption("Miasto");
        grid.addColumn(Customer1::getStreet).setCaption("Ulica");
        grid.addColumn(Customer1::getContractSignedDate).setCaption("Data podpisania");
        grid.addColumn(Customer1::getContractEndDate).setCaption("Koniec umowy");

        StringToBooleanConverter converter = new StringToBooleanConverter("", VaadinIcons.CLOSE_CIRCLE.getHtml(),
                VaadinIcons.CHECK_CIRCLE.getHtml());

        grid.addColumn(customer1 -> converter.convertToPresentation(customer1.isDomesticClient(), new ValueContext()),
                new HtmlRenderer()).setCaption("Zagraniczny");
        //
        grid.addColumn(Customer1::getLastContactDate).setCaption("Ostatni kontakt");
        grid.addColumn(Customer1::getLastContactPerson).setCaption("Ostatni kontankt z");
        grid.addColumn(Customer1::getCustomerPackage).setCaption("Pakiet");
        grid.addColumn(Customer1::getPotentialPackage).setCaption("Zainteresowany");
        grid.addColumn(Customer1::getPlannedContactDate).setCaption("Planowany kontakt");

        grid.addStyleName("table");
        grid.setStyleGenerator(g -> {
            if (g.getContractEndDate().minusMonths(3).isBefore(LocalDate.now()))
                return "error-row";
            else
                return null;
        });

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
        List<Customer1> customers1 = session.createQuery("from Customer1 ORDER BY id").list();
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

    private void buildEditButton() {
        editButton.setEnabled(false);
        editButton.setCaption("Edytuj");

        editButton.addClickListener(e -> {
            getUI().addWindow(new AddCustomer(singleSelect.getValue()));
        });

    }

    private void buildDeleteButton() {
        deleteButton.setEnabled(false);
        deleteButton.setCaption("Usuń");

        deleteButton.addClickListener(e -> {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.delete(singleSelect.getValue());
            session.getTransaction().commit();
            session.close();

            updateList();
        });
    }

    private void buildClearButton() {
        clearButton.setIcon(VaadinIcons.CLOSE_SMALL);

        clearButton.addClickListener(e -> {
            filterText.clear();
            updateList();
        });
    }

    @Subscribe
    public void tableUpdate(final CRMEvent.UpdatedTableContentEvent event) {
        updateList();
    }

}
