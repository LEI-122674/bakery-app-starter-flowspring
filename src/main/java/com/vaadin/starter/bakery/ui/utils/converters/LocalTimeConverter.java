package com.vaadin.starter.bakery.ui.utils.converters;

import com.vaadin.starter.bakery.ui.utils.FormattingUtils;
import static com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil.convertIfNotNull;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.HOUR_FORMATTER;

import java.time.LocalTime;

/**
 * Utility class for converting {@link LocalTime} values into formatted strings.
 * <p>
 * Uses the {@link FormattingUtils#HOUR_FORMATTER} to produce human-readable
 * hour and minute representations.
 * </p>
 */
public class LocalTimeConverter {

    /**
     * Converts a {@link LocalTime} value to a formatted string.
     * <p>
     * If the {@code modelValue} is {@code null}, this method returns {@code null}.
     * </p>
     *
     * @param modelValue the {@link LocalTime} value to convert
     * @return the formatted time string, or {@code null} if {@code modelValue} is {@code null}
     */
    public String encode(LocalTime modelValue) {
        return convertIfNotNull(modelValue, HOUR_FORMATTER::format);
    }
}
