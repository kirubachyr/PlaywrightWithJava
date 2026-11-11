package com.company.qa.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.microsoft.playwright.assertions.PlaywrightAssertions;

import com.company.qa.base.BaseTest;
import com.company.qa.config.Config;
import com.company.qa.pages.InventoryPage;
import com.company.qa.pages.LoginPage;

/**
 * UI tests for valid and invalid login scenarios.
 */
public class LoginTest extends BaseTest {

    @Test(description = "Valid user can sign in and see inventory")
    public void validLoginShouldOpenInventory() {
        LoginPage loginPage = new LoginPage(page());
        InventoryPage inventoryPage = new InventoryPage(page());

        loginPage.open(Config.baseUrl());
        loginPage.login("standard_user", "secret_sauce");

        // Playwright assertion retries until the locator is visible or times out.
        PlaywrightAssertions.assertThat(inventoryPage.title())
            .hasText("Products");

        Assert.assertTrue(
            inventoryPage.currentUrl().contains("inventory.html"),
            "Expected to navigate to the inventory page"
        );

        Assert.assertTrue(
            inventoryPage.productCount() > 0,
            "Expected at least one product to be displayed"
        );
    }

    @Test(description = "Invalid user sees a login error")
    public void invalidLoginShouldShowError() {
        LoginPage loginPage = new LoginPage(page());

        loginPage.open(Config.baseUrl());
        loginPage.login("invalid_user", "wrong_password");

        Assert.assertTrue(
            loginPage.isErrorVisible(),
            "Expected an error message for invalid credentials"
        );

        Assert.assertTrue(
            loginPage.errorText().contains("Username and password"),
            "Error message did not contain the expected text"
        );
    }
}
