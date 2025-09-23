package com.vaadin.starter.bakery.ui.crud;

import java.util.function.UnaryOperator;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;

import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.app.security.CurrentUser;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.backend.data.entity.util.EntityUtil;
import com.vaadin.starter.bakery.backend.service.CrudService;
import com.vaadin.starter.bakery.backend.service.UserFriendlyDataException;
import com.vaadin.starter.bakery.ui.utils.messages.CrudErrorMessage;
import com.vaadin.starter.bakery.ui.utils.messages.Message;
import com.vaadin.starter.bakery.ui.views.EntityView;

/**
 * Presenter responsible for coordinating CRUD operations for a given entity type.
 * <p>
 * Acts as a mediator between the backend {@link CrudService},
 * the authenticated {@link CurrentUser}, and the {@link EntityView}.
 * Handles persistence operations, exception management, view state,
 * and confirmation dialogs for potentially destructive actions.
 * </p>
 *
 * @param <T> the type of entity
 * @param <V> the type of view bound to this presenter
 */
public class EntityPresenter<T extends AbstractEntity, V extends EntityView<T>>
        implements HasLogger {

    private CrudService<T> crudService;
    private CurrentUser currentUser;
    private V view;
    private EntityPresenterState<T> state = new EntityPresenterState<T>();

    /**
     * Creates a new {@code EntityPresenter}.
     *
     * @param crudService the service for performing CRUD operations
     * @param currentUser the currently authenticated user
     */
    public EntityPresenter(CrudService<T> crudService, CurrentUser currentUser) {
        this.crudService = crudService;
        this.currentUser = currentUser;
    }

    /**
     * Sets the view associated with this presenter.
     *
     * @param view the view to bind
     */
    public void setView(V view) {
        this.view = view;
    }

    /**
     * Returns the view currently associated with this presenter.
     *
     * @return the bound view
     */
    public V getView() {
        return view;
    }

    /**
     * Deletes the currently loaded entity after user confirmation.
     * If deletion succeeds, {@code onSuccess} is invoked.
     *
     * @param onSuccess callback executed when deletion succeeds
     */
    public void delete(CrudOperationListener<T> onSuccess) {
        Message CONFIRM_DELETE = Message.CONFIRM_DELETE.createMessage();
        confirmIfNecessaryAndExecute(true, CONFIRM_DELETE, () -> {
            if (executeOperation(() -> crudService.delete(currentUser.getUser(),
                    state.getEntity()))) {
                onSuccess.execute(state.getEntity());
            }
        }, () -> {
        });
    }

    /**
     * Saves the currently loaded entity.
     * If the save operation succeeds, {@code onSuccess} is invoked.
     *
     * @param onSuccess callback executed when saving succeeds
     */
    public void save(CrudOperationListener<T> onSuccess) {
        if (executeOperation(() -> saveEntity())) {
            onSuccess.execute(state.getEntity());
        }
    }

    /**
     * Executes an update function on the current entity.
     *
     * @param updater a function that transforms the current entity
     * @return {@code true} if the update was successful, {@code false} otherwise
     */
    public boolean executeUpdate(UnaryOperator<T> updater) {
        return executeOperation(() -> {
            state.updateEntity(updater.apply(getEntity()), isNew());
        });
    }

    /**
     * Executes a persistence operation safely and handles known exceptions.
     *
     * @param operation the operation to execute
     * @return {@code true} if operation succeeded, {@code false} otherwise
     */
    private boolean executeOperation(Runnable operation) {
        try {
            operation.run();
            return true;
        } catch (UserFriendlyDataException e) {
            consumeError(e, e.getMessage(), true);
        } catch (DataIntegrityViolationException e) {
            consumeError(e, CrudErrorMessage.OPERATION_PREVENTED_BY_REFERENCES, true);
        } catch (OptimisticLockingFailureException e) {
            consumeError(e, CrudErrorMessage.CONCURRENT_UPDATE, true);
        } catch (EntityNotFoundException e) {
            consumeError(e, CrudErrorMessage.ENTITY_NOT_FOUND, false);
        } catch (ConstraintViolationException e) {
            consumeError(e, CrudErrorMessage.REQUIRED_FIELDS_MISSING, false);
        }
        return false;
    }

    /**
     * Logs the error and notifies the user through the view.
     *
     * @param e            the exception that occurred
     * @param message      the error message to display
     * @param isPersistent whether the notification should persist
     */
    private void consumeError(Exception e, String message, boolean isPersistent) {
        getLogger().debug(message, e);
        view.showError(message, isPersistent);
    }

    /**
     * Saves the current entity and updates the internal state.
     */
    private void saveEntity() {
        state.updateEntity(
                crudService.save(currentUser.getUser(), state.getEntity()),
                isNew());
    }

    /**
     * Writes the entity's values from the view into the presenter state.
     *
     * @return {@code true} if writing succeeded, {@code false} otherwise
     */
    public boolean writeEntity() {
        try {
            view.write(state.getEntity());
            return true;
        } catch (ValidationException e) {
            view.showError(CrudErrorMessage.REQUIRED_FIELDS_MISSING, false);
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Clears the presenter state and resets the view.
     */
    public void close() {
        state.clear();
        view.clear();
    }

    /**
     * Cancels the current editing session, asking for confirmation
     * if there are unsaved changes.
     *
     * @param onConfirmed action executed if the cancellation is confirmed
     * @param onCancelled action executed if the cancellation is aborted
     */
    public void cancel(Runnable onConfirmed, Runnable onCancelled) {
        confirmIfNecessaryAndExecute(
                view.isDirty(),
                Message.UNSAVED_CHANGES.createMessage(state.getEntityName()),
                () -> {
                    view.clear();
                    onConfirmed.run();
                }, onCancelled);
    }

    /**
     * Executes a runnable after asking for user confirmation if required.
     *
     * @param needsConfirmation whether confirmation is needed
     * @param message           the confirmation message
     * @param onConfirmed       action executed if confirmed
     * @param onCancelled       action executed if cancelled
     */
    private void confirmIfNecessaryAndExecute(
            boolean needsConfirmation, Message message, Runnable onConfirmed,
            Runnable onCancelled) {
        if (needsConfirmation) {
            showConfirmationRequest(message, onConfirmed, onCancelled);
        } else {
            onConfirmed.run();
        }
    }

    /**
     * Shows a confirmation dialog with the given message and actions.
     *
     * @param message   the confirmation message
     * @param onOk      action executed if confirmed
     * @param onCancel  action executed if cancelled
     */
    private void showConfirmationRequest(
            Message message, Runnable onOk, Runnable onCancel) {
        view.getConfirmDialog().setText(message.getMessage());
        view.getConfirmDialog().setHeader(message.getCaption());
        view.getConfirmDialog().setCancelText(message.getCancelText());
        view.getConfirmDialog().setConfirmText(message.getOkText());
        view.getConfirmDialog().setOpened(true);

        final Registration okRegistration =
                view.getConfirmDialog().addConfirmListener(e -> onOk.run());
        final Registration cancelRegistration =
                view.getConfirmDialog().addCancelListener(e -> onCancel.run());
        state.updateRegistration(okRegistration, cancelRegistration);
    }

    /**
     * Loads an entity by its ID.
     *
     * @param id        the ID of the entity
     * @param onSuccess callback executed with the loaded entity
     * @return {@code true} if loading succeeded, {@code false} otherwise
     */
    public boolean loadEntity(Long id, CrudOperationListener<T> onSuccess) {
        return executeOperation(() -> {
            state.updateEntity(crudService.load(id), false);
            onSuccess.execute(state.getEntity());
        });
    }

    /**
     * Creates a new entity instance.
     *
     * @return the newly created entity
     */
    public T createNew() {
        state.updateEntity(crudService.createNew(currentUser.getUser()), true);
        return state.getEntity();
    }

    /**
     * Returns the currently managed entity.
     *
     * @return the current entity
     */
    public T getEntity() {
        return state.getEntity();
    }

    /**
     * Checks whether the current entity is new (not yet persisted).
     *
     * @return {@code true} if new, {@code false} otherwise
     */
    public boolean isNew() {
        return state.isNew();
    }

    /**
     * Functional interface representing a listener for CRUD operations.
     *
     * @param <T> the entity type
     */
    @FunctionalInterface
    public interface CrudOperationListener<T> {
        /**
         * Executes the callback with the given entity.
         *
         * @param entity the entity involved in the operation
         */
        void execute(T entity);
    }

}

/**
 * Maintains the presenter's internal state.
 * <p>
 * Stores the current entity, its name, confirmation dialog registrations,
 * and whether the entity is new.
 * </p>
 *
 * @param <T> the entity type
 */
class EntityPresenterState<T extends AbstractEntity> {

    private T entity;
    private String entityName;
    private Registration okRegistration;
    private Registration cancelRegistration;
    private boolean isNew = false;

    /**
     * Updates the state with a new entity.
     *
     * @param entity the new entity
     * @param isNew  whether the entity is new
     */
    void updateEntity(T entity, boolean isNew) {
        this.entity = entity;
        this.entityName = EntityUtil.getName(this.entity.getClass());
        this.isNew = isNew;
    }

    /**
     * Updates the confirmation dialog listeners.
     *
     * @param okRegistration     registration for confirm listener
     * @param cancelRegistration registration for cancel listener
     */
    void updateRegistration(
            Registration okRegistration, Registration cancelRegistration) {
        clearRegistration(this.okRegistration);
        clearRegistration(this.cancelRegistration);
        this.okRegistration = okRegistration;
        this.cancelRegistration = cancelRegistration;
    }

    /**
     * Clears the state completely.
     */
    void clear() {
        this.entity = null;
        this.entityName = null;
        this.isNew = false;
        updateRegistration(null, null);
    }

    /**
     * Removes a registration if present.
     *
     * @param registration the registration to clear
     */
    private void clearRegistration(Registration registration) {
        if (registration != null) {
            registration.remove();
        }
    }

    /**
     * Returns the current entity.
     *
     * @return the entity
     */
    public T getEntity() {
        return entity;
    }

    /**
     * Returns the name of the current entity type.
     *
     * @return the entity name
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * Returns whether the current entity is new.
     *
     * @return {@code true} if new, {@code false} otherwise
     */
    public boolean isNew() {
        return isNew;
    }

}
