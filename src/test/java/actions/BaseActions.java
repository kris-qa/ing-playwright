package actions;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.SelectOption;
import config.EnvConfig;

public class BaseActions {

    public void click(Locator locator) {
        sleep();
        locator.click();
    }

    public void doubleClick(Locator locator) {
        sleep();
        locator.dblclick();
    }

    public void fill(Locator locator, String value) {
        sleep();
        locator.fill(value);
    }

    public void clear(Locator locator) {
        sleep();
        locator.clear();
    }

    public void type(Locator locator, String value) {
        sleep();
        locator.pressSequentially(value);
    }

    public void press(Locator locator, String key) {
        sleep();
        locator.press(key);
    }

    public void check(Locator locator) {
        sleep();
        locator.check();
    }

    public void uncheck(Locator locator) {
        sleep();
        locator.uncheck();
    }

    public void selectByLabel(Locator locator, String label) {
        sleep();
        locator.selectOption(new SelectOption().setLabel(label));
    }

    public void selectByValue(Locator locator, String value) {
        sleep();
        locator.selectOption(value);
    }

    public void hover(Locator locator) {
        sleep();
        locator.hover();
    }

    public void focus(Locator locator) {
        sleep();
        locator.focus();
    }

    public String getText(Locator locator) {
        sleep();
        return locator.innerText();
    }

    public String getValue(Locator locator) {
        sleep();
        return locator.inputValue();
    }

    public boolean isVisible(Locator locator) {
        sleep();
        return locator.isVisible();
    }

    public boolean isEnabled(Locator locator) {
        sleep();
        return locator.isEnabled();
    }

    public boolean isChecked(Locator locator) {
        sleep();
        return locator.isChecked();
    }

    public void waitFor(Locator locator) {
        locator.waitFor();
    }

    public void sleep(){
        try { Thread.sleep(Integer.parseInt(EnvConfig.get("actions.base.delay"))); }
        catch (Exception e) { System.out.println("Sleep failed: " + e.getMessage()); }
    }
}
