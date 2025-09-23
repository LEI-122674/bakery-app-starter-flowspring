package com.vaadin.starter.bakery.ui.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * A custom {@link Grid} component for displaying integer values
 * with a button in each row. Each button opens a {@link RouteChangingDialog}
 * when clicked.
 *
 * <p>This component is registered as a Spring bean with UI scope,
 * so a new instance is created for each user interface.</p>
 */
@SpringComponent
@UIScope
public class GridComponent extends Grid<Integer> {

    /**
     * Constructs the grid component.
     * <p>
     * Initializes the grid with two integer items (0 and 1)
     * and adds a column containing buttons for each item.
     */
    public GridComponent() {
        setItems(0, 1);
        addComponentColumn(i -> createButton(i));
    }

    /**
     * Creates a {@link Button} for the given integer value.
     * <p>
     * The button label includes the integer value,
     * and clicking the button will open a {@link RouteChangingDialog}.
     *
     * @param i the integer value used in the button label
     * @return a new {@link Button} instance bound to the given integer
     */
    private Button createButton(Integer i) {
        return new Button("Test Button " + i, e -> {
            RouteChangingDialog dialog = new RouteChangingDialog();
            dialog.open();
        });
    }

}
