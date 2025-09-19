package com.vaadin.starter.bakery.ui.utils.converters;

import static com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil.convertIfNotNull;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.FULL_DATE_FORMATTER;

import java.time.LocalDateTime;

/**
 * Utility class for converting {@link LocalDateTime} values into formatted strings.
 * <p>
 * Formats the date part using a full date formatter and the time part using
 * {@link LocalTimeConverter}, producing a human-readable date-time string.
 * </p>
 */
public class LocalDateTimeConverter {

    private static final LocalTimeConverter TIME_FORMATTER = new LocalTimeConverter();

    /**
     * Converts a {@link LocalDateTime} value to a formatted string.
     * <p>
     * If the {@code modelValue} is {@code null}, this method returns {@code null}.
     * </p>
     *
     * @param modelValue the {@link LocalDateTime} value to convert
     * @return the formatted date-time string, or {@code null} if {@code modelValue} is {@code null}
     */
    public String encode(LocalDateTime modelValue) {
        return convertIfNotNull(modelValue,
                v -> FULL_DATE_FORMATTER.format(v) + " " + TIME_FORMATTER.encode(v.toLocalTime()));
    }
}
