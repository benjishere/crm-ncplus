package com.igorbrodevic.controller;

import com.igorbrodevic.data.Customer1;
import com.igorbrodevic.data.CustomerPackage;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

import java.sql.Time;
import java.time.LocalDate;

/**
 * Simple name editor Window.
 */
@SuppressWarnings("serial")
public class AddCustomer extends Window {

    private final TextField firstName = new TextField("Imię");
    private final TextField lastName = new TextField("Nazwisko");
    private final TextField street = new TextField("Ulica");
    private final TextField city = new TextField("Miasto");
    private final DateField contractSignedDate = new DateField();
    private final DateField contractEndDate = new DateField();
    private final NativeSelect<String> isDomesticClient = new NativeSelect<>();
    private final DateField lastContactDate = new DateField();
    private final NativeSelect<String> customerPackage = new NativeSelect<>();
    private final NativeSelect<String> potentialPackage = new NativeSelect<>();
    private final DateField plannedContactDate = new DateField();


    /*
     private String firstName = "";
    private String lastName = "";
    private String street = "";
    private String city = "";
    private LocalDate contractSignedDate;
    private LocalDate contractEndDate;
    private boolean isDomesticClient;
    private LocalDate lastContactDate;
    private CustomerPackage customerPackage;
    private CustomerPackage potentialPackage;
    private LocalDate plannedContactDate;
*/
    public AddCustomer(Customer1 currentName) {
        setCaption("Dodaj klienta");
        setModal(true);
        setClosable(false);
        setResizable(false);
        //setSizeUndefined();
        setWidth(600.0f, Unit.PIXELS);

        addStyleName("edit-dashboard");

        setContent(buildContent("JEJ"));
    }

    private Component buildContent(final String currentName) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        VerticalLayout result1 = new VerticalLayout();
        VerticalLayout result2 = new VerticalLayout();
        VerticalLayout finalResult = new VerticalLayout();

        firstName.setValue(currentName);
        //firstName.addStyleName("caption-on-left");
        firstName.focus();

        lastName.setValue(currentName);
        //lastName.addStyleName("caption-on-left");

        street.setValue(currentName);
        //street.addStyleName("caption-on-left");

        city.setValue(currentName);
        //city.addStyleName("caption-on-left");

        contractSignedDate.setValue(LocalDate.now());
        contractSignedDate.setCaption("Data podpisania umowy");

        result1.addComponents(firstName, lastName, street, city, contractSignedDate);

        contractEndDate.setValue(LocalDate.now());
        contractEndDate.setCaption("Data końca umowy");

        isDomesticClient.setItems("Klient polski", "Klient zagraniczny");
        isDomesticClient.setCaption("Narodowość");
        isDomesticClient.setEmptySelectionAllowed(false);


        result2.addComponents(contractEndDate, isDomesticClient);
        horizontalLayout.addComponents(result1, result2);

        finalResult.addComponents(horizontalLayout, buildFooter());
        //result.addComponent(buildFooter());

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
                //listener.dashboardNameEdited(nameField.getValue());
                close();
            }
        });
        save.setClickShortcut(KeyCode.ENTER, null);

        footer.addComponents(cancel, save);
        footer.setExpandRatio(cancel, 1);
        footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
        return footer;
    }


}
