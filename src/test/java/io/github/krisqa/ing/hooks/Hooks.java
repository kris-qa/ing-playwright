package io.github.krisqa.ing.hooks;

import com.microsoft.playwright.Page;
import io.github.krisqa.ing.config.BrowserManager;
import io.github.krisqa.ing.config.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import io.qameta.allure.AttachmentOptions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Hooks {

    private static final Path TRACES_DIR = Paths.get("target/traces");

    private final TestContext context;

    public Hooks(TestContext context) {
        this.context = context;
    }

    @Before
    public void setUp() {
        context.init();
    }

    @After
    public void tearDown(Scenario scenario) {
        try {
            if (scenario.isFailed() && context.hasPage()) {
                attachFailureArtifacts(scenario);
            }
        } finally {
            context.close();
        }
    }

    @AfterAll
    public static void closeBrowsers() {
        BrowserManager.closeAll();
    }

    private void attachFailureArtifacts(Scenario scenario) {
        Page page = context.getPage();
        String name = scenario.getName();

        Allure.attachment(
            "Screenshot - " + name,
            "image/png",
            new ByteArrayInputStream(page.screenshot()),
            AttachmentOptions.withFileExtension(".png")
        );

        Allure.attachment(
            "HTML - " + name,
            "text/html",
            new ByteArrayInputStream(page.content().getBytes(StandardCharsets.UTF_8)),
            AttachmentOptions.withFileExtension(".html")
        );

        String fileName = name.replaceAll("[^\\p{L}\\p{N}._-]", "_") + "-" + System.currentTimeMillis() + ".zip";
        Path trace = context.saveTrace(TRACES_DIR.resolve(fileName));

        if (trace != null) {
            try (InputStream input = Files.newInputStream(trace)) {
                Allure.attachment(
                    "Playwright trace - " + name,
                    "application/zip",
                    input,
                    AttachmentOptions.withFileExtension(".zip")
                );
            } catch (IOException e) {
                throw new UncheckedIOException("Nie udało się dołączyć trace: " + trace, e);
            }
        }
    }

}
