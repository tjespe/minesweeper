package minesweeper.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

public enum DifficultyLevel {
    EASY("Easy", 10, 8, 10), NORMAL("Normal", 40, 14, 18), HARD("Hard", 80, 17, 21);

    private static final HashMap<String, DifficultyLevel> MAP = new HashMap<>();

    private final String label;
    private final int bombs;
    private final int height;
    private final int width;

    private DifficultyLevel(String value, int bombs, int height, int width) {
        this.label = value;
        this.bombs = bombs;
        this.height = height;
        this.width = width;
    }

    public String getLabel() {
        return this.label;
    }

    public int getBombs() {
        return bombs;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public static Collection<DifficultyLevel> getAll() {
        return MAP.values().stream().sorted((l1, l2) -> l1.bombs - l2.bombs).collect(Collectors.toList());
    }

    public static Collection<String> getAllLabels() {
        return getAll().stream().map(DifficultyLevel::getLabel).collect(Collectors.toList());
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