/*
 * @authors:    Bachir Alhabbal     9334394
 *              Wadih El-Ghoussoubi 9459510
 *
 * This is the Main class containing the main() method that performs the
 * required heuristic searches for the assignment.
 */

package comp472asgn1;

import java.util.Scanner;
import java.util.Queue;
import java.util.LinkedList;
import java.io.*;

public class Main {

    private static char goal[][] = new char[3][3];

    public static void main(String[] args) throws FileNotFoundException, IOException {
        // Read the data from the file "in.txt".
        char state[][] = new char[3][3];
        File file = new File("in.txt");
        Scanner scan = new Scanner(file);
        String temp = scan.next();
        for (int i=0; i<6; i++){
            if (i<3){
                state[i] = temp.toCharArray();
            }
            if (i>=3){
                goal[i-3] = temp.toCharArray();
            }
            if (!scan.hasNext()) break;
            temp = scan.next();
        }
        System.out.println("Starting state is: ");
        print(state);
        System.out.println("Goal state is: ");
        print(goal);

        String[] heuristics = {"Misplaced Tiles", "Manhattan Distance", "n-Swap INADMISSIBLE", "Custom: h2-h3"};
        int[] steps = {0, 0, 0, 0};
        Node[] solution;

        for (int i=0; i !=heuristics.length; i++) {
            System.out.println("Performing Heuristic # "+(i+1)+" ("+heuristics[i]+")...");
            solution = search(state, i+1);
            if (solution.length==0) System.out.println("    Solution is empty!");
            steps[i] = solution.length;
            writeToFile(solution, "out"+(i+1)+".txt");
            System.out.println("    Completed in "+steps[i]+" steps.");
            System.out.println("    Solution Sequence written to file: \"out"+(i+1)+".txt\".");
        }
    }

    /*
     * Returns an array with the child nodes of the given node.
     */
    public static Node[] expand(Node n, int currentCost, Node[] prev) {
        Node[] nodes = new Node[4];
        Board temp = new Board(n.getState());
        nodes[0] = new Node(temp.getStateMoveUp(), currentCost+1);
        nodes[1] = new Node(temp.getStateMoveDown(), currentCost+1);
        nodes[2] = new Node(temp.getStateMoveRight(), currentCost+1);
        nodes[3] = new Node(temp.getStateMoveLeft(), currentCost+1);
        int numNull = 0;
        for (int i=0; i<4; i++){
            if (nodes[i].getState()==null || beenToState(nodes[i].getState(), prev)) numNull++;
        }
        int index = 0;
        Node[] correct = new Node[4-numNull];
        for (int i=0; i<4; i++){
            if (nodes[i].getState()==null || beenToState(nodes[i].getState(), prev)) continue;
            correct[index] = nodes[i];
            index++;
        }
        return correct;
    }

