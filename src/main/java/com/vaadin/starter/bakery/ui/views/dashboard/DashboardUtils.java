package com.vaadin.starter.bakery.ui.views.dashboard;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;

import com.vaadin.starter.bakery.backend.data.DeliveryStats;
import com.vaadin.starter.bakery.backend.data.OrderState;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.OrderSummary;
import com.vaadin.starter.bakery.ui.views.storefront.beans.OrdersCountData;
import com.vaadin.starter.bakery.ui.views.storefront.beans.OrdersCountDataWithChart;

/**
 * Classe utilitária para o dashboard da aplicação Bakery.
 * <p>
 * Fornece métodos estáticos para gerar instâncias de
 * {@link OrdersCountData} e {@link OrdersCountDataWithChart}, que são usadas
 * para exibir estatísticas de encomendas no painel principal (Dashboard).
 * </p>
 *
 * <h3>Funcionalidades principais:</h3>
 * <ul>
 *   <li>Contagem de encomendas para hoje, amanhã, novas ou não disponíveis.</li>
 *   <li>Determinação da próxima entrega agendada.</li>
 *   <li>Geração de subtítulos informativos (ex.: "Next delivery 14:00", "Last 2h ago").</li>
 * </ul>
 */
public class DashboardUtils {

	/** Padrão de texto usado para o subtítulo da próxima entrega. */
	private static final String NEXT_DELIVERY_PATTERN = "Next Delivery %s";

	/**
	 * Cria dados de contagem para encomendas restantes de hoje.
	 * <p>
	 * O número total de encomendas pendentes é calculado como:
	 * {@code dueToday - deliveredToday}.
	 * O método também procura a próxima encomenda pronta para entrega
	 * e define o subtítulo correspondente.
	 * </p>
	 *
	 * @param deliveryStats estatísticas de entrega
	 * @param ordersIterator iterador de {@link OrderSummary} para encomendas
	 * @return objeto {@link OrdersCountDataWithChart} para encomendas de hoje
	 */
	public static OrdersCountDataWithChart getTodaysOrdersCountData(
			DeliveryStats deliveryStats, Iterator<OrderSummary> ordersIterator) {

		OrdersCountDataWithChart ordersCountData = new OrdersCountDataWithChart(
				"Remaining Today",
				null,
				deliveryStats.getDueToday() - deliveryStats.getDeliveredToday(),
				deliveryStats.getDueToday()
		);

		LocalDate date = LocalDate.now();
		LocalTime time = LocalTime.now();

		while (ordersIterator.hasNext()) {
			OrderSummary order = ordersIterator.next();
			if (isOrderNextToDeliver(order, date, time)) {
				if (order.getDueDate().isEqual(date)) {
					ordersCountData.setSubtitle(
							String.format(NEXT_DELIVERY_PATTERN, order.getDueTime()));
				} else {
					ordersCountData.setSubtitle(
							String.format(NEXT_DELIVERY_PATTERN,
									order.getDueDate().getMonthValue() + "/" + order.getDueDate().getDayOfMonth()));
				}
				break;
			}
		}
		return ordersCountData;
	}

	/**
	 * Determina se uma encomenda é a próxima a ser entregue.
	 * <p>
	 * Uma encomenda é considerada próxima se estiver no estado
	 * {@link OrderState#READY} e a sua data/hora de entrega for
	 * posterior à data/hora atual.
	 * </p>
	 *
	 * @param order encomenda a verificar
	 * @param nowDate data atual
	 * @param nowTime hora atual
	 * @return {@code true} se a encomenda for a próxima a entregar, caso contrário {@code false}
	 */
	private static boolean isOrderNextToDeliver(OrderSummary order, LocalDate nowDate, LocalTime nowTime) {
		return order.getState() == OrderState.READY
				&& ((order.getDueDate().isEqual(nowDate) && order.getDueTime().isAfter(nowTime))
				|| order.getDueDate().isAfter(nowDate));
	}

