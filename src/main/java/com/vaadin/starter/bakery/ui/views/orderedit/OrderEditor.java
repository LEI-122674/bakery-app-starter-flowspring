package com.vaadin.starter.bakery.ui.views.orderedit;

import static com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil.createItemLabelGenerator;

import java.time.LocalTime;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.validator.BeanValidator;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.starter.bakery.backend.data.OrderState;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.PickupLocation;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.service.PickupLocationService;
import com.vaadin.starter.bakery.backend.service.ProductService;
import com.vaadin.starter.bakery.ui.crud.CrudEntityDataProvider;
import com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil;
import com.vaadin.starter.bakery.ui.events.CancelEvent;
import com.vaadin.starter.bakery.ui.utils.FormattingUtils;
import com.vaadin.starter.bakery.ui.utils.converters.LocalTimeConverter;
import com.vaadin.starter.bakery.ui.views.storefront.events.ReviewEvent;
import com.vaadin.starter.bakery.ui.views.storefront.events.ValueChangeEvent;

/**
 * Editor de pedidos que permite criar, ler, atualizar e revisar pedidos.
 *
 * <p>O editor gerencia os dados de {@link Order}, incluindo estado, data e hora
 * de entrega, local de pickup, informações do cliente e itens do pedido.</p>
 *
 * <p>O editor também dispara eventos de revisão e cancelamento e permite
 * integração com {@link OrderItemsEditor} para gerenciar produtos e preços.</p>
 */
