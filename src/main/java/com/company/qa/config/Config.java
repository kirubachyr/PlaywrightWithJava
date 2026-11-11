package com.company.qa.config;

/**
 * Central place for environment configuration.
 * Override values from Maven, for example:
 * mvn test -DbaseUrl=https://your-test-environment/
 */
public final class Config {

    private Config() {
        // Utility class: prevent instantiation.
    }

    public static String baseUrl() {
        return System.getProperty(
            "baseUrl",
            "https://www.saucedemo.com/"
        );
    }

    public static boolean headed() {
        return Boolean.parseBoolean(
            System.getProperty("headed", "false")
        );
    }

    public static int actionTimeoutMs() {
        return Integer.parseInt(
            System.getProperty("actionTimeoutMs", "10000")
        );
    }
}
