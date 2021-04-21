package minesweeper.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Board {
    private List<List<Field>> fields;
    private Stopwatch stopwatch;
    private Collection<BoardListener> changeListeners = new HashSet<>();

    // These constants describe x and y offsets for adjacent cells, respectively
    private static final int[] ABOVE = { 0, -1, };
    private static final int[] BELOW = { 0, 1, };
    private static final int[] LEFT = { -1, 0, };
    private static final int[] RIGHT = { 1, 0, };
    private static final int[] LEFTABOVE = { -1, -1, };
    private static final int[] RIGHTABOVE = { 1, -1, };
    private static final int[] RIGHTBELOW = { 1, 1, };
    private static final int[] LEFTBELOW = { -1, 1, };

    private static final int[][] ADJACENCY_OFFSETS = { ABOVE, BELOW, LEFT, RIGHT, LEFTABOVE, RIGHTABOVE, RIGHTBELOW,
            LEFTBELOW };

    private void createFields(int height, int width, int bombCount) {
        fields = new ArrayList<>();
        int placedBombs = 0;
        for (int i = 0; i < height; i++) {
            ArrayList<Field> row = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                int remainingFields = height * width - (i * width + j);
                double chanceOfBomb = ((double) bombCount - placedBombs) / (remainingFields);
                boolean shouldHaveBomb = chanceOfBomb > Math.random();
                if (shouldHaveBomb)
                    placedBombs += 1;
                row.add(new Field(shouldHaveBomb));
            }
            fields.add(row);
        }
    }

    public Board(int height, int width, int bombCount) {
        this.stopwatch = new Stopwatch();
        this.createFields(height, width, bombCount);
    }

    public Board(DifficultyLevel difficulty) {
        this.stopwatch = new Stopwatch();
        if (difficulty == (DifficultyLevel.EASY))
            this.createFields(8, 10, 10);
        else if (difficulty.equals(DifficultyLevel.NORMAL))
            this.createFields(14, 18, 40);
        else if (difficulty.equals(DifficultyLevel.HARD))
            this.createFields(17, 21, 80);
        else
            throw new IllegalArgumentException("Unexpected value: " + difficulty);
    }

    public Board(String serializedState) {
        String[] lines = serializedState.split("\\r?\\n");
        this.stopwatch = new Stopwatch();
        String time = lines[0];
        this.stopwatch.setTime(time);
        this.fields = Stream.of(lines)
                // Skip first line, because that's where the time is
                .skip(1)
                // Map each line to a list of Fields
                .map(line -> line.chars()
                        // Map each character in a line to a Field object
                        .mapToObj(status -> new Field((char) status))
                        // Collect the stream of Fields per row in a List
                        .collect(Collectors.toList()))
                // Collect the stream of rows in a List
                .collect(Collectors.toList());

    }

    private Field getField(int x, int y) {
        return this.fields.get(y).get(x);
    }

    public int getHeight() {
        return this.fields.size();
    }

    public int getWidth() {
        return this.fields.get(0).size();
    }

    public char getFieldStatus(int x, int y) {
        return getField(x, y).getStatus();
    }

    public boolean getFieldHasBomb(int x, int y) {
        return getField(x, y).getHasBomb();
    }

    public Collection<int[]> getAdjacentFields(int x, int y) {
        int height = this.fields.size();
        int width = this.fields.get(0).size();
        Collection<int[]> adjacent = new ArrayList<>();
        for (int[] offset : ADJACENCY_OFFSETS) {
            int col = x + offset[0];
            int row = y + offset[1];
            if (row >= 0 && col >= 0 && row < height && col < width)
                adjacent.add(new int[] { col, row });
        }
        return adjacent;
    }

    public int countAdjacentBombs(int x, int y) {
        int total = 0;
        for (int[] coords : this.getAdjacentFields(x, y)) {
            if (this.getField(coords[0], coords[1]).getHasBomb())
                total += 1;
        }
        return total;
    }

    public void openField(int x, int y) {
        if (!this.stopwatch.hasStarted())
            this.stopwatch.start();
        Field field = this.getField(x, y);
        field.open();
        this.fireStateChanged();
        if (this.getStatus() != PLAYING)
            this.stopwatch.stop();
        if (field.getHasBomb())
            return;
        if (this.countAdjacentBombs(x, y) == 0) {
            for (int[] coords : this.getAdjacentFields(x, y)) {
                Field adjacent = this.getField(coords[0], coords[1]);
                if (!adjacent.getHasBomb() && !adjacent.getIsOpened() && !adjacent.getIsFlagged())
                    this.openField(coords[0], coords[1]);
            }
        }
    }

    public void toggleFlag(int x, int y) {
        getField(x, y).toggleFlag();
    }

    public int getBombCount() {
        return Math.toIntExact(this.fields.stream().map(row -> row.stream().filter(Field::getHasBomb).count())
                .collect(Collectors.summingLong(Long::longValue)));
    }

    public int getRemainingFlags() {
        long placedFlags = (this.fields.stream().map(row -> row.stream().filter(Field::getIsFlagged).count())
                .collect(Collectors.summingLong(Long::longValue)));
        return this.getBombCount() - Math.toIntExact(placedFlags);
    }

    public static final char WON = 'w';
    public static final char LOST = 'l';
    public static final char PLAYING = 'p';

    public char getStatus() {
        boolean someNotOpened = false;
        for (List<Field> row : this.fields) {
            for (Field field : row) {
                if (field.getHasBomb() && field.getIsOpened()) {
                    return LOST;
                }
                if (!field.getIsOpened() && !field.getHasBomb()) {
                    someNotOpened = true;
                }
            }
        }
        if (someNotOpened)
            return PLAYING;
        return WON;
    }

    public String toString() {
        StringBuilder bld = new StringBuilder();
        bld.append("   ");
        for (int x = 0; x < this.fields.get(0).size(); x++) {
            bld.append(x + ". ");
        }
        bld.append("\n");
        for (int y = 0; y < this.fields.size(); y++) {
            List<Field> row = this.fields.get(y);
            bld.append(Integer.toString(y) + ". ");
            for (int x = 0; x < row.size(); x++) {
                bld.append(row.get(x).toString(countAdjacentBombs(x, y)) + " ");
            }
            bld.append("\n");
        }
        return bld.toString();
    }

    public void addStopwatchListener(StopwatchListener stopwatchListener) {
        this.stopwatch.addListener(stopwatchListener);
    }

    public void removeStopwatchListener(StopwatchListener stopwatchListener) {
        this.stopwatch.removeListener(stopwatchListener);
    }

    public String getSerializedState() {
        StringBuilder bld = new StringBuilder();
        bld.append(this.stopwatch.getTime() + "\n");
        for (Collection<Field> row : this.fields) {
            for (Field field : row) {
                bld.append(field.getStatus());
            }
            bld.append("\n");
        }
        return bld.toString();
    }

    public void addBoardListener(BoardListener listener) {
        if (!this.changeListeners.contains(listener)) {
            this.changeListeners.add(listener);
            listener.boardChanged(this);
        }
    }

    public void removeBoardListener(BoardListener listener) {
        this.changeListeners.remove(listener);
    }

    public void fireStateChanged() {
        for (BoardListener listener : this.changeListeners) {
            listener.boardChanged(this);
        }
    }

    public static void main(String[] args) {
        Board board = new Board(10, 10, 10);
        Scanner scanner = new Scanner(System.in);
        while (board.getStatus() == PLAYING) {
            try {
                System.out.println("Tid: " + board.stopwatch.getTime() + "\n");
                System.out.println(board);
                System.out.print("Hvilken rad vil du åpne? [0-" + Integer.toString(board.fields.size() - 1) + "] ");
                int y = Integer.parseInt(scanner.nextLine());
                System.out.print(
                        "Hvilken kolonne vil du åpne? [0-" + Integer.toString(board.fields.get(y).size() - 1) + "] ");
                int x = Integer.parseInt(scanner.nextLine());
                board.openField(x, y);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Please try again.");
            }
        }
        System.out.println(board);
        char status = board.getStatus();
        if (status == WON)
            System.out.println("Du vant!");
        else
            System.out.println("Du tapte");
        scanner.close();
    }
}
