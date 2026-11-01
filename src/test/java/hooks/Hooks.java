package hooks;

import config.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import io.qameta.allure.AttachmentOptions;

import java.io.ByteArrayInputStream;

public class Hooks {

    private final TestContext context;

    // Cucumber automatycznie wstrzykuje TestContext (Dependency Injection)
    public Hooks(TestContext context) {
        this.context = context;
    }

    @Before
    public void setUp() {
        context.init();
    }

    @After
    public void tearDown(Scenario scenario) {
        try {
            if (scenario.isFailed() && context.getPage() != null) {

                byte[] screenshot = context.getPage().screenshot();

                Allure.attachment(
                    "Screenshot - " + scenario.getName(),
                    "image/png",
                    new ByteArrayInputStream(screenshot),
                    AttachmentOptions.withFileExtension(".png")
                );
            }
        } finally {
            context.close();
        }
    }

}
