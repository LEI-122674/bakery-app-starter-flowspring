package com.vaadin.starter.bakery.ui.crud;

import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.app.security.CurrentUser;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.backend.service.CrudService;
import com.vaadin.starter.bakery.backend.service.UserFriendlyDataException;
import com.vaadin.starter.bakery.ui.utils.messages.CrudErrorMessage;
import com.vaadin.starter.bakery.ui.views.HasNotifications;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.util.function.Consumer;

/**
 * Presenter class for CRUD operations on entities.
 * <p>
 * This class acts as a mediator between the {@link CrudService},
 * the {@link CurrentUser}, and a view implementing {@link HasNotifications}.
 * It executes CRUD operations, handles exceptions consistently,
 * and provides user-friendly error messages and notifications.
 * </p>
 *
 * @param <E> the entity type managed by this presenter
 */
public class CrudEntityPresenter<E extends AbstractEntity> implements HasLogger {

    private final CrudService<E> crudService;
    private final CurrentUser currentUser;
    private final HasNotifications view;

    /**
     * Creates a new {@code CrudEntityPresenter}.
     *
     * @param crudService the service used to perform CRUD operations
     * @param currentUser the currently authenticated user
     * @param view        the view responsible for showing notifications
     */
    public CrudEntityPresenter(CrudService<E> crudService, CurrentUser currentUser, HasNotifications view) {
        this.crudService = crudService;
        this.currentUser = currentUser;
        this.view = view;
    }

    /**
     * Deletes the given entity using the CRUD service.
     * <p>
     * If the operation succeeds, the {@code onSuccess} consumer is invoked;
     * otherwise, {@code onFail} is invoked.
     * </p>
     *
     * @param entity    the entity to delete
     * @param onSuccess callback executed if the delete succeeds
     * @param onFail    callback executed if the delete fails
     */
    public void delete(E entity, Consumer<E> onSuccess, Consumer<E> onFail) {
        if (executeOperation(() -> crudService.delete(currentUser.getUser(), entity))) {
            onSuccess.accept(entity);
        } else {
            onFail.accept(entity);
        }
    }

    /**
     * Saves the given entity using the CRUD service.
     * <p>
     * If the operation succeeds, the {@code onSuccess} consumer is invoked;
     * otherwise, {@code onFail} is invoked.
     * </p>
     *
     * @param entity    the entity to save
     * @param onSuccess callback executed if the save succeeds
     * @param onFail    callback executed if the save fails
     */
    public void save(E entity, Consumer<E> onSuccess, Consumer<E> onFail) {
        if (executeOperation(() -> saveEntity(entity))) {
            onSuccess.accept(entity);
        } else {
            onFail.accept(entity);
        }
    }

    /**
     * Executes a CRUD operation safely, handling all known exceptions
     * and converting them into user-friendly error messages.
     *
     * @param operation the CRUD operation to execute
     * @return {@code true} if the operation succeeded, {@code false} otherwise
     */
    private boolean executeOperation(Runnable operation) {
        try {
            operation.run();
            return true;
        } catch (UserFriendlyDataException e) {
            // Commit failed because of application-level data constraints
            consumeError(e, e.getMessage(), true);
        } catch (DataIntegrityViolationException e) {
            // Commit failed because of validation errors or references
            consumeError(e, CrudErrorMessage.OPERATION_PREVENTED_BY_REFERENCES, true);
        } catch (OptimisticLockingFailureException e) {
            // Commit failed because of concurrent modification
            consumeError(e, CrudErrorMessage.CONCURRENT_UPDATE, true);
        } catch (EntityNotFoundException e) {
            // Commit failed because the entity does not exist
            consumeError(e, CrudErrorMessage.ENTITY_NOT_FOUND, false);
        } catch (ConstraintViolationException e) {
            // Commit failed because of missing required fields
            consumeError(e, CrudErrorMessage.REQUIRED_FIELDS_MISSING, false);
        }
        return false;
    }

    /**
     * Logs the error and shows a user notification in the view.
     *
     * @param e            the exception that occurred
     * @param message      the user-friendly error message
     * @param isPersistent {@code true} if the notification should persist,
     *                     {@code false} if it can disappear automatically
     */
    private void consumeError(Exception e, String message, boolean isPersistent) {
        getLogger().debug(message, e);
        view.showNotification(message, isPersistent);
    }

    /**
     * Saves the given entity to the backend.
     *
     * @param entity the entity to save
     */
    private void saveEntity(E entity) {
        crudService.save(currentUser.getUser(), entity);
    }

    /**
     * Loads the entity with the given ID and provides it to the {@code onSuccess} consumer.
     *
     * @param id        the ID of the entity to load
     * @param onSuccess callback executed with the loaded entity
     * @return {@code true} if the entity was loaded successfully, {@code false} otherwise
     */
    public boolean loadEntity(Long id, Consumer<E> onSuccess) {
        return executeOperation(() -> onSuccess.accept(crudService.load(id)));
    }
}
