/*
 * The node class holds the a state and the cost of the state.
 */

/*
 * Bachir - 9334394
 * Wadih - 9459510
 */


package aiproject1;

public class Node {

    private char state[][];
    private int cost;

    /*
     * This constructor takes in a 2D array for the state of the board and an
     * int for the cost of the state.
     */
    public Node(char s[][], int c){
        state= s;
        cost= c;
    }

    /*
     * This constructor is for making a node without any paramiters. It is not
     * recomended.
     */
    public Node(){
        state= null;
        cost= 0;
    }

    /*
     * Returns the cost of the state.
     */
    public int getCost(){
        return cost;
    }

    /*
     * returns the state in the form of a 2D character array.
     */
    public char[][] getState(){
        return state;
    }

    /*
     * Sets a new state for the node. Returns true if the staet was changed and
     * the state given was not null.
     */
    public boolean setState(char s[][]){
        if (s== null) return false;
        state= s;
        return true;
    }

    /*
     * Checks to make sure the new cost is not less then zero and changes it.
     * If the value was changed then it returns true otherwise to returns false.
     */
    public boolean setCost(int c){
        if (c<0) return false;
        cost= c;
        return true;
    }

}