	/**
	 * Cria dados de contagem para encomendas que não estão disponíveis hoje.
	 *
	 * @param deliveryStats estatísticas de entrega
	 * @return objeto {@link OrdersCountData} com o número de encomendas indisponíveis
	 */
	public static OrdersCountData getNotAvailableOrdersCountData(DeliveryStats deliveryStats) {
		return new OrdersCountData("Not Available", "Delivery tomorrow",
				deliveryStats.getNotAvailableToday());
	}

	/**
	 * Cria dados de contagem para encomendas de amanhã.
	 * <p>
	 * O subtítulo é definido com a primeira hora de entrega
	 * planeada para o dia seguinte (caso exista).
	 * </p>
	 *
	 * @param deliveryStats estatísticas de entrega
	 * @param ordersIterator iterador de {@link OrderSummary} para encomendas
	 * @return objeto {@link OrdersCountData} para encomendas de amanhã
	 */
	public static OrdersCountData getTomorrowOrdersCountData(
			DeliveryStats deliveryStats, Iterator<OrderSummary> ordersIterator) {

		OrdersCountData ordersCountData = new OrdersCountData(
				"Tomorrow",
				null,
				deliveryStats.getDueTomorrow()
		);

		LocalDate date = LocalDate.now().plusDays(1);
		LocalTime minTime = LocalTime.MAX;

		while (ordersIterator.hasNext()) {
			OrderSummary order = ordersIterator.next();
			if (order.getDueDate().isBefore(date)) {
				continue;
			}

			if (order.getDueDate().isEqual(date) && order.getDueTime().isBefore(minTime)) {
				minTime = order.getDueTime();
			}

			if (order.getDueDate().isAfter(date)) {
				break;
			}
		}

		if (!LocalTime.MAX.equals(minTime)) {
			ordersCountData.setSubtitle("First delivery " + minTime);
		}

		return ordersCountData;
	}

	/**
	 * Cria dados de contagem para novas encomendas recebidas recentemente.
	 * <p>
	 * O subtítulo é gerado com base no tempo decorrido desde a última encomenda.
	 * </p>
	 *
	 * @param deliveryStats estatísticas de entrega
	 * @param lastOrder última encomenda recebida
	 * @return objeto {@link OrdersCountData} para novas encomendas
	 */
	public static OrdersCountData getNewOrdersCountData(DeliveryStats deliveryStats, Order lastOrder) {
		return new OrdersCountData("New", createSubtitle(lastOrder), deliveryStats.getNewOrders());
	}

	/** Padrão de texto para subtítulos de novas encomendas. */
	private static final String NEW_ORDERS_COUNT_SUBTITLE_PATTERN = "Last %d%s ago";

	/**
	 * Cria o subtítulo para o contador de novas encomendas,
	 * baseado no tempo decorrido desde a última encomenda.
	 * <p>
	 * O tempo é expresso em dias, horas ou minutos. Se a encomenda
	 * for muito recente (ou futura), o texto será "Last just added".
	 * </p>
	 *
	 * @param lastOrder última encomenda registada
	 * @return string representando o tempo decorrido (ex.: "Last 2h ago")
	 */
	private static String createSubtitle(Order lastOrder) {
		LocalDateTime currTime = LocalDateTime.now();
		LocalDateTime timestamp = lastOrder.getHistory().get(0).getTimestamp();

		long value = timestamp.until(currTime, ChronoUnit.DAYS);
		if (value > 0) {
			return String.format(NEW_ORDERS_COUNT_SUBTITLE_PATTERN, value, "d");
		}

		value = timestamp.until(currTime, ChronoUnit.HOURS);
		if (value > 0) {
			return String.format(NEW_ORDERS_COUNT_SUBTITLE_PATTERN, value, "h");
		}

		value = timestamp.until(currTime, ChronoUnit.MINUTES);
		if (value > 0) {
			return String.format(NEW_ORDERS_COUNT_SUBTITLE_PATTERN, value, "m");
		}

		// fallback se os dados contiverem encomendas futuras
		return "Last just added";
	}
}
