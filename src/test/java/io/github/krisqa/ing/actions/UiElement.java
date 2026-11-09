package io.github.krisqa.ing.actions;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;
import io.github.krisqa.ing.finder.ElementFinder;

public class UiElement {
    private final Locator locator;
    private final BaseActions actions;
    private final String description;

    public UiElement(Locator locator, BaseActions actions, String description) {
        this.locator = locator;
        this.actions = actions;
        this.description = description;
    }

    // Actions

    public void click() {
        actions.click(this);
    }

    public void doubleClick() {
        actions.doubleClick(this);
    }

    public void fill(String value) {
        actions.fill(this, value);
    }

    public void type(String value) {
        actions.type(this, value);
    }

    public void clear() {
        actions.clear(this);
    }

    public void press(String key) {
        actions.press(this, key);
    }

    public void check() {
        actions.check(this);
    }

    public void uncheck() {
        actions.uncheck(this);
    }

    public void hover() {
        actions.hover(this);
    }

    public void focus() {
        actions.focus(this);
    }

    public void selectByLabel(String label) {
        actions.selectByLabel(this, label);
    }

    public void selectByValue(String value) {
        actions.selectByValue(this, value);
    }

    // Getters

    public String getText() {
        return actions.getText(this);
    }

    public String getValue() {
        return actions.getValue(this);
    }

    public boolean isVisible() {
        return actions.isVisible(this);
    }

    public boolean isEnabled() {
        return actions.isEnabled(this);
    }

    public boolean isChecked() {
        return actions.isChecked(this);
    }

    public void waitFor() {
        actions.waitFor(this);
    }

    // Nested element searching

    public UiElement findByCss(String selector, Object... args) {
        return new ElementFinder(locator, actions, description).byCss(selector, args);
    }

    public UiElement findByXpath(String xpath, Object... args) {
        return new ElementFinder(locator, actions, description).byXpath(xpath, args);
    }

    public UiElement findByRole(AriaRole role, String name) {
        return new ElementFinder(locator, actions, description).byRole(role, name);
    }

    public Locator getLocator() {
        return locator;
    }

    public String getDescription() {
        return description;
    }
}
