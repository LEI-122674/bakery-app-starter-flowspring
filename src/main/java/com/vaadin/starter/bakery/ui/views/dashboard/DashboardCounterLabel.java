package com.vaadin.starter.bakery.ui.views.dashboard;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
import com.vaadin.starter.bakery.ui.views.storefront.beans.OrdersCountData;

/**
 * {@code DashboardCounterLabel} é um componente Vaadin personalizado baseado em
 * {@link LitTemplate}, utilizado no dashboard da aplicação.
 *
 * <p>
 * O componente exibe um rótulo composto por:
 * </p>
 * <ul>
 *   <li>Um título ({@link H4})</li>
 *   <li>Um subtítulo ({@link Div})</li>
 *   <li>Um contador numérico ({@link Span})</li>
 * </ul>
 *
 * <h3>Integração</h3>
 * <p>
 * Este componente está associado ao elemento HTML {@code <dashboard-counter-label>}
 * e ao módulo JavaScript {@code ./src/views/dashboard/dashboard-counter-label.js},
 * onde o template é definido.
 * </p>
 *
 * <h3>Utilização</h3>
 * <p>
 * Os dados são fornecidos através de {@link OrdersCountData}, que contém título,
 * subtítulo e número de pedidos. O método {@link #setOrdersCountData(OrdersCountData)}
 * preenche os elementos do template com esses valores.
 * </p>
 */
@Tag("dashboard-counter-label")
@JsModule("./src/views/dashboard/dashboard-counter-label.js")
public class DashboardCounterLabel extends LitTemplate {

	/** Elemento HTML para o título do contador (ligado ao template via {@code @Id("title")}). */
	@Id("title")
	private H4 title;

	/** Elemento HTML para o subtítulo (ligado ao template via {@code @Id("subtitle")}). */
	@Id("subtitle")
	private Div subtitle;

	/** Elemento HTML para o valor numérico (ligado ao template via {@code @Id("count")}). */
	@Id("count")
	private Span count;

	/**
	 * Atualiza o componente com os dados fornecidos.
	 *
	 * @param data objeto {@link OrdersCountData} que contém o título, subtítulo e número de pedidos
	 */
	public void setOrdersCountData(OrdersCountData data) {
		title.setText(data.getTitle());
		subtitle.setText(data.getSubtitle());
		count.setText(String.valueOf(data.getCount()));
	}
}
