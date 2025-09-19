package com.vaadin.starter.bakery.ui.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

/**
 * Event that is fired when a validation operation fails on a UI component.
 * <p>
 * This event extends {@link ComponentEvent} and is triggered when the
 * component's data fails validation rules. Typically used to provide
 * feedback to the user or trigger error handling logic.
 * </p>
 */
public class ValidationFailedEvent extends ComponentEvent<Component> {

    /**
     * Creates a new {@code ValidationFailedEvent}.
     * <p>
     * This event is always considered to originate from the server-side.
     * </p>
     *
     * @param source the component that fired the event
     */
    public ValidationFailedEvent(Component source) {
        super(source, false);
    }
}
