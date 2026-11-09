package io.github.krisqa.ing.finder;

import io.github.krisqa.ing.actions.BaseActions;
import io.github.krisqa.ing.actions.UiElement;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElementFinder {

    private static final Logger log = LoggerFactory.getLogger(ElementFinder.class);

    private final Page page;
    private final Locator root;
    private final BaseActions actions;
    // Czytelny opis obszaru wyszukiwania do logów; null = cała strona
    private final String scope;

    public ElementFinder(Page page, BaseActions actions) {
        this.page = page;
        this.root = null;
        this.actions = actions;
        this.scope = null;
    }

    public ElementFinder(Locator root, BaseActions actions, String scope) {
        this.page = root.page();
        this.root = root;
        this.actions = actions;
        this.scope = scope;
    }

    private Locator getContext() {
        return root != null ? root : page.locator("html");
    }

    private String describe(String what) {
        return scope == null ? what : what + " w " + scope;
    }

    private UiElement createElement(Locator locator, String what) {
        String description = describe(what);
        log.info("Wyszukanie elementu: {}", description);
        return new UiElement(locator, actions, description);
    }

    private ElementFinder createScope(Locator locator, String what) {
        return new ElementFinder(locator, actions, describe(what));
    }

    private static String format(String selector, Object... args) {
        return args.length == 0 ? selector : String.format(selector, args);
    }

    public UiElement byCss(String selector, Object... args) {
        String css = format(selector, args);
        return createElement(getContext().locator(css), "CSS '" + css + "'");
    }

    public UiElement byXpath(String xpath, Object... args) {
        String path = format(xpath, args);
        return createElement(getContext().locator("xpath=" + path), "XPath '" + path + "'");
    }

    public UiElement byId(String id) {
        return byCss("[id='%s']", id);
    }

    public UiElement byText(String text) {
        return createElement(getContext().getByText(text), "tekst '" + text + "'");
    }

    public UiElement byLabel(String label) {
        return createElement(getContext().getByLabel(label), "pole z etykietą '" + label + "'");
    }

    public UiElement byPlaceholder(String placeholder) {
        return createElement(getContext().getByPlaceholder(placeholder), "pole z podpowiedzią '" + placeholder + "'");
    }

    public UiElement byRole(AriaRole role) {
        return createElement(getContext().getByRole(role), role.name());
    }

    public UiElement byRole(AriaRole role, String name) {
        return createElement(
            getContext().getByRole(
                role,
                new Locator.GetByRoleOptions().setName(name)
            ),
            role.name() + " '" + name + "'"
        );
    }

    public UiElement byRole(AriaRole role, String name, boolean exact) {
        return createElement(
            getContext().getByRole(
                role,
                new Locator.GetByRoleOptions()
                    .setName(name)
                    .setExact(exact)
            ),
            role.name() + " '" + name + "'"
        );
    }

    public UiElement byTestId(String testId) {
        return createElement(getContext().getByTestId(testId), "data-testid '" + testId + "'");
    }

    public UiElement byTitle(String title) {
        return createElement(getContext().getByTitle(title), "element z tytułem '" + title + "'");
    }

    public ElementFinder within(String cssSelector) {
        return createScope(getContext().locator(cssSelector), "'" + cssSelector + "'");
    }

    public ElementFinder header() {
        return createScope(getContext().locator("header"), "nagłówku strony");
    }

    public ElementFinder body() {
        return new ElementFinder(page.locator("body"), actions, "body");
    }

    public ElementFinder navigation() {
        return createScope(getContext().getByRole(AriaRole.NAVIGATION), "nawigacji");
    }
}
