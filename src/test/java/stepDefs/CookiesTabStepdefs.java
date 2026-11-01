package stepDefs;

import com.microsoft.playwright.BrowserContext;
import config.TestContext;
import io.cucumber.java.pl.Kiedy;
import io.cucumber.java.pl.Wtedy;
import org.junit.jupiter.api.Assertions;
import pages.CookiesTab;
import verification.CookieConsent;
import verification.CookieVerification;

public class CookiesTabStepdefs {

    private final CookiesTab cookiesTab;
    private final BrowserContext browserContext;

    public CookiesTabStepdefs(TestContext context) {
        this.cookiesTab = new CookiesTab(context.getPage(), context.getContext());
        this.browserContext = context.getContext();
    }

    @Kiedy("zaakceptuje wszystkie cookies")
    public void acceptAllCookiesType() {
        cookiesTab.clickBottomButton("Zaakceptuj wszystkie");
    }

    @Kiedy("zaakceptuje niezbędne cookies")
    public void acceptTechnicalCookiesType() {
        cookiesTab.clickBottomButton("Dostosuj");
        cookiesTab.clickBottomButton("Zaakceptuj zaznaczone");;
    }

    @Kiedy("zaakceptuje {string}")
    public void acceptCookiesType(String cookiesType) {
        cookiesTab.clickBottomButton("Dostosuj");
        cookiesTab.switchCookiesType(cookiesType);
        cookiesTab.clickBottomButton("Zaakceptuj zaznaczone");
    }

    @Wtedy("zaakceptowano tylko niezbędne cookies")
    public void verifyConsentNecessaryCookiesType() {
        CookieVerification cookieVerification = new CookieVerification(browserContext);
        Assertions.assertTrue(cookieVerification.hasConsent(CookieConsent.NECESSARY));
    }

    @Wtedy("zaakceptowano analityczne cookies")
    public void verifyConsentAnalyticalCookiesType() {
        CookieVerification cookieVerification = new CookieVerification(browserContext);
        Assertions.assertTrue(cookieVerification.hasConsent(CookieConsent.ANALYTICS));
    }

    @Wtedy("zaakceptowano marketingowe cookies")
    public void verifyConsentMarketingCookiesType() {
        CookieVerification cookieVerification = new CookieVerification(browserContext);
        Assertions.assertTrue(cookieVerification.hasConsent(CookieConsent.MARKETING));
    }

    @Wtedy("zaakceptowano wszystkie cookies")
    public void verifyConsentAllCookiesType() {
        CookieVerification cookieVerification = new CookieVerification(browserContext);
        Assertions.assertTrue(cookieVerification.hasConsent(CookieConsent.ALL));
    }
}
