package com.vaadin.starter.bakery.ui.crud;

import java.util.List;

import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.backend.service.FilterableCrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;

import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.QuerySortOrderBuilder;

/**
 * A data provider for CRUD entities that integrates with
 * {@link FilterableCrudService} and provides pageable access to entity data.
 * <p>
 * This implementation supports filtering, paging, and sorting,
 * and is designed to be used with Vaadin {@code Grid} and {@code Crud} components.
 * </p>
 *
 * @param <T> the entity type managed by this data provider
 */
public class CrudEntityDataProvider<T extends AbstractEntity>
        extends FilterablePageableDataProvider<T, String> {

    private final FilterableCrudService<T> crudService;
    private List<QuerySortOrder> defaultSortOrders;

    /**
     * Creates a new {@code CrudEntityDataProvider}.
     *
     * @param crudService the service used to fetch and count entities
     */
    public CrudEntityDataProvider(FilterableCrudService<T> crudService) {
        this.crudService = crudService;
        setSortOrders();
    }

    /**
     * Initializes the default sort order for entities.
     * <p>
     * By default, entities are sorted by ascending {@code id}.
     * </p>
     */
    private void setSortOrders() {
        QuerySortOrderBuilder builder = new QuerySortOrderBuilder();
        builder.thenAsc("id");
        defaultSortOrders = builder.build();
    }

    /**
     * Fetches a page of entities from the backend service, applying
     * the given filter and pageable information.
     *
     * @param query    the data query, including optional filter
     * @param pageable the paging and sorting information
     * @return a {@link Page} of entities
     */
    @Override
    protected Page<T> fetchFromBackEnd(Query<T, String> query, Pageable pageable) {
        return crudService.findAnyMatching(query.getFilter(), pageable);
    }

    /**
     * Returns the default sort orders applied when no explicit sort
     * order is provided by the query.
     *
     * @return a list of default sort orders
     */
    @Override
    protected List<QuerySortOrder> getDefaultSortOrders() {
        return defaultSortOrders;
    }

    /**
     * Returns the number of entities matching the given query filter.
     *
     * @param query the data query, including optional filter
     * @return the total number of matching entities
     */
    @Override
    protected int sizeInBackEnd(Query<T, String> query) {
        return (int) crudService.countAnyMatching(query.getFilter());
    }
}
