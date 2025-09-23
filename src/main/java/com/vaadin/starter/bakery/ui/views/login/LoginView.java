package com.vaadin.starter.bakery.ui.views.login;

import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.starter.bakery.app.security.SecurityUtils;
import com.vaadin.starter.bakery.ui.views.storefront.StorefrontView;

/**
 * Página de login da aplicação Bakery.
 *
 * <p>Esta classe estende {@link LoginOverlay} para fornecer
 * uma interface de login modal. Implementa {@link BeforeEnterObserver}
 * e {@link AfterNavigationObserver} para controlar o fluxo de navegação
 * antes e depois de eventos de navegação.</p>
 *
 * <p>O login utiliza credenciais de exemplo:
 * <ul>
 *   <li>admin@vaadin.com / admin</li>
 *   <li>barista@vaadin.com / barista</li>
 * </ul>
 * </p>
 *
 * <p>Funcionalidades principais:
 * <ul>
 *   <li>Redirecionamento automático para {@link StorefrontView} se o utilizador já estiver autenticado.</li>
 *   <li>Exibição de mensagem de erro quando o parâmetro "error" está presente na URL.</li>
 *   <li>Personalização do formulário de login, incluindo título, campos de usuário e senha, botão de submit e remoção do link de "forgot password".</li>
 * </ul>
 * </p>
 */
@Route
@PageTitle("###Bakery###")
public class LoginView extends LoginOverlay
		implements AfterNavigationObserver, BeforeEnterObserver {

	/**
	 * Construtor que inicializa a sobreposição de login com
	 * texto e formulário internacionalizado (i18n).
	 */
	public LoginView() {
		LoginI18n i18n = LoginI18n.createDefault();
		i18n.setHeader(new LoginI18n.Header());
		i18n.getHeader().setTitle("###Bakery###");
		i18n.getHeader().setDescription(
				"admin@vaadin.com + admin\n" +
						"barista@vaadin.com + barista"
		);
		i18n.setAdditionalInformation(null);
		i18n.setForm(new LoginI18n.Form());
		i18n.getForm().setSubmit("Sign in");
		i18n.getForm().setTitle("Sign in");
		i18n.getForm().setUsername("Email");
		i18n.getForm().setPassword("Password");

		setI18n(i18n);
		setForgotPasswordButtonVisible(false);
		setAction("login");
	}

	/**
	 * Executado antes de entrar na rota.
	 * Se o utilizador já estiver autenticado, redireciona para {@link StorefrontView}.
	 * Caso contrário, abre o overlay de login.
	 *
	 * @param event evento de navegação antes da entrada
	 */
	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		if (SecurityUtils.isUserLoggedIn()) {
			event.forwardTo(StorefrontView.class);
		} else {
			setOpened(true);
		}
	}

	/**
	 * Executado após a navegação.
	 * Mostra uma mensagem de erro no formulário se o parâmetro
	 * "error" estiver presente na URL.
	 *
	 * @param event evento de navegação após a entrada
	 */
	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		setError(
				event.getLocation().getQueryParameters().getParameters().containsKey("error")
		);
	}
}
