package com.igorbrodevic.controller;

import com.igorbrodevic.data.Customer1;
import com.igorbrodevic.data.CustomerPackage;
import com.igorbrodevic.data.CustomerStatus;
import com.igorbrodevic.data.HibernateUtil;
import com.igorbrodevic.event.CRMEvent;
import com.igorbrodevic.event.CRMEventBus;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import org.hibernate.Session;

import java.sql.Time;
import java.time.LocalDate;

/**
 * Simple name editor Window.
 */
@SuppressWarnings("serial")
public class AddCustomer extends Window {

    private final TextField firstName = new TextField("Imię");
    private final TextField lastName = new TextField("Nazwisko");
    private final TextField mobile = new TextField("Numer telefonu");
    private final TextField street = new TextField("Ulica");
    private final TextField city = new TextField("Miasto");
    private final DateField contractSignedDate = new DateField("Data podpisania umowy");
    private final DateField contractEndDate = new DateField("Data końca umowy");
    //private final boolean isDomesticClient = true;
    private final NativeSelect<String> isDomesticClient = new NativeSelect<>("Narodowość");
    private final DateField lastContactDate = new DateField("Data ostatniego kontaktu");
    private final TextField lastContantPerson = new TextField("Ostatni kontakt z:");
    private final TextField customerPackage = new TextField("Pakiet klienta");
    private final TextField potentialPackage = new TextField("Potencjalnie zainteresowany");
    private final DateField plannedContactDate = new DateField("Data planowanego kontaktu z klientem");

    Binder<Customer1> binder = new Binder<>();
    private CRMEventBus crmEventBus;
    private Customer1 customer1;

    public AddCustomer(Customer1 passedCustomer) {
        crmEventBus.register(this);
        setCaption("Dodaj klienta");
        setModal(true);
        setClosable(false);
        setResizable(false);
        setWidth(600.0f, Unit.PIXELS);

        addStyleName("edit-dashboard");
        customer1 = passedCustomer;

        setContent(buildContent(customer1));
    }

    private Component buildContent(Customer1 customer1) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        VerticalLayout result1 = new VerticalLayout();
        VerticalLayout result2 = new VerticalLayout();
        VerticalLayout finalResult = new VerticalLayout();


        firstName.focus();
        binder.bind(firstName, Customer1::getFirstName, Customer1::setCity);
        binder.bind(lastName, Customer1::getLastName, Customer1::setLastName);
        binder.bind(mobile, Customer1::getMobile, Customer1::setMobile);
        binder.bind(street, Customer1::getStreet, Customer1::setStreet);
        binder.bind(city, Customer1::getCity, Customer1::setCity);
        binder.bind(contractSignedDate, Customer1::getContractSignedDate, Customer1::setContractSignedDate);
        binder.bind(contractEndDate, Customer1::getContractEndDate, Customer1::setContractEndDate);

        binder.bind(lastContactDate, Customer1::getLastContactDate, Customer1::setLastContactDate);
        binder.bind(lastContantPerson, Customer1::getLastContactPerson, Customer1::setLastContactPerson);
        binder.bind(plannedContactDate, Customer1::getPlannedContactDate, Customer1::setPlannedContactDate);



        isDomesticClient.setItems("Klient polski", "Klient zagraniczny");
        isDomesticClient.setCaption("Narodowość");
        isDomesticClient.setEmptySelectionAllowed(false);
        isDomesticClient.setValue("Klient polski");

        binder.bind(customerPackage, Customer1::getCustomerPackage, Customer1::setCustomerPackage);
        binder.bind(potentialPackage, Customer1::getPotentialPackage, Customer1::setPotentialPackage);
        binder.readBean(customer1);


        result1.addComponents(firstName, lastName, mobile, street, city, contractSignedDate, isDomesticClient);
        result2.addComponents(contractEndDate, customerPackage, potentialPackage, lastContactDate, lastContantPerson, plannedContactDate);
        horizontalLayout.addComponents(result1, result2);

        finalResult.addComponents(horizontalLayout, buildFooter());

        return finalResult;
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        Button cancel = new Button("Anuluj");
        cancel.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                close();
            }
        });
        cancel.setClickShortcut(KeyCode.ESCAPE, null);

        Button save = new Button("Zapisz");
        save.addStyleName(ValoTheme.BUTTON_PRIMARY);
        save.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                try {
                    binder.writeBean(customer1);
                } catch (ValidationException e) {
                    e.printStackTrace();
                }
                session.saveOrUpdate(customer1);
                session.getTransaction().commit();
                session.close();

                CRMEventBus.post(new CRMEvent.UpdatedTableContentEvent());

                close();
            }
        });
        save.setClickShortcut(KeyCode.ENTER, null);

        footer.addComponents(cancel, save);
        footer.setExpandRatio(cancel, 1);
        footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
        return footer;
    }

    private Customer1 getCustomer() {
        boolean isDomesticClient = (this.isDomesticClient.getValue().equals("Klient polski") ? false : true);


        Customer1 customer1 = new Customer1(this.firstName.getValue(), this.lastName.getValue(),
                this.mobile.getValue(), this.street.getValue(), this.city.getValue(),
                contractSignedDate.getValue(), contractEndDate.getValue(), isDomesticClient,
                lastContactDate.getValue(), lastContantPerson.getValue(), customerPackage.getValue(),
                potentialPackage.getValue(), plannedContactDate.getValue());

        return customer1;
    }




}
