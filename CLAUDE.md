# playwrightING

Testy E2E UI strony ing.pl: Java 21, Playwright for Java 1.49, Cucumber 7 (Gherkin po polsku), JUnit 5 Suite, Allure, SLF4J (slf4j-simple).
Pełna analiza i otwarte tematy: `ANALIZA.md` (czytaj tylko gdy potrzebne). Szczegóły: `src/test/java/io/github/krisqa/ing/CLAUDE.md`, `src/test/resources/CLAUDE.md`.

## Komendy
- Testy: `./mvnw clean test -Denv=dev -Dbrowser=chromium` (browser: chromium|firefox|webkit; env: dev|preprod|local)
- Tag: `-Dcucumber.filter.tags="@Cookies"`; sekwencyjnie: `-Dcucumber.execution.parallel.config.fixed.parallelism=1`
- Kompilacja: `./mvnw -q test-compile`; raport: `./mvnw allure:report`
- Zawsze `./mvnw` (Maven Wrapper, Maven 3.9.9), nie `mvn`.
- Testy idą na produkcyjne ing.pl - filtrowanie wyników: `grep -E "^\[(INFO|WARNING|ERROR)\] Tests run:|BUILD"`.

## Decyzje użytkownika (nie podważać, nie zgłaszać jako problem)
- Wszystkie środowiska (dev/preprod/local) celowo wskazują `https://ing.pl` - pokaz wielu konfiguracji.
- Tytuł strony weryfikowany w całości (`hasTitle(String)`), bez regexów/escapowania.
- Logowanie ma być generyczne (warstwy frameworka), nigdy w step defach.
- Wartości konfigurowalne (timeouty, interwały) trzymać w plikach środowisk, nie jako stałe w kodzie.
- Bez XPath - lokatory po rolach ARIA (`getByRole` + exact).
- Nie logować `[REQUEST FAILED]`. Bez cron w CI, bez Dependabota.
- Nazwy `simplelogger.properties` nie da się zmienić (wymóg slf4j-simple).

## Sposób pracy
- Odpowiedzi i dokumentacja po polsku.
- Nie commitować bez prośby. Po zmianach uruchomić testy (co najmniej chromium) i zaktualizować `ANALIZA.md`, gdy zmienia się stan opisany w analizie.
- Komentarze w kodzie oszczędnie - tylko gdy wyjaśniają "dlaczego".

## CI (`.github/workflows/run-tests.yaml`)
Triggery: PR do develop (DEV), push do develop (DEV), push do master (PREPROD), `workflow_dispatch`. PR do master celowo nie uruchamia testów. Joby: `e2e-tests` (macierz 3 przeglądarek na ubuntu-latest, przeglądarki instalowane przez `exec:java` CLI, check JUnit w PR), `allure-pages` (push do master → GitHub Pages).
Przy aktualizacji Playwrighta zmienić ręcznie `PLAYWRIGHT_VERSION` w `.env` i `playwright.version` w `pom.xml` (CI tego nie sprawdza).
W CI ing.pl może pokazać captchę (IP centrów danych) - nie implementować obchodzenia zabezpieczeń anty-botowych.
