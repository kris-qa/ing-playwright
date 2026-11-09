# Analiza kodu testów automatycznych – playwrightING

Data analizy: 2026-11-09 (zaktualizowana po realizacji etapów 1, 2, porządków, wymianie selektorów XPath na lokatory po rolach ARIA, dodaniu logowania operacji, weryfikacji adresu podstron, zmianach architektonicznych i zmianach konfiguracji CI)
Zakres: `src/test` (Java + `.feature`), konfiguracja (`pom.xml`, `*.properties`), Docker, GitHub Actions.

Dokument zawiera wyłącznie **otwarte** problemy i rekomendacje.

## Spis treści

1. [Podsumowanie](#1-podsumowanie)
2. [Architektura](#2-architektura)
3. [Otwarte problemy](#3-otwarte-problemy)
4. [Analiza scenariuszy testowych](#4-analiza-scenariuszy-testowych)
5. [Plan dalszych prac](#5-plan-dalszych-prac)

---

## 1. Podsumowanie

Framework ma przemyślaną strukturę warstwową (Step def → Page Object → ElementFinder → UiElement → BaseActions) i DI przez PicoContainer. Step defy dostają strony z rejestru `Pages` i tylko wywołują metody stron, a weryfikacje są w Page Objectach. Akcje korzystają z auto-wait Playwrighta, a asercje ponawiają sprawdzenie. Selektory opierają się na rolach ARIA i nazwach dostępnych. Jedna przeglądarka jest współdzielona przez wszystkie scenariusze, a każdy scenariusz dostaje izolowany kontekst. Log testu pokazuje każdy scenariusz, krok, wyszukanie elementu, akcję i nawigację; działa to generycznie, bez kodu w step defach. Scenariusze wykonują się równolegle. Przy błędzie raport Allure zawiera trace. CI pokazuje wyniki testów w PR i publikuje raporty Allure z historią na GitHub Pages.

Do zrobienia zostały: drobna uwaga (sekcja 3) i **rozszerzenie pokrycia testami** (sekcja 5).

| Obszar | Ocena | Komentarz |
|---|---|---|
| Architektura | 5/5 | Czytelne warstwy, rejestr stron wstrzykiwany przez DI, weryfikacje w Page Objectach, limity czasu z konfiguracji, pakiety `io.github.krisqa.ing.*` |
| Stabilność testów | 5/5 | Auto-wait, asercje z ponawianiem, lokatory po rolach ARIA |
| Selektory | 5/5 | `getByRole` z dokładną nazwą, zawężone do kontenera; brak XPath |
| Scenariusze BDD | 5/5 | Spójny słownik kroków, oba pliki jako szablony scenariuszy z sekcją `Założenia` |
| Konfiguracja / CI | 5/5 | Równoległe scenariusze, wyniki w PR, Allure z historią na Pages, Maven Wrapper, `concurrency` |
| Logowanie | 5/5 | Scenariusze, kroki, elementy, akcje z czasem i nawigacja – automatycznie dla nowych step defów |
| Higiena kodu | 5/5 | Bez martwego kodu i pozostałości szablonu projektu |

---

## 2. Architektura

### Stos technologiczny

| Komponent | Wersja |
|---|---|
| Java | 21 |
| Maven | 3.9.9 przez Maven Wrapper (`./mvnw`) |
| Playwright for Java | 1.49.0 (`playwright.version` w `pom.xml`; obraz Docker Compose z `PLAYWRIGHT_VERSION` w `.env`) |
| Cucumber | 7.20.1 (JUnit Platform engine, PicoContainer) |
| JUnit | 5.11.3 / Platform Suite 1.11.3 |
| Allure | 3.0.0 (BOM), allure-maven 3.1.0 |
| SLF4J | 2.0.17 (slf4j-simple) |

### Warstwy

Wszystkie pakiety leżą pod `src/test/java/io/github/krisqa/ing/` (prefiks zgodny z `groupId` `io.github.kris-qa`).

```
.feature (Gherkin PL)
   └─ stepdefs (jedna linia na krok)
        └─ pages.Pages (rejestr stron, wstrzykiwany przez PicoContainer)
             └─ pages (BasePage: shouldHaveTitle, shouldHaveUrlPath; MainPage, CookiesTab)
                  ├─ verification (CookieVerification, CookieConsent)
                  └─ finder.ElementFinder
                       └─ actions.UiElement
                            └─ actions.BaseActions ── Playwright Locator

hooks.Hooks ── config.TestContext ── config.BrowserManager ── config.BrowserConfig / config.EnvConfig
(TestContext wstrzykiwany przez PicoContainer do hooków i rejestru Pages)

logging.StepLogger – plugin Cucumbera (zarejestrowany w IngRunner) logujący scenariusze i kroki
runners.IngRunner  – punkt wejścia
```

Nowy krok wymaga tylko metody w step defie, która woła metodę strony z `Pages`. Nowa strona to klasa dziedzicząca po `BasePage` i getter w `Pages`.

### Przepływ scenariusza

1. Surefire uruchamia `IngRunner` (`@Suite`, glue: `io.github.krisqa.ing.stepdefs`, `io.github.krisqa.ing.hooks`; pluginy: Allure, `StepLogger`, raport JUnit XML w `target/cucumber-reports/`). Scenariusze wykonują się równolegle w 3 wątkach.
2. PicoContainer tworzy na scenariusz jeden `TestContext` i jeden rejestr `Pages`.
3. `Hooks.setUp()` → `TestContext.init()`: nowy `BrowserContext` na współdzielonej przeglądarce z `BrowserManager` (jedna na wątek), limity czasu z konfiguracji środowiska, opcjonalne nagrywanie trace i listenery logujące przez SLF4J.
4. Kroki wołają metody stron; weryfikacje: `BasePage.shouldHaveTitle` / `shouldHaveUrlPath`, `CookiesTab.shouldHaveConsent` (przez `CookieVerification`).
5. `Hooks.tearDown()`: przy błędzie screenshot, HTML strony i trace do Allure; zamknięcie kontekstu.
6. `@AfterAll`: `BrowserManager.closeAll()` zamyka przeglądarki.

### Konfiguracja

| Parametr | Źródło | Domyślnie |
|---|---|---|
| `-Denv` | `environments/env.<env>.properties` | `dev` |
| `-Dbrowser` | `browsers/<browser>.properties` | `chromium` |
| `browser.headless`, `cookie.consent.timeout.ms`, `cookie.consent.poll.interval.ms`, `trace.on.failure` | plik środowiska | wymagane |
| `timeout.action.ms` (akcje i nawigacja), `timeout.assertion.ms` (asercje, ścieżka adresu) | plik środowiska | wymagane (15000 / 5000) |
| slowMo, viewport | plik przeglądarki | 0 / 1920×1080 |
| poziom i format logów (z nazwą wątku) | `src/test/resources/simplelogger.properties` | INFO |
| równoległość scenariuszy | `src/test/resources/junit-platform.properties` | 3 wątki (`-Dcucumber.execution.parallel.config.fixed.parallelism=1` = sekwencyjnie) |
| wersja obrazu Docker Compose | `.env` (`PLAYWRIGHT_VERSION`) | równa `playwright.version` |

### CI (GitHub Actions)

| Job | Kiedy | Co robi |
|---|---|---|
| `e2e-tests` | PR do `develop` i push do `develop` (DEV), push do `master` (PREPROD); macierz chromium / firefox / webkit | instaluje przeglądarkę zgodną z `pom.xml` (cache), uruchamia testy, publikuje check „Wyniki testów - &lt;przeglądarka&gt;” w PR, wysyła artefakty (Allure, JUnit, trace) |
| `allure-pages` | push do `master` | generuje raporty Allure z historią dla każdej przeglądarki i publikuje je na GitHub Pages |

Dodatkowo: `concurrency` anuluje nieaktualne przebiegi, a `./mvnw` zapewnia tę samą wersję Mavena lokalnie i w CI.

### Logowanie

Logowanie jest generyczne. Nowe step defy i Page Objects korzystające z `ElementFinder` / `UiElement` są logowane bez dodatkowego kodu.

| Poziom | Gdzie | Co trafia do logu |
|---|---|---|
| Scenariusz, krok | `logging.StepLogger` | start scenariusza, treść kroku, wynik kroku z czasem (błąd jako ERROR), wynik scenariusza |
| Element | `finder.ElementFinder` | „Wyszukanie elementu” z czytelnym opisem, np. `BUTTON 'Dostosuj' w '.cookie-policy-content'` |
| Akcja | `actions.BaseActions` | każda akcja przed i po wykonaniu, z czasem obejmującym oczekiwanie Playwrighta na element; niepowodzenie jako ERROR |
| Nawigacja | `config.TestContext` | „Nawigacja do: URL” dla każdego przejścia ramki głównej |
| Strona | `config.TestContext` | `[PAGE ERROR]` (błędy JS) i `[HTTP ERROR]` (status ≥ 400) jako WARN; konsola przeglądarki jako DEBUG |

Nazwa `simplelogger.properties` jest narzucona przez `slf4j-simple` i nie może być zmieniona. Nieudane żądania (`[REQUEST FAILED]`) celowo nie są logowane.

---

## 3. Otwarte problemy

Brak istotnych problemów. Drobne uwagi:

| # | Prio | Uwaga | Lokalizacja |
|---|---|---|---|
| 1 | P3 | Kontener Docker Compose działa jako root, więc na Linuksie pliki w `target/` należą do roota (na macOS z Docker Desktop problem nie występuje). Uruchomienie jako zwykły użytkownik wymaga też zmiany katalogu domowego i wolumenu z repozytorium Maven (`/root/.m2`). | `docker-compose.yml` |
| 2 | P3 | Job `allure-pages` zadziała dopiero po jednorazowym włączeniu GitHub Pages: Settings → Pages → Source: GitHub Actions. | ustawienia repozytorium |

---

## 4. Analiza scenariuszy testowych

| Plik | Funkcja | Scenariusze | Tagi |
|---|---|---|---|
| `mainPageCookies.feature` | Zgody na pliki cookies na stronie głównej | 1 szablon, 4 przykłady | `@Cookies @GUI` + `@AllCookies/@Necessary/@Analytics/@Marketing` |
| `mainPageNavigation.feature` | Nawigacja na stronie głównej | 1 szablon, 3 przykłady | `@Navigation @GUI` |

### Cookies

- Brakujące przypadki: analityczne + marketingowe jednocześnie, zamknięcie banera bez wyboru, trwałość zgody po przeładowaniu oraz **brak żądań do domen analitycznych przy zgodzie „tylko niezbędne”**. Ten ostatni ma największą wartość z punktu widzenia RODO.
- Testy nie sprawdzają, czy baner zniknął.

### Nawigacja

- Każda pozycja menu jest osobnym przykładem startującym ze strony głównej, a cookies są akceptowane w `Założenia`.
- Weryfikowana jest ścieżka adresu podstrony (np. `/premium`). Domena jest pomijana, bo zależy od środowiska i przekierowań (`ing.pl` → `www.ing.pl`).
- Weryfikowany jest pełny tytuł strony (świadoma decyzja). Zmiana tytułu przez zespół treści wymaga aktualizacji tabeli `Przykłady`. Znak `|` w tytule zapisuje się w tabeli jako `\|`.

---

## 5. Plan dalszych prac

### Rozszerzenie pokrycia

1. Trwałość zgody po przeładowaniu strony.
2. Analityczne + marketingowe jednocześnie.
3. Brak żądań do domen analitycznych przy zgodzie „tylko niezbędne” (`page.onRequest`).
4. Widok mobilny (osobny profil przeglądarki).

