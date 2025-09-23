package com.vaadin.starter.bakery.ui.views.storefront;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.util.EntityUtil;
import com.vaadin.starter.bakery.ui.MainView;
import com.vaadin.starter.bakery.ui.components.SearchBar;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.views.EntityView;
import com.vaadin.starter.bakery.ui.views.orderedit.OrderDetails;
import com.vaadin.starter.bakery.ui.views.orderedit.OrderEditor;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.EDIT_SEGMENT;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.ORDER_ID;

/**
 * Vaadin view for the storefront page.
 * <p>
 * Displays orders in a grid with headers, supports searching, filtering, and
 * creating new orders. Integrates the {@link OrderPresenter} for handling
 * interactions and navigation, and manages the editor and details dialogs.
 */
@Tag("storefront-view")
@JsModule("./src/views/storefront/storefront-view.js")
@Route(value = BakeryConst.PAGE_STOREFRONT_ORDER_TEMPLATE, layout = MainView.class)
@RouteAlias(value = BakeryConst.PAGE_STOREFRONT_ORDER_EDIT_TEMPLATE, layout = MainView.class)
@RouteAlias(value = BakeryConst.PAGE_ROOT, layout = MainView.class)
@PageTitle(BakeryConst.TITLE_STOREFRONT)
@PermitAll
public class StorefrontView extends LitTemplate
        implements HasLogger, BeforeEnterObserver, EntityView<Order> {

    @Id("search")
    private SearchBar searchBar;

    @Id("grid")
    private Grid<Order> grid;

    @Id("dialog")
    private Dialog dialog;

    private ConfirmDialog confirmation;

    private final OrderEditor orderEditor;
    private final OrderDetails orderDetails = new OrderDetails();
    private final OrderPresenter presenter;

    /**
     * Constructs the storefront view with required presenter and editor.
     * Initializes the search bar, grid, and dialog, and sets up event listeners.
     *
     * @param presenter   The presenter handling order logic.
     * @param orderEditor The editor component for creating or editing orders.
     */
    @Autowired
    public StorefrontView(OrderPresenter presenter, OrderEditor orderEditor) {
        this.presenter = presenter;
        this.orderEditor = orderEditor;

        searchBar.setActionText("New order");
        searchBar.setCheckboxText("Show past orders");
        searchBar.setPlaceHolder("Search");

        grid.setSelectionMode(Grid.SelectionMode.NONE);

        // Configure grid columns using OrderCard template and header generation
        grid.addColumn(OrderCard.getTemplate()
                .withProperty("orderCard", OrderCard::create)
                .withProperty("header", order -> presenter.getHeaderByOrderId(order.getId()))
                .withFunction("cardClick",
                        order -> UI.getCurrent().navigate(BakeryConst.PAGE_STOREFRONT + "/" + order.getId())));

        // Search bar listeners
        getSearchBar().addFilterChangeListener(
                e -> presenter.filterChanged(getSearchBar().getFilter(), getSearchBar().isCheckboxChecked()));
        getSearchBar().addActionClickListener(e -> presenter.createNewOrder());

        presenter.init(this);

        dialog.addDialogCloseActionListener(e -> presenter.cancel());
    }

    @Override
    public ConfirmDialog getConfirmDialog() {
        return confirmation;
    }

    @Override
    public void setConfirmDialog(ConfirmDialog confirmDialog) {
        this.confirmation = confirmDialog;
    }

    /**
     * Opens or closes the dialog.
     *
     * @param opened True to open, false to close.
     */
    void setOpened(boolean opened) {
        dialog.setOpened(opened);
    }

    /**
     * Handles route navigation before entering the view.
     * Opens an order in edit or view mode if an order ID is present in the route.
     *
     * @param event The navigation event.
     */
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> orderId = event.getRouteParameters().getLong(ORDER_ID);
        if (orderId.isPresent()) {
            boolean isEditView = EDIT_SEGMENT.equals(getLastSegment(event));
            presenter.onNavigation(orderId.get(), isEditView);
        } else if (dialog.isOpened()) {
            presenter.closeSilently();
        }
    }

    /**
     * Navigates back to the main storefront view.
     */
    void navigateToMainView() {
        getUI().ifPresent(ui -> ui.navigate(BakeryConst.PAGE_STOREFRONT));
    }

    @Override
    public boolean isDirty() {
        return orderEditor.hasChanges() || orderDetails.isDirty();
    }

    @Override
    public void write(Order entity) throws ValidationException {
        orderEditor.write(entity);
    }

    /**
     * Validates fields in the order editor.
     *
     * @return Stream of invalid fields.
     */
    public Stream<HasValue<?, ?>> validate() {
        return orderEditor.validate();
    }

    SearchBar getSearchBar() {
        return searchBar;
    }

    OrderEditor getOpenedOrderEditor() {
        return orderEditor;
    }

    OrderDetails getOpenedOrderDetails() {
        return orderDetails;
    }

    Grid<Order> getGrid() {
        return grid;
    }

    @Override
    public void clear() {
        orderDetails.setDirty(false);
        orderEditor.clear();
    }

    /**
     * Controls which dialog elements are visible based on edit mode.
     *
     * @param editing True to show editor, false to show details view.
     */
    void setDialogElementsVisibility(boolean editing) {
        dialog.add(editing ? orderEditor : orderDetails);
        orderEditor.setVisible(editing);
        orderDetails.setVisible(!editing);
    }

    @Override
    public String getEntityName() {
        return EntityUtil.getName(Order.class);
    }

    /**
     * Retrieves the last segment of the route path from a navigation event.
     *
     * @param event The navigation event.
     * @return The last segment of the route path.
     */
    private String getLastSegment(BeforeEnterEvent event) {
        List<String> segments = event.getLocation().getSegments();
        return segments.get(segments.size() - 1);
    }
}
