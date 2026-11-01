package verification;

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
