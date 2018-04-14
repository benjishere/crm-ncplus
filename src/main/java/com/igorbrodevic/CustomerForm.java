package com.igorbrodevic;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import javafx.scene.input.KeyCode;

public class CustomerForm extends FormLayout {

    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private TextField email = new TextField("email");
    private NativeSelect status = new NativeSelect("Status");
    private PopupDateField birthDate = new PopupDateField("Birthdate");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");

    private CustomerService service = CustomerService.getInstance();
    private Customer customer;
    private MyUI myUI;

    public CustomerForm(MyUI myUI) {
        this.myUI = myUI;

        status.addItems(CustomerStatus.values());

        save.addStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());


        setSizeUndefined();
        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        buttons.setSpacing(true);
        addComponents(firstName, lastName, email, status, birthDate, buttons);
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;

        BeanFieldGroup.bindFieldsUnbuffered(customer, this);
        delete.setVisible(customer.isPersisted());
        setVisible(true);
        firstName.selectAll();
    }

    private void save() {
        service.save(customer);
         myUI.updateList();
         setVisible(false);
    }

    private void delete() {
        service.delete(customer);
        myUI.updateList();
        setVisible(false);

    }
}
