package minesweeper.model;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Stopwatch {
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("00");

	private Timer timer;
	private Long startTime;
	private Long stopTime;
	private Collection<StopwatchListener> stopwatchListeners = new HashSet<>();

	public void addListener(StopwatchListener stopwatchListener) {
		if (!stopwatchListeners.contains(stopwatchListener)) {
			stopwatchListeners.add(stopwatchListener);
			stopwatchListener.timeChanged(getTime());
		}
	}

	public void removeListener(StopwatchListener stopwatchListener) {
		stopwatchListeners.remove(stopwatchListener);
	}

	private void initializeTimeline() {
		if (this.timer != null)
			throw new IllegalStateException("A timeline object already exists.");
		this.timer = new Timer();
		this.timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				sendTimeUpdate();
			}
		}, 0, 1000);
	}

	public void start() {
		if (this.hasStarted())
			throw new IllegalStateException("Stopwatch already started");
		this.startTime = System.currentTimeMillis();
		sendTimeUpdate();
		initializeTimeline();
	}

	public void setTime(String time) {
		String[] parts = time.split(":");
		String minuteString = parts[0];
		String secondsString = parts[1];
		long minutes = Long.parseLong(minuteString);
		long seconds = Long.parseLong(secondsString);
		long now = System.currentTimeMillis();
		this.startTime = now - (minutes * 60 * 1000) - seconds * 1000;
		sendTimeUpdate();
		initializeTimeline();
	}

	public void stop() {
		if (!this.hasStarted())
			throw new IllegalStateException("Cannot stop before starting");
		this.stopTime = System.currentTimeMillis();
		if (this.timer != null) {
			this.timer.cancel();
			this.timer = null;
		}
	}

	public boolean hasStarted() {
		return startTime != null;
	}

	private long getMillis() {
		if (!this.hasStarted())
			return 0;
		return stopTime == null ? System.currentTimeMillis() - startTime : stopTime - startTime;
	}

	private boolean isOutOfTime() {
		return this.getMillis() / 1000 >= 3600;
	}

	public String getTime() {
		long currentTime = this.getMillis();

		if (this.isOutOfTime()) {
			throw new IllegalStateException("Out of time");
		}

		long minutes = TimeUnit.MILLISECONDS.toMinutes(currentTime);
		long timeLeft = currentTime - TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeft);

		return DECIMAL_FORMAT.format(minutes) + ":" + DECIMAL_FORMAT.format(seconds);
	}

	protected void sendTimeUpdate() {
		for (StopwatchListener listener : this.stopwatchListeners) {
			if (this.isOutOfTime()) {
				listener.timeIsUp();
				this.stop();
			} else {
				listener.timeChanged(this.getTime());
			}
		}
	}
}