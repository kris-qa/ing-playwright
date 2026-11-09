## Playwright ING – Automation Testing Framework

Framework automatyzacji testów UI oparty na Java, Playwright, Cucumber, JUnit 5, Maven oraz Allure.

Projekt umożliwia uruchamianie testów automatycznych na różnych środowiskach oraz przeglądarkach, zarówno lokalnie, jak i w ramach GitHub Actions.

### 1. Technologie


|Technologia|Zastosowanie|
|---|---|
|Java 21|Język programowania|
|Maven (Maven Wrapper `./mvnw`)|Zarządzanie projektem i zależnościami|
|Playwright|Automatyzacja przeglądarek|
|Cucumber|Testy BDD|
|JUnit 5|Uruchamianie testów|
|Allure|Raportowanie wyników|
|GitHub Actions|CI/CD|

Projekt zawiera Maven Wrapper - zamiast lokalnie zainstalowanego `mvn` używaj `./mvnw` (Windows: `mvnw.cmd`).
Wrapper pobiera przy pierwszym uruchomieniu wersję Mavena zapisaną w `.mvn/wrapper/maven-wrapper.properties`.

### 2. Konfiguracja środowisk
Konfiguracja środowiska znajduje się w katalogu:
`src/test/resources/environments/`

Każde środowisko posiada osobny plik .properties.
Są dodane konfiguracje dwóch środowisk (dev i preprod) oraz konfiguracja lokalna (local)

Plik `env.local.properties` nie jest wersjonowany. Aby go utworzyć, skopiuj wzorzec
`env.local.properties.example` do `env.local.properties` i dostosuj wartości.

Wybór środowiska odbywa się za pomocą parametru Maven:
`-Denv=dev`

### 3. Konfiguracja przeglądarek
Pliki konfiguracyjne przeglądarek znajdują się w:

`src/test/resources/browsers/`

Dostępne przeglądarki:

* Chromium
* Firefox
* WebKit

Przykładowe uruchomienie:

`./mvnw clean test -Dbrowser=chromium`

Instalacja przeglądarek w wersji zgodnej z `playwright.version` w `pom.xml`:

`./mvnw exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install chromium firefox webkit"`

### 4. Uruchamianie testów lokalnie

* Wszystkie testy na środowisku DEV:

`./mvnw clean test -Denv=dev`

* Wszystkie testy na PREPROD:

`./mvnw clean test -Denv=preprod`

* Uruchomienie konkretnej przeglądarki:

`./mvnw clean test -Denv=dev -Dbrowser=chromium`

* Scenariusze wykonują się równolegle (3 wątki, konfiguracja w `src/test/resources/junit-platform.properties`).
  Uruchomienie sekwencyjne, np. przy debugowaniu:

`./mvnw clean test -Dcucumber.execution.parallel.config.fixed.parallelism=1`

* Uruchomienie w Dockerze (wersja obrazu Playwright z pliku `.env`):

`docker compose up`


### 5. Filtrowanie testów za pomocą tagów Cucumber
Testy można oznaczać tagami bezpośrednio w plikach .feature.

* Uruchomienie testów oznaczonych tagiem:

`./mvnw clean test -Denv=dev -Dcucumber.filter.tags="@Cookies"`

* Uruchomienie kilku grup testowych:

`./mvnw clean test -Denv=dev -Dcucumber.filter.tags="@Cookies or @Navigation"`

### 6. Uruchamianie z Intelij IDEA

Testy można uruchamiać bezpośrednio z IntelliJ IDEA.

1. Otwórz klasę IngRunner.
2. Kliknij zieloną ikonę uruchamiania przy klasie.
3. Wybierz Run.


Aby przekazać parametry:

1. Otwórz Run → Edit Configurations.
2. Wybierz konfigurację JUnit.
3. W sekcji VM Options dodaj:
`-Denv=dev -Dbrowser=chromium`

### 7. Raport Allure, trace i logi

* Wygenerowanie raportu HTML po testach (`target/site/allure-maven-plugin/index.html`):

`./mvnw allure:report`

* Przy niepowodzeniu scenariusza do raportu Allure dołączane są: screenshot, HTML strony oraz Playwright trace
  (również w `target/traces/`). Trace włącza klucz `trace.on.failure` w pliku środowiska. Podgląd trace:
  https://trace.playwright.dev

* Logi testów (slf4j-simple) konfiguruje plik `src/test/resources/simplelogger.properties`.
  Komunikaty konsoli przeglądarki są na poziomie DEBUG:

`./mvnw clean test -Dorg.slf4j.simpleLogger.defaultLogLevel=debug`

### 8. GitHub Actions

Workflow `.github/workflows/run-tests.yaml` uruchamia się przy PR do `develop` (DEV), pushu do `develop` (DEV),
pushu do `master` (PREPROD) oraz ręcznie (`workflow_dispatch`).

* **e2e-tests** - testy na chromium, firefox i webkit. Wyniki są widoczne w PR do `develop` jako check „Wyniki testów - &lt;przeglądarka&gt;”,
  a raporty (Allure, JUnit, trace) jako artefakty.
* **allure-pages** - po pushu do `master` publikuje raporty Allure z historią na GitHub Pages (osobny raport dla każdej przeglądarki).
  Wymaga jednorazowego włączenia: Settings → Pages → Source: **GitHub Actions**.
