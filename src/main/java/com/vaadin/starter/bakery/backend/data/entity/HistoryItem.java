package com.vaadin.starter.bakery.backend.data.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.vaadin.starter.bakery.backend.data.OrderState;

/**
 * Entity that represents a historical record of changes to an order or related process.
 * <p>
 * Each history item contains information about:
 * <ul>
 *   <li>The new {@link #newState} of the order (if applicable)</li>
 *   <li>A descriptive {@link #message}</li>
 *   <li>The {@link #timestamp} of the event</li>
 *   <li>The {@link #createdBy user} who created the record</li>
 * </ul>
 * </p>
 */
@Entity
public class HistoryItem extends AbstractEntity {

	/**
	 * The new state of the order after this history event.
	 * <p>
	 * May be {@code null} if the event is informational only.
	 * </p>
	 */
	private OrderState newState;

	/**
	 * A message describing the event.
	 * <p>
	 * Cannot be blank and must not exceed 255 characters.
	 * </p>
	 */
	@NotBlank
	@Size(max = 255)
	private String message;

	/**
	 * The timestamp of when the event occurred.
	 * <p>
	 * Cannot be {@code null}. Automatically initialized when created using
	 * the {@link #HistoryItem(User, String)} constructor.
	 * </p>
	 */
	@NotNull
	private LocalDateTime timestamp;

	/**
	 * The user who created this history item.
	 * <p>
	 * Cannot be {@code null}.
	 * </p>
	 */
	@ManyToOne
	@NotNull
	private User createdBy;

	/**
	 * Default constructor required by JPA and Spring Data.
	 * <p>
	 * Should not be used directly in application code.
	 * </p>
	 */
	HistoryItem() {
		// Empty constructor is needed by Spring Data / JPA
	}

	/**
	 * Creates a new history item with the given creator and message.
	 * <p>
	 * The {@link #timestamp} is automatically set to the current time.
	 * </p>
	 *
	 * @param createdBy the user who created the history item
	 * @param message   a description of the event
	 */
	public HistoryItem(User createdBy, String message) {
		this.createdBy = createdBy;
		this.message = message;
		this.timestamp = LocalDateTime.now();
	}

	/**
	 * Returns the new state of the order.
	 *
	 * @return the new order state, or {@code null} if none
	 */
	public OrderState getNewState() {
		return newState;
	}

	/**
	 * Sets the new state of the order.
	 *
	 * @param newState the new state to set
	 */
	public void setNewState(OrderState newState) {
		this.newState = newState;
	}

	/**
	 * Returns the descriptive message of the history event.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the descriptive message of the history event.
	 *
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Returns the timestamp when the history item was created.
	 *
	 * @return the timestamp
	 */
	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the timestamp of the history item.
	 *
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Returns the user who created this history item.
	 *
	 * @return the creator of the history item
	 */
	public User getCreatedBy() {
		return createdBy;
	}

	/**
	 * Sets the user who created this history item.
	 *
	 * @param createdBy the user to set
	 */
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}
}
