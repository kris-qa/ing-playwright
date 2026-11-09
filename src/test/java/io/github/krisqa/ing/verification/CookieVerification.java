package io.github.krisqa.ing.verification;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.options.Cookie;
import io.github.krisqa.ing.config.EnvConfig;
import org.junit.jupiter.api.Assertions;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public class CookieVerification {
    public static final String CONSENT_COOKIE = "cookiePolicyGDPR";

    private final BrowserContext context;
    private final Duration consentTimeout;
    private final long pollIntervalMs;

    public CookieVerification(BrowserContext context) {
        this.context = context;
        this.consentTimeout = Duration.ofMillis(Long.parseLong(EnvConfig.get("cookie.consent.timeout.ms")));
        this.pollIntervalMs = Long.parseLong(EnvConfig.get("cookie.consent.poll.interval.ms"));
    }

    // Wszystkie cookies kontekstu - niezależnie od adresu środowiska (app.url)
    public List<Cookie> getCookies() {
        return context.cookies();
    }

    public Optional<Cookie> getCookie(String name) {
        return getCookies().stream()
            .filter(cookie -> cookie.name.equals(name))
            .findFirst();
    }

    public String getCookieValue(String name) {
        return getCookie(name)
            .map(cookie -> cookie.value)
            .orElse(null);
    }

    public CookieConsent getConsent() {

        String value = getCookieValue(CONSENT_COOKIE);

        if (value == null) {
            return null;
        }

        for (CookieConsent consent : CookieConsent.values()) {
            if (consent.getValue().equals(value)) {
                return consent;
            }
        }

        return null;
    }

    public boolean hasConsent(CookieConsent expected) {
        return getConsent() == expected;
    }

    // Cookie zgody zapisuje JavaScript po kliknięciu, więc odpytujemy aż do oczekiwanej wartości lub timeoutu.
    // Zwraca ostatnio odczytaną zgodę, żeby asercja pokazała faktyczną wartość.
    // Timeout i interwał: cookie.consent.timeout.ms / cookie.consent.poll.interval.ms w pliku środowiska.
    public CookieConsent waitForConsent(CookieConsent expected) {
        long deadline = System.nanoTime() + consentTimeout.toNanos();
        CookieConsent consent = getConsent();

        while (consent != expected && System.nanoTime() < deadline) {
            try {
                Thread.sleep(pollIntervalMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            consent = getConsent();
        }

        return consent;
    }

    public void shouldHaveConsent(CookieConsent expected) {
        Assertions.assertEquals(
            expected,
            waitForConsent(expected),
            "Nieprawidłowa wartość cookie zgody " + CONSENT_COOKIE
        );
    }

}
