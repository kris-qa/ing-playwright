package pages;

import actions.UiElement;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.nio.file.Paths;

public class MainPage extends BasePage {


    public MainPage(Page page) {
        super(page);
    }

    public void navigateTo(String url) {
        page.navigate(url);
        System.out.println("URL: " + page.url());
        System.out.println("TITLE: " + page.title());

        System.out.println("BODY:");
        System.out.println(
            page.locator("body").innerText(
                new Locator.InnerTextOptions().setTimeout(5000)
            )
        );
        page.screenshot(
            new Page.ScreenshotOptions()
                .setPath(Paths.get("target/screenshots/ing-response.png"))
                .setFullPage(true)
        );
    }

    public void clickMenuTab(String tabText) {
        UiElement mainMenuTab = finder.byXpath("//span[@class='cms-menu__label' and contains(text(), '%s')]", tabText);
        mainMenuTab.click();
    }

    public String getTitleText() {
        UiElement title = finder.byXpath("//title");
        return title.getText();
    }
}
