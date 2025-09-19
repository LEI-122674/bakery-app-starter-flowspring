package com.vaadin.starter.bakery.ui.dataproviders;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;

import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.QuerySortOrderBuilder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.service.OrderService;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;

/**
 * A pageable data provider for {@link Order} entities.
 * <p>
 * Supports filtering, sorting, and observing pages of orders. Designed
 * for use in Vaadin grids with server-side paging.
 * </p>
 */
@SpringComponent
@UIScope
public class OrdersGridDataProvider extends FilterablePageableDataProvider<Order, OrdersGridDataProvider.OrderFilter> {

    /**
     * Filter parameters for {@link OrdersGridDataProvider}.
     */
    public static class OrderFilter implements Serializable {
        private String filter;
        private boolean showPrevious;

        /**
         * Creates a new order filter.
         *
         * @param filter       a text filter applied to order fields
         * @param showPrevious whether to include previous orders
         */
        public OrderFilter(String filter, boolean showPrevious) {
            this.filter = filter;
            this.showPrevious = showPrevious;
        }

        /**
         * Returns the text filter.
         *
         * @return the filter string
         */
        public String getFilter() {
            return filter;
        }

        /**
         * Returns whether previous orders are included.
         *
         * @return true if previous orders should be shown, false otherwise
         */
        public boolean isShowPrevious() {
            return showPrevious;
        }

        /**
         * Returns an empty filter (no text filter and no previous orders).
         *
         * @return an empty {@link OrderFilter}
         */
        public static OrderFilter getEmptyFilter() {
            return new OrderFilter("", false);
        }
    }

    private final OrderService orderService;
    private List<QuerySortOrder> defaultSortOrders;
    private Consumer<Page<Order>> pageObserver;

    /**
     * Creates a new {@code OrdersGridDataProvider}.
     *
     * @param orderService the service used to fetch and count orders
     */
    @Autowired
    public OrdersGridDataProvider(OrderService orderService) {
        this.orderService = orderService;
        setSortOrders(BakeryConst.DEFAULT_SORT_DIRECTION, BakeryConst.ORDER_SORT_FIELDS);
    }

    /**
     * Sets the default sort orders for the data provider.
     *
     * @param direction  the sort direction (ascending or descending)
     * @param properties the properties to sort by
     */
    private void setSortOrders(Sort.Direction direction, String[] properties) {
        QuerySortOrderBuilder builder = new QuerySortOrderBuilder();
        for (String property : properties) {
            if (direction.isAscending()) {
                builder.thenAsc(property);
            } else {
                builder.thenDesc(property);
            }
        }
        defaultSortOrders = builder.build();
    }

    @Override
    protected Page<Order> fetchFromBackEnd(Query<Order, OrderFilter> query, Pageable pageable) {
        OrderFilter filter = query.getFilter().orElse(OrderFilter.getEmptyFilter());
        Page<Order> page = orderService.findAnyMatchingAfterDueDate(
                Optional.ofNullable(filter.getFilter()),
                getFilterDate(filter.isShowPrevious()),
                pageable
        );
        if (pageObserver != null) {
            pageObserver.accept(page);
        }
        return page;
    }

    @Override
    protected List<QuerySortOrder> getDefaultSortOrders() {
        return defaultSortOrders;
    }

    @Override
    protected int sizeInBackEnd(Query<Order, OrderFilter> query) {
        OrderFilter filter = query.getFilter().orElse(OrderFilter.getEmptyFilter());
        return (int) orderService.countAnyMatchingAfterDueDate(
                Optional.ofNullable(filter.getFilter()),
                getFilterDate(filter.isShowPrevious())
        );
    }

    /**
     * Returns the reference date used for filtering orders.
     *
     * @param showPrevious whether to include previous orders
     * @return an {@link Optional} containing the date to filter from, or empty to include all orders
     */
    private Optional<LocalDate> getFilterDate(boolean showPrevious) {
        if (showPrevious) {
            return Optional.empty();
        }
        return Optional.of(LocalDate.now().minusDays(1));
    }

    /**
     * Sets a page observer callback, which is invoked whenever a page is fetched.
     *
     * @param pageObserver a {@link Consumer} that receives the fetched page
     */
    public void setPageObserver(Consumer<Page<Order>> pageObserver) {
        this.pageObserver = pageObserver;
    }

    @Override
    public Object getId(Order item) {
        return item.getId();
    }
}
