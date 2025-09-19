package com.vaadin.starter.bakery.ui.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

/**
 * Event that is fired when a delete action occurs in a UI component.
 * <p>
 * This event extends {@link ComponentEvent} and can be used to listen
 * for user-initiated or programmatic delete actions.
 * </p>
 */
public class DeleteEvent extends ComponentEvent<Component> {

    /**
     * Creates a new {@code DeleteEvent}.
     *
     * @param source     the component that fired the event
     * @param fromClient {@code true} if the event originated from the client-side, {@code false} if from the server-side
     */
    public DeleteEvent(Component source, boolean fromClient) {
        super(source, fromClient);
    }
}
