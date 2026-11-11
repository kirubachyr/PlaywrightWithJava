package com.company.qa.base;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;

import com.company.qa.config.Config;
import com.company.qa.factory.BrowserFactory;
import com.company.qa.utils.FrameworkPaths;

/**
 * TestNG base class.
 *
 * A separate Playwright/browser/context/page is created per test thread.
 * This avoids sharing Playwright's non-thread-safe objects across workers.
 */
public class BaseTest {

    private static final ThreadLocal<Playwright> PLAYWRIGHT = new ThreadLocal<>();
    private static final ThreadLocal<Browser> BROWSER = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> CONTEXT = new ThreadLocal<>();
    private static final ThreadLocal<Page> PAGE = new ThreadLocal<>();
    private static final ThreadLocal<String> BROWSER_NAME = new ThreadLocal<>();

    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    public void setUp(
        @Optional("chromium") String browserName,
        Method method
    ) {
        BROWSER_NAME.set(browserName);

        Playwright playwright = Playwright.create();
        PLAYWRIGHT.set(playwright);

        Browser browser = BrowserFactory.launch(playwright, browserName);
        BROWSER.set(browser);

        BrowserContext context = browser.newContext();
        CONTEXT.set(context);

        Page page = context.newPage();
        page.setDefaultTimeout(Config.actionTimeoutMs());
        PAGE.set(page);

        // Start tracing before the test actions so the trace captures the flow.
        context.tracing().start(
            new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true)
        );

        System.out.printf(
            "%n[%s] Starting %s on %s%n",
            Thread.currentThread().getName(),
            method.getName(),
            browserName
        );
    }

    protected Page page() {
        return PAGE.get();
    }

    protected BrowserContext context() {
        return CONTEXT.get();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        String safeTestName = result.getMethod().getMethodName()
            .replaceAll("[^a-zA-Z0-9_-]", "_");
        String browserName = BROWSER_NAME.get() == null
            ? "unknown" : BROWSER_NAME.get();

        try {
            // Capture screenshot and trace only for failed tests.
            if (result.getStatus() == ITestResult.FAILURE && PAGE.get() != null) {
                PAGE.get().screenshot(
                    new Page.ScreenshotOptions()
                        .setPath(FrameworkPaths.screenshot(
                            safeTestName + "-" + browserName + ".png"
                        ))
                        .setFullPage(true)
                );
            }

            if (CONTEXT.get() != null) {
                if (result.getStatus() == ITestResult.FAILURE) {
                    CONTEXT.get().tracing().stop(
                        new Tracing.StopOptions().setPath(
                            FrameworkPaths.trace(
                                safeTestName + "-" + browserName + ".zip"
                            )
                        )
                    );
                } else {
                    // Stop tracing without retaining successful test traces.
                    CONTEXT.get().tracing().stop();
                }
            }
        } finally {
            // Close in reverse order. Each ThreadLocal is removed to prevent
            // stale objects from being reused by a TestNG worker thread.
            if (CONTEXT.get() != null) {
                CONTEXT.get().close();
                CONTEXT.remove();
            }

            if (BROWSER.get() != null) {
                BROWSER.get().close();
                BROWSER.remove();
            }

            if (PLAYWRIGHT.get() != null) {
                PLAYWRIGHT.get().close();
                PLAYWRIGHT.remove();
            }

            PAGE.remove();
            BROWSER_NAME.remove();
        }
    }
}
