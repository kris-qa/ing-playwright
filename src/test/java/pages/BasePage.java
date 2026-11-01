package pages;

import actions.BaseActions;
import finder.ElementFinder;
import com.microsoft.playwright.Page;

public class BasePage {
    protected Page page;
    protected final BaseActions actions;
    protected final ElementFinder finder;

    public BasePage(Page page) {
        this.page = page;
        this.actions = new BaseActions();
        this.finder = new ElementFinder(page, actions);
    }
}
