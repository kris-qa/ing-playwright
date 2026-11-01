package finder;

import actions.BaseActions;
import actions.UiElement;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class ElementFinder {

    private final Page page;
    private final Locator root;
    private final BaseActions actions;

    // Wyszukiwanie na całej stronie
    public ElementFinder(Page page, BaseActions actions) {
        this.page = page;
        this.root = null;
        this.actions = actions;
    }

    // Wyszukiwanie wewnątrz konkretnego kontenera
    public ElementFinder(Locator root, BaseActions actions) {
        this.page = null;
        this.root = root;
        this.actions = actions;
    }

    private Locator getContext() {
        return root != null ? root : page.locator("html");
    }

    private UiElement createElement(Locator locator) {
        return new UiElement(locator, actions);
    }

    public UiElement byCss(String selector, Object... args) {
        return createElement(
            getContext().locator(String.format(selector, args))
        );
    }

    public UiElement byXpath(String xpath, Object... args) {
        return createElement(
            getContext().locator("xpath=" + String.format(xpath, args))
        );
    }

    public UiElement byId(String id) {
        return byCss("[id='%s']", id);
    }

    public UiElement byText(String text) {
        return createElement(getContext().getByText(text));
    }

    public UiElement byLabel(String label) {
        return createElement(getContext().getByLabel(label));
    }

    public UiElement byPlaceholder(String placeholder) {
        return createElement(getContext().getByPlaceholder(placeholder));
    }

    public UiElement byRole(AriaRole role) {
        return createElement(getContext().getByRole(role));
    }

    public UiElement byRole(AriaRole role, String name) {
        return createElement(
            getContext().getByRole(
                role,
                new Locator.GetByRoleOptions().setName(name)
            )
        );
    }

    public UiElement byRole(AriaRole role, String name, boolean exact) {
        return createElement(
            getContext().getByRole(
                role,
                new Locator.GetByRoleOptions()
                    .setName(name)
                    .setExact(exact)
            )
        );
    }

    public UiElement byTestId(String testId) {
        return createElement(getContext().getByTestId(testId));
    }

    public UiElement byTitle(String title) {
        return createElement(getContext().getByTitle(title));
    }

    // Dostęp do sekcji strony
    public ElementFinder header() {
        return new ElementFinder(page.locator("header"), actions);
    }

    public ElementFinder body() {
        return new ElementFinder(page.locator("body"), actions);
    }

    public ElementFinder navigation() {
        return new ElementFinder(
            page.getByRole(AriaRole.NAVIGATION), actions
        );
    }
}