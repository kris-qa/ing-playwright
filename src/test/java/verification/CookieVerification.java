package verification;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.options.Cookie;

import java.util.List;
import java.util.Optional;

public class CookieVerification {
    private static final String CONSENT_COOKIE = "cookiePolicyGDPR";

    private final BrowserContext context;

    public CookieVerification(BrowserContext context) {
        this.context = context;
    }

    public List<Cookie> getCookies() {
        return context.cookies("https://www.ing.pl");
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

}
