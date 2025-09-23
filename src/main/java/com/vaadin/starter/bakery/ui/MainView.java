package com.vaadin.starter.bakery.ui;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.TITLE_DASHBOARD;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.TITLE_LOGOUT;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.TITLE_PRODUCTS;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.TITLE_STOREFRONT;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.TITLE_USERS;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.views.HasConfirmation;
import com.vaadin.starter.bakery.ui.views.admin.products.ProductsView;
import com.vaadin.starter.bakery.ui.views.admin.users.UsersView;
import com.vaadin.starter.bakery.ui.views.dashboard.DashboardView;
import com.vaadin.starter.bakery.ui.views.storefront.StorefrontView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

/**
 * The main application layout for the Bakery App.
 * <p>
 * This class provides a navigation menu with tabs for different views
 * (Storefront, Dashboard, Users, Products) and handles logout actions.
 * It also manages a global {@link ConfirmDialog} for views that implement {@link HasConfirmation}.
 */

public class MainView extends AppLayout {

	/**
	 * Used to check access permissions for views based on Spring Security annotations.
	 */
	@Autowired
	private AccessAnnotationChecker accessChecker;

	/**
	 * A global confirm dialog shared with views that require confirmation.
	 */
	private final ConfirmDialog confirmDialog = new ConfirmDialog();

	/**
	 * The main navigation menu containing tabs.
	 */
	private Tabs menu;

	private static final String LOGOUT_SUCCESS_URL = "/" + BakeryConst.PAGE_ROOT;

	/**
	 * Initializes the main view after all dependencies are injected.
	 * <p>
	 * Sets up the confirm dialog, navigation menu, app name, and logout handling.
	 */
	@PostConstruct
	public void init() {
		confirmDialog.setCancelable(true);
		confirmDialog.setConfirmButtonTheme("raised tertiary error");
		confirmDialog.setCancelButtonTheme("raised tertiary");

		this.setDrawerOpened(false);
		Span appName = new Span("###Bakery###");
		appName.addClassName("hide-on-mobile");

		menu = createMenuTabs();

		// Handle logout action
		menu.addSelectedChangeListener(e -> {
			if (e.getSelectedTab() == null) {
				return;
			}

			e.getSelectedTab().getId().ifPresent(id -> {
				if ("logout-tab".equals(id)) {
					UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
					SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
					logoutHandler.logout(
							VaadinServletRequest.getCurrent().getHttpServletRequest(), null,
							null
					);
				}
			});
		});

		this.addToNavbar(appName);
		this.addToNavbar(true, menu);
		this.getElement().appendChild(confirmDialog.getElement());

		// Hide navbar on search focus
		getElement().addEventListener("search-focus", e -> getElement().getClassList().add("hide-navbar"));
		getElement().addEventListener("search-blur", e -> getElement().getClassList().remove("hide-navbar"));
	}

	/**
	 * Called after navigation occurs.
	 * <p>
	 * Updates the selected tab in the menu to match the current view,
	 * and assigns the confirm dialog to views implementing {@link HasConfirmation}.
	 */
	@Override
	protected void afterNavigation() {
		super.afterNavigation();
		confirmDialog.setOpened(false);
		if (getContent() instanceof HasConfirmation) {
			((HasConfirmation) getContent()).setConfirmDialog(confirmDialog);
		}

		RouteConfiguration configuration = RouteConfiguration.forSessionScope();
		if (configuration.isRouteRegistered(this.getContent().getClass())) {
			String target = configuration.getUrl(this.getContent().getClass());
			Optional<Component> tabToSelect = menu.getChildren().filter(tab -> {
				Component child = tab.getChildren().findFirst().get();
				return child instanceof RouterLink && ((RouterLink) child).getHref().equals(target);
			}).findFirst();
			tabToSelect.ifPresent(tab -> menu.setSelectedTab((Tab) tab));
		} else {
			menu.setSelectedTab(null);
		}
	}

	/**
	 * Creates the main menu tabs for navigation.
	 *
	 * @return the {@link Tabs} component containing all menu tabs
	 */
	private Tabs createMenuTabs() {
		final Tabs tabs = new Tabs();
		tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
		tabs.add(getAvailableTabs());
		return tabs;
	}

	/**
	 * Generates the available tabs for the main menu based on access permissions.
	 *
	 * @return an array of {@link Tab} components
	 */
	private Tab[] getAvailableTabs() {
		final List<Tab> tabs = new ArrayList<>(4);
		tabs.add(createTab(VaadinIcon.EDIT, TITLE_STOREFRONT, StorefrontView.class));
		tabs.add(createTab(VaadinIcon.CLOCK, TITLE_DASHBOARD, DashboardView.class));
		if (accessChecker.hasAccess(UsersView.class,
				VaadinServletRequest.getCurrent().getHttpServletRequest())) {
			tabs.add(createTab(VaadinIcon.USER, TITLE_USERS, UsersView.class));
		}
		if (accessChecker.hasAccess(ProductsView.class,
				VaadinServletRequest.getCurrent().getHttpServletRequest())) {
			tabs.add(createTab(VaadinIcon.CALENDAR, TITLE_PRODUCTS, ProductsView.class));
		}

		final String contextPath = VaadinServlet.getCurrent().getServletContext().getContextPath();
		final Tab logoutTab = createTab(createLogoutLink(contextPath));
		logoutTab.setId("logout-tab");
		tabs.add(logoutTab);

		return tabs.toArray(new Tab[tabs.size()]);
	}

	/**
	 * Creates a {@link Tab} with the given icon, title, and navigation target.
	 *
	 * @param icon the icon for the tab
	 * @param title the display title
	 * @param viewClass the target view class
	 * @return a configured {@link Tab} component
	 */
	private static Tab createTab(VaadinIcon icon, String title, Class<? extends Component> viewClass) {
		return createTab(populateLink(new RouterLink("", viewClass), icon, title));
	}

	/**
	 * Wraps a {@link Component} in a {@link Tab}.
	 *
	 * @param content the component to include inside the tab
	 * @return the configured {@link Tab}
	 */
	private static Tab createTab(Component content) {
		final Tab tab = new Tab();
		tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
		tab.add(content);
		return tab;
	}

	/**
	 * Creates a logout link for the main menu.
	 *
	 * @param contextPath the servlet context path
	 * @return an {@link Anchor} representing the logout link
	 */
	private static Anchor createLogoutLink(String contextPath) {
		return populateLink(new Anchor(), VaadinIcon.ARROW_RIGHT, TITLE_LOGOUT);
	}

	/**
	 * Populates a component with an icon and title.
	 *
	 * @param a the component to populate (e.g., {@link Anchor} or {@link RouterLink})
	 * @param icon the icon to display
	 * @param title the title text
	 * @param <T> the component type
	 * @return the populated component
	 */
	private static <T extends HasComponents> T populateLink(T a, VaadinIcon icon, String title) {
		a.add(icon.create());
		a.add(title);
		return a;
	}
}


