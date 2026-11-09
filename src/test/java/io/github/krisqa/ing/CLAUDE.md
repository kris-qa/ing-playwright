# Kod frameworka (io.github.krisqa.ing)

## Warstwy
`stepdefs` → `pages.Pages` (rejestr) → `pages.*` (Page Objecty, `BasePage`) → `finder.ElementFinder` → `actions.UiElement` → `actions.BaseActions` → Playwright `Locator`.
`hooks.Hooks` + `config.TestContext` (BrowserContext/Page na scenariusz) + `config.BrowserManager` (Playwright/Browser per wątek, `ThreadLocal`).
`logging.StepLogger` - plugin Cucumbera (rejestracja w `runners.IngRunner`). `verification` - weryfikacja cookie zgody.

## Jak dodać
- **Krok:** metoda w klasie `stepdefs`, konstruktor przyjmuje `Pages`; ciało = jedno wywołanie metody strony. Bez asercji, Playwright API i logowania w step defach.
- **Stronę:** klasa `extends BasePage` w `pages`, getter z leniwym tworzeniem w `Pages`.
- **Weryfikację:** metoda `shouldXxx` w Page Objecie; wspólne (`shouldHaveTitle`, `shouldHaveUrlPath`) w `BasePage`.
- **Element:** przez `finder` (`byRole(role, name, true)`, `within(css)`, `header()`), nie `page.locator(...)` bezpośrednio - wtedy logowanie działa automatycznie.

## Konwencje
- Lokatory: `getByRole` + `exact=true`, zawężanie kontenerem (`within(".cookie-policy-content")`, `header()`). Przełączniki: `check()`, nie `click()`.
- Logowanie: SLF4J `log.info/warn`, nigdy `System.out`. `BaseActions` loguje akcję + czas; `ElementFinder` loguje wyszukanie z opisem; `TestContext` loguje nawigację, `[PAGE ERROR]`, `[HTTP ERROR]`.
- Konfiguracja: `EnvConfig.get(key)` (rzuca wyjątek przy braku klucza) - nowy klucz dodać do wszystkich `env.*.properties` i `env.local.properties.example`.
- Kod musi być bezpieczny wątkowo (scenariusze równolegle, 3 wątki).
- Regex w asercjach Playwrighta wykonuje się w JS - `Pattern.quote` (`\Q..\E`) nie działa.

## Znane fakty o ing.pl (sprawdzone 2026-11-09)
- Baner cookies: 2 kontenery `.cookie-policy-content` (widok główny i "Dostosuj"); przyciski "Zaakceptuj wszystkie", "Dostosuj", "Zaakceptuj zaznaczone"; przełączniki `role=switch` o nazwach "Cookies analityczne"/"Cookies marketingowe".
- Cookie zgody `cookiePolicyGDPR`: 1=NECESSARY, 2=MARKETING, 3=ANALYTICS, 4=ALL (`CookieConsent`).
- Menu: linki w `header` (`nav aria-label="Segmenty"`): Premium → `/premium`, Private Banking → `/private-banking`, Przedsiębiorcy → `/male-firmy`.
- `https://ing.pl` przekierowuje na `https://www.ing.pl` - weryfikować ścieżkę URL, nie domenę.
