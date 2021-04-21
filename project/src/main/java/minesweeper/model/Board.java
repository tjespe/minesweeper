package minesweeper.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class Board {
    private ArrayList<ArrayList<Field>> fields;

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

    public static final String EASY = "Easy";
    public static final String NORMAL = "Normal";
    public static final String HARD = "Hard";
    
    private int flagCount;

    private void createFields(int height, int width, int bombCount) {
    	this.flagCount = bombCount;
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
        this.createFields(height, width, bombCount);
    }

    public Board(String difficulty) {
        if (difficulty.equals(Board.EASY))
            this.createFields(8, 10, 10);
        else if (difficulty.equals(Board.NORMAL))
            this.createFields(14, 18, 40);
        else if (difficulty.equals(Board.HARD))
            this.createFields(17, 21, 80);
        else
            throw new IllegalArgumentException("Unexpected value: " + difficulty);
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
        // TODO: Check that we actually need this method
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
        Field field = this.getField(x, y);
        field.open();
        if (field.getHasBomb())
            return;
        if (this.countAdjacentBombs(x, y) == 0) {
            for (int[] coords : this.getAdjacentFields(x, y)) {
                Field adjacent = this.getField(coords[0], coords[1]);
                /*if (!adjacent.getHasBomb() && !adjacent.getIsOpened())
                    this.openField(coords[0], coords[1]);*/
                if (!adjacent.getHasBomb() && !adjacent.getIsOpened() && !adjacent.getIsFlagged())
                    this.openField(coords[0], coords[1]);
            }
        }
    }
    
    public void toggleFlag(int x, int y) {
    	if (getField(x, y).getIsFlagged()) flagCount ++;
    	else flagCount --;
    	getField(x,y).toggleFlag();
    }
    
    public int getFlagCount() {
    	return flagCount;
    }

    public static final char WON = 'w';
    public static final char LOST = 'l';
    public static final char PLAYING = 'p';

    public char getStatus() {
        boolean someNotOpened = false;
        for (ArrayList<Field> row : this.fields) {
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
            ArrayList<Field> row = this.fields.get(y);
            bld.append(Integer.toString(y) + ". ");
            for (int x = 0; x < row.size(); x++) {
                bld.append(row.get(x).toString(countAdjacentBombs(x, y)) + " ");
            }
            bld.append("\n");
        }
        return bld.toString();
    }

    public static void main(String[] args) {
        Board board = new Board(10, 10, 10);
        Scanner scanner = new Scanner(System.in);
        while (board.getStatus() == PLAYING) {
            try {
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
