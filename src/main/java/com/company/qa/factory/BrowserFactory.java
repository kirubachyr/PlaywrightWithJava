package com.company.qa.factory;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

import com.company.qa.config.Config;

/**
 * Creates the requested browser engine.
 * Keep browser selection in one place so tests do not duplicate launch logic.
 */
public final class BrowserFactory {

    private BrowserFactory() {
        // Utility class.
    }

    public static Browser launch(Playwright playwright, String browserName) {
        BrowserType.LaunchOptions options =
            new BrowserType.LaunchOptions().setHeadless(!Config.headed());

        switch (browserName.toLowerCase()) {
            case "chromium":
                return playwright.chromium().launch(options);
            case "firefox":
                return playwright.firefox().launch(options);
            case "webkit":
                return playwright.webkit().launch(options);
            default:
                throw new IllegalArgumentException(
                    "Unsupported browser: " + browserName
                );
        }
    }
}
