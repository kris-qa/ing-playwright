package io.github.krisqa.ing.logging;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.Status;
import io.cucumber.plugin.event.TestCaseFinished;
import io.cucumber.plugin.event.TestCaseStarted;
import io.cucumber.plugin.event.TestStepFinished;
import io.cucumber.plugin.event.TestStepStarted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StepLogger implements ConcurrentEventListener {

    private static final Logger log = LoggerFactory.getLogger(StepLogger.class);

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseStarted.class, this::scenarioStarted);
        publisher.registerHandlerFor(TestStepStarted.class, this::stepStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::stepFinished);
        publisher.registerHandlerFor(TestCaseFinished.class, this::scenarioFinished);
    }

    private void scenarioStarted(TestCaseStarted event) {
        log.info("=== Scenariusz: {}", event.getTestCase().getName());
    }

    private void stepStarted(TestStepStarted event) {
        if (event.getTestStep() instanceof PickleStepTestStep step) {
            log.info("--- Krok: {}{}", step.getStep().getKeyword(), step.getStep().getText());
        }
    }

    private void stepFinished(TestStepFinished event) {
        if (event.getTestStep() instanceof PickleStepTestStep step) {
            Status status = event.getResult().getStatus();
            long ms = event.getResult().getDuration().toMillis();

            if (status == Status.PASSED) {
                log.info("--- Krok zakończony: {} ({} ms)", status, ms);
            } else {
                log.error("--- Krok zakończony: {} ({} ms): {}{}", status, ms,
                    step.getStep().getKeyword(), step.getStep().getText());
            }
        }
    }

    private void scenarioFinished(TestCaseFinished event) {
        log.info("=== Wynik scenariusza '{}': {}", event.getTestCase().getName(), event.getResult().getStatus());
    }
}
