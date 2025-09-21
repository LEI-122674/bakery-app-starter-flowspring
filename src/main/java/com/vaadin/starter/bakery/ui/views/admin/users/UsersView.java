package com.vaadin.starter.bakery.ui.views.admin.users;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_USERS;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.ui.MainView;
import com.vaadin.starter.bakery.ui.components.GridComponent;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;

/**
 * {@code UsersView} é a vista administrativa responsável por apresentar a lista
 * de utilizadores da aplicação Bakery.
 * <p>
 * A vista está disponível apenas para utilizadores com o papel
 * {@link Role#ADMIN} ou {@link Role#BARISTA}.
 * </p>
 *
 * <h3>Configuração da rota</h3>
 * <ul>
 *   <li>Rota: {@link BakeryConst#PAGE_USERS}</li>
 *   <li>Layout: {@link MainView}</li>
 *   <li>Título da página: {@link BakeryConst#TITLE_USERS}</li>
 * </ul>
 *
 * <h3>Funcionalidade</h3>
 * <p>
 * A vista consiste num {@link GridComponent} que é injetado e adicionado ao layout
 * quando o bean é inicializado. O grid é responsável por listar os utilizadores.
 * </p>
 */
@Route(value = PAGE_USERS, layout = MainView.class)
@PageTitle(BakeryConst.TITLE_USERS)
@RolesAllowed({Role.ADMIN, Role.BARISTA})
// @AnyOfficeRoleAllowed  // Exemplo de anotação alternativa comentada
public class UsersView extends VerticalLayout {

	/**
	 * Componente de grelha que exibe a lista de utilizadores.
	 * É injetado pelo Spring.
	 */
	@Autowired
	private GridComponent grid;

	/**
	 * Método de inicialização chamado após a injeção de dependências.
	 * <p>
	 * Adiciona o {@link GridComponent} ao layout vertical da vista.
	 * </p>
	 */
	@PostConstruct
	public void init() {
		add(grid);
	}
}
