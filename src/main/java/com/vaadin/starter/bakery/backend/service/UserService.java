package com.vaadin.starter.bakery.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.repositories.UserRepository;

/**
 * Service class for managing {@link User} entities.
 * <p>
 * Provides CRUD operations, filtering, and pagination for users stored in
 * the {@link UserRepository}. It also enforces security rules such as:
 * <ul>
 *   <li>Preventing modification or deletion of locked users.</li>
 *   <li>Preventing users from deleting their own account.</li>
 * </ul>
 * </p>
 *
 * <p>
 * This service implements {@link FilterableCrudService}, making it compatible
 * with Vaadin’s data handling components while supporting advanced filtering.
 * </p>
 *
 * @author
 * @version 1.0
 * @since 1.0
 */
@Service
public class UserService implements FilterableCrudService<User> {

	/** Error message when modifying or deleting a locked user. */
	public static final String MODIFY_LOCKED_USER_NOT_PERMITTED =
			"User has been locked and cannot be modified or deleted";

	/** Error message when a user attempts to delete their own account. */
	private static final String DELETING_SELF_NOT_PERMITTED =
			"You cannot delete your own account";

	private final UserRepository userRepository;

	/**
	 * Constructs a new {@code UserService} with the provided repository.
	 *
	 * @param userRepository the repository used to access {@link User} data
	 */
	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Finds users that match a given filter string. The search is case-insensitive
	 * and matches against email, first name, last name, or role.
	 *
	 * @param filter   an optional string used to filter users
	 * @param pageable pagination information
	 * @return a page of {@link User} entities matching the filter
	 */
	public Page<User> findAnyMatching(Optional<String> filter, Pageable pageable) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return getRepository()
					.findByEmailLikeIgnoreCaseOrFirstNameLikeIgnoreCaseOrLastNameLikeIgnoreCaseOrRoleLikeIgnoreCase(
							repositoryFilter, repositoryFilter, repositoryFilter, repositoryFilter, pageable);
		} else {
			return find(pageable);
		}
	}

	/**
	 * Counts the number of users that match a given filter string.
	 *
	 * @param filter an optional string used to filter users
	 * @return the number of users matching the filter
	 */
	@Override
	public long countAnyMatching(Optional<String> filter) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return userRepository
					.countByEmailLikeIgnoreCaseOrFirstNameLikeIgnoreCaseOrLastNameLikeIgnoreCaseOrRoleLikeIgnoreCase(
							repositoryFilter, repositoryFilter, repositoryFilter, repositoryFilter);
		} else {
			return count();
		}
	}

	/**
	 * Returns the underlying JPA repository used by this service.
	 *
	 * @return the {@link UserRepository} instance
	 */
	@Override
	public UserRepository getRepository() {
		return userRepository;
	}

	/**
	 * Retrieves a page of all users without filtering.
	 *
	 * @param pageable pagination information
	 * @return a page of {@link User} entities
	 */
	public Page<User> find(Pageable pageable) {
		return getRepository().findBy(pageable);
	}

	/**
	 * Saves a {@link User} entity.
	 * <p>
	 * Locked users cannot be modified. If the user is locked,
	 * a {@link UserFriendlyDataException} is thrown.
	 * </p>
	 *
	 * @param currentUser the user performing the save operation
	 * @param entity      the user entity to save
	 * @return the saved {@link User} entity
	 * @throws UserFriendlyDataException if the user is locked
	 */
	@Override
	public User save(User currentUser, User entity) {
		throwIfUserLocked(entity);
		return getRepository().saveAndFlush(entity);
	}

	/**
	 * Deletes a {@link User} entity.
	 * <p>
	 * This method prevents:
	 * <ul>
	 *   <li>A user from deleting their own account.</li>
	 *   <li>Deletion of locked users.</li>
	 * </ul>
	 * </p>
	 *
	 * @param currentUser  the user performing the delete operation
	 * @param userToDelete the user to delete
	 * @throws UserFriendlyDataException if attempting to delete self or a locked user
	 */
	@Override
	@Transactional
	public void delete(User currentUser, User userToDelete) {
		throwIfDeletingSelf(currentUser, userToDelete);
		throwIfUserLocked(userToDelete);
		FilterableCrudService.super.delete(currentUser, userToDelete);
	}

	/**
	 * Throws an exception if a user attempts to delete their own account.
	 *
	 * @param currentUser the currently logged-in user
	 * @param user        the user to be deleted
	 * @throws UserFriendlyDataException if attempting to delete self
	 */
	private void throwIfDeletingSelf(User currentUser, User user) {
		if (currentUser.equals(user)) {
			throw new UserFriendlyDataException(DELETING_SELF_NOT_PERMITTED);
		}
	}

	/**
	 * Throws an exception if the given user is locked.
	 *
	 * @param entity the user entity
	 * @throws UserFriendlyDataException if the user is locked
	 */
	private void throwIfUserLocked(User entity) {
		if (entity != null && entity.isLocked()) {
			throw new UserFriendlyDataException(MODIFY_LOCKED_USER_NOT_PERMITTED);
		}
	}

	/**
	 * Creates a new {@link User} instance.
	 *
	 * @param currentUser the user creating the entity (not currently used)
	 * @return a new {@link User} instance
	 */
	@Override
	public User createNew(User currentUser) {
		return new User();
	}

}
