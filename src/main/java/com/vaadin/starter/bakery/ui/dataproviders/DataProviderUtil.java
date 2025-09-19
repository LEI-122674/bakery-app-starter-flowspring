package com.vaadin.starter.bakery.ui.dataproviders;

import java.util.function.Function;
import java.util.function.Supplier;

import com.vaadin.flow.component.ItemLabelGenerator;

/**
 * Utility class providing helper methods for safely converting values
 * and creating item label generators for Vaadin components.
 */
public class DataProviderUtil {

    /**
     * Converts the given {@code source} using the provided {@code converter}
     * if the source is not {@code null}.
     * <p>
     * If the source is {@code null}, this method returns {@code null}.
     * </p>
     *
     * @param source    the input value to convert, may be {@code null}
     * @param converter the function used to perform the conversion
     * @param <S>       the type of the source value
     * @param <T>       the type of the target value
     * @return the converted value, or {@code null} if the source was {@code null}
     */
    public static <S, T> T convertIfNotNull(S source, Function<S, T> converter) {
        return convertIfNotNull(source, converter, () -> null);
    }

    /**
     * Converts the given {@code source} using the provided {@code converter}
     * if the source is not {@code null}.
     * <p>
     * If the source is {@code null}, this method returns a value supplied
     * by the given {@code nullValueSupplier}.
     * </p>
     *
     * @param source             the input value to convert, may be {@code null}
     * @param converter          the function used to perform the conversion
     * @param nullValueSupplier  a supplier providing a fallback value when the source is {@code null}
     * @param <S>                the type of the source value
     * @param <T>                the type of the target value
     * @return the converted value, or the value from {@code nullValueSupplier} if the source was {@code null}
     */
    public static <S, T> T convertIfNotNull(S source, Function<S, T> converter, Supplier<T> nullValueSupplier) {
        return source != null ? converter.apply(source) : nullValueSupplier.get();
    }

    /**
     * Creates an {@link ItemLabelGenerator} that converts items into
     * labels using the provided {@code converter} function.
     * <p>
     * If the item is {@code null}, an empty string is returned.
     * </p>
     *
     * @param converter the function used to generate string labels from items
     * @param <T>       the type of the item
     * @return an {@link ItemLabelGenerator} that produces labels based on the converter
     */
    public static <T> ItemLabelGenerator<T> createItemLabelGenerator(Function<T, String> converter) {
        return item -> convertIfNotNull(item, converter, () -> "");
    }
}
