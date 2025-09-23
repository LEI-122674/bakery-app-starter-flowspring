package com.vaadin.starter.bakery.ui.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.DebounceSettings;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.Synchronize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.DebouncePhase;

/**
 * A custom search bar component implemented as a {@link LitTemplate}.
 * <p>
 * This component contains:
 * <ul>
 *   <li>A text field for entering filter text</li>
 *   <li>A clear button to reset the filter</li>
 *   <li>An action button for triggering a custom action</li>
 *   <li>A checkbox state managed by the template element</li>
 * </ul>
 *
 * <p>The component fires custom events when the filter value or the checkbox state changes.</p>
 */
@Tag("search-bar")
@JsModule("./src/components/search-bar.js")
public class SearchBar extends LitTemplate {

    @Id("field")
    private TextField textField;

    @Id("clear")
    private Button clearButton;

    @Id("action")
    private Button actionButton;

    /**
     * Constructs a new {@code SearchBar}.
     * <p>
     * Initializes the text field with eager value change mode,
     * adds listeners to propagate custom events, and wires the clear button
     * and checkbox property to fire filter change events.
     * </p>
     */
    public SearchBar() {
        textField.setValueChangeMode(ValueChangeMode.EAGER);

        ComponentUtil.addListener(textField, SearchValueChanged.class,
                e -> fireEvent(new FilterChanged(this, false)));

        clearButton.addClickListener(e -> {
            textField.clear();
            getElement().setProperty("checkboxChecked", false);
        });

        getElement().addPropertyChangeListener("checkboxChecked", e -> fireEvent(new FilterChanged(this, false)));
    }

    /**
     * Returns the current filter text entered in the search bar.
     *
     * @return the filter string
     */
    public String getFilter() {
        return textField.getValue();
    }

    /**
     * Returns whether the checkbox is currently checked.
     *
     * @return {@code true} if the checkbox is checked, {@code false} otherwise
     */
    @Synchronize("checkbox-checked-changed")
    public boolean isCheckboxChecked() {
        return getElement().getProperty("checkboxChecked", false);
    }

    /**
     * Sets the placeholder text for the search field.
     *
     * @param placeHolder the placeholder text to display
     */
    public void setPlaceHolder(String placeHolder) {
        textField.setPlaceholder(placeHolder);
    }

    /**
     * Sets the label text for the action button.
     *
     * @param actionText the text for the action button
     */
    public void setActionText(String actionText) {
        getElement().setProperty("buttonText", actionText);
    }

    /**
     * Sets the label text for the checkbox.
     *
     * @param checkboxText the text for the checkbox
     */
    public void setCheckboxText(String checkboxText) {
        getElement().setProperty("checkboxText", checkboxText);
    }

    /**
     * Adds a listener for filter change events.
     * <p>
     * A filter change event is fired when either the text field value
     * or the checkbox state changes.
     * </p>
     *
     * @param listener the listener to add
     */
    public void addFilterChangeListener(ComponentEventListener<FilterChanged> listener) {
        this.addListener(FilterChanged.class, listener);
    }

    /**
     * Adds a listener for action button click events.
     *
     * @param listener the listener to add
     */
    public void addActionClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
        actionButton.addClickListener(listener);
    }

    /**
     * Returns the action button instance.
     *
     * @return the action button
     */
    public Button getActionButton() {
        return actionButton;
    }

    /**
     * Event triggered when the text field value changes.
     * <p>
     * This event is debounced with a 300 ms trailing phase delay.
     * </p>
     */
    @DomEvent(value = "value-changed", debounce = @DebounceSettings(timeout = 300, phases = DebouncePhase.TRAILING))
    public static class SearchValueChanged extends ComponentEvent<TextField> {
        /**
         * Creates a new {@code SearchValueChanged} event.
         *
         * @param source     the text field source component
         * @param fromClient {@code true} if the event originated from the client
         */
        public SearchValueChanged(TextField source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    /**
     * Event triggered when the filter value or checkbox state changes.
     */
    public static class FilterChanged extends ComponentEvent<SearchBar> {
        /**
         * Creates a new {@code FilterChanged} event.
         *
         * @param source     the search bar source component
         * @param fromClient {@code true} if the event originated from the client
         */
        public FilterChanged(SearchBar source, boolean fromClient) {
            super(source, fromClient);
        }
    }
}
