package com.vaadin.starter.bakery.ui.views.storefront.converters;

import java.io.Serializable;

/**
 * Represents a formatted date object for use in the storefront UI.
 * <p>
 * This class provides a simplified structure with three string-based fields:
 * <ul>
 *   <li>{@code day} – the day of the month (e.g., "15").</li>
 *   <li>{@code weekday} – the day of the week (e.g., "Monday").</li>
 *   <li>{@code date} – the full formatted date string (e.g., "2025-09-18").</li>
 * </ul>
 * <p>
 * Implements {@link Serializable} to support use in UI components
 * that may require serialization.
 */
public class StorefrontDate implements Serializable {

    /** The day of the month (e.g., "15"). */
    private String day;

    /** The day of the week (e.g., "Monday"). */
    private String weekday;

    /** The full formatted date string (e.g., "2025-09-18"). */
    private String date;

    /**
     * Returns the full formatted date string.
     *
     * @return the formatted date string
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the full formatted date string.
     *
     * @param date the formatted date string to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Returns the day of the month.
     *
     * @return the day of the month
     */
    public String getDay() {
        return day;
    }

    /**
     * Sets the day of the month.
     *
     * @param day the day of the month to set
     */
    public void setDay(String day) {
        this.day = day;
    }

    /**
     * Returns the weekday.
     *
     * @return the weekday
     */
    public String getWeekday() {
        return weekday;
    }

    /**
     * Sets the weekday.
     *
     * @param weekday the weekday to set
     */
    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }
}
