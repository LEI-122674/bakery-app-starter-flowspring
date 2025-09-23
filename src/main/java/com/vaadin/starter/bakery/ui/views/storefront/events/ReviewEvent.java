package com.vaadin.starter.bakery.ui.views.storefront.events;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.starter.bakery.ui.views.orderedit.OrderEditor;

/**
 * Event representing a request to review an order within the {@link OrderEditor} component.
 * <p>
 * This event can be fired when the user initiates a review action, such as confirming
 * the contents of the order before finalizing it.
 * </p>
 */
public class ReviewEvent extends ComponentEvent<OrderEditor> {

    /**
     * Creates a new {@code ReviewEvent}.
     *
     * @param component the {@link OrderEditor} instance that fired the event
     */
    public ReviewEvent(OrderEditor component) {
        super(component, false);
    }
}
