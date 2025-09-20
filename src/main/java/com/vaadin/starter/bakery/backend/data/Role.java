package com.vaadin.starter.bakery.backend.data;

/**
 * Defines the user roles for the bakery application.
 * <p>
 * Roles determine access permissions for views and actions within the system.
 * This class contains only static constants and utility methods and cannot be instantiated.
 */
public class Role {

	/** Role representing a barista user with limited access to relevant views. */
	public static final String BARISTA = "barista";

	/** Role representing a baker user with limited access to relevant views. */
	public static final String BAKER = "baker";

	/**
	 * Role representing an administrator user.
	 * <p>
	 * This role implicitly allows access to all views and actions.
	 */
	public static final String ADMIN = "admin";

	/**
	 * Private constructor to prevent instantiation.
	 * <p>
	 * This class is intended to be used only via its static fields and methods.
	 */
	private Role() {
		// Static methods and fields only
	}

	/**
	 * Returns an array containing all defined roles.
	 *
	 * @return array of all role names
	 */
	public static String[] getAllRoles() {
		return new String[] { BARISTA, BAKER, ADMIN };
	}
}
