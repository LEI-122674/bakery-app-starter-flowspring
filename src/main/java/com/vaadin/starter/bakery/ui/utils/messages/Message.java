package com.vaadin.starter.bakery.ui.utils.messages;

/**
 * Represents a dialog message with customizable caption, message text, and button labels.
 * <p>
 * This class is used to display confirmation or informational dialogs to the user,
 * such as delete confirmations or unsaved changes warnings.
 */

public class Message {
    /** Default caption for delete confirmation dialogs. */
	public static final String CONFIRM_CAPTION_DELETE = "Confirm Delete";
    /** Default message for delete confirmation dialogs. */
    public static final String CONFIRM_MESSAGE_DELETE = "Are you sure you want to delete the selected Item? This action cannot be undone.";
    /** Default label for the delete confirmation button. */
    public static final String BUTTON_CAPTION_DELETE = "Delete";
    /** Default label for the cancel button. */
	public static final String BUTTON_CAPTION_CANCEL = "Cancel";

    /**
     * Predefined message supplier for unsaved changes warning dialogs.
     * The message supports one formatting parameter (e.g., entity name).
     */
	public static final MessageSupplier UNSAVED_CHANGES = createMessage("Unsaved Changes", "Discard", "Continue Editing",
			"There are unsaved modifications to the %s. Discard changes?");

    /**
     * Predefined message supplier for delete confirmation dialogs.
     */
	public static final MessageSupplier CONFIRM_DELETE = createMessage(CONFIRM_CAPTION_DELETE, BUTTON_CAPTION_DELETE,
			BUTTON_CAPTION_CANCEL, CONFIRM_MESSAGE_DELETE);

	private final String caption;
	private final String okText;
	private final String cancelText;
	private final String message;

    /**
     * Constructs a new {@code Message} with the specified properties.
     *
     * @param caption    the dialog title or caption
     * @param okText     the text for the confirmation button
     * @param cancelText the text for the cancel button
     * @param message    the message body to display in the dialog
     */
	public Message(String caption, String okText, String cancelText, String message) {
		this.caption = caption;
		this.okText = okText;
		this.cancelText = cancelText;
		this.message = message;
	}

    /**
     * Creates a {@link MessageSupplier} that returns a new {@link Message}
     * with the specified template strings and a formatable message.
     *
     * @param caption    the dialog caption
     * @param okText     the OK button text
     * @param cancelText the Cancel button text
     * @param message    the message body with optional format specifiers (e.g., %s)
     * @return a {@link MessageSupplier} that generates formatted messages
     */
	private static MessageSupplier createMessage(String caption, String okText, String cancelText, String message) {
		return (parameters) -> new Message(caption, okText, cancelText, String.format(message, parameters));
	}

    /**
     * Returns the caption of the message.
     *
     * @return the caption
     */
	public String getCaption() {
		return caption;
	}

    /**
     * Returns the text for the OK/confirm button.
     *
     * @return the OK button text
     */
	public String getOkText() {
		return okText;
	}

    /**
     * Returns the text for the Cancel button.
     *
     * @return the Cancel button text
     */
	public String getCancelText() {
		return cancelText;
	}

    /**
     * Returns the message body.
     *
     * @return the message
     */
	public String getMessage() {
		return message;
	}

    /**
     * Functional interface for supplying {@link Message} instances.
     * <p>
     * Implementations should return a {@link Message} with optionally formatted text
     * based on input parameters.
     */
    @FunctionalInterface
	public interface MessageSupplier {
        /**
         * Creates a new {@link Message} with optional parameters for message formatting.
         *
         * @param parameters parameters to be used in {@code String.format} for the message
         * @return a new {@link Message} instance
         */
		Message createMessage(Object... parameters);
	}

}
