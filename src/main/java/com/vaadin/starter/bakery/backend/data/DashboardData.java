package com.vaadin.starter.bakery.backend.data;

import java.util.LinkedHashMap;
import java.util.List;

import com.vaadin.starter.bakery.backend.data.entity.Product;

/**
 * Stores dashboard statistics and delivery/sales data for the bakery application.
 * <p>
 * Provides aggregate information for dashboard views, including deliveries per day/month,
 * sales per month, and product-specific delivery quantities.
 */
public class DashboardData {

    /** Delivery statistics summary (e.g., completed and pending deliveries). */
    private DeliveryStats deliveryStats;

    /** List containing the number of deliveries for each day of the current month. */
    private List<Number> deliveriesThisMonth;

    /** List containing the number of deliveries for each month of the current year. */
    private List<Number> deliveriesThisYear;

    /** Matrix with sales statistics per month. Each row represents a month; columns represent different sales figures. */
    private Number[][] salesPerMonth;

    /** Map of delivered products and their respective quantities. */
    private LinkedHashMap<Product, Integer> productDeliveries;

    /**
     * Returns the delivery statistics summary.
     *
     * @return the current delivery statistics
     */
    public DeliveryStats getDeliveryStats() {
        return deliveryStats;
    }

    /**
     * Sets the delivery statistics summary.
     *
     * @param deliveryStats the delivery statistics to set
     */
    public void setDeliveryStats(DeliveryStats deliveryStats) {
        this.deliveryStats = deliveryStats;
    }

    /**
     * Returns the list of deliveries for each day of the current month.
     *
     * @return a list of deliveries per day for the current month
     */
    public List<Number> getDeliveriesThisMonth() {
        return deliveriesThisMonth;
    }

    /**
     * Sets the list of deliveries for each day of the current month.
     *
     * @param deliveriesThisMonth the deliveries per day to set for the current month
     */
    public void setDeliveriesThisMonth(List<Number> deliveriesThisMonth) {
        this.deliveriesThisMonth = deliveriesThisMonth;
    }

    /**
     * Returns the list of deliveries for each month of the current year.
     *
     * @return a list of deliveries per month for the current year
     */
    public List<Number> getDeliveriesThisYear() {
        return deliveriesThisYear;
    }

    /**
     * Sets the list of deliveries for each month of the current year.
     *
     * @param deliveriesThisYear the deliveries per month to set for the current year
     */
    public void setDeliveriesThisYear(List<Number> deliveriesThisYear) {
        this.deliveriesThisYear = deliveriesThisYear;
    }

    /**
     * Sets the sales statistics matrix per month.
     *
     * @param salesPerMonth a 2D array representing sales statistics per month
     */
    public void setSalesPerMonth(Number[][] salesPerMonth) {
        this.salesPerMonth = salesPerMonth;
    }

    /**
     * Returns the sales statistics for a given month.
     *
     * @param monthIndex the index of the month (0-based)
     * @return an array of sales statistics for the specified month
     */
    public Number[] getSalesPerMonth(int monthIndex) {
        return salesPerMonth[monthIndex];
    }

    /**
     * Returns the deliveries (quantities) for each product.
     *
     * @return a map of products and their corresponding delivered quantities
     */
    public LinkedHashMap<Product, Integer> getProductDeliveries() {
        return productDeliveries;
    }

    /**
     * Sets the deliveries (quantities) for each product.
     *
     * @param productDeliveries a map of products and their corresponding delivered quantities
     */
    public void setProductDeliveries(LinkedHashMap<Product, Integer> productDeliveries) {
        this.productDeliveries = productDeliveries;
    }
}
