package com.vaadin.starter.bakery.ui.views.storefront.events;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.starter.bakery.ui.views.orderedit.OrderDetails;

/**
 * Event representing a comment added to an order within the {@link OrderDetails} component.
 * <p>
 * This event is fired when a user submits a comment related to a specific order.
 * </p>
 */
public class CommentEvent extends ComponentEvent<OrderDetails> {

    private Long orderId;
    private String message;

    /**
     * Creates a new {@code CommentEvent}.
     *
     * @param component the {@link OrderDetails} instance that fired the event
     * @param orderId the ID of the order the comment is associated with
     * @param message the comment message
     */
    public CommentEvent(OrderDetails component, Long orderId, String message) {
        super(component, false);
        this.orderId = orderId;
        this.message = message;
    }

    /**
     * Returns the ID of the order associated with this comment.
     *
     * @return the order ID
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * Returns the comment message associated with this event.
     *
     * @return the comment message
     */
    public String getMessage() {
        return message;
    }
}
