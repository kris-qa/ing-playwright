package io.github.krisqa.ing.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class MainPage extends BasePage {


    public MainPage(Page page) {
        super(page);
    }

    public void navigateTo(String url) {
        page.navigate(url);
    }

    public void clickMenuTab(String tabText) {
        finder.header().byRole(AriaRole.LINK, tabText, true).click();
    }
}
