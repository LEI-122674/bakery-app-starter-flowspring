package com.vaadin.starter.bakery.ui.views.storefront.beans;


/**
 * Represents the header information of an order card in the storefront UI.
 * <p>
 * The header contains two text fields:
 * <ul>
 *   <li>{@code main} - the primary text, usually the title or main label.</li>
 *   <li>{@code secondary} - the secondary text, usually a subtitle or additional detail.</li>
 * </ul>
 */
public class OrderCardHeader {

    /** The primary header text (e.g., title or main label). */
    private String main;

    /** The secondary header text (e.g., subtitle or additional detail). */
    private String secondary;

    /**
     * Creates a new {@code OrderCardHeader} instance with the given main and secondary text.
     *
     * @param main the main header text
     * @param secondary the secondary header text
     */
	public OrderCardHeader(String main, String secondary) {
		this.main = main;
		this.secondary = secondary;
	}

    /**
     * Returns the main header text.
     *
     * @return the main header text
     */
	public String getMain() {
		return main;
	}

    /**
     * Sets the main header text.
     *
     * @param main the main header text to set
     */
	public void setMain(String main) {
		this.main = main;
	}

    /**
     * Returns the secondary header text.
     *
     * @return the secondary header text
     */
	public String getSecondary() {
		return secondary;
	}

    /**
     * Sets the secondary header text.
     *
     * @param secondary the secondary header text to set
     */
	public void setSecondary(String secondary) {
		this.secondary = secondary;
	}
}
