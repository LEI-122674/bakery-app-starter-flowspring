package com.vaadin.starter.bakery.ui.views.storefront.events;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.starter.bakery.ui.views.orderedit.OrderItemEditor;

/**
 * Event triggered when the comment of an {@link OrderItemEditor} changes.
 * <p>
 * This event carries the updated comment value entered by the user.
 */
public class CommentChangeEvent extends ComponentEvent<OrderItemEditor> {

    /** The updated comment text associated with the order item. */
    private final String comment;

    /**
     * Creates a new {@code CommentChangeEvent}.
     *
     * @param component the source {@link OrderItemEditor} where the comment was changed
     * @param comment the updated comment text
     */
    public CommentChangeEvent(OrderItemEditor component, String comment) {
        super(component, false);
        this.comment = comment;
    }

    /**
     * Returns the updated comment text.
     *
     * @return the updated comment
     */
    public String getComment() {
        return comment;
    }
}
