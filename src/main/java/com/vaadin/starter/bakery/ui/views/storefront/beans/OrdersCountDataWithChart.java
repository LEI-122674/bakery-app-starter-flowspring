package com.vaadin.starter.bakery.ui.views.storefront.beans;

/**
 * Extends {@link OrdersCountData} by including overall statistics
 * for use in visual representations such as charts.
 * <p>
 * In addition to the title, subtitle, and count from the base class,
 * this class also holds:
 * <ul>
 *   <li>{@code overall} – the total number of orders, used for calculating proportions.</li>
 * </ul>
 */
public class OrdersCountDataWithChart extends OrdersCountData {

    /** The overall number of orders (used as a reference for charts). */
    private Integer overall;

    /**
     * Creates an empty {@code OrdersCountDataWithChart} instance.
     * <p>
     * Fields can be set later using setters.
     */
    public OrdersCountDataWithChart() {
    }

    /**
     * Creates a new {@code OrdersCountDataWithChart} instance with the given values.
     *
     * @param title the title of the data item
     * @param subtitle the subtitle of the data item
     * @param count the count of orders for this category
     * @param overall the total number of orders
     */
    public OrdersCountDataWithChart(String title, String subtitle, Integer count, Integer overall) {
        super(title, subtitle, count);
        this.overall = overall;
    }

    /**
     * Returns the overall number of orders.
     *
     * @return the overall number of orders
     */
    public Integer getOverall() {
        return overall;
    }

    /**
     * Sets the overall number of orders.
     *
     * @param overall the overall number of orders to set
     */
    public void setOverall(Integer overall) {
        this.overall = overall;
    }
}
