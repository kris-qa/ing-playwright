package io.github.krisqa.ing.stepdefs;

import io.cucumber.java.pl.*;
import io.github.krisqa.ing.config.EnvConfig;
import io.github.krisqa.ing.pages.Pages;

public class MainPageStepdefs {

    private final Pages pages;

    public MainPageStepdefs(Pages pages) {
        this.pages = pages;
    }

    @Kiedy("użytkownik uruchomi stronę główną")
    public void userOpenMainPage() {
        pages.mainPage().navigateTo(EnvConfig.get("app.url"));
    }

    @Kiedy("użytkownik wybierze w menu głównym {string}")
    public void mainMenuNavigate(String menuTab) {
        pages.mainPage().clickMenuTab(menuTab);
    }

    @Wtedy("tytuł strony powinien mieć wartość {string}")
    public void verifyTitleOnPage(String expectedTitleText) {
        pages.mainPage().shouldHaveTitle(expectedTitleText);
    }

    @Wtedy("adres strony powinien mieć ścieżkę {string}")
    public void verifyUrlPath(String expectedPath) {
        pages.mainPage().shouldHaveUrlPath(expectedPath);
    }
}
