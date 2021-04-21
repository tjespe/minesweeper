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
	private Timeline timeline;
	private Long startTime;
	private long currentTime;
	private String stopTime;
	private int seconds, minutes;
	private String ddSeconds, ddMinutes;
	private DecimalFormat dFormat = new DecimalFormat("00");
	private Collection<StopwatchListener> stopwatchListeners = new ArrayList<>();
	
	
	public void addStopwatchListener(StopwatchListener stopwatchListener) {
		stopwatchListeners.add(stopwatchListener);
		stopwatchListener.timeChanged(getTime());
	}
	
	public void removeStopwatchListener(StopwatchListener stopwatchListener) {
		stopwatchListeners.remove(stopwatchListener);
	}
	
	public void startStopwatch() {
		startTime = System.currentTimeMillis();
		sendTimeUpdate();
		timeline = new Timeline(new KeyFrame(
		        Duration.millis(1000),
		        actionEvent -> sendTimeUpdate()));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}
	
	public boolean hasStarted() {
		return startTime != null;
	}
	
	public String getTime() {
		if (!hasStarted()) return "00:00";
		currentTime = System.currentTimeMillis() - startTime;
		
		if (currentTime/1000 >=3600) {
			throw new IllegalStateException("Out of time");
		}
		
		minutes = (int) TimeUnit.MILLISECONDS.toMinutes(currentTime);
		long timeLeft = currentTime - TimeUnit.MINUTES.toMillis(minutes);
		seconds = (int) TimeUnit.MILLISECONDS.toSeconds(timeLeft);
		
		ddSeconds = dFormat.format(seconds);
		ddMinutes = dFormat.format(minutes);
		
		return ddMinutes + ":" + ddSeconds;
	}
	
	public void sendTimeUpdate() {
		for (StopwatchListener listener : this.stopwatchListeners)
			listener.timeChanged(this.getTime());
	}
}