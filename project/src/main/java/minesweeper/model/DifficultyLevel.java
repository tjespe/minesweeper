package minesweeper.model;

import java.util.Collection;
import java.util.HashMap;

public enum DifficultyLevel {
    EASY("Easy"), NORMAL("Normal"), HARD("Hard");

    private static final HashMap<String, DifficultyLevel> MAP = new HashMap<>();

    private String label;
    private int height;
    private int width;
    private int bombs;

    private DifficultyLevel(String value) {
        this.label = value;
    }

    public static Collection<DifficultyLevel> getAll() {
        return MAP.values();
    }

    public String getLabel() {
        return this.label;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getBombs() {
        return bombs;
    }

    public static DifficultyLevel getByLabel(String name) {
        return MAP.get(name);
    }

    static {
        for (DifficultyLevel field : DifficultyLevel.values()) {
            MAP.put(field.getLabel(), field);
        }
    }
}