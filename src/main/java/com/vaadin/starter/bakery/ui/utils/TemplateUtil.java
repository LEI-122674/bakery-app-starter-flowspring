package com.vaadin.starter.bakery.ui.utils;

/**
 * Utility class for generating template-based URLs or route locations.
 * <p>
 * Provides helper methods for building navigation paths that may include
 * optional entity identifiers.
 * </p>
 *
 * <p>
 * This class is stateless and intended for use in UI components where
 * consistent URL generation is required.
 * </p>
 *
 * @author
 * @version 1.0
 * @since 1.0
 */
public class TemplateUtil {

	/**
	 * Generates a location string by appending an optional entity ID
	 * to a base page path.
	 * <p>
	 * If {@code entityId} is {@code null} or empty, only the {@code basePage}
	 * will be returned. Otherwise, the {@code entityId} is appended as a path
	 * segment (prefixed with a slash).
	 * </p>
	 *
	 * <pre>
	 * Examples:
	 * generateLocation("products", "123")  → "products/123"
	 * generateLocation("products", null)   → "products"
	 * </pre>
	 *
	 * @param basePage the base page path (e.g., "products", "users")
	 * @param entityId the optional entity identifier to append
	 * @return the generated location string
	 */
	public static String generateLocation(String basePage, String entityId) {
		return basePage + (entityId == null || entityId.isEmpty() ? "" : "/" + entityId);
	}
}
