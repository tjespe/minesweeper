package minesweeper.model;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;
import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

public class Stopwatch {
	private Timeline timeline;
	private long startTime = System.currentTimeMillis();;
	private long currentTime;
	private long stopTime;
	private int seconds, minutes;
	private String ddSeconds, ddMinutes;
	private DecimalFormat dFormat = new DecimalFormat("00");
		
	public String updateTime() {
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
	
	/*
	private Timeline timeline;
	private long startTime;
	private long currentTime;
	private long stopTime;
	private int seconds, minutes;
	private String ddSeconds, ddMinutes;
	private DecimalFormat dFormat = new DecimalFormat("00");
	
	
	public void startStopwatch() {
		startTime = System.currentTimeMillis();
		timeline = new Timeline(new KeyFrame(
		        Duration.millis(1000),
		        actionEvent -> updateTime()));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}
	
	public String updateTime() {
		
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
	
	public void stopTimer() {
		timeline.stop();
	} 
	*/
}
	