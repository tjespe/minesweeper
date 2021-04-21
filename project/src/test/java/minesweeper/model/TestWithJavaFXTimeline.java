package minesweeper.model;

import org.junit.jupiter.api.BeforeAll;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public abstract class TestWithJavaFXTimeline {
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
}
