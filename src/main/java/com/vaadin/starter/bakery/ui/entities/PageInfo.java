package com.vaadin.starter.bakery.ui.entities;

/**
 * Represents the metadata of a page in the UI, including its link, icon, and title.
 */
public class PageInfo {

    private final String link;
    private final String icon;
    private final String title;

    /**
     * Creates a new {@code PageInfo} instance.
     *
     * @param link  the navigation link for the page
     * @param icon  the icon associated with the page
     * @param title the title of the page
     */
    public PageInfo(String link, String icon, String title) {
        this.link = link;
        this.icon = icon;
        this.title = title;
    }

    /**
     * Returns the navigation link of the page.
     *
     * @return the page link
     */
    public String getLink() {
        return link;
    }

    /**
     * Returns the icon associated with the page.
     *
     * @return the page icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Returns the title of the page.
     *
     * @return the page title
     */
    public String getTitle() {
        return title;
    }
}

