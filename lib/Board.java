/**
 * lib/Board.java - lab2
 * The game board abstraction.
 */

public class Board {
    private int[][] board = new int[3][3];
    private int turn = 1 + (int)Math.round(Math.random() * 1);

    public boolean play(int x, int y) {
        // if you try to play somewhere where a play exist,
        // it's going to be ignored
        if (board[x][y] > 0) {
            return false;
        }

        // place the player
        board[x][y] = turn;

        // change the turn
        if ((++ turn) == 3) turn = 1;
    }

    private String[] places = {" ","X","O"};
    public String toString() {
        String str = "";

        for (int[] row : board) {
            str += (row[0]) + " | " + (row[1]) + " | " + (row[2]);
        }
    }
}