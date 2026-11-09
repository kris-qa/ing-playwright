package io.github.krisqa.ing.config;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class TestContext {

    private static final Logger log = LoggerFactory.getLogger(TestContext.class);

    private BrowserContext context;
    private Page page;
    private boolean tracing;

    public void init() {

        context = BrowserConfig.createContext(BrowserManager.getBrowser());
        context.setDefaultTimeout(Double.parseDouble(EnvConfig.get("timeout.action.ms")));
        PlaywrightAssertions.setDefaultAssertionTimeout(Double.parseDouble(EnvConfig.get("timeout.assertion.ms")));

        if (Boolean.parseBoolean(EnvConfig.get("trace.on.failure"))) {
            context.tracing().start(
                new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
            );
            tracing = true;
        }

        page = context.newPage();

        page.onFrameNavigated(frame -> {
            if (frame == page.mainFrame()) {
                log.info("Nawigacja do: {}", frame.url());
            }
        });

        page.onPageError(error ->
            log.warn("[PAGE ERROR] {}", error)
        );

        page.onConsoleMessage(message ->
            log.debug("[CONSOLE {}] {}", message.type(), message.text())
        );

        page.onResponse(response -> {
            if (response.status() >= 400) {
                log.warn("[HTTP ERROR] {} | {}", response.status(), response.url());
            }
        });
    }

    public boolean hasPage() {
        return page != null;
    }

    public Page getPage() {
        if (page == null) {
            throw new IllegalStateException(
                "Page jest null. Sprawdź inicjalizację TestContext."
            );
        }
        return page;
    }

    public BrowserContext getContext() {
        if (context == null) {
            throw new IllegalStateException(
                "BrowserContext nie został zainicjalizowany."
            );
        }
        return context;
    }

    public Path saveTrace(Path path) {
        if (!tracing) {
            return null;
        }

        context.tracing().stop(new Tracing.StopOptions().setPath(path));
        tracing = false;
        return path;
    }

    public void close() {
        if (tracing && context != null) {
            context.tracing().stop();
            tracing = false;
        }

        if (page != null) {
            page.close();
            page = null;
        }

        if (context != null) {
            context.close();
            context = null;
        }
    }
}
