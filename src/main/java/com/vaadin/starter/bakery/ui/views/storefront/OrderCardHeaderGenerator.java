package com.vaadin.starter.bakery.ui.views.storefront;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.ui.views.storefront.beans.OrderCardHeader;

/**
 * Generates and manages headers for order cards in the storefront view.
 * <p>
 * Headers are based on the due date of orders and can represent periods such as
 * "Today", "Yesterday", "This week", "Upcoming", etc.
 * This class maps each order to an appropriate header and maintains the
 * chronological order of headers.
 */
public class OrderCardHeaderGenerator {

    /**
     * A wrapper class to associate a date matching predicate with a header
     * and track the last selected order for that header.
     */
    private class HeaderWrapper {
        private Predicate<LocalDate> matcher;
        private OrderCardHeader header;
        private Long selected;

        /**
         * Constructs a HeaderWrapper.
         *
         * @param matcher A predicate to match dates against this header.
         * @param header  The OrderCardHeader associated with this predicate.
         */
        public HeaderWrapper(Predicate<LocalDate> matcher, OrderCardHeader header) {
            this.matcher = matcher;
            this.header = header;
        }

        /**
         * Checks if the given date matches this header.
         *
         * @param date The date to test.
         * @return true if the date matches; false otherwise.
         */
        public boolean matches(LocalDate date) {
            return matcher.test(date);
        }

        /**
         * Returns the last selected order ID for this header.
         *
         * @return The last selected order ID.
         */
        public Long getSelected() {
            return selected;
        }

        /**
         * Sets the last selected order ID for this header.
         *
         * @param selected The order ID to set.
         */
        public void setSelected(Long selected) {
            this.selected = selected;
        }

        /**
         * Returns the OrderCardHeader associated with this wrapper.
         *
         * @return The header.
         */
        public OrderCardHeader getHeader() {
            return header;
        }
    }

    private final DateTimeFormatter HEADER_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("EEE, MMM d");
    private final Map<Long, OrderCardHeader> ordersWithHeaders = new HashMap<>();
    private List<HeaderWrapper> headerChain = new ArrayList<>();

    /**
     * Generates a header for recent orders (before this week).
     *
     * @return The "Recent" OrderCardHeader.
     */
    private OrderCardHeader getRecentHeader() {
        return new OrderCardHeader("Recent", "Before this week");
    }

    /**
     * Generates a header for orders from yesterday.
     *
     * @return The "Yesterday" OrderCardHeader.
     */
    private OrderCardHeader getYesterdayHeader() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return new OrderCardHeader("Yesterday", secondaryHeaderFor(yesterday));
    }

    /**
     * Generates a header for orders from today.
     *
     * @return The "Today" OrderCardHeader.
     */
    private OrderCardHeader getTodayHeader() {
        LocalDate today = LocalDate.now();
        return new OrderCardHeader("Today", secondaryHeaderFor(today));
    }

    /**
     * Generates a header for orders from this week before yesterday.
     *
     * @return The "This week before yesterday" OrderCardHeader.
     */
    private OrderCardHeader getThisWeekBeforeYesterdayHeader() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate thisWeekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
        return new OrderCardHeader("This week before yesterday", secondaryHeaderFor(thisWeekStart, yesterday));
    }

    /**
     * Generates a header for orders from this week starting tomorrow.
     *
     * @param showPrevious Whether to show previous headers.
     * @return The corresponding OrderCardHeader.
     */
    private OrderCardHeader getThisWeekStartingTomorrow(boolean showPrevious) {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate nextWeekStart = today.minusDays(today.getDayOfWeek().getValue()).plusWeeks(1);
        return new OrderCardHeader(showPrevious ? "This week starting tomorrow" : "This week",
                secondaryHeaderFor(tomorrow, nextWeekStart));
    }

    /**
     * Generates a header for upcoming orders (after this week).
     *
     * @return The "Upcoming" OrderCardHeader.
     */
    private OrderCardHeader getUpcomingHeader() {
        return new OrderCardHeader("Upcoming", "After this week");
    }

    /**
     * Formats a single date for a secondary header.
     *
     * @param date The date to format.
     * @return The formatted date string.
     */
    private String secondaryHeaderFor(LocalDate date) {
        return HEADER_DATE_TIME_FORMATTER.format(date);
    }

    /**
     * Formats a date range for a secondary header.
     *
     * @param start The start date.
     * @param end   The end date.
     * @return The formatted date range string.
     */
    private String secondaryHeaderFor(LocalDate start, LocalDate end) {
        return secondaryHeaderFor(start) + " - " + secondaryHeaderFor(end);
    }

    /**
     * Retrieves the header for the specified order ID.
     *
     * @param id The order ID.
     * @return The associated OrderCardHeader, or null if none exists.
     */
    public OrderCardHeader get(Long id) {
        return ordersWithHeaders.get(id);
    }

    /**
     * Resets the header chain and clears previously assigned order headers.
     *
     * @param showPrevious Whether to include headers for previous periods.
     */
    public void resetHeaderChain(boolean showPrevious) {
        this.headerChain = createHeaderChain(showPrevious);
        ordersWithHeaders.clear();
    }

    /**
     * Processes a list of orders and assigns each order to its appropriate header.
     *
     * @param orders The list of orders to process.
     */
    public void ordersRead(List<Order> orders) {
        Iterator<HeaderWrapper> headerIterator = headerChain.stream().filter(h -> h.getSelected() == null).iterator();
        if (!headerIterator.hasNext()) {
            return;
        }

        HeaderWrapper current = headerIterator.next();
        for (Order order : orders) {
            // If last selected, discard orders that match it.
            if (current.getSelected() != null && current.matches(order.getDueDate())) {
                continue;
            }
            while (current != null && !current.matches(order.getDueDate())) {
                current = headerIterator.hasNext() ? headerIterator.next() : null;
            }
            if (current == null) {
                break;
            }
            current.setSelected(order.getId());
            ordersWithHeaders.put(order.getId(), current.getHeader());
        }
    }

    /**
     * Creates the chain of headers in chronological order.
     *
     * @param showPrevious Whether to include previous period headers.
     * @return The list of HeaderWrapper objects representing the header chain.
     */
    private List<HeaderWrapper> createHeaderChain(boolean showPrevious) {
        List<HeaderWrapper> headerChain = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate startOfTheWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        if (showPrevious) {
            LocalDate yesterday = today.minusDays(1);
            // Week starting on Monday
            headerChain.add(new HeaderWrapper(d -> d.isBefore(startOfTheWeek), this.getRecentHeader()));
            if (startOfTheWeek.isBefore(yesterday)) {
                headerChain.add(new HeaderWrapper(d -> d.isBefore(yesterday) && !d.isAfter(startOfTheWeek),
                        this.getThisWeekBeforeYesterdayHeader()));
            }
            headerChain.add(new HeaderWrapper(yesterday::equals, this.getYesterdayHeader()));
        }
        LocalDate firstDayOfTheNextWeek = startOfTheWeek.plusDays(7);
        headerChain.add(new HeaderWrapper(today::equals, getTodayHeader()));
        headerChain.add(new HeaderWrapper(d -> d.isAfter(today) && d.isBefore(firstDayOfTheNextWeek),
                getThisWeekStartingTomorrow(showPrevious)));
        headerChain.add(new HeaderWrapper(d -> !d.isBefore(firstDayOfTheNextWeek), getUpcomingHeader()));
        return headerChain;
    }
}
