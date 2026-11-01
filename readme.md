## Playwright ING – Automation Testing Framework

Framework automatyzacji testów UI oparty na Java, Playwright, Cucumber, JUnit 5, Maven oraz Allure.

Projekt umożliwia uruchamianie testów automatycznych na różnych środowiskach oraz przeglądarkach, zarówno lokalnie, jak i w ramach GitHub Actions.

### 1. Technologie


|Technologia|Zastosowanie|
|---|---|
|Java 21|Język programowania|
|Maven|Zarządzanie projektem i zależnościami|
|Playwright|Automatyzacja przeglądarek|
|Cucumber|Testy BDD|
|JUnit 5|Uruchamianie testów|
|Allure|Raportowanie wyników|
|GitHub|Actions	CI/CD|

### 2. Konfiguracja środowisk
Konfiguracja środowiska znajduje się w katalogu:
`src/test/resources/environments/`

Każde środowisko posiada osobny plik .properties.
Są dodane konfiguracje dwóch środowisk (dev i prepprod) oraz konfiguracja lokalna (local)

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

`mvn clean test -Dbrowser=chromium`

### 4. Uruchamianie testów lokalnie

* Wszystkie testy na środowisku DEV:

`mvn clean test -Denv=dev`

* Wszystkie testy na PREPROD:

`mvn clean test -Denv=preprod`

* Uruchomienie konkretnej przeglądarki:

`mvn clean test -Denv=dev -Dbrowser=chromium`


### 5. Filtrowanie testów za pomocą tagów Cucumber
Testy można oznaczać tagami bezpośrednio w plikach .feature.

* Uruchomienie testów oznaczonych tagiem:

`mvn clean test -Denv=dev -Dcucumber.filter.tags="@Cookies"`

* Uruchomienie kilku grup testowych:

`mvn clean test -Denv=dev -Dcucumber.filter.tags="@Cookies or @Navigation"`

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