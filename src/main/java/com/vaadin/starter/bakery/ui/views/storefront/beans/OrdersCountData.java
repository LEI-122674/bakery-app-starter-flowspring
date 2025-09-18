package com.vaadin.starter.bakery.ui.views.storefront.beans;

/**
 * Represents statistical information about orders,
 * typically used for displaying counts in the storefront UI.
 * <p>
 * Each instance contains:
 * <ul>
 *   <li>{@code title} – the main label for the data.</li>
 *   <li>{@code subtitle} – an additional descriptive text.</li>
 *   <li>{@code count} – the numeric value representing the number of orders.</li>
 * </ul>
 */
public class OrdersCountData {

    /** The main label for this data item. */
    private String title;

    /** The additional descriptive text for this data item. */
    private String subtitle;

    /** The number of orders represented by this data item. */
    private Integer count;

    /**
     * Returns the title of this data item.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this data item.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the subtitle of this data item.
     *
     * @return the subtitle
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * Sets the subtitle of this data item.
     *
     * @param subtitle the subtitle to set
     */
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    /**
     * Returns the count of orders represented by this data item.
     *
     * @return the count of orders
     */
    public Integer getCount() {
        return count;
    }

    /**
     * Sets the count of orders represented by this data item.
     *
     * @param count the count to set
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * Creates an empty {@code OrdersCountData} instance.
     * <p>
     * Fields can be set later using setters.
     */
    public OrdersCountData() {
    }

    /**
     * Creates a new {@code OrdersCountData} instance with the given values.
     *
     * @param title the title of this data item
     * @param subtitle the subtitle of this data item
     * @param count the number of orders
     */
    public OrdersCountData(String title, String subtitle, Integer count) {
        this.title = title;
        this.subtitle = subtitle;
        this.count = count;
    }
}
