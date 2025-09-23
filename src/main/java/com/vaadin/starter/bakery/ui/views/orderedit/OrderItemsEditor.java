package com.vaadin.starter.bakery.ui.views.orderedit;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.shared.Registration;
import com.vaadin.starter.bakery.backend.data.entity.OrderItem;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.ui.views.storefront.events.TotalPriceChangeEvent;

/**
 * Editor de múltiplos itens de um pedido.
 *
 * <p>Permite adicionar, editar e remover {@link OrderItemEditor} dentro de um pedido,
 * calculando o preço total e monitorando mudanças de estado.</p>
 *
 * <p>Implementa {@link HasValueAndElement} para integração com formulários e binding de dados
 * com lista de {@link OrderItem}.</p>
 */
public class OrderItemsEditor extends Div
		implements HasValueAndElement<ComponentValueChangeEvent<OrderItemsEditor, List<OrderItem>>, List<OrderItem>> {

	/** Editor vazio usado para adicionar novos itens */
	private OrderItemEditor empty;

	/** Provedor de dados para seleção de produtos nos editores */
	private DataProvider<Product, String> productDataProvider;

	/** Preço total acumulado de todos os itens */
	private int totalPrice = 0;

	/** Indica se houve alterações nos itens */
	private boolean hasChanges = false;

	/** Suporte interno para gerenciamento do valor do componente */
	private final AbstractFieldSupport<OrderItemsEditor, List<OrderItem>> fieldSupport;

	/**
	 * Construtor que inicializa o editor de múltiplos itens.
	 *
	 * @param productDataProvider provedor de dados para produtos
	 */
	public OrderItemsEditor(DataProvider<Product, String> productDataProvider) {
		this.productDataProvider = productDataProvider;
		this.fieldSupport = new AbstractFieldSupport<>(this, Collections.emptyList(),
				Objects::equals, c -> {});
	}

	@Override
	public void setValue(List<OrderItem> items) {
		fieldSupport.setValue(items);
		removeAll();
		totalPrice = 0;
		hasChanges = false;

		if (items != null) {
			items.forEach(this::createEditor);
		}
		createEmptyElement();
		setHasChanges(false);
	}

	/**
	 * Cria um editor individual para cada {@link OrderItem}.
	 *
	 * @param value item do pedido
	 * @return editor criado
	 */
	private OrderItemEditor createEditor(OrderItem value) {
		OrderItemEditor editor = new OrderItemEditor(productDataProvider);
		getElement().appendChild(editor.getElement());

		editor.addPriceChangeListener(e -> updateTotalPriceOnItemPriceChange(e.getOldValue(), e.getNewValue()));
		editor.addProductChangeListener(e -> productChanged(e.getSource(), e.getProduct()));
		editor.addCommentChangeListener(e -> setHasChanges(true));
		editor.addDeleteListener(e -> {
			OrderItemEditor orderItemEditor = e.getSource();
			if (orderItemEditor != empty) {
				remove(orderItemEditor);
				OrderItem orderItem = orderItemEditor.getValue();
				setValue(getValue().stream()
						.filter(element -> element != orderItem)
						.collect(Collectors.toList()));
				updateTotalPriceOnItemPriceChange(orderItem.getTotalPrice(), 0);
				setHasChanges(true);
			}
		});

		editor.setValue(value);
		return editor;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		HasValueAndElement.super.setReadOnly(readOnly);
		getChildren().forEach(e -> ((OrderItemEditor) e).setReadOnly(readOnly));
	}

	@Override
	public List<OrderItem> getValue() {
		return fieldSupport.getValue();
	}

	/**
	 * Trata mudanças de produto em um editor.
	 *
	 * @param item editor que mudou
	 * @param product novo produto selecionado
	 */
	private void productChanged(OrderItemEditor item, Product product) {
		setHasChanges(true);
		if (empty == item) {
			createEmptyElement();
			OrderItem orderItem = new OrderItem();
			orderItem.setProduct(product);
			item.setValue(orderItem);
			setValue(Stream.concat(getValue().stream(), Stream.of(orderItem)).collect(Collectors.toList()));
		}
	}

	/**
	 * Atualiza o preço total do pedido com base em alterações de preço de um item.
	 *
	 * @param oldItemPrice preço antigo do item
	 * @param newItemPrice novo preço do item
	 */
	private void updateTotalPriceOnItemPriceChange(int oldItemPrice, int newItemPrice) {
		final int delta = newItemPrice - oldItemPrice;
		totalPrice += delta;
		setHasChanges(true);
		fireEvent(new TotalPriceChangeEvent(this, totalPrice));
	}

	/**
	 * Cria o editor vazio usado para adicionar novos itens.
	 */
	private void createEmptyElement() {
		empty = createEditor(null);
	}

	/**
	 * Adiciona um listener para mudanças no preço total.
	 *
	 * @param listener listener a ser adicionado
	 * @return registro do listener
	 */
	public Registration addPriceChangeListener(ComponentEventListener<TotalPriceChangeEvent> listener) {
		return addListener(TotalPriceChangeEvent.class, listener);
	}

	/**
	 * Retorna se houve alterações nos itens.
	 *
	 * @return {@code true} se houve alterações, {@code false} caso contrário
	 */
	public boolean hasChanges() {
		return hasChanges;
	}

	/**
	 * Define se houve alterações nos itens.
	 *
	 * @param hasChanges indica se houve mudanças
	 */
	private void setHasChanges(boolean hasChanges) {
		this.hasChanges = hasChanges;
		if (hasChanges) {
			fireEvent(new com.vaadin.starter.bakery.ui.views.storefront.events.ValueChangeEvent(this));
		}
	}

	/**
	 * Valida todos os editores de itens.
	 *
	 * @return stream de campos com erros de validação
	 */
	public Stream<HasValue<?, ?>> validate() {
		return getChildren()
				.filter(component -> fieldSupport.getValue().size() == 0 || !component.equals(empty))
				.map(editor -> ((OrderItemEditor) editor).validate())
				.flatMap(stream -> stream);
	}

	@Override
	public Registration addValueChangeListener(
			ValueChangeListener<? super ComponentValueChangeEvent<OrderItemsEditor, List<OrderItem>>> listener) {
		return fieldSupport.addValueChangeListener(listener);
	}
}
