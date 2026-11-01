package pages;

import actions.UiElement;
import com.microsoft.playwright.Page;

public class MainPage extends BasePage {


    public MainPage(Page page) {
        super(page);
    }

    public void navigateTo(String url) {
        page.navigate(url);
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
