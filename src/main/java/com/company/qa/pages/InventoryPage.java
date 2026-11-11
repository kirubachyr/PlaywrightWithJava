package com.company.qa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * Page Object for the inventory/products screen.
 */
public class InventoryPage {

    private final Page page;
    private final Locator title;
    private final Locator inventoryItems;

    public InventoryPage(Page page) {
        this.page = page;
        this.title = page.locator(".title");
        this.inventoryItems = page.locator(".inventory_item");
    }

    public Locator title() {
        return title;
    }

    public int productCount() {
        return inventoryItems.count();
    }

    public String currentUrl() {
        return page.url();
    }
}
