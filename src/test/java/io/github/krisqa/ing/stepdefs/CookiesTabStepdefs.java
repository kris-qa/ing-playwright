package io.github.krisqa.ing.stepdefs;

import io.cucumber.java.pl.Kiedy;
import io.cucumber.java.pl.Wtedy;
import io.github.krisqa.ing.pages.Pages;
import io.github.krisqa.ing.verification.CookieConsent;

public class CookiesTabStepdefs {

    private final Pages pages;

    public CookiesTabStepdefs(Pages pages) {
        this.pages = pages;
    }

    @Kiedy("użytkownik zaakceptuje {string}")
    public void acceptCookies(String choice) {
        pages.cookiesTab().acceptCookies(choice);
    }

    // Zgoda: nazwa stałej z CookieConsent (NECESSARY, ANALYTICS, MARKETING, ALL)
    @Wtedy("zapisana zgoda na cookies to {string}")
    public void verifyConsent(String consentName) {
        pages.cookiesTab().shouldHaveConsent(CookieConsent.valueOf(consentName));
    }
}
