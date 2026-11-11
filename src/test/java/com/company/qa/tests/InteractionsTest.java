package com.company.qa.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.company.qa.base.BaseTest;

/**
 * Small interaction examples.
 *
 * The Sauce Demo page has a sort <select>. The checkbox/radio examples below
 * use a small inline HTML page so the exercise does not depend on a separate
 * third-party form website.
 */
public class InteractionsTest extends BaseTest {

    @Test(description = "Select an inventory sort option")
    public void shouldSelectPriceLowToHigh() {
        page().navigate("https://www.saucedemo.com/");
        page().locator("#user-name").fill("standard_user");
        page().locator("#password").fill("secret_sauce");
        page().locator("#login-button").click();

        page().locator("[data-test='product-sort-container']")
            .selectOption("lohi");

        Assert.assertEquals(
            page().locator("[data-test='product-sort-container']")
                .inputValue(),
            "lohi"
        );
    }

    @Test(description = "Demonstrate checkbox, radio and keyboard actions")
    public void shouldUseFormControlsAndKeyboard() {
        // setContent is useful for isolated component-level interaction demos.
        page().setContent(
            "<html><body>" +
            "<label><input type='checkbox' id='terms'>Accept terms</label>" +
            "<label><input type='radio' name='gender' value='male'>Male</label>" +
            "<input id='search' placeholder='Search'>" +
            "</body></html>"
        );

        page().locator("#terms").check();
        page().locator("input[value='male']").check();

        page().locator("#search").fill("invoice");
        page().locator("#search").press("Control+A");

        Assert.assertTrue(page().locator("#terms").isChecked());
        Assert.assertTrue(page().locator("input[value='male']").isChecked());

        // Mouse API demonstration: click at the center of the search field.
        page().locator("#search").click();
    }
}
