package io.github.krisqa.ing.verification;

// Wartości cookie cookiePolicyGDPR, które baner zgód na stronie zapisuje dla poszczególnych wyborów.
// Mapowanie wynika z działania strony (nie z dokumentacji) i jest weryfikowane przez scenariusze @Cookies -
// jeśli strona zmieni format cookie, te scenariusze zaczną padać.
public enum CookieConsent {
    NECESSARY("1"),
    MARKETING("2"),
    ANALYTICS("3"),
    ALL("4");

    private final String value;

    CookieConsent(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
