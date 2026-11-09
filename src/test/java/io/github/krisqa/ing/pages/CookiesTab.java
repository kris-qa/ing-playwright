package io.github.krisqa.ing.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.github.krisqa.ing.finder.ElementFinder;
import io.github.krisqa.ing.verification.CookieConsent;
import io.github.krisqa.ing.verification.CookieVerification;

public class CookiesTab extends BasePage {

    private static final String COOKIE_BANNER = ".cookie-policy-content";

    public CookiesTab(Page page){
        super(page);
    }

    private ElementFinder banner() {
        return finder.within(COOKIE_BANNER);
    }

    public void clickBottomButton(String buttonText) {
        banner().byRole(AriaRole.BUTTON, buttonText, true).click();
    }

    public void switchCookiesType(String cookiesType) {
        banner().byRole(AriaRole.SWITCH, cookiesType, true).check();
    }

    // Wybór: "wszystkie", "tylko niezbędne" albo nazwa typu cookies z banera (np. "Cookies analityczne")
    public void acceptCookies(String choice) {
        switch (choice) {
            case "wszystkie" -> clickBottomButton("Zaakceptuj wszystkie");
            case "tylko niezbędne" -> {
                clickBottomButton("Dostosuj");
                clickBottomButton("Zaakceptuj zaznaczone");
            }
            default -> {
                clickBottomButton("Dostosuj");
                switchCookiesType(choice);
                clickBottomButton("Zaakceptuj zaznaczone");
            }
        }
    }

    public void shouldHaveConsent(CookieConsent expected) {
        new CookieVerification(page.context()).shouldHaveConsent(expected);
    }
}