    /*
     * Used for the Misplaced Tiles Heuristic.
     * Given a state and the goal state h1 returns the number of misplaced
     * numbers the state has from the goal state.
     */
    public static int h1(char[][] state) {
        int number = 0;
        for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                if (state[i][j]!= goal[i][j]) number++;
            }
        }
        return number;
    }

    /*
     * Used for the Manhattan Distance Heuristic.
     * h2 calculates a value based on the number of spaces away the current
     * number is from its goal position then adds them all together to create
     * a value for the whole state.
     */
    public static int h2(char[][] state) {
        int total = 0;
        Board board1 = new Board(state);
        Board board2 = new Board(goal);
        for (int i=0; i<9; i++){
            int temp1[]= board1.findOnBoard(i);
            int temp2[]= board2.findOnBoard(i);
            int x1= temp1[0];
            int x2= temp2[0];
            int y1= temp1[1];
            int y2= temp2[1];
            total+= Math.abs(x1-x2)+Math.abs(y1-y2);
        }
        int temp1[]= board1.getBlankLocation();
        int temp2[]= board2.getBlankLocation();
        int x1= temp1[0];
        int x2= temp2[0];
        int y1= temp1[1];
        int y2= temp2[1];
        total+= Math.abs(x1-x2)+Math.abs(y1-y2);
        return total;
    }

    /*
     * Used for the n-Swap Heuristic (INADMISSIBLE).
     * Given a state and the goal state h3 returns the number of steps required
     * to reach the goal assuming we can swap any two tiles.
     */
    public static int h3(char[][] state) {
        int number = 0;
        char[][] tempS = new char[3][3];
        for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                tempS[i][j]= state[i][j];
            }
        }
        Board temp = new Board(tempS);
        Board target = new Board(goal);
        while (!equalArrays(temp.getBoardState(), target.getBoardState())) {
            for (int i=0; i<3; i++){
                for (int j=0; j<3; j++){
                    char here = temp.getBoardState()[i][j];
                    char there = target.getBoardState()[i][j];
                    if (here != there){
                        tempS[i][j] = there;
                        int[] loc = temp.findCharOnBoard(there);
                        tempS[loc[0]][loc[1]] = here;
                        temp.setCurrentState(tempS);
                        number++;
                    }
                }
            }
        }
        return number;
    }

    /*
     * Used for a Custom Heuristic.
     * Given a state and the goal state h4 returns a combination of the second
     * and third heuristics, by computing the difference.
     */
    public static int h4(char[][] state) {
        return Math.abs(h2(state) - h3(state));
    }

    /*
     * Heuristic Selector.
     * Calls the heuristic function according to the passed integer.
     */
    public static int h(char[][] state, int heuristic) {
        switch (heuristic) {
            case 1 :
                return h1(state);
            case 2 :
                return h2(state);
            case 3 :
                return h3(state);
            case 4 :
                return h4(state);
            default:
                return h1(state);
        }
    }

    /*
     * Given an array of nodes findLeast goes through them and picks the one with
     * the lowest cost + the heuristic specified.
     */
    public static Node findLeast(Node[] nodes, int heuristic) {
        if (nodes.length==0) return null;
        char temp[][] = nodes[0].getState();
        int tempCost = nodes[0].getCost();
        for (int i=1; i<nodes.length; i++){
            if (nodes[i].getState()==null) System.out.println("findLess found a null!"); // delete
            if ((nodes[i].getCost()+h(nodes[i].getState(), heuristic))<(tempCost+h(temp, heuristic))){
                temp= nodes[i].getState();
                tempCost= nodes[i].getCost();
            }
        }
        return new Node(temp, tempCost);
    }

    /*
     * A* Search using the provided Heuristic indentifier.
     * This method implements an A* search algorithm to find the path to the goal
     * given the initial state. This method uses the findLeast() method which uses
     * the h() method to calculate a cost value for the state.
     */
    public static Node[] search(char[][] i, int heuristic) {
        int currentCost = 0;
        Node start = new Node(i, currentCost);
        Queue<Node> answer = new LinkedList<Node>();
        Queue<Node> fringe = new LinkedList<Node>();
        fringe.add(start);
        while(!fringe.isEmpty()){
            Node T = (Node)fringe.remove();
            //print(T.getState());
            if (equalArrays(T.getState(), goal)){
                Node[] nodes = new Node[answer.size()];
                answer.toArray(nodes);
                return nodes;
            }
            Node[] nodes = new Node[answer.size()];
            answer.toArray(nodes);
            Node[] expand = expand(T, currentCost, nodes);
            Node N = new Node();
            int count = 1;
            if (expand.length>0) N = findLeast(expand, heuristic);
            while (expand.length==0){
                expand = expand(nodes[nodes.length-count], currentCost, nodes);
                N = findLeast(expand, heuristic);
                count++;
            }
            fringe.add(N);
            answer.add(N);
        }
        return new Node[0];
    }

    /*
     * Prints the 2D array that is passed to it.
     * Only works on a 3x3 array.
     */
    public static void print(char[][] c) {
        if (c==null) {
            System.out.println("Print has been given a null array!");
            return;
        }
        for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                System.out.print(c[i][j]);
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    /*
     * The writeToFile method writes the given array of nodes to a specified
     * output file name.
     */
    public static void writeToFile(Node[] nodes, String name) throws FileNotFoundException, IOException {
        //File outFile = new File(name);
        //FileOutputStream outFileStream = new FileOutputStream(outFile);
        //DataOutputStream out = new DataOutputStream(outFileStream);
        FileWriter outFileStream = new FileWriter(name);
        BufferedWriter out = new BufferedWriter(outFileStream);
        for (int n=0; n<nodes.length; n++){
            for (int i=0; i<3; i++){
                for (int j=0; j<3; j++){
                    //out.writeChar(nodes[n].getState()[i][j]);
                    out.write(nodes[n].getState()[i][j]);
                }
                //out.writeChars("\n");
                out.write("\n");
            }
            //out.writeChars("\n");
            out.write("\n");
        }
        out.close();
    }

    /*
     * This method returns true if the two characters arrays it is given have
     * the same values in the same places.
     * Only works on 3x3 char arrays.
     */
    public static boolean equalArrays(char[][] one, char[][] two) {
        for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                if (one[i][j]!= two[i][j]){
                    return false;
                }
            }
        }
        return true;
    }

    /*
     * This method searches an array of nodes if it contains a given state.
     * It returns true if the state is found in the node array.
     */
    public static boolean beenToState(char[][] state, Node[] nodes) {
        for (int i=0; i<nodes.length; i++){
            if (equalArrays(state, nodes[i].getState())) return true;
        }
        return false;
    }
}
