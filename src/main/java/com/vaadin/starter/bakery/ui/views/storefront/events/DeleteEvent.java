package com.vaadin.starter.bakery.ui.views.storefront.events;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.starter.bakery.ui.views.orderedit.OrderItemEditor;

/**
 * Event triggered when a delete action is performed on an {@link OrderItemEditor}.
 * <p>
 * This event is typically used to signal that an order item should be removed
 * from the current order.
 */
public class DeleteEvent extends ComponentEvent<OrderItemEditor> {

    /**
     * Creates a new {@code DeleteEvent}.
     *
     * @param component the source {@link OrderItemEditor} where the delete action occurred
     */
    public DeleteEvent(OrderItemEditor component) {
        super(component, false);
    }
}
