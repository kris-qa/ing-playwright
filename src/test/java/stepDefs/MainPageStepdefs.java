package stepDefs;

import config.EnvConfig;
import config.TestContext;
import io.cucumber.java.pl.*;
import org.junit.jupiter.api.Assertions;
import pages.MainPage;

public class MainPageStepdefs {

    private final MainPage mainPage;

    public MainPageStepdefs(TestContext context) {
        this.mainPage = new MainPage(context.getPage());
    }

    @Kiedy("Użytkownik uruchomi stronę główną")
    public void userOpenMainPage() {
        mainPage.navigateTo(EnvConfig.get("app.url"));
    }

    @Kiedy("w menu głównym wybierze {string}")
    public void mainMenuNavigate(String menuTab) {
        mainPage.clickMenuTab(menuTab);
    }

    @Wtedy("Title na stronie powinno mieć wartość {string}")
    public void verifyTitleOnPage(String expectedTitleText) {
        Assertions.assertEquals(expectedTitleText, mainPage.getTitleText());
    }
}
