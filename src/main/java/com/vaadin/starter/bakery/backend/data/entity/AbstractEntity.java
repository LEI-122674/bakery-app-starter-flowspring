package com.vaadin.starter.bakery.backend.data.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * Base class for all JPA entities in the application.
 * <p>
 * Provides a unique identifier {@link #id} and a {@link #version} field
 * for optimistic locking. This class is marked with {@link MappedSuperclass},
 * so its properties are inherited by subclasses but it is not a table itself.
 * </p>
 */
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

	/**
	 * The unique identifier for the entity.
	 * <p>
	 * It is automatically generated when the entity is persisted.
	 * </p>
	 */
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * Version number for optimistic locking.
	 * <p>
	 * Updated automatically by JPA each time the entity is modified.
	 * Used to prevent concurrent updates from overwriting each other.
	 * </p>
	 */
	@Version
	private int version;

	/**
	 * Returns the unique identifier of the entity.
	 *
	 * @return the entity ID, or {@code null} if the entity is not yet persisted
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Returns the current version of the entity.
	 *
	 * @return the version number
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Computes a hash code based on {@link #id} and {@link #version}.
	 *
	 * @return the computed hash code
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id, version);
	}

	/**
	 * Checks if this entity is equal to another object.
	 * <p>
	 * Two entities are considered equal if they are of the same type,
	 * and have the same {@link #id} and {@link #version}.
	 * </p>
	 *
	 * @param o the object to compare with
	 * @return {@code true} if the entities are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		AbstractEntity that = (AbstractEntity) o;
		return version == that.version &&
				Objects.equals(id, that.id);
	}
}
