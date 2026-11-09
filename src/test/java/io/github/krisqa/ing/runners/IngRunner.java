package io.github.krisqa.ing.runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;


@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(
    key = PLUGIN_PROPERTY_NAME,
    value = "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm,"
        + "io.github.krisqa.ing.logging.StepLogger,"
        + "junit:target/cucumber-reports/cucumber-junit.xml"
)@ConfigurationParameter(
    key = GLUE_PROPERTY_NAME,
    value = "io.github.krisqa.ing.stepdefs,io.github.krisqa.ing.hooks"
)
public class IngRunner {
}
