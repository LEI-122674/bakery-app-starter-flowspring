package com.vaadin.starter.bakery.backend.data.entity;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Entity that represents a customer of the application.
 * <p>
 * A customer has a {@link #fullName}, a {@link #phoneNumber}, and optional
 * {@link #details}. Validation constraints ensure that values are not blank
 * and follow formatting rules where applicable.
 * </p>
 */
@Entity
public class Customer extends AbstractEntity {

	/**
	 * The full name of the customer.
	 * <p>
	 * Cannot be blank and must not exceed 255 characters.
	 * </p>
	 */
	@NotBlank
	@Size(max = 255)
	private String fullName;

	/**
	 * The phone number of the customer.
	 * <p>
	 * Cannot be blank and must not exceed 20 characters.
	 * A simple phone number validation allows:
	 * <ul>
	 *   <li>An optional international prefix (starting with +)</li>
	 *   <li>Digits separated by spaces or dashes</li>
	 *   <li>A total of 4–14 digits</li>
	 * </ul>
	 * Example valid formats: {@code +123 456 789}, {@code 555-1234}.
	 * </p>
	 */
	@NotBlank
	@Size(max = 20, message = "{bakery.phone.number.invalid}")
	@Pattern(regexp = "^(\\+\\d+)?([ -]?\\d+){4,14}$", message = "{bakery.phone.number.invalid}")
	private String phoneNumber;

	/**
	 * Additional details about the customer.
	 * <p>
	 * Optional field with a maximum length of 255 characters.
	 * </p>
	 */
	@Size(max = 255)
	private String details;

	/**
	 * Returns the full name of the customer.
	 *
	 * @return the full name
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * Sets the full name of the customer.
	 *
	 * @param fullName the name to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * Returns the phone number of the customer.
	 *
	 * @return the phone number
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * Sets the phone number of the customer.
	 *
	 * @param phoneNumber the phone number to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * Returns additional details about the customer.
	 *
	 * @return the details, or {@code null} if none
	 */
	public String getDetails() {
		return details;
	}

	/**
	 * Sets additional details about the customer.
	 *
	 * @param details the details to set
	 */
	public void setDetails(String details) {
		this.details = details;
	}
}
