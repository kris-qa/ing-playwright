package actions;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;
import finder.ElementFinder;

public class UiElement {
    private final Locator locator;
    private final BaseActions actions;

    public UiElement(Locator locator, BaseActions actions) {
        this.locator = locator;
        this.actions = actions;
    }

    // Actions

    public void click() {
        actions.click(locator);
    }

    public void doubleClick() {
        actions.doubleClick(locator);
    }

    public void fill(String value) {
        actions.fill(locator, value);
    }

    public void type(String value) {
        actions.type(locator, value);
    }

    public void clear() {
        actions.clear(locator);
    }

    public void press(String key) {
        actions.press(locator, key);
    }

    public void check() {
        actions.check(locator);
    }

    public void uncheck() {
        actions.uncheck(locator);
    }

    public void hover() {
        actions.hover(locator);
    }

    public void focus() {
        actions.focus(locator);
    }

    public void selectByLabel(String label) {
        actions.selectByLabel(locator, label);
    }

    public void selectByValue(String value) {
        actions.selectByValue(locator, value);
    }

    // Getters

    public String getText() {
        return actions.getText(locator);
    }

    public String getValue() {
        return actions.getValue(locator);
    }

    public boolean isVisible() {
        return actions.isVisible(locator);
    }

    public boolean isEnabled() {
        return actions.isEnabled(locator);
    }

    public boolean isChecked() {
        return actions.isChecked(locator);
    }

    public void waitFor() {
        actions.waitFor(locator);
    }

    // Nested element searching

    public UiElement findByCss(String selector, Object... args) {
        return new ElementFinder(locator, actions).byCss(selector, args);
    }

    public UiElement findByXpath(String xpath, Object... args) {
        return new ElementFinder(locator, actions).byXpath(xpath, args);
    }

    public UiElement findByRole(AriaRole role, String name) {
        return new ElementFinder(locator, actions).byRole(role, name);
    }

    public Locator getLocator() {
        return locator;
    }
}
