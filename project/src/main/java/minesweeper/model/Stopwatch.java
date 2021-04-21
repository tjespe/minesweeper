package minesweeper.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

public class Stopwatch {
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("00");

	private Timeline timeline;
	private Long startTime;
	private Long stopTime;
	private Collection<StopwatchListener> stopwatchListeners = new ArrayList<>();

	public void addListener(StopwatchListener stopwatchListener) {
		if (stopwatchListeners.add(stopwatchListener))
			stopwatchListener.timeChanged(getTime());
	}

	public void removeListener(StopwatchListener stopwatchListener) {
		stopwatchListeners.remove(stopwatchListener);
	}

	public void start() {
		if (this.hasStarted())
			throw new IllegalStateException("Stopwatch already started");
		startTime = System.currentTimeMillis();
		sendTimeUpdate();
		timeline = new Timeline(new KeyFrame(Duration.millis(1000), actionEvent -> sendTimeUpdate()));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

	public void stop() {
		if (!this.hasStarted())
			throw new IllegalStateException("Cannot stop before starting");
		this.stopTime = System.currentTimeMillis();
		timeline.stop();
	}

	public boolean hasStarted() {
		return startTime != null;
	}

	public String getTime() {
		if (!hasStarted())
			return DECIMAL_FORMAT.format(0) + ":" + DECIMAL_FORMAT.format(0);
		long currentTime = stopTime == null ? System.currentTimeMillis() - startTime : stopTime - startTime;

		if (currentTime / 1000 >= 3600) {
			throw new IllegalStateException("Out of time");
		}

		long minutes = TimeUnit.MILLISECONDS.toMinutes(currentTime);
		long timeLeft = currentTime - TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeft);

		return DECIMAL_FORMAT.format(minutes) + ":" + DECIMAL_FORMAT.format(seconds);
	}

	public void sendTimeUpdate() {
		for (StopwatchListener listener : this.stopwatchListeners)
			listener.timeChanged(this.getTime());
	}
}