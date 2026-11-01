package pages;

import actions.UiElement;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.options.Cookie;


import java.util.List;

public class CookiesTab extends BasePage {

    private final BrowserContext browserContext;

    public CookiesTab(Page page, BrowserContext browserContext){
        super(page);
        this.browserContext = browserContext;
    }

    public void clickBottomButton(String buttonText) {
        UiElement bottomButton = finder.byXpath(
            "//div[@class='cookie-policy-content']//button[contains(text(),'%s')]",
            buttonText
        );
        bottomButton.click();
    }
    //Cookies analityczne
    public void switchCookiesType(String cookiesType) {
        UiElement switchCookies = finder.byXpath(
            "//div[@class='cookie-policy-type' and contains(., '%s')]//descendant::div[@class='cookie-policy-switch']//span[@class='cookie-policy-slider-thumb']",
            cookiesType
        );
        switchCookies.click();
    }

    public List<Cookie> getConsenCokiesType() {
        List<Cookie> cookies = browserContext.cookies();

        cookies.forEach(cookie -> {
            System.out.println("------------------------------");
            System.out.println("Name:     " + cookie.name);
            System.out.println("Value:    " + cookie.value);
            System.out.println("Domain:   " + cookie.domain);
            System.out.println("Path:     " + cookie.path);
            System.out.println("Expires:  " + cookie.expires);
            System.out.println("HttpOnly: " + cookie.httpOnly);
            System.out.println("Secure:   " + cookie.secure);
            System.out.println("SameSite: " + cookie.sameSite);
        });
        // 3. Znajdź ciasteczko odpowiedzialne za consent (nazwa zależy od platformy np. OneTrust, Cookiebot)
        Cookie consentCookie = cookies.stream()
            .filter(c -> c.name.equals("cookie_consent") || c.name.contains("consent"))
            .findFirst()
            .orElse(null);
        return cookies;
    }
}
