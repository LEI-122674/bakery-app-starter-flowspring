package com.vaadin.starter.bakery.ui;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.VIEWPORT;

/**
 * Configures the application shell for the Bakery App.
 * <p>
 * This class sets global application settings such as the viewport,
 * theme, and Progressive Web App (PWA) properties.
 * </p>
 * <ul>
 *     <li>{@link Viewport} sets the viewport meta tag for responsive design.</li>
 *     <li>{@link Theme} sets the global theme for the application.</li>
 *     <li>{@link PWA} configures Progressive Web App settings including name, start path, colors,
 *     and offline resources.</li>
 * </ul>
 */
@Viewport(VIEWPORT)
@Theme("bakery")
@PWA(
		name = "Bakery App Starter",
		shortName = "###Bakery###",
		startPath = "login",
		backgroundColor = "#227aef",
		themeColor = "#227aef",
		offlinePath = "offline-page.html",
		offlineResources = {"images/offline-login-banner.jpg"}
)
public class AppShell implements AppShellConfigurator {
	// No additional methods or fields are needed; this class serves as the global app shell configuration.
}
