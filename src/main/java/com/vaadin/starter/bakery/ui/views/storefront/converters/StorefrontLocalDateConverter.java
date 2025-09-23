package com.vaadin.starter.bakery.ui.views.storefront.converters;

import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.MONTH_AND_DAY_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.WEEKDAY_FULLNAME_FORMATTER;

import java.time.LocalDate;

/**
 * Converter that transforms a {@link LocalDate} into a {@link StorefrontDate},
 * applying specific formatting rules for how dates are displayed in the storefront UI.
 * <p>
 * This converter produces:
 * <ul>
 *   <li>{@code day} – formatted using {@code MONTH_AND_DAY_FORMATTER} (e.g., "Sep 18").</li>
 *   <li>{@code weekday} – formatted using {@code WEEKDAY_FULLNAME_FORMATTER} (e.g., "Thursday").</li>
 *   <li>{@code date} – the ISO-8601 string representation of the {@link LocalDate} (e.g., "2025-09-18").</li>
 * </ul>
 */
public class StorefrontLocalDateConverter {

    /**
     * Encodes a {@link LocalDate} into a {@link StorefrontDate} with formatted fields.
     * <p>
     * If the input is {@code null}, this method returns {@code null}.
     *
     * @param modelValue the {@link LocalDate} to be converted
     * @return a {@link StorefrontDate} containing the formatted values,
     *         or {@code null} if the input was {@code null}
     */
    public StorefrontDate encode(LocalDate modelValue) {
        StorefrontDate result = null;
        if (modelValue != null) {
            result = new StorefrontDate();
            result.setDay(MONTH_AND_DAY_FORMATTER.format(modelValue));
            result.setWeekday(WEEKDAY_FULLNAME_FORMATTER.format(modelValue));
            result.setDate(modelValue.toString());
        }
        return result;
    }
}
