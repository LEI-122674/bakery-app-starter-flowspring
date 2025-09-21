package com.vaadin.starter.bakery.ui.views.dashboard;

import com.vaadin.flow.component.charts.model.DataSeriesItem;

/**
 * Extensão de {@link DataSeriesItem} que adiciona suporte para
 * configuração de raios personalizados em gráficos circulares
 * (por exemplo, {@code SolidGauge} ou {@code Pie}).
 *
 * <p>
 * Esta classe permite definir:
 * <ul>
 *   <li>{@link #radius} — raio externo do item.</li>
 *   <li>{@link #innerRadius} — raio interno do item (útil para
 *       criar efeitos de anel ou medidores de progresso).</li>
 * </ul>
 * </p>
 *
 * <p>
 * Sempre que um destes valores é definido através dos setters,
 * o método {@link #makeCustomized()} é chamado para assinalar
 * que o item contém propriedades personalizadas e deve ser
 * renderizado de acordo.
 * </p>
 *
 * <p><b>Exemplo de utilização:</b></p>
 *
 * <pre>{@code
 * DataSeriesItemWithRadius point = new DataSeriesItemWithRadius();
 * point.setY(75);
 * point.setInnerRadius("80%");
 * point.setRadius("100%");
 * }</pre>
 */
public class DataSeriesItemWithRadius extends DataSeriesItem {

	/**
	 * Raio externo do item (pode ser expresso em percentagem ou em pixels).
	 * Exemplo: {@code "100%"} ou {@code "120"}.
	 */
	private String radius;

	/**
	 * Raio interno do item (pode ser expresso em percentagem ou em pixels).
	 * Exemplo: {@code "80%"} ou {@code "90"}.
	 */
	private String innerRadius;

	/**
	 * Obtém o valor atual do raio externo.
	 *
	 * @return raio externo em percentagem ou pixels
	 */
	public String getRadius() {
		return radius;
	}

	/**
	 * Define o raio externo do item.
	 * Invoca automaticamente {@link #makeCustomized()} para ativar
	 * propriedades personalizadas.
	 *
	 * @param radius raio externo em percentagem ou pixels
	 */
	public void setRadius(String radius) {
		this.radius = radius;
		makeCustomized();
	}

	/**
	 * Obtém o valor atual do raio interno.
	 *
	 * @return raio interno em percentagem ou pixels
	 */
	public String getInnerRadius() {
		return innerRadius;
	}

	/**
	 * Define o raio interno do item.
	 * Invoca automaticamente {@link #makeCustomized()} para ativar
	 * propriedades personalizadas.
	 *
	 * @param innerRadius raio interno em percentagem ou pixels
	 */
	public void setInnerRadius(String innerRadius) {
		this.innerRadius = innerRadius;
		makeCustomized();
	}
}
