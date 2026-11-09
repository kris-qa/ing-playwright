# Zasoby testowe

## Pliki
- `features/*.feature` - Gherkin `# language: pl` (Funkcja, Założenia, Szablon scenariusza, Przykłady, Zakładając/Kiedy/Wtedy/I).
- `environments/env.<env>.properties` - wymagane klucze: `app.url`, `browser.headless`, `cookie.consent.timeout.ms`, `cookie.consent.poll.interval.ms`, `trace.on.failure`, `timeout.action.ms`, `timeout.assertion.ms`. `env.local.properties` nie jest w gicie (wzorzec: `.example`) - przy nowym kluczu dopisać też do niego.
- `browsers/<browser>.properties` - `browser.type`, `browser.slow.mo`, `browser.width/height`.
- `junit-platform.properties` - równoległość Cucumbera (3 wątki).
- `simplelogger.properties` - format logów (z nazwą wątku); `allure.properties` - katalog wyników.

## Konwencje Gherkin
- Kroki zaczynają się od "użytkownik" małą literą; `Wtedy` w formie "... powinien mieć ..." / "zapisana zgoda ...".
- Wspólne kroki startowe w `Założenia`; warianty jako `Szablon scenariusza` + `Przykłady`.
- Tagi: `@Cookies`/`@Navigation` na funkcji, `@GUI` na szablonie, tagi wariantów (`@AllCookies`, `@Necessary`, `@Analytics`, `@Marketing`) na osobnych blokach `Przykłady`.
- Nazwy kolumn bez polskich znaków (`wybor`, `tytul`, `sciezka`).
- Znak `|` w komórce tabeli zapisywać jako `\|`.

## Istniejące kroki
- `użytkownik uruchomi stronę główną`
- `użytkownik zaakceptuje {string}` - "wszystkie" | "tylko niezbędne" | nazwa przełącznika z banera
- `zapisana zgoda na cookies to {string}` - nazwa stałej `CookieConsent`
- `użytkownik wybierze w menu głównym {string}`
- `adres strony powinien mieć ścieżkę {string}`
- `tytuł strony powinien mieć wartość {string}` - pełny tytuł
