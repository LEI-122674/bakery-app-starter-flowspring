package com.vaadin.starter.bakery.ui.views.storefront.events;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.starter.bakery.ui.views.orderedit.OrderItemEditor;

/**
 * Event triggered when the price of an item in an {@link OrderItemEditor} changes.
 * <p>
 * This event provides both the old and the new price values so that
 * listeners can react accordingly, for example by updating totals
 * or triggering validations.
 */
public class PriceChangeEvent extends ComponentEvent<OrderItemEditor> {

    /** The previous price before the change. */
    private final int oldValue;

    /** The updated price after the change. */
    private final int newValue;

    /**
     * Creates a new {@code PriceChangeEvent}.
     *
     * @param component the source {@link OrderItemEditor} where the price was changed
     * @param oldValue the price before the change
     * @param newValue the new price after the change
     */
    public PriceChangeEvent(OrderItemEditor component, int oldValue, int newValue) {
        super(component, false);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * Returns the price before the change.
     *
     * @return the old price
     */
    public int getOldValue() {
        return oldValue;
    }

    /**
     * Returns the updated price after the change.
     *
     * @return the new price
     */
    public int getNewValue() {
        return newValue;
    }
}
