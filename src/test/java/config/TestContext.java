package config;

import com.microsoft.playwright.*;

public class TestContext {

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    public void init() {
        playwright = Playwright.create();
        browser = BrowserConfig.createBrowser(playwright);
        context = BrowserConfig.createContext(browser);
        page = context.newPage();
        System.out.println("CHOSEN ENV: " +System.getProperty("env"));
        System.out.println("CHOSEN BROWSER: " +System.getProperty("browser"));

        // Błędy JavaScript
        page.onPageError(error ->
            System.out.println("[PAGE ERROR] " + error)
        );

        // Błędy konsoli
        page.onConsoleMessage(message ->
            System.out.println(
                "[CONSOLE " + message.type() + "] " + message.text()
            )
        );

        // Nieudane żądania HTTP
        page.onRequestFailed(request ->
            System.out.println(
                "[REQUEST FAILED] " + request.url()
                    + " | " + request.failure()
            )
        );

        // Odpowiedzi HTTP
        page.onResponse(response -> {
            if (response.status() >= 400) {
                System.out.println(
                    "[HTTP ERROR] " + response.status()
                        + " | " + response.url()
                );
            }
        });
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

    public void close() {
        if (page != null) {
            page.close();
            page = null;
        }

        if (context != null) {
            context.close();
            context = null;
        }

        if (browser != null) {
            browser.close();
            browser = null;
        }

        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
    }
}