@Tag("order-editor")
@JsModule("./src/views/orderedit/order-editor.js")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OrderEditor extends LitTemplate {

	/** Título do editor */
	@Id("title")
	private H2 title;

	/** Container de informações meta do pedido */
	@Id("metaContainer")
	private Div metaContainer;

	/** Exibe o número do pedido */
	@Id("orderNumber")
	private Span orderNumber;

	/** ComboBox de estado do pedido */
	@Id("status")
	private ComboBox<OrderState> status;

	/** DatePicker da data de entrega */
	@Id("dueDate")
	private DatePicker dueDate;

	/** ComboBox da hora de entrega */
	@Id("dueTime")
	private ComboBox<LocalTime> dueTime;

	/** ComboBox do local de pickup */
	@Id("pickupLocation")
	private ComboBox<PickupLocation> pickupLocation;

	/** Nome do cliente */
	@Id("customerName")
	private TextField customerName;

	/** Número de telefone do cliente */
	@Id("customerNumber")
	private TextField customerNumber;

	/** Detalhes adicionais do cliente */
	@Id("customerDetails")
	private TextField customerDetails;

	/** Botão de cancelar edição */
	@Id("cancel")
	private Button cancel;

	/** Botão para revisão do pedido */
	@Id("review")
	private Button review;

	/** Container para itens do pedido */
	@Id("itemsContainer")
	private Div itemsContainer;

	/** Editor dos itens do pedido */
	private OrderItemsEditor itemsEditor;

	/** Usuário atualmente logado */
	private User currentUser;

	/** Binder para validação de campos do pedido */
	private BeanValidationBinder<Order> binder = new BeanValidationBinder<>(Order.class);

	/** Conversor de hora local para exibição */
	private final LocalTimeConverter localTimeConverter = new LocalTimeConverter();

	/**
	 * Construtor que inicializa o editor de pedidos, configura data providers
	 * para produtos e locais de pickup, e adiciona listeners para revisão e cancelamento.
	 *
	 * @param locationService serviço de locais de pickup
	 * @param productService serviço de produtos
	 */
	@Autowired
	public OrderEditor(PickupLocationService locationService, ProductService productService) {
		DataProvider<PickupLocation, String> locationDataProvider = new CrudEntityDataProvider<>(locationService);
		DataProvider<Product, String> productDataProvider = new CrudEntityDataProvider<>(productService);
		itemsEditor = new OrderItemsEditor(productDataProvider);

		itemsContainer.add(itemsEditor);

		cancel.addClickListener(e -> fireEvent(new CancelEvent(this, false)));
		review.addClickListener(e -> fireEvent(new ReviewEvent(this)));

		status.setItemLabelGenerator(createItemLabelGenerator(OrderState::getDisplayName));
		status.setItems(DataProvider.ofItems(OrderState.values()));
		status.addValueChangeListener(
				e -> {
					getElement().setProperty("status", DataProviderUtil.convertIfNotNull(e.getValue(), OrderState::name));
				});
		binder.forField(status)
				.withValidator(new BeanValidator(Order.class, "state"))
				.bind(Order::getState, (o, s) -> {
					o.changeState(currentUser, s);
				});

		dueDate.setRequired(true);
		binder.bind(dueDate, "dueDate");

		SortedSet<LocalTime> timeValues = IntStream.rangeClosed(8, 16)
				.mapToObj(i -> LocalTime.of(i, 0))
				.collect(Collectors.toCollection(TreeSet::new));
		dueTime.setItems(timeValues);
		dueTime.setItemLabelGenerator(localTimeConverter::encode);
		binder.bind(dueTime, "dueTime");

		pickupLocation.setItemLabelGenerator(createItemLabelGenerator(PickupLocation::getName));
		pickupLocation.setItems(locationDataProvider);
		binder.bind(pickupLocation, "pickupLocation");
		pickupLocation.setRequired(false);

		customerName.setRequired(true);
		binder.bind(customerName, "customer.fullName");

		customerNumber.setRequired(true);
		binder.bind(customerNumber, "customer.phoneNumber");

		binder.bind(customerDetails, "customer.details");

		itemsEditor.setRequiredIndicatorVisible(true);
		binder.bind(itemsEditor, "items");

		itemsEditor.addPriceChangeListener(e -> setTotalPrice(e.getTotalPrice()));

		ComponentUtil.addListener(itemsEditor, ValueChangeEvent.class, e -> review.setEnabled(hasChanges()));
		binder.addValueChangeListener(e -> {
			if (e.getOldValue() != null) {
				review.setEnabled(hasChanges());
			}
		});
	}

	/**
	 * @return true se houver alterações nos campos ou nos itens do pedido
	 */
	public boolean hasChanges() {
		return binder.hasChanges() || itemsEditor.hasChanges();
	}

	/** Limpa o formulário do editor */
	public void clear() {
		binder.readBean(null);
		itemsEditor.setValue(null);
	}

	/** Fecha o editor e reseta o preço total */
	public void close() {
		setTotalPrice(0);
	}

	/**
	 * Grava os dados do editor no objeto {@link Order}.
	 *
	 * @param order pedido a ser atualizado
	 * @throws ValidationException se algum campo for inválido
	 */
	public void write(Order order) throws ValidationException {
		binder.writeBean(order);
	}

	/**
	 * Lê os dados de um {@link Order} e atualiza a UI.
	 *
	 * @param order pedido a exibir
	 * @param isNew indica se é um novo pedido
	 */
	public void read(Order order, boolean isNew) {
		binder.readBean(order);

		this.orderNumber.setText(isNew ? "" : order.getId().toString());
		title.setVisible(isNew);
		metaContainer.setVisible(!isNew);

		if (order.getState() != null) {
			getElement().setProperty("status", order.getState().name());
		}

		review.setEnabled(false);
	}

	/**
	 * Valida os campos do pedido e retorna os componentes com erros.
	 *
	 * @return stream de componentes com erros de validação
	 */
	public Stream<HasValue<?, ?>> validate() {
		Stream<HasValue<?, ?>> errorFields = binder.validate().getFieldValidationErrors().stream()
				.map(BindingValidationStatus::getField);

		return Stream.concat(errorFields, itemsEditor.validate());
	}

	/**
	 * Adiciona um listener para revisão do pedido.
	 *
	 * @param listener listener a ser adicionado
	 * @return registro do listener
	 */
	public Registration addReviewListener(ComponentEventListener<ReviewEvent> listener) {
		return addListener(ReviewEvent.class, listener);
	}

	/**
	 * Adiciona um listener para cancelamento da edição.
	 *
	 * @param listener listener a ser adicionado
	 * @return registro do listener
	 */
	public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
		return addListener(CancelEvent.class, listener);
	}

	/**
	 * Define o preço total exibido no editor.
	 *
	 * @param totalPrice preço total em inteiro (centavos)
	 */
	private void setTotalPrice(int totalPrice) {
		getElement().getProperty("totalPrice", FormattingUtils.formatAsCurrency(totalPrice));
	}

	/**
	 * Define o usuário atualmente logado, necessário para alterações de estado.
	 *
	 * @param currentUser usuário logado
	 */
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
}
