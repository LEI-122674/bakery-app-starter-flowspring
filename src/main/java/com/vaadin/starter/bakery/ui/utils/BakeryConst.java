package com.vaadin.starter.bakery.ui.utils;

import java.util.Locale;

import org.springframework.data.domain.Sort;

/**
 * Centralized application constants used throughout the Bakery app.
 * <p>
 * This class defines application-wide constants for:
 * <ul>
 *   <li>Localization</li>
 *   <li>Page routes and URL templates</li>
 *   <li>UI titles</li>
 *   <li>Sorting defaults</li>
 *   <li>Viewport settings</li>
 *   <li>Notification configurations</li>
 * </ul>
 * </p>
 *
 * <p>
 * Constants in this class are intended to be shared across the UI and
 * service layers to ensure consistency in navigation, sorting,
 * and display logic.
 * </p>
 *
 * @author
 * @version 1.0
 * @since 1.0
 */
public class BakeryConst {

	/** Default locale for the application (US English). */
	public static final Locale APP_LOCALE = Locale.US;

	/** Route parameter name for order IDs. */
	public static final String ORDER_ID = "orderID";

	/** URL segment used for edit routes. */
	public static final String EDIT_SEGMENT = "edit";

	// ------------------------
	// Page route definitions
	// ------------------------

	/** Root page route. */
	public static final String PAGE_ROOT = "";

	/** Storefront page route. */
	public static final String PAGE_STOREFRONT = "storefront";

	/** Storefront route template with optional order ID. */
	public static final String PAGE_STOREFRONT_ORDER_TEMPLATE =
			PAGE_STOREFRONT + "/:" + ORDER_ID + "?";

	/** Storefront route template for editing an order. */
	public static final String PAGE_STOREFRONT_ORDER_EDIT_TEMPLATE =
			PAGE_STOREFRONT + "/:" + ORDER_ID + "/" + EDIT_SEGMENT;

	/** Formatted storefront edit route (with order ID). */
	public static final String PAGE_STOREFRONT_ORDER_EDIT =
			"storefront/%d/edit";

	/** Dashboard page route. */
	public static final String PAGE_DASHBOARD = "dashboard";

	/** Users page route. */
	public static final String PAGE_USERS = "users";

	/** Products page route. */
	public static final String PAGE_PRODUCTS = "products";

	// ------------------------
	// UI Titles
	// ------------------------

	/** Title for storefront views. */
	public static final String TITLE_STOREFRONT = "Storefront";

	/** Title for dashboard views. */
	public static final String TITLE_DASHBOARD = "Dashboard";

	/** Title for users view. */
	public static final String TITLE_USERS = "Users";

	/** Title for products view. */
	public static final String TITLE_PRODUCTS = "Products";

	/** Title for logout option. */
	public static final String TITLE_LOGOUT = "Logout";

	/** Title displayed when a page is not found. */
	public static final String TITLE_NOT_FOUND = "Page was not found";

	// ------------------------
	// Sorting
	// ------------------------

	/** Default fields for sorting orders. */
	public static final String[] ORDER_SORT_FIELDS = {"dueDate", "dueTime", "id"};

	/** Default sort direction for order listings. */
	public static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.ASC;

	// ------------------------
	// UI / Display
	// ------------------------

	/** Default viewport settings for responsive UI design. */
	public static final String VIEWPORT =
			"width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover";

	/** Default notification display duration (in milliseconds). Mutable for testing. */
	public static int NOTIFICATION_DURATION = 4000;
}
