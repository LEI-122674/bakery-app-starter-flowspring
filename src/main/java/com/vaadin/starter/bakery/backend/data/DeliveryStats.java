package com.vaadin.starter.bakery.backend.data;

/**
 * Stores summary statistics related to deliveries for the bakery application.
 * <p>
 * Tracks the number of deliveries completed today, deliveries due today and tomorrow,
 * unavailable deliveries, and newly placed orders.
 */
public class DeliveryStats {

	/** Number of deliveries that have been completed today. */
	private int deliveredToday;

	/** Number of deliveries scheduled for today. */
	private int dueToday;

	/** Number of deliveries scheduled for tomorrow. */
	private int dueTomorrow;

	/** Number of deliveries that are not available today (e.g., canceled or delayed). */
	private int notAvailableToday;

	/** Number of newly placed orders that have not yet been processed. */
	private int newOrders;

	/**
	 * Returns the number of deliveries completed today.
	 *
	 * @return delivered today count
	 */
	public int getDeliveredToday() {
		return deliveredToday;
	}

	/**
	 * Sets the number of deliveries completed today.
	 *
	 * @param deliveredToday delivered today count
	 */
	public void setDeliveredToday(int deliveredToday) {
		this.deliveredToday = deliveredToday;
	}

	/**
	 * Returns the number of deliveries due today.
	 *
	 * @return due today count
	 */
	public int getDueToday() {
		return dueToday;
	}

	/**
	 * Sets the number of deliveries due today.
	 *
	 * @param dueToday due today count
	 */
	public void setDueToday(int dueToday) {
		this.dueToday = dueToday;
	}

	/**
	 * Returns the number of deliveries due tomorrow.
	 *
	 * @return due tomorrow count
	 */
	public int getDueTomorrow() {
		return dueTomorrow;
	}

	/**
	 * Sets the number of deliveries due tomorrow.
	 *
	 * @param dueTomorrow due tomorrow count
	 */
	public void setDueTomorrow(int dueTomorrow) {
		this.dueTomorrow = dueTomorrow;
	}

	/**
	 * Returns the number of deliveries not available today.
	 *
	 * @return not available today count
	 */
	public int getNotAvailableToday() {
		return notAvailableToday;
	}

	/**
	 * Sets the number of deliveries not available today.
	 *
	 * @param notAvailableToday not available today count
	 */
	public void setNotAvailableToday(int notAvailableToday) {
		this.notAvailableToday = notAvailableToday;
	}

	/**
	 * Returns the number of newly placed orders.
	 *
	 * @return new orders count
	 */
	public int getNewOrders() {
		return newOrders;
	}

	/**
	 * Sets the number of newly placed orders.
	 *
	 * @param newOrders new orders count
	 */
	public void setNewOrders(int newOrders) {
		this.newOrders = newOrders;
	}
}
