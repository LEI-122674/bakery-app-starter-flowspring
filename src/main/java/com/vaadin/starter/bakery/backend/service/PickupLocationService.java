package com.vaadin.starter.bakery.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.vaadin.starter.bakery.backend.data.entity.PickupLocation;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.repositories.PickupLocationRepository;

/**
 * Service class for managing {@link PickupLocation} entities.
 * <p>
 * Provides CRUD operations, search, and default location retrieval.
 * </p>
 */
@Service
public class PickupLocationService implements FilterableCrudService<PickupLocation> {

	private final PickupLocationRepository pickupLocationRepository;

	/**
	 * Creates a new {@code PickupLocationService}.
	 *
	 * @param pickupLocationRepository the repository for pickup locations
	 */
	@Autowired
	public PickupLocationService(PickupLocationRepository pickupLocationRepository) {
		this.pickupLocationRepository = pickupLocationRepository;
	}

	/**
	 * Finds pickup locations matching an optional filter.
	 * <p>
	 * If a filter is provided, it performs a case-insensitive "like" search
	 * against location names. If not, all locations are returned.
	 * </p>
	 *
	 * @param filter   optional filter string
	 * @param pageable paging information
	 * @return a page of matching pickup locations
	 */
	public Page<PickupLocation> findAnyMatching(Optional<String> filter, Pageable pageable) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return pickupLocationRepository.findByNameLikeIgnoreCase(repositoryFilter, pageable);
		} else {
			return pickupLocationRepository.findAll(pageable);
		}
	}

	/**
	 * Counts pickup locations matching an optional filter.
	 * <p>
	 * If a filter is provided, it counts case-insensitive matches against
	 * location names. If not, counts all locations.
	 * </p>
	 *
	 * @param filter optional filter string
	 * @return the number of matching pickup locations
	 */
	public long countAnyMatching(Optional<String> filter) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return pickupLocationRepository.countByNameLikeIgnoreCase(repositoryFilter);
		} else {
			return pickupLocationRepository.count();
		}
	}

	/**
	 * Retrieves the default pickup location.
	 * <p>
	 * The default is defined as the first available location (page size 1).
	 * </p>
	 *
	 * @return the default pickup location
	 */
	public PickupLocation getDefault() {
		return findAnyMatching(Optional.empty(), PageRequest.of(0, 1)).iterator().next();
	}

	/**
	 * Returns the JPA repository backing this service.
	 *
	 * @return the pickup location repository
	 */
	@Override
	public JpaRepository<PickupLocation, Long> getRepository() {
		return pickupLocationRepository;
	}

	/**
	 * Creates a new pickup location instance.
	 *
	 * @param currentUser the user creating the location (not used here)
	 * @return a new pickup location
	 */
	@Override
	public PickupLocation createNew(User currentUser) {
		return new PickupLocation();
	}
}
