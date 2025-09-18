package com.vaadin.starter.bakery.ui.views.storefront.events;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.ui.views.orderedit.OrderItemEditor;

/**
 * Event that represents a change in a product within the {@link OrderItemEditor} component.
 * <p>
 * This event is fired when a product is modified or selected in the editor.
 * </p>
 */
public class ProductChangeEvent extends ComponentEvent<OrderItemEditor> {

    private final Product product;

    /**
     * Creates a new {@code ProductChangeEvent}.
     *
     * @param component the {@link OrderItemEditor} instance that fired the event
     * @param product the {@link Product} that was changed
     */
    public ProductChangeEvent(OrderItemEditor component, Product product) {
        super(component, false);
        this.product = product;
    }

    /**
     * Returns the {@link Product} associated with this event.
     *
     * @return the changed product
     */
    public Product getProduct() {
        return product;
    }

}
