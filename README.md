# Playwright Java Real-Time Automation Framework

A commented starter framework using **Playwright Java + Maven + TestNG**.
It demonstrates UI automation, Page Object Model, browser contexts, cross-browser
execution, parallel TestNG execution, screenshots, traces, API testing and Azure
DevOps CI.

> This project uses the official Playwright Java API. It does not use the
> Node.js `@playwright/test` runner, TypeScript, or `playwright.config.ts`.

## 1. Prerequisites

- JDK 17 or later
- Maven 3.9 or later
- Eclipse, IntelliJ IDEA or VS Code with Java support
- Internet access for Maven dependencies, browser binaries and the demo site

The `pom.xml` pins Playwright Java to `1.64.0` and TestNG to `7.11.0`.
If that Playwright version is not available in your Maven repository, select a
published Playwright Java version and keep the dependency and browser binaries
on the same version.

## 2. Import into Eclipse

1. Extract the ZIP.
2. Open Eclipse and choose **File → Import → Existing Maven Projects**.
3. Select the extracted `PlaywrightJavaRealTimeFramework` folder.
4. Finish the import.
5. Right-click the project → **Maven → Update Project**.
6. Confirm the project JRE is JDK 17 or later.

For IntelliJ, choose **Open** and select the extracted folder or its `pom.xml`.

## 3. Install Playwright browsers

From the project root, install the browser binaries that match the Maven
Playwright Java dependency:

```bash
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
```

On supported Linux CI agents, install required operating-system dependencies too:

```bash
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install --with-deps"
```

To install only one engine:

```bash
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install chromium"
```

## 4. Run tests

Run the TestNG suite (Chromium, Firefox and WebKit suite entries run as parallel
TestNG `<test>` groups):

```bash
mvn clean test
```

Run only the default browser test from the IDE by running an individual TestNG
class. The BaseTest default browser is Chromium.

Run headed or headless:
- Default: headless (suitable for CI)
- Local headed example: `mvn test -Dheaded=true`
- Override base URL: `mvn test -DbaseUrl=https://www.saucedemo.com/`

The demo tests use Sauce Demo credentials:
- Username: `standard_user`
- Password: `secret_sauce`

Use only test accounts and non-production systems.

## 5. Cross-browser execution

`testng.xml` passes a browser parameter to the test class for each browser:
- Chromium
- Firefox
- WebKit

This is the Java approach: TestNG parameters / DataProviders and a Java
BrowserFactory. Java does not use the Node.js Playwright Test
`playwright.config.ts` projects feature.

Playwright WebKit is a WebKit build for automation. It is not identical to
testing Safari on physical Apple devices or every macOS/iOS Safari release.

## 6. Framework structure

```text
src
├── main/java/com/company/qa
│   ├── config/Config.java
│   ├── factory/BrowserFactory.java
│   ├── pages/LoginPage.java
│   ├── pages/InventoryPage.java
│   └── utils/FrameworkPaths.java
└── test/java/com/company/qa
    ├── base/BaseTest.java
    └── tests
        ├── LoginTest.java
        ├── InteractionsTest.java
        └── ApiTest.java
```

- `BrowserFactory`: chooses Chromium, Firefox or WebKit.
- `BaseTest`: creates a fresh Playwright/browser/context/page per test thread.
- `LoginPage` and `InventoryPage`: Page Object Model classes.
- `LoginTest`: UI login validation and Playwright assertions.
- `InteractionsTest`: select, checkbox, radio, keyboard and mouse examples.
- `ApiTest`: APIRequestContext GET request and TestNG response assertions.
- Failed tests: save screenshot and trace ZIP under `target/artifacts`.

## 7. Test artifacts and reports

After execution, check:

```text
target/
├── artifacts/
│   ├── screenshots/
│   └── traces/
└── surefire-reports/
```

Open a trace:

```bash
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="show-trace target/artifacts/traces/<trace-file>.zip"
```

TestNG/Surefire XML files are published by the Azure pipeline. Screenshots
and traces are published as a pipeline artifact.

## 8. Azure DevOps

The repository includes `azure-pipelines.yml`. Push the project to Azure Repos,
create a pipeline from the existing YAML file, and run it. The pipeline:
1. Selects Java 17.
2. Installs Playwright browser binaries and Linux dependencies.
3. Runs `mvn clean test`.
4. Publishes Surefire XML results.
5. Publishes `target/artifacts`.

## 9. Important engineering notes

- Playwright Java objects are not thread-safe. This framework creates a
  separate Playwright, Browser, BrowserContext and Page for each test thread.
- BrowserContext isolates cookies and storage. A Page represents a tab.
- Playwright actions auto-wait for actionability. Avoid fixed `Thread.sleep`
  calls except when deliberately demonstrating timing.
- Locator assertions from `PlaywrightAssertions` retry until timeout.
- `APIRequestContext` is useful for API checks and test-data setup. Rest Assured
  can also remain in the same project if your team already uses it.
- The Sauce Demo site is a public demo target, not a substitute for your
  organisation's approved test environment.
- The framework is a learning starter. Add secrets management, environment
  configuration, richer logging, test-data management and review before
  adopting it in a production pipeline.
