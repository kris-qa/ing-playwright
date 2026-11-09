package io.github.krisqa.ing.config;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Playwright;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class BrowserManager {

    private static final Logger log = LoggerFactory.getLogger(BrowserManager.class);

    private static final List<Playwright> ALL_PLAYWRIGHTS = new CopyOnWriteArrayList<>();
    private static final ThreadLocal<Playwright> PLAYWRIGHT = new ThreadLocal<>();
    private static final ThreadLocal<Browser> BROWSER = new ThreadLocal<>();

    private BrowserManager() {
    }

    public static Browser getBrowser() {
        Browser browser = BROWSER.get();

        if (browser == null || !browser.isConnected()) {
            browser = BrowserConfig.createBrowser(getPlaywright());
            BROWSER.set(browser);
            log.info("Uruchomiono przeglądarkę {} {} (środowisko: {})",
                browser.browserType().name(), browser.version(), EnvConfig.getEnvironment());
        }

        return browser;
    }

    private static Playwright getPlaywright() {
        Playwright playwright = PLAYWRIGHT.get();

        if (playwright == null) {
            playwright = Playwright.create();
            PLAYWRIGHT.set(playwright);
            ALL_PLAYWRIGHTS.add(playwright);
        }

        return playwright;
    }

    public static void closeAll() {
        ALL_PLAYWRIGHTS.forEach(Playwright::close);
        ALL_PLAYWRIGHTS.clear();
    }
}
