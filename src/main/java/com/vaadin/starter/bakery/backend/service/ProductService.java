package com.vaadin.starter.bakery.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.repositories.ProductRepository;


/**
 * Service class for managing {@link Product} entities.
 * <p>
 * Provides CRUD operations, filtering, and pagination for products stored in
 * the {@link ProductRepository}. It also ensures unique product names and
 * handles user-friendly error messages in case of data integrity violations.
 * </p>
 * 
 * <p>
 * This service implements {@link FilterableCrudService}, making it compatible
 * with Vaadin’s data handling components while supporting search functionality
 * with filters.
 * </p>
 * 
 * @author LEI-122674
 * @version 1.0
 */
@Service
public class ProductService implements FilterableCrudService<Product> {

	private final ProductRepository productRepository;

	/**
	 * Constructs a new {@link ProductService} with the provided repository.
	 *
	 * @param productRepository the repository used to access {@link Product} data
	 */
	@Autowired
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	/**
	 * Finds products that match a given filter string. The search is case-insensitive
	 * and matches product names partially.
	 *
	 * @param filter   an optional string used to filter product names
	 * @param pageable pagination information
	 * @return a page of {@link Product} entities matching the filter
	 */
	@Override
	public Page<Product> findAnyMatching(Optional<String> filter, Pageable pageable) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return productRepository.findByNameLikeIgnoreCase(repositoryFilter, pageable);
		} else {
			return find(pageable);
		}
	}

	/**
	 * Counts the number of products that match a given filter string.
	 *
	 * @param filter an optional string used to filter product names
	 * @return the number of products matching the filter
	 */
	@Override
	public long countAnyMatching(Optional<String> filter) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return productRepository.countByNameLikeIgnoreCase(repositoryFilter);
		} else {
			return count();
		}
	}

	/**
	 * Retrieves a page of all products without filtering.
	 *
	 * @param pageable pagination information
	 * @return a page of {@link Product} entities
	 */
	public Page<Product> find(Pageable pageable) {
		return productRepository.findBy(pageable);
	}

	/**
	 * Returns the underlying JPA repository used by this service.
	 *
	 * @return the {@link ProductRepository} instance
	 */
	@Override
	public JpaRepository<Product, Long> getRepository() {
		return productRepository;
	}

	/**
	 * Creates a new instance of {@link Product}. This method can be extended to
	 * initialize default values if necessary.
	 *
	 * @param currentUser the user creating the product (not currently used)
	 * @return a new {@link Product} instance
	 */
	@Override
	public Product createNew(User currentUser) {
		return new Product();
	}

	/**
	 * Saves a {@link Product} entity. If a product with the same name already
	 * exists, a {@link UserFriendlyDataException} is thrown with a descriptive
	 * message for the end-user.
	 *
	 * @param currentUser the user saving the product (not currently used)
	 * @param entity      the product entity to save
	 * @return the saved {@link Product} entity
	 * @throws UserFriendlyDataException if a product with the same name already exists
	 */
	@Override
	public Product save(User currentUser, Product entity) {
		try {
			return FilterableCrudService.super.save(currentUser, entity);
		} catch (DataIntegrityViolationException e) {
			throw new UserFriendlyDataException(
					"There is already a product with that name. Please select a unique name for the product.");
		}
	}

}

