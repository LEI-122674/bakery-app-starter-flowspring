package com.vaadin.starter.bakery.ui.views.storefront;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.starter.bakery.app.security.CurrentUser;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.service.OrderService;
import com.vaadin.starter.bakery.ui.crud.EntityPresenter;
import com.vaadin.starter.bakery.ui.dataproviders.OrdersGridDataProvider;
import com.vaadin.starter.bakery.ui.dataproviders.OrdersGridDataProvider.OrderFilter;
import com.vaadin.starter.bakery.ui.views.storefront.beans.OrderCardHeader;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_STOREFRONT_ORDER_EDIT;

/**
 * Presenter class for the storefront view.
 * <p>
 * Handles the interaction between the StorefrontView, the data provider,
 * and order-related services. Manages the creation, editing, reviewing,
 * and navigation of orders, as well as header generation for order cards.
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OrderPresenter {

    private OrderCardHeaderGenerator headersGenerator;
    private StorefrontView view;

    private final EntityPresenter<Order, StorefrontView> entityPresenter;
    private final OrdersGridDataProvider dataProvider;
    private final CurrentUser currentUser;
    private final OrderService orderService;

    /**
     * Constructs the OrderPresenter with required dependencies.
     * Sets up the header generator and registers a page observer for the data provider.
     *
     * @param orderService    Service for managing orders.
     * @param dataProvider    Data provider for the orders grid.
     * @param entityPresenter Presenter handling CRUD operations for orders.
     * @param currentUser     The currently logged-in user.
     */
    @Autowired
    OrderPresenter(OrderService orderService, OrdersGridDataProvider dataProvider,
                   EntityPresenter<Order, StorefrontView> entityPresenter, CurrentUser currentUser) {
        this.orderService = orderService;
        this.entityPresenter = entityPresenter;
        this.dataProvider = dataProvider;
        this.currentUser = currentUser;

        headersGenerator = new OrderCardHeaderGenerator();
        headersGenerator.resetHeaderChain(false);

        // Observe pages to assign headers to loaded orders
        dataProvider.setPageObserver(p -> headersGenerator.ordersRead(p.getContent()));
    }

    /**
     * Initializes the presenter with the given storefront view.
     * Sets up event listeners and configures editors and details view.
     *
     * @param view The StorefrontView to initialize.
     */
    void init(StorefrontView view) {
        this.entityPresenter.setView(view);
        this.view = view;

        view.getGrid().setDataProvider(dataProvider);
        view.getOpenedOrderEditor().setCurrentUser(currentUser.getUser());

        view.getOpenedOrderEditor().addCancelListener(e -> cancel());
        view.getOpenedOrderEditor().addReviewListener(e -> review());

        view.getOpenedOrderDetails().addSaveListenter(e -> save());
        view.getOpenedOrderDetails().addCancelListener(e -> cancel());
        view.getOpenedOrderDetails().addBackListener(e -> back());
        view.getOpenedOrderDetails().addEditListener(e -> edit());
        view.getOpenedOrderDetails().addCommentListener(e -> addComment(e.getMessage()));
    }

    /**
     * Retrieves the header associated with a specific order ID.
     *
     * @param id The order ID.
     * @return The corresponding OrderCardHeader, or null if none.
     */
    OrderCardHeader getHeaderByOrderId(Long id) {
        return headersGenerator.get(id);
    }

    /**
     * Updates the filter for the orders grid and resets headers.
     *
     * @param filter       The text filter to apply.
     * @param showPrevious Whether to show headers for previous periods.
     */
    public void filterChanged(String filter, boolean showPrevious) {
        headersGenerator.resetHeaderChain(showPrevious);
        dataProvider.setFilter(new OrderFilter(filter, showPrevious));
    }

    /**
     * Handles navigation to an order by its ID, optionally opening it for editing.
     *
     * @param id   The order ID.
     * @param edit True to open the order for editing, false for viewing.
     */
    void onNavigation(Long id, boolean edit) {
        entityPresenter.loadEntity(id, e -> open(e, edit));
    }

    /**
     * Opens a new order for creation.
     */
    void createNewOrder() {
        open(entityPresenter.createNew(), true);
    }

    /**
     * Cancels the current editing operation and closes the editor.
     */
    void cancel() {
        entityPresenter.cancel(this::close, () -> view.setOpened(true));
    }

    /**
     * Closes the editor silently without notifications.
     */
    void closeSilently() {
        entityPresenter.close();
        view.setOpened(false);
    }

    /**
     * Navigates to the order edit page using the current UI.
     */
    void edit() {
        UI.getCurrent()
                .navigate(String.format(PAGE_STOREFRONT_ORDER_EDIT,
                        entityPresenter.getEntity().getId()));
    }

    /**
     * Returns from the details view to the main dialog view.
     */
    void back() {
        view.setDialogElementsVisibility(true);
    }

    /**
     * Reviews the current order in the editor.
     * Validates fields and opens the details view if validation passes.
     */
    void review() {
        List<HasValue<?, ?>> fields = view.validate().collect(Collectors.toList());
        if (fields.isEmpty()) {
            if (entityPresenter.writeEntity()) {
                view.setDialogElementsVisibility(false);
                view.getOpenedOrderDetails().display(entityPresenter.getEntity(), true);
            }
        } else if (fields.get(0) instanceof Focusable) {
            ((Focusable<?>) fields.get(0)).focus();
        }
    }

    /**
     * Saves the current order and displays notifications.
     * Refreshes the grid data provider accordingly.
     */
    void save() {
        entityPresenter.save(e -> {
            if (entityPresenter.isNew()) {
                view.showCreatedNotification();
                dataProvider.refreshAll();
            } else {
                view.showUpdatedNotification();
                dataProvider.refreshItem(e);
            }
            close();
        });
    }

    /**
     * Adds a comment to the current order.
     *
     * @param comment The comment text to add.
     */
    void addComment(String comment) {
        if (entityPresenter.executeUpdate(e -> orderService.addComment(currentUser.getUser(), e, comment))) {
            // Reopen order in view mode after adding comment
            open(entityPresenter.getEntity(), false);
        }
    }

    /**
     * Opens the given order in either edit or view mode.
     *
     * @param order The order to open.
     * @param edit  True for edit mode, false for view mode.
     */
    private void open(Order order, boolean edit) {
        view.setDialogElementsVisibility(edit);
        view.setOpened(true);

        if (edit) {
            view.getOpenedOrderEditor().read(order, entityPresenter.isNew());
        } else {
            view.getOpenedOrderDetails().display(order, false);
        }
    }

    /**
     * Closes the editor and returns to the main view.
     */
    private void close() {
        view.getOpenedOrderEditor().close();
        view.setOpened(false);
        view.navigateToMainView();
        entityPresenter.close();
    }
}
