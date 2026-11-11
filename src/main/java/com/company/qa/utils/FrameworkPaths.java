package com.company.qa.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

/**
 * Creates output folders used for screenshots and traces.
 */
public final class FrameworkPaths {

    private FrameworkPaths() {
        // Utility class.
    }

    public static Path screenshot(String fileName) {
        return create("target/artifacts/screenshots").resolve(fileName);
    }

    public static Path trace(String fileName) {
        return create("target/artifacts/traces").resolve(fileName);
    }

    private static Path create(String folder) {
        Path path = Paths.get(folder);
        try {
            Files.createDirectories(path);
        } catch (IOException exception) {
            throw new RuntimeException(
                "Could not create artifact directory: " + path,
                exception
            );
        }
        return path;
    }
}
