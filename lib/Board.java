package lib;
/**
 * lib/Board.java - lab2
 * The game board abstraction.
 */
import java.util.Stack;
public class Board {
    private int[][] board = new int[3][3];
    private int turn = 1 + (int)Math.round(Math.random() * 1);
    final private String[] PLACES = {"*","X","O"};
    final private int[] xDir = {1,1,1,0,-1,-1,-1,0};
    final private int[] yDir = {1,0,-1,-1,-1,0,1,1};
    public boolean play(int x, int y) {
        // if you try to play somewhere where a play exist,
        // it's going to be ignored
        if (board[x][y] > 0) {
            return false;
        }

        // place the player
        board[x][y] = turn;

        // check if board is a winning board
        System.out.println(PLACES[checkWinStatus()]);
        // change the turn
        if ((++ turn) == 3) turn = 1;
        return true;
    }
    public String toString() {
        String str = "";
        /*
        for (int[] row : board) {
            str += (row[0]) + " | " + (row[1]) + " | " + (row[2]) + "\n";
            str += "---------" + "\n";
        }
        */
        for (int i = 0; i < 3; i++){
            int[] row = board[i];
            str += (PLACES[row[0]] + " | " + PLACES[row[1]] + " | " + PLACES[row[2]] + "\n");
            str += (i!=2)?("---------" + "\n"):("");
        }
        return str;
    }
    private int checkWinStatus(){
        for(int y = 0; y < 3; y++){
            for(int x = 0; x < 3; x++){
                boolean[][] visited = new boolean[3][3];
                Stack<Coordinate> s = new Stack<Coordinate>();
                int length = 0;
                int current = board[x][y];
                if (current == 0) break;
                s.push(new Coordinate(x,y));
                length++;
                for(int i = 0; i < 8; i++){
                    int xTmp = x;
                    int yTmp = y;
                    for(int n = 0; n < 2; n++){
                        xTmp += xDir[i];
                        yTmp += yDir[i];
                        if (xTmp > 2 || xTmp < 0) break;
                        if (yTmp > 2 || yTmp < 0) break;
                        if (board[xTmp][yTmp] != current) break;
                        if (n == 1){
                            return current;
                        }
                    }
                }
            }
        }
        return 0;
    }
}

class Coordinate{
    private int x;
    private int y;

    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
    public String toString(){
        return "[" + x + ", " + y + "]";
    }
}