package com.vaadin.starter.bakery.ui.views.storefront.events;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.starter.bakery.ui.views.orderedit.OrderItemsEditor;

/**
 * Event that represents a change in the total price within the {@link OrderItemsEditor} component.
 * <p>
 * This event is fired whenever the total price of the order items is updated.
 * </p>
 */
public class TotalPriceChangeEvent extends ComponentEvent<OrderItemsEditor> {

    private final Integer totalPrice;

    /**
     * Creates a new {@code TotalPriceChangeEvent}.
     *
     * @param component the {@link OrderItemsEditor} instance that fired the event
     * @param totalPrice the new total price of the order items
     */
    public TotalPriceChangeEvent(OrderItemsEditor component, Integer totalPrice) {
        super(component, false);
        this.totalPrice = totalPrice;
    }

    /**
     * Returns the updated total price associated with this event.
     *
     * @return the new total price
     */
    public Integer getTotalPrice() {
        return totalPrice;
    }

}
