package com.vaadin.starter.bakery.ui.views;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.starter.bakery.ui.components.GridComponent;

/**
 * A simple test view that demonstrates how to include a custom
 * {@link GridComponent} inside a Vaadin {@link VerticalLayout}.
 * <p>
 * This view is available at the route {@code /test}.
 */
@Route("test")
class TestPage extends VerticalLayout {

    /**
     * The grid component that will be displayed in this view.
     * It is injected by Spring.
     */
    @Autowired
    private GridComponent grid;

    /**
     * Initializes the view after all dependencies have been injected.
     * <p>
     * This method adds the {@link GridComponent} to the layout.
     */
    @PostConstruct
    public void init() {
        add(grid);
    }
}
