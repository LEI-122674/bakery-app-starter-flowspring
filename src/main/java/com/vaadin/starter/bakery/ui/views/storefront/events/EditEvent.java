package com.vaadin.starter.bakery.ui.views.storefront.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

/**
 * Event triggered when an edit action is performed on a UI {@link Component}.
 * <p>
 * This event can be used to notify that the user has initiated an edit
 * operation, for example on an order item or another editable element
 * in the storefront.
 */
public class EditEvent extends ComponentEvent<Component> {

    /**
     * Creates a new {@code EditEvent}.
     *
     * @param source the {@link Component} where the edit action occurred
     */
    public EditEvent(Component source) {
        super(source, false);
    }
}
