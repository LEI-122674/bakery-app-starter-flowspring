package com.vaadin.starter.bakery.backend.data;

import java.util.LinkedHashMap;
import java.util.List;

import com.vaadin.starter.bakery.backend.data.entity.Product;

/**
 * Stores dashboard statistics and delivery/sales data for the bakery application.
 * Used to provide aggregate information for dashboard views.
 */
public class DashboardData {

    /** Delivery statistics summary (e.g., completed and pending deliveries). */
    private DeliveryStats deliveryStats;

    /** List containing the number of deliveries for each day of the current month. */
    private List<Number> deliveriesThisMonth;

    /** List containing the number of deliveries for each month of the current year. */
    private List<Number> deliveriesThisYear;

    /** Matrix with sales statistics per month. Each row represents a month, columns represent different sales figures. */
    private Number[][] salesPerMonth;

    /** Map of delivered products and their respective quantities. */
    private LinkedHashMap<Product, Integer> productDeliveries;

    /**
     * Returns the delivery statistics summary.
     * @return delivery statistics
     */
    public DeliveryStats getDeliveryStats() {
        return deliveryStats;
    }

    /**
     * Sets the delivery statistics summary.
     * @param deliveryStats delivery statistics
     */
    public void setDeliveryStats(DeliveryStats deliveryStats) {
        this.deliveryStats = deliveryStats;
    }

    /**
     * Returns the list of deliveries for each day of the current month.
     * @return deliveries per day for current month
     */
    public List<Number> getDeliveriesThisMonth() {
        return deliveriesThisMonth;
    }

    /**
     * Sets the list of deliveries for each day of the current month.
     * @param deliveriesThisMonth deliveries per day for current month
     */
    public void setDeliveriesThisMonth(List<Number> deliveriesThisMonth) {
        this.deliveriesThisMonth = deliveriesThisMonth;
    }

    /**
     * Returns the list of deliveries for each month of the current year.
     * @return deliveries per month for current year
     */
    public List<Number> getDeliveriesThisYear() {
        return deliveriesThisYear;
    }

    /**
     * Sets the list of deliveries for each month of the current year.
     * @param deliveriesThisYear deliveries per month for current year
     */
    public void setDeliveriesThisYear(List<Number> deliveriesThisYear) {
        this.deliveriesThisYear = deliveriesThisYear;
    }

    /**
     * Sets the sales statistics matrix per month.
     * @param salesPerMonth sales statistics per month
     */
    public void setSalesPerMonth(Number[][] salesPerMonth) {
        this.salesPerMonth = salesPerMonth;
    }

    /**
     * Returns the sales statistics for a given month.
     * @param i index of the month
     * @return sales statistics for the month
     */
    public Number[] getSalesPerMonth(int i) {
        return salesPerMonth[i];
    }

    /**
     * Returns the deliveries (quantities) for each product.
     * @return map of products and their deliveries
     */
    public LinkedHashMap<Product, Integer> getProductDeliveries() {
        return productDeliveries;
    }

    /**
     * Sets the deliveries (quantities) for each product.
     * @param productDeliveries map of products and their deliveries
     */
    public void setProductDeliveries(LinkedHashMap<Product, Integer> productDeliveries) {
        this.productDeliveries = productDeliveries;
    }

}
