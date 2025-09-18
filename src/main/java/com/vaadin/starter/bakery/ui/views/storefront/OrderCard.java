package com.vaadin.starter.bakery.ui.views.storefront;

import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.HOUR_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.MONTH_AND_DAY_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.SHORT_DAY_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.WEEKDAY_FULLNAME_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.WEEK_OF_YEAR_FIELD;

import java.time.LocalDate;
import java.util.List;

import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.OrderItem;
import com.vaadin.starter.bakery.backend.data.entity.OrderSummary;

/**
 * Represents a UI card for displaying order information in grids
 * such as Storefront and Dashboard.
 * <p>
 * Uses {@link LitRenderer} to reduce CPU and memory usage compared
 * to {@link com.vaadin.flow.data.renderer.ComponentRenderer}.
 * The card may optionally include a header to group orders visually by date or category.
 */
public class OrderCard {


    /**
     * Creates a {@link LitRenderer} template for displaying an order card in a grid.
     * The template binds the order data and an optional header,
     * and supports click events on the card.
     *
     * @return a {@link LitRenderer} template for Order objects.
     */
	public static LitRenderer<Order> getTemplate() {
		return LitRenderer.of(
				  "<order-card"
				+ "  .header='${item.header}'"
				+ "  .orderCard='${item.orderCard}'"
				+ "  @card-click='${cardClick}'>"
				+ "</order-card>");
	}

    /**
     * Factory method to create an {@link OrderCard} instance from an {@link OrderSummary}.
     *
     * @param order the order summary to wrap.
     * @return a new {@link OrderCard} instance.
     */
	public static OrderCard create(OrderSummary order) {
		return new OrderCard(order);
	}

	private boolean recent, inWeek;

	private final OrderSummary order;

    /**
     * Constructs an {@link OrderCard} from an {@link OrderSummary}.
     * Determines if the order is considered "recent" (today or yesterday)
     * or falls within the current week.
     *
     * @param order the order summary to wrap.
     */
	public OrderCard(OrderSummary order) {
		this.order = order;
		LocalDate now = LocalDate.now();
		LocalDate date = order.getDueDate();
		recent = date.equals(now) || date.equals(now.minusDays(1));
		inWeek = !recent && now.getYear() == date.getYear() && now.get(WEEK_OF_YEAR_FIELD) == date.get(WEEK_OF_YEAR_FIELD);
	}

    /**
     * Returns the pickup location name for orders that are recent or in the same week.
     *
     * @return the pickup location name, or null if not applicable.
     */
	public String getPlace() {
		return recent || inWeek ? order.getPickupLocation().getName() : null;
	}

    /**
     * Returns the formatted due time for recent orders.
     *
     * @return the due time as a string, or null if the order is not recent.
     */
	public String getTime() {
		return recent ? HOUR_FORMATTER.format(order.getDueTime()) : null;
	}

    /**
     * Returns a short day string (e.g., Mon, Tue) for orders within the current week.
     *
     * @return a short day string, or null if the order is not in the same week.
     */
	public String getShortDay() {
		return inWeek ? SHORT_DAY_FORMATTER.format(order.getDueDate()) : null;
	}

    /**
     * Returns the formatted due time for orders within the current week.
     *
     * @return the due time as a string, or null if the order is not in the same week.
     */
	public String getSecondaryTime() {
		return inWeek ? HOUR_FORMATTER.format(order.getDueTime()) : null;
	}

    /**
     * Returns the formatted month and day for orders not recent and not in the current week.
     *
     * @return the month and day as a string, or null if the order is recent or in the same week.
     */
	public String getMonth() {
		return recent || inWeek ? null : MONTH_AND_DAY_FORMATTER.format(order.getDueDate());
	}

    /**
     * Returns the full weekday name for orders not recent and not in the current week.
     *
     * @return the full weekday name, or null if the order is recent or in the same week.
     */
	public String getFullDay() {
		return recent || inWeek ? null : WEEKDAY_FULLNAME_FORMATTER.format(order.getDueDate());
	}

    /**
     * Returns the current state of the order as a string.
     *
     * @return the order state.
     */
	public String getState() {
		return order.getState().toString();
	}


    /**
     * Returns the full name of the customer who placed the order.
     *
     * @return the customer's full name.
     */
	public String getFullName() {
		return order.getCustomer().getFullName();
	}

    /**
     * Returns the list of items included in the order.
     *
     * @return a list of {@link OrderItem} objects.
     */
	public List<OrderItem> getItems() {
		return order.getItems();
	}
}
