package minesweeper.model;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.util.Duration;
import javafx.util.Pair;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

class MockedListener implements StopwatchListener {
    public ArrayList<String> receivedValues = new ArrayList<>();

    @Override
    public void timeChanged(String newTimeValue) {
        receivedValues.add(newTimeValue);
    }

}

public class StopwatchTest {
    @BeforeAll
    public static void initializeJavaFX() {
        /**
         * It seems like there is a bug in JavaFX that causes a NullPointerException is
         * thrown the first time the play method is called on the Java Timeline class.
         */
        try {
            new Timeline(new KeyFrame(Duration.millis(1000), actionEvent -> {
            })).play();
        } catch (NullPointerException e) {
        }

    }

    @Test
    public void addAndRemoveListenerTest() {
        Stopwatch stopwatch = new Stopwatch();
        MockedListener listener = new MockedListener();
        Assertions.assertArrayEquals(new String[] {}, listener.receivedValues.toArray());
        stopwatch.addListener(listener);

        // Validate that we immediately get a value when we start listening
        Assertions.assertArrayEquals(new String[] { "00:00" }, listener.receivedValues.toArray());

        // Validate that sendTimeUpdate sends another update to the listener
        stopwatch.sendTimeUpdate();
        Assertions.assertArrayEquals(new String[] { "00:00", "00:00" }, listener.receivedValues.toArray());

        // Validate that we don't get new updates when we stop listening
        stopwatch.removeListener(listener);
        stopwatch.sendTimeUpdate();
        Assertions.assertArrayEquals(new String[] { "00:00", "00:00" }, listener.receivedValues.toArray());
    }

    @Test
    public void startTest() {
        // Test that we can start once but not twice
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        Assertions.assertThrows(IllegalStateException.class, () -> {
            stopwatch.start();
        });
    }

    @Test
    public void stopTest() {
        // Test that we cannot stop before starting
        Stopwatch stopwatch = new Stopwatch();
        Assertions.assertThrows(IllegalStateException.class, () -> {
            stopwatch.stop();
        });
        // Test that we can start after stopping
        stopwatch.start();
        stopwatch.stop();

    }

    @Test
    public void hasStartedTest() {
        Stopwatch stopwatch = new Stopwatch();
        Assertions.assertFalse(stopwatch.hasStarted());
        stopwatch.start();
        Assertions.assertTrue(stopwatch.hasStarted());
    }

    @Test
    public void getTimeTest() {
        Stopwatch stopwatch = new Stopwatch();
        Assertions.assertEquals("00:00", stopwatch.getTime());
    }

}
