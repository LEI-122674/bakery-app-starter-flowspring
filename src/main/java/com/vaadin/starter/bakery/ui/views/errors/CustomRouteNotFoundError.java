package com.vaadin.starter.bakery.ui.views.errors;

import javax.servlet.http.HttpServletResponse;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.RouteNotFoundError;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.starter.bakery.ui.MainView;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;

/**
 * Classe personalizada para tratar erros de rota não encontrada (HTTP 404).
 *
 * <p>Substitui o comportamento padrão de {@link RouteNotFoundError},
 * fornecendo uma mensagem simples e um link para a página inicial da aplicação.</p>
 *
 * <p>Quando um utilizador tenta aceder a uma rota inexistente,
 * esta classe é responsável por mostrar a mensagem de erro
 * e retornar o código de estado HTTP apropriado.</p>
 *
 * <p><b>Exemplo de mensagem exibida:</b></p>
 * <blockquote>
 * Oops you hit a 404. Go to the front page.
 * </blockquote>
 */
@ParentLayout(MainView.class)
@PageTitle(BakeryConst.TITLE_NOT_FOUND)
public class CustomRouteNotFoundError extends RouteNotFoundError {

	/**
	 * Construtor que inicializa a mensagem e adiciona um link
	 * de navegação para a página inicial.
	 *
	 * <p>O link é criado através de {@link ElementFactory#createRouterLink}
	 * e convertido para {@link RouterLink}, sendo posteriormente
	 * anexado ao layout do erro.</p>
	 */
	public CustomRouteNotFoundError() {
		RouterLink link = Component.from(
				ElementFactory.createRouterLink("", "Go to the front page."),
				RouterLink.class);
		getElement().appendChild(new Text("Oops you hit a 404. ").getElement(), link.getElement());
	}

	/**
	 * Define o parâmetro de erro e retorna o código de estado HTTP
	 * correspondente a "Não Encontrado" (404).
	 *
	 * @param event evento de navegação que originou o erro
	 * @param parameter parâmetros associados ao erro de rota não encontrada
	 * @return {@link HttpServletResponse#SC_NOT_FOUND} (404)
	 */
	@Override
	public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
		return HttpServletResponse.SC_NOT_FOUND;
	}
}
