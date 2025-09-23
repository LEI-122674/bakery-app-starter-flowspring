package com.vaadin.starter.bakery.ui.views.orderedit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.shared.Registration;
import com.vaadin.starter.bakery.backend.data.entity.HistoryItem;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.ui.events.CancelEvent;
import com.vaadin.starter.bakery.ui.events.SaveEvent;
import com.vaadin.starter.bakery.ui.utils.converters.CurrencyFormatter;
import com.vaadin.starter.bakery.ui.utils.converters.LocalDateTimeConverter;
import com.vaadin.starter.bakery.ui.utils.converters.LocalTimeConverter;
import com.vaadin.starter.bakery.ui.views.storefront.converters.StorefrontLocalDateConverter;
import com.vaadin.starter.bakery.ui.views.storefront.events.CommentEvent;
import com.vaadin.starter.bakery.ui.views.storefront.events.EditEvent;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

/**
 * Componente de UI que exibe um resumo completo (somente leitura) de um pedido,
 * incluindo um campo de comentários para adicionar novas observações.
 *
 * <p>Permite visualizar dados do pedido, histórico de alterações e interagir
 * com eventos de salvar, editar, cancelar e adicionar comentários.</p>
 *
 * <p>O componente utiliza {@link LitTemplate} e um template JavaScript associado
 * para renderizar a interface.</p>
 */
@Tag("order-details")
@JsModule("./src/views/orderedit/order-details.js")
public class OrderDetails extends LitTemplate {

	/** Pedido atualmente exibido */
	private Order order;

	/** Botão de voltar */
	@Id("back")
	private Button back;

	/** Botão de cancelar */
	@Id("cancel")
	private Button cancel;

	/** Botão de salvar */
	@Id("save")
	private Button save;

	/** Botão de editar */
	@Id("edit")
	private Button edit;

	/** Elemento que contém o histórico de alterações do pedido */
	@Id("history")
	private Element history;

	/** Elemento que contém os comentários do pedido */
	@Id("comment")
	private Element comment;

	/** Botão para enviar comentário */
	@Id("sendComment")
	private Button sendComment;

	/** Campo de texto para inserir comentário */
	@Id("commentField")
	private TextField commentField;

	/** Flag indicando se o pedido foi modificado */
	private boolean isDirty;

	/**
	 * Construtor que inicializa os listeners para salvar, cancelar, editar
	 * e enviar comentários.
	 */
	public OrderDetails() {
		sendComment.addClickListener(e -> {
			String message = commentField.getValue();
			message = message == null ? "" : message.trim();
			if (!message.isEmpty()) {
				commentField.clear();
				fireEvent(new CommentEvent(this, order.getId(), message));
			}
		});
		save.addClickListener(e -> fireEvent(new SaveEvent(this, false)));
		cancel.addClickListener(e -> fireEvent(new CancelEvent(this, false)));
		edit.addClickListener(e -> fireEvent(new EditEvent(this)));
	}

	/**
	 * Exibe os detalhes de um pedido.
	 *
	 * @param order o pedido a exibir
	 * @param review indica se o pedido está em modo de revisão
	 */
	public void display(Order order, boolean review) {
		getElement().setProperty("review", review);
		this.order = order;

		JsonObject item = beanToJson(order);

		// Formata campos adicionais para exibição
		item.put("formattedDueDate", beanToJson(new StorefrontLocalDateConverter().encode(order.getDueDate())));
		item.put("formattedDueTime", new LocalTimeConverter().encode(order.getDueTime()));
		item.put("formattedTotalPrice", new CurrencyFormatter().encode(order.getTotalPrice()));

		// Formata preço de cada produto
		JsonArray orderItems = item.getArray("items");
		for (int i = 0; i < orderItems.length(); i++) {
			JsonObject itemProduct = orderItems.getObject(i).getObject("product");
			Product product = order.getItems().get(i).getProduct();
			itemProduct.put("formattedPrice", new CurrencyFormatter().encode(product.getPrice()));
		}

		// Formata timestamps do histórico
		JsonArray orderHistory = item.getArray("history");
		for (int i = 0; i < orderHistory.length(); i++) {
			JsonObject itemHistory = orderHistory.getObject(i);
			HistoryItem historyItem = order.getHistory().get(i);
			itemHistory.put("formattedTimestamp", new LocalDateTimeConverter().encode(historyItem.getTimestamp()));
		}

		getElement().setPropertyJson("item", item);

		if (!review) {
			commentField.clear();
		}
		this.isDirty = review;
	}

	/**
	 * Converte um bean Java para JsonObject usando Jackson.
	 *
	 * @param bean objeto Java a converter
	 * @return JsonObject equivalente, ou null em caso de erro
	 */
	private JsonObject beanToJson(Object bean) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			return Json.parse(objectMapper.writeValueAsString(bean));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/** @return true se o pedido foi modificado */
	public boolean isDirty() {
		return isDirty;
	}

	/** Define se o pedido foi modificado */
	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}

	/**
	 * Adiciona um listener para o evento de salvar.
	 *
	 * @param listener listener a ser adicionado
	 * @return registro do listener
	 */
	public Registration addSaveListenter(ComponentEventListener<SaveEvent> listener) {
		return addListener(SaveEvent.class, listener);
	}

	/**
	 * Adiciona um listener para o evento de edição.
	 *
	 * @param listener listener a ser adicionado
	 * @return registro do listener
	 */
	public Registration addEditListener(ComponentEventListener<EditEvent> listener) {
		return addListener(EditEvent.class, listener);
	}

	/**
	 * Adiciona um listener para o botão de voltar.
	 *
	 * @param listener listener a ser adicionado
	 * @return registro do listener
	 */
	public Registration addBackListener(ComponentEventListener<ClickEvent<Button>> listener) {
		return back.addClickListener(listener);
	}

	/**
	 * Adiciona um listener para envio de comentários.
	 *
	 * @param listener listener a ser adicionado
	 * @return registro do listener
	 */
	public Registration addCommentListener(ComponentEventListener<CommentEvent> listener) {
		return addListener(CommentEvent.class, listener);
	}

	/**
	 * Adiciona um listener para o evento de cancelar.
	 *
	 * @param listener listener a ser adicionado
	 * @return registro do listener
	 */
	public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
		return addListener(CancelEvent.class, listener);
	}
}
