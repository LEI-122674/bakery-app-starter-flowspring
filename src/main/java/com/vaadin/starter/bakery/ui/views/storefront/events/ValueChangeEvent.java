package com.vaadin.starter.bakery.ui.views.storefront.events;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.starter.bakery.ui.views.orderedit.OrderItemsEditor;

/**
 * Event representing a change in a value within the {@link OrderItemsEditor} component.
 * <p>
 * This event is fired when a value (such as quantity or selection) of order items is modified.
 * </p>
 */
public class ValueChangeEvent extends ComponentEvent<OrderItemsEditor> {

    /**
     * Creates a new {@code ValueChangeEvent}.
     *
     * @param component the {@link OrderItemsEditor} instance that fired the event
     */
    public ValueChangeEvent(OrderItemsEditor component) {
        super(component, false);
    }
}
