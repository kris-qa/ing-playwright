package config;

import com.microsoft.playwright.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BrowserConfig {
    private static Properties loadProperties() {
        String env = System.getProperty("browser", "chromium");
        Properties props = new Properties();
        String propFileName = "browsers/" + env + ".properties";

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
        Properties props = loadProperties();
        String browserType = props.getProperty("browser.type", "chromium").toLowerCase();
        boolean headless = Boolean.parseBoolean(EnvConfig.get("browser.headless"));
        double slowMo = Double.parseDouble(props.getProperty("browser.slow.mo", "0"));

        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
            .setHeadless(headless)
            .setSlowMo(slowMo);

        switch (browserType) {
            case "firefox":
                return playwright.firefox().launch(options);
            case "webkit":
                return playwright.webkit().launch(options);
            case "chromium":
            default:
                return playwright.chromium().launch(options);
        }
    }

    public static BrowserContext createContext(Browser browser) {
        Properties props = loadProperties();
        int width = Integer.parseInt(props.getProperty("browser.width", "1280"));
        int height = Integer.parseInt(props.getProperty("browser.height", "720"));

        return browser.newContext(new Browser.NewContextOptions().setViewportSize(width, height));
    }

}
