package com.vaadin.starter.bakery.ui.utils.messages;

/**
 * Utility class that provides standardized error messages for CRUD operations.
 * <p>
 * This class contains constant string messages that can be used throughout the application
 * to display user-friendly error messages related to entity operations.
 * <p>
 * The class is final and has a private constructor to prevent instantiation.
 */
public final class CrudErrorMessage {

    /**
     * Error message displayed when the selected entity could not be found.
     */
    public static final String ENTITY_NOT_FOUND = "The selected entity was not found.";

    /**
     * Error message displayed when a concurrent update occurs.
     * <p>
     * Indicates that another user or process might have updated the entity,
     * and suggests refreshing the data and trying again.
     */
    public static final String CONCURRENT_UPDATE =
            "Somebody else might have updated the data. Please refresh and try again.";

    /**
     * Error message displayed when an operation is prevented due to database references.
     * <p>
     * Indicates that the entity cannot be deleted or modified because other entities
     * depend on it via foreign key relationships.
     */
    public static final String OPERATION_PREVENTED_BY_REFERENCES =
            "The operation can not be executed as there are references to entity in the database.";

    /**
     * Error message displayed when required fields are missing in a form.
     */
    public static final String REQUIRED_FIELDS_MISSING =
            "Please fill out all required fields before proceeding.";

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private CrudErrorMessage() {
    }

}
