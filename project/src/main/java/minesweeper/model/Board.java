package minesweeper.model;

import java.util.ArrayList;
import java.util.Map;

public class Board {
    private ArrayList<ArrayList<Field>> fields;

    // These constants describe x and y offsets for adjacent cells, respectively
    public static final int[] ABOVE = { 0, -1, };
    public static final int[] BELOW = { 0, 1, };
    public static final int[] LEFT = { -1, 0, };
    public static final int[] RIGHT = { 1, 0, };
    public static final int[] LEFTABOVE = { -1, -1, };
    public static final int[] RIGHTABOVE = { 1, -1, };
    public static final int[] RIGHTBELOW = { 1, 1, };
    public static final int[] LEFTBELOW = { -1, 1, };

    public static final int[][] ADJACENCY_OFFSETS = { ABOVE, BELOW, LEFT, RIGHT, LEFTABOVE, RIGHTABOVE, RIGHTBELOW,
            LEFTBELOW };

    public static Map<String, int[]> offsets = Map.of();

    public Board(int height, int width, int bombCount) {
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

    public int getAdjacentBombs(int x, int y) {
        int total = 0;
        int height = this.fields.size();
        int width = this.fields.get(0).size();
        for (int[] offset : ADJACENCY_OFFSETS) {
            int row = y + offset[1];
            int col = x + offset[0];
            if (row > 0 && col > 0 && row < width && col < height && this.fields.get(row).get(col).getHasBomb())
                total += 1;
        }
        return total;
    }

    public String toString() {
        String result = "";
        for (int y = 0; y < this.fields.size(); y++) {
            ArrayList<Field> row = this.fields.get(y);
            for (int x = 0; x < row.size(); x++) {
                result += row.get(x).toString(getAdjacentBombs(x, y)) + " ";
            }
            result += "\n";
        }
        return result;
    }

    public static void main(String[] args) {
        Board board = new Board(10, 10, 10);
        System.out.println(board);
        board.fields.get(4).get(5).open();
        System.out.println(board);
    }
}
