package com.vaadin.starter.bakery.ui.crud;

import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.crud.CrudEditor;
import com.vaadin.flow.component.crud.CrudI18n;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.starter.bakery.app.security.CurrentUser;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.backend.data.entity.util.EntityUtil;
import com.vaadin.starter.bakery.backend.service.FilterableCrudService;
import com.vaadin.starter.bakery.ui.components.SearchBar;
import com.vaadin.starter.bakery.ui.utils.TemplateUtil;
import com.vaadin.starter.bakery.ui.views.HasNotifications;

import java.util.function.Consumer;

/**
 * An abstract base class for CRUD (Create, Read, Update, Delete) views
 * in the Bakery application.
 * <p>
 * This class provides a {@link Crud} component for managing entities,
 * a {@link SearchBar} for filtering, and built-in support for
 * navigation and confirmation messages.
 * </p>
 *
 * @param <E> the entity type managed by the CRUD view
 */
public abstract class AbstractBakeryCrudView<E extends AbstractEntity> extends VerticalLayout
        implements HasUrlParameter<Long>, HasNotifications {

    private static final String DISCARD_MESSAGE =
            "There are unsaved modifications to the %s. Discard changes?";
    private static final String DELETE_MESSAGE =
            "Are you sure you want to delete the selected %s? This action cannot be undone.";

    private final CrudEntityPresenter<E> entityPresenter;

    private final Crud<E> crud;

    /**
     * Returns the base page route used for navigation.
     *
     * @return the base page route as a string
     */
    protected abstract String getBasePage();

    /**
     * Configures the grid used inside the CRUD component.
     *
     * @param grid the grid instance to configure
     */
    protected abstract void setupGrid(Grid<E> grid);

    /**
     * Creates a new entity instance for the CRUD editor.
     *
     * @return a new entity instance
     */
    protected abstract E createItem();

    /**
     * Constructs an {@code AbstractBakeryCrudView}.
     *
     * @param beanType    the entity class type
     * @param service     the service for managing entity persistence
     * @param grid        the grid for displaying entities
     * @param editor      the editor for editing entities
     * @param currentUser the current user
     */
    public AbstractBakeryCrudView(Class<E> beanType,
                                  FilterableCrudService<E> service,
                                  Grid<E> grid,
                                  CrudEditor<E> editor,
                                  CurrentUser currentUser) {
        setHeightFull();
        setPadding(false);
        setSpacing(false);

        crud = new Crud<>(beanType, grid, editor);
        grid.setSelectionMode(Grid.SelectionMode.NONE);

        CrudI18n crudI18n = CrudI18n.createDefault();
        String entityName = EntityUtil.getName(beanType);
        crudI18n.setNewItem("New " + entityName);
        crudI18n.setEditItem("Edit " + entityName);
        crudI18n.setEditLabel("Edit " + entityName);
        crudI18n.getConfirm().getCancel()
                .setContent(String.format(DISCARD_MESSAGE, entityName));
        crudI18n.getConfirm().getDelete()
                .setContent(String.format(DELETE_MESSAGE, entityName));
        crudI18n.setDeleteItem("Delete");
        crud.setI18n(crudI18n);
        crud.setToolbarVisible(false);
        crud.setHeightFull();

        CrudEntityDataProvider<E> dataProvider = new CrudEntityDataProvider<>(service);
        grid.setDataProvider(dataProvider);
        setupGrid(grid);
        Crud.addEditColumn(grid);

        entityPresenter = new CrudEntityPresenter<>(service, currentUser, this);

        SearchBar searchBar = new SearchBar();
        searchBar.setActionText("New " + entityName);
        searchBar.setPlaceHolder("Search");
        searchBar.addFilterChangeListener(e -> dataProvider.setFilter(searchBar.getFilter()));
        searchBar.getActionButton().getElement().setAttribute("new-button", true);
        searchBar.addActionClickListener(e ->
                crud.edit(createItem(), Crud.EditMode.NEW_ITEM));

        setupCrudEventListeners(entityPresenter);

        add(searchBar, crud);
    }

    /**
     * Sets up CRUD event listeners for edit, cancel, save, and delete actions.
     *
     * @param entityPresenter the presenter responsible for entity operations
     */
    private void setupCrudEventListeners(CrudEntityPresenter<E> entityPresenter) {
        Consumer<E> onSuccess = entity -> navigateToEntity(null);
        Consumer<E> onFail = entity -> {
            throw new RuntimeException("The operation could not be performed.");
        };

        crud.addEditListener(e ->
                entityPresenter.loadEntity(e.getItem().getId(),
                        entity -> navigateToEntity(entity.getId().toString())));

        crud.addCancelListener(e -> navigateToEntity(null));

        crud.addSaveListener(e ->
                entityPresenter.save(e.getItem(), onSuccess, onFail));

        crud.addDeleteListener(e ->
                entityPresenter.delete(e.getItem(), onSuccess, onFail));
    }

    /**
     * Navigates to the CRUD view of the specified entity ID.
     *
     * @param id the entity ID as a string, or {@code null} to navigate to the base page
     */
    protected void navigateToEntity(String id) {
        getUI().ifPresent(ui ->
                ui.navigate(TemplateUtil.generateLocation(getBasePage(), id)));
    }

    /**
     * Handles the URL parameter for opening a specific entity in edit mode.
     *
     * @param event the navigation event
     * @param id    the optional entity ID from the URL
     */
    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long id) {
        if (id != null) {
            E item = crud.getEditor().getItem();
            if (item != null && id.equals(item.getId())) {
                return;
            }
            entityPresenter.loadEntity(id,
                    entity -> crud.edit(entity, Crud.EditMode.EXISTING_ITEM));
        } else {
            crud.setOpened(false);
        }
    }
}
