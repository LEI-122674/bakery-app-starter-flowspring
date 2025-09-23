/**
 * Factory class responsible for creating presenter beans.
 * <p>
 * Defines Spring-managed beans for {@link EntityPresenter} instances
 * with prototype scope, ensuring that each request for a presenter
 * results in a new instance.
 * </p>
 */
package com.vaadin.starter.bakery.ui.crud;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.vaadin.starter.bakery.app.security.CurrentUser;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.service.OrderService;
import com.vaadin.starter.bakery.ui.views.storefront.StorefrontView;

/**
 * Spring configuration class that provides presenter beans.
 */
@Configuration
public class PresenterFactory {

    /**
     * Creates a prototype-scoped {@link EntityPresenter} for managing
     * {@link Order} entities within the {@link StorefrontView}.
     * <p>
     * Because the bean is defined with {@code prototype} scope,
     * a new presenter instance is created every time it is requested
     * from the Spring context.
     * </p>
     *
     * @param crudService the service handling CRUD operations for {@link Order}
     * @param currentUser the currently authenticated user
     * @return a new {@link EntityPresenter} configured for orders
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public EntityPresenter<Order, StorefrontView> orderEntityPresenter(
            OrderService crudService, CurrentUser currentUser) {
        return new EntityPresenter<>(crudService, currentUser);
    }

}
