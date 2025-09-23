package com.vaadin.starter.bakery.ui.views.orderedit;

import java.util.Objects;
import java.util.stream.Stream;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.shared.Registration;
import com.vaadin.starter.bakery.backend.data.entity.OrderItem;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.ui.utils.FormattingUtils;
import com.vaadin.starter.bakery.ui.views.storefront.events.CommentChangeEvent;
import com.vaadin.starter.bakery.ui.views.storefront.events.DeleteEvent;
import com.vaadin.starter.bakery.ui.views.storefront.events.PriceChangeEvent;
import com.vaadin.starter.bakery.ui.views.storefront.events.ProductChangeEvent;

/**
 * Editor de um único item de pedido.
 *
 * <p>Permite selecionar um produto, quantidade, adicionar comentários,
 * calcular preço total e deletar o item do pedido.</p>
 *
 * <p>Implementa {@link HasValueAndElement} para integração com
 * formulários e binding de dados.</p>
 */
@Tag("order-item-editor")
@JsModule("./src/views/orderedit/order-item-editor.js")
public class OrderItemEditor extends LitTemplate
		implements HasValueAndElement<ComponentValueChangeEvent<OrderItemEditor, OrderItem>, OrderItem> {

	/** ComboBox de produtos disponíveis */
	@Id("products")
	private ComboBox<Product> products;

	/** Botão para deletar o item */
	@Id("delete")
	private Button delete;

	/** Campo de quantidade do produto */
	@Id("amount")
	private IntegerField amount;

	/** Exibe o preço total do item */
	@Id("price")
	private Div price;

	/** Campo de comentário do item */
	@Id("comment")
	private TextField comment;

	/** Preço total calculado do item */
	private int totalPrice;

	/** Suporte interno para gerenciamento do valor do componente */
	private final AbstractFieldSupport<OrderItemEditor, OrderItem> fieldSupport;

	/** Binder para validação dos campos do item */
	private BeanValidationBinder<OrderItem> binder = new BeanValidationBinder<>(OrderItem.class);

	/**
	 * Construtor que inicializa o editor de item de pedido.
	 *
	 * @param productDataProvider provedor de dados para os produtos
	 */
	public OrderItemEditor(DataProvider<Product, String> productDataProvider) {
		this.fieldSupport = new AbstractFieldSupport<>(this, null, Objects::equals, c -> {});
		products.setItems(productDataProvider);
		products.addValueChangeListener(e -> {
			setPrice();
			fireEvent(new ProductChangeEvent(this, e.getValue()));
		});
		amount.addValueChangeListener(e -> setPrice());
		comment.addValueChangeListener(e -> fireEvent(new CommentChangeEvent(this, e.getValue())));

		binder.forField(amount).bind("quantity");
		amount.setRequiredIndicatorVisible(true);
		binder.forField(comment).bind("comment");
		binder.forField(products).bind("product");
		products.setRequired(true);

		delete.addClickListener(e -> fireEvent(new DeleteEvent(this)));
		setPrice();
	}

	/**
	 * Calcula e atualiza o preço total do item baseado na quantidade e preço do produto.
	 * Dispara {@link PriceChangeEvent} se o preço for alterado.
	 */
	private void setPrice() {
		int oldValue = totalPrice;
		Integer selectedAmount = amount.getValue();
		Product product = products.getValue();
		totalPrice = 0;
		if (selectedAmount != null && product != null) {
			totalPrice = selectedAmount * product.getPrice();
		}
		price.setText(FormattingUtils.formatAsCurrency(totalPrice));
		if (oldValue != totalPrice) {
			fireEvent(new PriceChangeEvent(this, oldValue, totalPrice));
		}
	}

	@Override
	public void setValue(OrderItem value) {
		fieldSupport.setValue(value);
		binder.setBean(value);
		boolean noProductSelected = value == null || value.getProduct() == null;
		amount.setEnabled(!noProductSelected);
		delete.setEnabled(!noProductSelected);
		comment.setEnabled(!noProductSelected);
		setPrice();
	}

	@Override
	public OrderItem getValue() {
		return fieldSupport.getValue();
	}

	/**
	 * Valida os campos do item.
	 *
	 * @return stream de componentes com erros de validação
	 */
	public Stream<HasValue<?, ?>> validate() {
		return binder.validate().getFieldValidationErrors().stream()
				.map(BindingValidationStatus::getField);
	}

	/**
	 * Adiciona um listener para mudanças no preço do item.
	 *
	 * @param listener listener a ser adicionado
	 * @return registro do listener
	 */
	public Registration addPriceChangeListener(ComponentEventListener<PriceChangeEvent> listener) {
		return addListener(PriceChangeEvent.class, listener);
	}

	/**
	 * Adiciona um listener para mudanças no produto selecionado.
	 *
	 * @param listener listener a ser adicionado
	 * @return registro do listener
	 */
	public Registration addProductChangeListener(ComponentEventListener<ProductChangeEvent> listener) {
		return addListener(ProductChangeEvent.class, listener);
	}

	/**
	 * Adiciona um listener para mudanças no comentário do item.
	 *
	 * @param listener listener a ser adicionado
	 * @return registro do listener
	 */
	public Registration addCommentChangeListener(ComponentEventListener<CommentChangeEvent> listener) {
		return addListener(CommentChangeEvent.class, listener);
	}

	/**
	 * Adiciona um listener para quando o item é deletado.
	 *
	 * @param listener listener a ser adicionado
	 * @return registro do listener
	 */
	public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
		return addListener(DeleteEvent.class, listener);
	}

	@Override
	public Registration addValueChangeListener(
			ValueChangeListener<? super ComponentValueChangeEvent<OrderItemEditor, OrderItem>> listener) {
		return fieldSupport.addValueChangeListener(listener);
	}
}
