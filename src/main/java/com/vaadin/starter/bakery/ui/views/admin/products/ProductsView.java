package com.vaadin.starter.bakery.ui.views.admin.products;

import com.vaadin.flow.component.crud.BinderCrudEditor;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.starter.bakery.app.security.CurrentUser;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.service.ProductService;
import com.vaadin.starter.bakery.ui.MainView;
import com.vaadin.starter.bakery.ui.crud.AbstractBakeryCrudView;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.utils.converters.CurrencyFormatter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Currency;

import javax.annotation.security.RolesAllowed;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_PRODUCTS;

/**
 * {@code ProductsView} é a vista administrativa para gerir produtos da aplicação Bakery.
 * <p>
 * Esta classe estende {@link AbstractBakeryCrudView}, disponibilizando um CRUD completo
 * para entidades do tipo {@link Product}. A interface inclui:
 * </p>
 * <ul>
 *   <li>Uma grelha ({@link Grid}) para listar os produtos existentes, com nome e preço unitário.</li>
 *   <li>Um formulário para criar ou editar produtos, com validação de campos.</li>
 * </ul>
 *
 * <h3>Segurança</h3>
 * Apenas utilizadores com o papel {@link Role#ADMIN} podem aceder a esta vista.
 *
 * <h3>Configuração de rota</h3>
 * <ul>
 *   <li>Rota: {@link BakeryConst#PAGE_PRODUCTS}</li>
 *   <li>Layout: {@link MainView}</li>
 *   <li>Título da página: {@link BakeryConst#TITLE_PRODUCTS}</li>
 * </ul>
 */
@Route(value = PAGE_PRODUCTS, layout = MainView.class)
@PageTitle(BakeryConst.TITLE_PRODUCTS)
@RolesAllowed(Role.ADMIN)
public class ProductsView extends AbstractBakeryCrudView<Product> {

	/** Utilitário para formatar valores monetários no grid. */
	private CurrencyFormatter currencyFormatter = new CurrencyFormatter();

	/**
	 * Construtor da vista de produtos.
	 *
	 * @param service serviço responsável pela gestão de {@link Product}
	 * @param currentUser utilizador autenticado atualmente
	 */
	@Autowired
	public ProductsView(ProductService service, CurrentUser currentUser) {
		super(Product.class, service, new Grid<>(), createForm(), currentUser);
	}

	/**
	 * Cria uma nova instância de {@link Product}, usada quando
	 * se pretende adicionar um novo produto através do formulário.
	 *
	 * @return novo objeto {@link Product}
	 */
	@Override
	protected Product createItem() {
		return new Product();
	}

	/**
	 * Configura as colunas da grelha de produtos.
	 *
	 * @param grid grelha a ser configurada
	 */
	@Override
	protected void setupGrid(Grid<Product> grid) {
		grid.addColumn(Product::getName).setHeader("Product Name").setFlexGrow(10);
		grid.addColumn(p -> currencyFormatter.encode(p.getPrice())).setHeader("Unit Price");
	}

	/**
	 * Devolve a rota base associada a esta vista.
	 *
	 * @return string correspondente à rota {@link BakeryConst#PAGE_PRODUCTS}
	 */
	@Override
	protected String getBasePage() {
		return PAGE_PRODUCTS;
	}

	/**
	 * Cria o formulário usado no editor CRUD para adicionar ou editar produtos.
	 * O formulário inclui:
	 * <ul>
	 *   <li>Campo de texto para o nome do produto.</li>
	 *   <li>Campo de texto para o preço unitário, com conversão através de {@link PriceConverter}.</li>
	 *   <li>Validação de entrada para garantir que apenas números válidos são aceites.</li>
	 *   <li>Prefixo com o símbolo da moeda configurada.</li>
	 * </ul>
	 *
	 * @return instância de {@link BinderCrudEditor} configurada para {@link Product}
	 */
	private static BinderCrudEditor<Product> createForm() {
		TextField name = new TextField("Product name");
		name.getElement().setAttribute("colspan", "2");

		TextField price = new TextField("Unit price");
		price.getElement().setAttribute("colspan", "2");

		FormLayout form = new FormLayout(name, price);

		BeanValidationBinder<Product> binder = new BeanValidationBinder<>(Product.class);

		// Binding do campo nome → propriedade "name"
		binder.bind(name, "name");

		// Binding do campo preço → propriedade "price" com conversor
		binder.forField(price).withConverter(new PriceConverter()).bind("price");
		price.setPattern("\\d+(\\.\\d?\\d?)?$");
		price.setPreventInvalidInput(true);

		String currencySymbol = Currency.getInstance(BakeryConst.APP_LOCALE).getSymbol();
		price.setPrefixComponent(new Span(currencySymbol));

		return new BinderCrudEditor<>(binder, form);
	}

}
