package com.company.qa.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;

/**
 * API test using Playwright Java's APIRequestContext.
 * This test is independent of the UI browser lifecycle.
 */
public class ApiTest {

    @Test(description = "GET post returns HTTP 200 and expected fields")
    public void getPostShouldReturnExpectedFields() {
        try (Playwright playwright = Playwright.create()) {
            APIRequestContext request =
                playwright.request().newContext();

            try {
                APIResponse response = request.get(
                    "https://jsonplaceholder.typicode.com/posts/1"
                );

                Assert.assertEquals(response.status(), 200);
                Assert.assertTrue(response.ok());

                String body = response.text();
                Assert.assertTrue(body.contains("\"userId\""));
                Assert.assertTrue(body.contains("\"title\""));
                Assert.assertTrue(body.contains("\"body\""));

            } finally {
                // Dispose request context to release HTTP resources.
                request.dispose();
            }
        }
    }
}
