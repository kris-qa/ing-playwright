package io.github.krisqa.ing.config;

import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class EnvConfig {
    private static final Properties properties = new Properties();

    private static final String ENV =
        System.getProperty("env", "dev");

    static {
        String fileName = "environments/env." + ENV + ".properties";

        try (InputStream input =
                 EnvConfig.class.getClassLoader()
                     .getResourceAsStream(fileName)) {

            if (input == null) {
                throw new IllegalStateException(
                    "Environment config not found: " + fileName
                );
            }

            properties.load(
                new InputStreamReader(input, StandardCharsets.UTF_8)
            );

        } catch (IOException e) {
            throw new IllegalStateException(
                "Cannot load environment config: " + fileName, e
            );
        }
    }

    public static String get(String key) {
        String value = properties.getProperty(key);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                "Missing environment property: " + key
            );
        }

        return value;
    }

    public static String getEnvironment() {
        return ENV;
    }
}
