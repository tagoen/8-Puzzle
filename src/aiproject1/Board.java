/*
 * The board class was designed to keep track of the current state of the board.
 * It also has the ability to return a new state with a valid move applied to
 * it. When it returns a new state it does not make that state the current state.
 * If you want to set a new state then use the set current state method.
 */

/*
 * Bachir - 9334394
 * Wadih - 9459510
 */


package aiproject1;

/*
 * The constructor for the board class that sets the 2d array that holds the
 * current board state as well as the location of the blank space.
 */
public class Board {

    private char board[][];
    private int blankLocation[];

    public Board(char b[][]){
        board= b;
        //Find the location of b.
        blankLocation= new int[2];
        for (int k=0; k<3; k++){
            for (int c=0; c<3; c++){
                if (board[k][c]== 'B' || board[k][c]== 'b'){
                    blankLocation[0]= c;
                    blankLocation[1]= k;
                }
            }
        }
    }

    /*
     * Same thing as the other constructor except the location of the
     * blank space is given. It does not attempt to find it.
     */
    public Board(char b[][], int l[]){
        board= b;
        blankLocation= l;
    }

    /*
     * Returns the current state of the board.
     */
    public char[][] getBoardState(){
        return board;
    }

    /*
     * Returns the location of the blank space in the current board state.
     */
    public int[] getBlankLocation(){
        return blankLocation;
    }

    /*
     * Returns a new state of the board where the blank space is moved one space
     * down from where it is in the current state.
     */
    public char[][] getStateMoveDown(){
        int y= blankLocation[0];
        int x= blankLocation[1]+1;
        if ((x<0||x>2) || (y<0||y>2)) return null;
        char boardTemp[][]= new char[3][3];
        for (int k=0; k<3; k++){
            for (int c=0; c<3; c++){
                boardTemp[k][c]= board[k][c];
            }
        }
        boardTemp[blankLocation[1]][blankLocation[0]]= boardTemp[x][y];
        boardTemp[x][y]= 'B';
        return boardTemp;
    }

    /*
     * Returns a new state of the board where the blank space is moved one space
     * up from where it is in the current state.
     */
    public char[][] getStateMoveUp(){
        int y= blankLocation[0];
        int x= blankLocation[1]-1;
        if ((x<0||x>2) || (y<0||y>2)) return null;
        char boardTemp[][]= new char[3][3];
        for (int k=0; k<3; k++){
            for (int c=0; c<3; c++){
                boardTemp[k][c]= board[k][c];
            }
        }
        boardTemp[blankLocation[1]][blankLocation[0]]= boardTemp[x][y];
        boardTemp[x][y]= 'B';
        return boardTemp;
    }

    /*
     * Returns a new state of the board where the blank space is moved one space
     * to the left of where it is in the current state.
     */
    public char[][] getStateMoveLeft(){
        int y= blankLocation[0]-1;
        int x= blankLocation[1];
        if ((x<0||x>2) || (y<0||y>2)) return null;
        char boardTemp[][]= new char[3][3];
        for (int k=0; k<3; k++){
            for (int c=0; c<3; c++){
                boardTemp[k][c]= board[k][c];
            }
        }
        boardTemp[blankLocation[1]][blankLocation[0]]= boardTemp[x][y];
        boardTemp[x][y]= 'B';
        return boardTemp;
    }

    /*
     * Returns a new state of the board where the blank space is moved one space
     * to the right of where it is in the current state.
     */
    public char[][] getStateMoveRight(){
        int y= blankLocation[0]+1;
        int x= blankLocation[1];
        if ((x<0||x>2) || (y<0||y>2)) return null;
        char boardTemp[][]= new char[3][3];
        for (int k=0; k<3; k++){
            for (int c=0; c<3; c++){
                boardTemp[k][c]= board[k][c];
            }
        }
        boardTemp[blankLocation[1]][blankLocation[0]]= boardTemp[x][y];
        boardTemp[x][y]= 'B';
        return boardTemp;
    }

    /*
     * Sets a new current state of the baord. Returns true unless it gets a
     * null board state.
     */
    public boolean setCurrentState(char b[][]){
        if (b== null) return false;
        board= b;
        // find the new B.
        for (int k=0; k<3; k++){
            for (int c=0; c<3; c++){
                if (board[k][c]== 'b' || board[k][c]== 'B'){
                    blankLocation[0]= c;
                    blankLocation[1]= k;
                }
            }
        }
        return true;
    }

    /*
     * Given an integer it tries to find it on the board and returns its location
     * in the form of an array with x at the index of 0 and y at 1.
     *
     * Note: it does not find the blank space. (B)
     */
    public int[] findOnBoard(int n){
        int temp[]= new int[2];
        for (int k=0; k<3; k++){
            for (int c=0; c<3; c++){
                if (board[k][c]=='B') continue;
                int i= Integer.parseInt(Character.toString(board[k][c]));
                if (i== n){
                    temp[0]=k;
                    temp[1]=c;
                }
            }
        }
        return temp;
    }


}
