package minesweeper.model;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MockedListener implements StopwatchListener {
    public ArrayList<String> receivedValues = new ArrayList<>();
    public boolean receivedTimeIsUp = false;

    @Override
    public void timeChanged(String newTimeValue) {
        receivedValues.add(newTimeValue);
    }

    @Override
    public void timeIsUp() {
        receivedTimeIsUp = true;
    }

}

public class StopwatchTest extends TestWithJavaFXTimeline {

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

    @Test
    public void setTimeTest() {
        String time = "03:24";
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.setTime(time);
        Assertions.assertEquals(time, stopwatch.getTime());
    }

    @Test
    public void testTimeIsUp() throws InterruptedException {
        Stopwatch stopwatch = new Stopwatch();
        MockedListener listener = new MockedListener();
        stopwatch.addListener(listener);
        Assertions.assertEquals(1, listener.receivedValues.size());
        stopwatch.setTime("60:00");
        stopwatch.sendTimeUpdate();
        Assertions.assertTrue(listener.receivedTimeIsUp);
        Assertions.assertEquals(1, listener.receivedValues.size());
    }

}
