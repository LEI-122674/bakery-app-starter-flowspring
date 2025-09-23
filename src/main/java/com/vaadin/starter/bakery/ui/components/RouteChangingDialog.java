package com.vaadin.starter.bakery.ui.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;

/**
 * A custom {@link Dialog} component that demonstrates simple navigation control.
 * <p>
 * When opened, the dialog pushes a new state ("home") into the browser history.
 * It also provides a "Back" button that navigates back in the browser history
 * and closes the dialog.
 * </p>
 */
public class RouteChangingDialog extends Dialog {

    /**
     * Constructs a new {@link RouteChangingDialog}.
     * <p>
     * Adds a "Back" button that, when clicked, navigates the browser one step back
     * in its history and closes the dialog. Additionally, when the dialog is opened,
     * it pushes a "home" state into the browser history.
     * </p>
     */
    public RouteChangingDialog() {
        Button backButton = new Button("Back", e -> {
            UI.getCurrent().getPage().getHistory().back();
            close();
        });
        add(backButton);

        addOpenedChangeListener(e -> {
            if (isOpened())
                UI.getCurrent().getPage().getHistory().pushState(null, "home");
        });
    }
}
