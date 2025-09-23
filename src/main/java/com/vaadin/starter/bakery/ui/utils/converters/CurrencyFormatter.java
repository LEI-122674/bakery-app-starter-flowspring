package com.vaadin.starter.bakery.ui.utils.converters;

import com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil;
import com.vaadin.starter.bakery.ui.utils.FormattingUtils;

/**
 * Utility class for formatting integer values as currency strings.
 * <p>
 * This class provides a method to convert integer model values into
 * properly formatted currency strings suitable for display in the UI.
 * </p>
 */
public class CurrencyFormatter {

    /**
     * Converts an integer model value to a formatted currency string.
     * <p>
     * If the {@code modelValue} is {@code null}, this method returns {@code null}.
     * </p>
     *
     * @param modelValue the integer value to format
     * @return the formatted currency string, or {@code null} if {@code modelValue} is {@code null}
     */
    public String encode(Integer modelValue) {
        return DataProviderUtil.convertIfNotNull(modelValue, FormattingUtils::formatAsCurrency);
    }
}
