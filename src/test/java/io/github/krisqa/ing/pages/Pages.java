package io.github.krisqa.ing.pages;

import io.github.krisqa.ing.config.TestContext;

public class Pages {

    private final TestContext context;
    private MainPage mainPage;
    private CookiesTab cookiesTab;

    public Pages(TestContext context) {
        this.context = context;
    }

    public MainPage mainPage() {
        if (mainPage == null) {
            mainPage = new MainPage(context.getPage());
        }
        return mainPage;
    }

    public CookiesTab cookiesTab() {
        if (cookiesTab == null) {
            cookiesTab = new CookiesTab(context.getPage());
        }
        return cookiesTab;
    }
}
