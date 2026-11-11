package com.company.qa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * Page Object for the Sauce Demo login screen.
 * Locators are kept private; tests use business-level methods.
 */
public class LoginPage {

    private final Page page;
    private final Locator username;
    private final Locator password;
    private final Locator loginButton;
    private final Locator errorMessage;

    public LoginPage(Page page) {
        this.page = page;
        this.username = page.locator("#user-name");
        this.password = page.locator("#password");
        this.loginButton = page.locator("#login-button");
        this.errorMessage = page.locator("[data-test='error']");
    }

    public void open(String baseUrl) {
        page.navigate(baseUrl);
    }

    public void login(String user, String pass) {
        username.fill(user);
        password.fill(pass);
        loginButton.click();
    }

    public boolean isErrorVisible() {
        return errorMessage.isVisible();
    }

    public String errorText() {
        return errorMessage.textContent();
    }
}
