/*
 * @authors:    Bachir Alhabbal     9334394
 *              Wadih El-Ghoussoubi 9459510
 * 
 * The board class keeps track of the current state of the board.
 * It can also return a new state with a valid move applied to it.
 * When it returns a new state it does not make that state the current state.
 * To set a new state, then the setCurrentState method should be used.
 */

package comp472asgn1;

public class Board {

    private char board[][];
    private int blankLocation[];

    /*
     * A constructor that sets the 2D array that holds the
     * current board state as well as the location of the blank space.
     */
    public Board(char b[][]) {
        board = b;
        //Find the location of b (the blank slot).
        blankLocation = new int[2];
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                if (board[i][j] == 'B' || board[i][j] == 'b'){
                    blankLocation[0] = j;
                    blankLocation[1] = i;
                }
            }
        }
    }

    /*
     * Alternative Constructor where the location of the blank
     * space is given, the method does not attempt to find it.
     */
    public Board(char b[][], int bl[]) {
        board = b;
        blankLocation = bl;
    }

    /*
     * Returns the current state of the board.
     */
    public char[][] getBoardState() {
        return board;
    }

    /*
     * Returns the location of the blank space in the current board state.
     */
    public int[] getBlankLocation() {
        return blankLocation;
    }

    /*
     * Returns a new state of the board where the blank space is moved one space
     * down from where it is in the current state.
     */
    public char[][] getStateMoveDown() {
        int y = blankLocation[0];
        int x = blankLocation[1]+1;
        if ((x<0||x>2) || (y<0||y>2)) return null;
        char boardTemp[][] = new char[3][3];
        for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                boardTemp[i][j]= board[i][j];
            }
        }
        boardTemp[blankLocation[1]][blankLocation[0]] = boardTemp[x][y];
        boardTemp[x][y] = 'B';
        return boardTemp;
    }

    /*
     * Returns a new state of the board where the blank space is moved one space
     * up from where it is in the current state.
     */
    public char[][] getStateMoveUp() {
        int y = blankLocation[0];
        int x = blankLocation[1]-1;
        if ((x<0||x>2) || (y<0||y>2)) return null;
        char boardTemp[][] = new char[3][3];
        for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                boardTemp[i][j]= board[i][j];
            }
        }
        boardTemp[blankLocation[1]][blankLocation[0]] = boardTemp[x][y];
        boardTemp[x][y] = 'B';
        return boardTemp;
    }

    /*
     * Returns a new state of the board where the blank space is moved one space
     * to the left of where it is in the current state.
     */
    public char[][] getStateMoveLeft() {
        int y = blankLocation[0]-1;
        int x = blankLocation[1];
        if ((x<0||x>2) || (y<0||y>2)) return null;
        char boardTemp[][]= new char[3][3];
        for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                boardTemp[i][j]= board[i][j];
            }
        }
        boardTemp[blankLocation[1]][blankLocation[0]] = boardTemp[x][y];
        boardTemp[x][y] = 'B';
        return boardTemp;
    }

    /*
     * Returns a new state of the board where the blank space is moved one space
     * to the right of where it is in the current state.
     */
    public char[][] getStateMoveRight() {
        int y = blankLocation[0]+1;
        int x = blankLocation[1];
        if ((x<0||x>2) || (y<0||y>2)) return null;
        char boardTemp[][]= new char[3][3];
        for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                boardTemp[i][j]= board[i][j];
            }
        }
        boardTemp[blankLocation[1]][blankLocation[0]] = boardTemp[x][y];
        boardTemp[x][y] = 'B';
        return boardTemp;
    }

    /*
     * Sets a new current state of the baord. Returns true unless it gets a
     * null board state.
     */
    public boolean setCurrentState(char b[][]) {
        if (b == null) return false;
        board = b;
        // find the location of the new B.
        for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                if (board[i][j]== 'b' || board[i][j]== 'B'){
                    blankLocation[0]= j;
                    blankLocation[1]= i;
                }
            }
        }
        return true;
    }

    /*
     * This method tries to find a given integer on the board and returns
     * the coordinates of its location in the form of an array with
     * x at index 0 and
     * y at index 1.
     * It does not find the blank space. (B)
     */
    public int[] findOnBoard(int n) {
        int temp[] = new int[2];
        for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                if (board[i][j]=='B') continue;
                int k = Integer.parseInt(Character.toString(board[i][j]));
                if (k == n){
                    temp[0]=i;
                    temp[1]=j;
                }
            }
        }
        return temp;
    }

    /*
     * Same as above, but more general.
     * Works on characters.
     */
    public int[] findCharOnBoard(char n) {
        int temp[] = new int[2];
        for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                if (board[i][j] == n){
                    temp[0]=i;
                    temp[1]=j;
                }
            }
        }
        return temp;
    }
}
