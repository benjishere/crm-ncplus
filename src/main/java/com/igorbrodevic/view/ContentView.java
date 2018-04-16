package com.igorbrodevic.view;

import com.google.common.eventbus.Subscribe;
import com.igorbrodevic.CRMUI;
import com.igorbrodevic.event.CRMEventBus;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.ValoTheme;
import com.igorbrodevic.event.CRMEvent.NotificationsCountUpdatedEvent;


public final class ContentView extends Panel implements View {

    private NotificationsButton notificationsButton;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        //notificationsButton.updateNotificationsCount(null);
    }

    public interface DashboardEditListener {
        void dashboardNameEdited(String name);
    }

    public static final class NotificationsButton extends Button {
        private static final String STYLE_UNREAD = "unread";
        public static final String ID = "dashboard-notifications";

        public NotificationsButton() {
            setIcon(FontAwesome.BELL);
            setId(ID);
            addStyleName("notifications");
            addStyleName(ValoTheme.BUTTON_ICON_ONLY);
            CRMEventBus.register(this);
        }

        /*@Subscribe
        public void updateNotificationsCount(
                final NotificationsCountUpdatedEvent event) {
            setUnreadCount(CRMUI.getDataProvider()
                    .getUnreadNotificationsCount());
        }*/

        public void setUnreadCount(final int count) {
            setCaption(String.valueOf(count));

            String description = "Notifications";
            if (count > 0) {
                addStyleName(STYLE_UNREAD);
                description += " (" + count + " unread)";
            } else {
                removeStyleName(STYLE_UNREAD);
            }
            setDescription(description);
        }
    }
    
}
