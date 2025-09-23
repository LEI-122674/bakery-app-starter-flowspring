package com.vaadin.starter.bakery.ui.views.admin.products;

import static com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil.convertIfNotNull;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.starter.bakery.ui.utils.FormattingUtils;

/**
 * {@code PriceConverter} é um conversor entre o valor apresentado ao utilizador
 * (em forma de {@link String}, ex: "12,50") e o valor utilizado no modelo
 * (em {@link Integer}, representando o preço em cêntimos, ex: 1250).
 * <p>
 * Este conversor é usado em formulários Vaadin para ligar campos de texto
 * com valores monetários armazenados no modelo como inteiros.
 * </p>
 *
 * <p><b>Exemplo de conversão:</b></p>
 * <ul>
 *   <li>Apresentação → Modelo: "12,50" → 1250</li>
 *   <li>Modelo → Apresentação: 1250 → "12,50"</li>
 * </ul>
 *
 * <p>A formatação e análise do valor é feita através de um
 * {@link DecimalFormat} obtido em {@link FormattingUtils#getUiPriceFormatter()}.</p>
 */
class PriceConverter implements Converter<String, Integer> {

	/** Formatador usado para apresentação e parsing de valores monetários. */
	private final DecimalFormat df = FormattingUtils.getUiPriceFormatter();

	/**
	 * Converte um valor em {@link String} (inserido pelo utilizador na UI)
	 * para um {@link Integer} representando o preço em cêntimos.
	 *
	 * @param presentationValue valor introduzido pelo utilizador (ex: "12,50")
	 * @param valueContext contexto adicional do Vaadin Binder (não usado aqui)
	 * @return {@link Result#ok(Object)} com o valor convertido em cêntimos
	 *         ou {@link Result#error(String)} caso o valor seja inválido
	 */
	@Override
	public Result<Integer> convertToModel(String presentationValue, ValueContext valueContext) {
		try {
			return Result.ok((int) Math.round(df.parse(presentationValue).doubleValue() * 100));
		} catch (ParseException e) {
			return Result.error("Invalid value");
		}
	}

	/**
	 * Converte um valor do modelo (em cêntimos) para uma representação
	 * formatada em {@link String}, adequada para apresentação ao utilizador.
	 *
	 * @param modelValue valor do modelo em cêntimos (ex: 1250)
	 * @param valueContext contexto adicional do Vaadin Binder (não usado aqui)
	 * @return valor formatado em {@link String} (ex: "12,50"),
	 *         ou string vazia se o valor for {@code null}
	 */
	@Override
	public String convertToPresentation(Integer modelValue, ValueContext valueContext) {
		return convertIfNotNull(modelValue, i -> df.format(BigDecimal.valueOf(i, 2)), () -> "");
	}
}
