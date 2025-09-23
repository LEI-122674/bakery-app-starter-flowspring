package com.vaadin.starter.bakery.ui.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;

/**
 * Utility class for formatting dates, times, and numerical values consistently
 * across the Bakery application.
 * <p>
 * Provides preconfigured {@link DateTimeFormatter} instances and helper methods
 * for:
 * <ul>
 *   <li>Date formatting (short, full, month/day, weekday)</li>
 *   <li>Time formatting (hours with AM/PM)</li>
 *   <li>Currency formatting (locale-aware)</li>
 *   <li>UI price formatting without grouping</li>
 * </ul>
 * </p>
 *
 * <p>
 * All formatting uses the application’s default locale
 * {@link BakeryConst#APP_LOCALE}.
 * </p>
 *
 * @author
 * @version 1.0
 * @since 1.0
 */
public class FormattingUtils {

	/** Decimal format string used for rendering two decimal places. */
	public static final String DECIMAL_ZERO = "0.00";

	// ------------------------
	// Date / Time Formatters
	// ------------------------

	/**
	 * Formatter for abbreviated month name and day number.
	 * <p>
	 * Example: {@code Nov 20}
	 * </p>
	 */
	public static final DateTimeFormatter MONTH_AND_DAY_FORMATTER =
			DateTimeFormatter.ofPattern("MMM d", BakeryConst.APP_LOCALE);

	/**
	 * Formatter for full weekday name.
	 * <p>
	 * Example: {@code Monday}
	 * </p>
	 */
	public static final DateTimeFormatter WEEKDAY_FULLNAME_FORMATTER =
			DateTimeFormatter.ofPattern("EEEE", BakeryConst.APP_LOCALE);

	/**
	 * Temporal field for extracting the week number of the year
	 * from a {@link LocalDate}.
	 */
	public static final TemporalField WEEK_OF_YEAR_FIELD =
			WeekFields.of(BakeryConst.APP_LOCALE).weekOfWeekBasedYear();

	/**
	 * Formatter for abbreviated weekday and day number.
	 * <p>
	 * Example: {@code Mon 20}
	 * </p>
	 */
	public static final DateTimeFormatter SHORT_DAY_FORMATTER =
			DateTimeFormatter.ofPattern("E d", BakeryConst.APP_LOCALE);

	/**
	 * Formatter for full date representation.
	 * <p>
	 * Example: {@code 03.03.2001}
	 * </p>
	 */
	public static final DateTimeFormatter FULL_DATE_FORMATTER =
			DateTimeFormatter.ofPattern("dd.MM.yyyy", BakeryConst.APP_LOCALE);

	/**
	 * Formatter for time values using AM/PM style.
	 * <p>
	 * Example: {@code 2:00 PM}
	 * </p>
	 */
	public static final DateTimeFormatter HOUR_FORMATTER =
			DateTimeFormatter.ofPattern("h:mm a", BakeryConst.APP_LOCALE);

	// ------------------------
	// Utility Methods
	// ------------------------

	/**
	 * Returns the full month name of the given date using the application’s locale.
	 *
	 * @param date the date to extract the month name from
	 * @return the full month name (e.g., {@code November})
	 */
	public static String getFullMonthName(LocalDate date) {
		return date.getMonth().getDisplayName(TextStyle.FULL, BakeryConst.APP_LOCALE);
	}

	/**
	 * Formats an integer amount expressed in cents as a localized currency string.
	 *
	 * @param valueInCents the monetary value in cents
	 * @return a formatted currency string (e.g., {@code $1.99})
	 */
	public static String formatAsCurrency(int valueInCents) {
		return NumberFormat.getCurrencyInstance(BakeryConst.APP_LOCALE)
				.format(BigDecimal.valueOf(valueInCents, 2));
	}

	/**
	 * Returns a {@link DecimalFormat} instance for displaying prices in the UI.
	 * <p>
	 * The formatter:
	 * <ul>
	 *   <li>Uses two decimal places</li>
	 *   <li>Suppresses grouping (no thousand separators)</li>
	 * </ul>
	 * </p>
	 *
	 * @return a configured {@link DecimalFormat} for UI price display
	 */
	public static DecimalFormat getUiPriceFormatter() {
		DecimalFormat formatter = new DecimalFormat("#" + DECIMAL_ZERO,
				DecimalFormatSymbols.getInstance(BakeryConst.APP_LOCALE));
		formatter.setGroupingUsed(false);
		return formatter;
	}
}
