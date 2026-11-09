package io.github.krisqa.ing.pages;

import io.github.krisqa.ing.actions.BaseActions;
import io.github.krisqa.ing.config.EnvConfig;
import io.github.krisqa.ing.finder.ElementFinder;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import org.junit.jupiter.api.Assertions;

import java.net.URI;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BasePage {
    protected Page page;
    protected final BaseActions actions;
    protected final ElementFinder finder;

    public BasePage(Page page) {
        this.page = page;
        this.actions = new BaseActions();
        this.finder = new ElementFinder(page, actions);
    }

    // Weryfikacje wspólne dla wszystkich stron

    public void shouldHaveTitle(String expectedTitle) {
        assertThat(page).hasTitle(expectedTitle);
    }

    // Sprawdzana jest tylko ścieżka - domena zależy od środowiska (app.url) i przekierowań
    public void shouldHaveUrlPath(String expectedPath) {
        try {
            page.waitForURL(
                url -> expectedPath.equals(URI.create(url).getPath()),
                new Page.WaitForURLOptions().setTimeout(Double.parseDouble(EnvConfig.get("timeout.assertion.ms")))
            );
        } catch (TimeoutError e) {
            Assertions.fail("Oczekiwana ścieżka adresu: " + expectedPath + ", aktualny adres: " + page.url());
        }
    }
}
