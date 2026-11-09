package io.github.krisqa.ing.actions;

import com.microsoft.playwright.options.SelectOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

// Playwright sam czeka na gotowość elementu (actionability checks) przed każdą akcją,
// więc czas w logu obejmuje również oczekiwanie na element
public class BaseActions {

    private static final Logger log = LoggerFactory.getLogger(BaseActions.class);

    public void click(UiElement element) {
        perform("Kliknięcie", element, () -> element.getLocator().click());
    }

    public void doubleClick(UiElement element) {
        perform("Podwójne kliknięcie", element, () -> element.getLocator().dblclick());
    }

    public void fill(UiElement element, String value) {
        perform("Wypełnienie wartością '" + value + "'", element, () -> element.getLocator().fill(value));
    }

    public void clear(UiElement element) {
        perform("Wyczyszczenie", element, () -> element.getLocator().clear());
    }

    public void type(UiElement element, String value) {
        perform("Wpisanie '" + value + "'", element, () -> element.getLocator().pressSequentially(value));
    }

    public void press(UiElement element, String key) {
        perform("Naciśnięcie klawisza " + key, element, () -> element.getLocator().press(key));
    }

    public void check(UiElement element) {
        perform("Zaznaczenie", element, () -> element.getLocator().check());
    }

    public void uncheck(UiElement element) {
        perform("Odznaczenie", element, () -> element.getLocator().uncheck());
    }

    public void selectByLabel(UiElement element, String label) {
        perform("Wybór opcji '" + label + "'", element,
            () -> element.getLocator().selectOption(new SelectOption().setLabel(label)));
    }

    public void selectByValue(UiElement element, String value) {
        perform("Wybór wartości '" + value + "'", element, () -> element.getLocator().selectOption(value));
    }

    public void hover(UiElement element) {
        perform("Najechanie kursorem", element, () -> element.getLocator().hover());
    }

    public void focus(UiElement element) {
        perform("Ustawienie fokusu", element, () -> element.getLocator().focus());
    }

    public String getText(UiElement element) {
        return read("Odczyt tekstu", element, () -> element.getLocator().innerText());
    }

    public String getValue(UiElement element) {
        return read("Odczyt wartości", element, () -> element.getLocator().inputValue());
    }

    public boolean isVisible(UiElement element) {
        return read("Sprawdzenie widoczności", element, () -> element.getLocator().isVisible());
    }

    public boolean isEnabled(UiElement element) {
        return read("Sprawdzenie dostępności", element, () -> element.getLocator().isEnabled());
    }

    public boolean isChecked(UiElement element) {
        return read("Sprawdzenie zaznaczenia", element, () -> element.getLocator().isChecked());
    }

    public void waitFor(UiElement element) {
        perform("Oczekiwanie na element", element, () -> element.getLocator().waitFor());
    }

    private void perform(String action, UiElement element, Runnable operation) {
        read(action, element, () -> {
            operation.run();
            return null;
        });
    }

    private <T> T read(String action, UiElement element, Supplier<T> operation) {
        log.info("{}: {}", action, element.getDescription());
        long start = System.nanoTime();

        try {
            T result = operation.get();
            log.info("{} - wykonano w {} ms{}", action, elapsedMs(start), result == null ? "" : ", wynik: " + result);
            return result;
        } catch (RuntimeException e) {
            log.error("{} - niepowodzenie po {} ms: {}", action, elapsedMs(start), element.getDescription());
            throw e;
        }
    }

    private static long elapsedMs(long start) {
        return (System.nanoTime() - start) / 1_000_000;
    }
}
