package io.github.krisqa.ing.config;

import com.microsoft.playwright.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BrowserConfig {

    private static final Properties PROPERTIES = loadProperties();

    private static Properties loadProperties() {
        String browser = System.getProperty("browser", "chromium");
        Properties props = new Properties();
        String propFileName = "browsers/" + browser + ".properties";

        try (InputStream input = BrowserConfig.class.getClassLoader().getResourceAsStream(propFileName)) {
            if (input == null) {
                throw new RuntimeException("Nie znaleziono pliku konfiguracji: " + propFileName);
            }
            props.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Błąd podczas ładowania pliku właściwości: " + propFileName, ex);
        }
        return props;
    }

    public static Browser createBrowser(Playwright playwright) {
        String browserType = PROPERTIES.getProperty("browser.type", "chromium").toLowerCase();
        boolean headless = Boolean.parseBoolean(EnvConfig.get("browser.headless"));
        double slowMo = Double.parseDouble(PROPERTIES.getProperty("browser.slow.mo", "0"));

        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
            .setHeadless(headless)
            .setSlowMo(slowMo);

        switch (browserType) {
            case "chromium":
                return playwright.chromium().launch(options);
            case "firefox":
                return playwright.firefox().launch(options);
            case "webkit":
                return playwright.webkit().launch(options);
            default:
                throw new IllegalStateException(
                    "Nieobsługiwany browser.type: " + browserType + " (dozwolone: chromium, firefox, webkit)"
                );
        }
    }

    public static BrowserContext createContext(Browser browser) {
        int width = Integer.parseInt(PROPERTIES.getProperty("browser.width", "1920"));
        int height = Integer.parseInt(PROPERTIES.getProperty("browser.height", "1080"));

        return browser.newContext(new Browser.NewContextOptions().setViewportSize(width, height));
    }

}
