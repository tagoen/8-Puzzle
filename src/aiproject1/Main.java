package aiproject1;
/*
 * Bachir - 9334394
 * Wadih - 9459510
 */




import java.util.Scanner;
import java.util.Queue;
import java.util.LinkedList;
import java.io.*;


public class Main {
    private static char goal[][]= new char[3][3];

    public static void main(String[] args) throws FileNotFoundException, IOException {
        // take in the data from the file "in.txt".
        char state[][]= new char[3][3];
        File file= new File("in.txt");
        Scanner scan= new Scanner(file);
        String temp= scan.next();
        for (int k=0; k<6; k++){
            if (k<3){
                state[k]= temp.toCharArray();
            }
            if (k>=3){
                goal[k-3]= temp.toCharArray();
            }
            if (!scan.hasNext()) break;
            temp= scan.next();
        }
        System.out.println("startint state is: ");
        print(state);
        System.out.println("goal state is: ");
        print(goal);

        System.out.println("Starting with heuristic1...");
        Node[] answer= search1(state);
        int num1= 0;
        int num2= 0;
        if (answer.length==0) System.out.println("answer was empty!!!");
        for (int k=0; k<answer.length; k++){
            print(answer[k].getState());
            num1++;
        }
        writeToFile(answer, "out.txt");
        System.out.println("Now moving to heuristic2...");
        answer= search2(state);
        if (answer.length==0) System.out.println("answer was empty!!!");
        for (int k=0; k<answer.length; k++){
            print(answer[k].getState());
            num2++;
        }
        writeToFile(answer, "out2.txt");
        System.out.println("Answers have been writen to their files (out1.txt and out2.txt).\n");
        System.out.println("There were "+num1+" steps for h1 and "+"there were "+num2+" steps for h2.");

    }

    /*
     * returns an array with the child nodes of the node given.
     */
    public static Node[] expand(Node n, int currentCost, Node[] prev){
        Node[] nodes= new Node[4];
        Board temp= new Board(n.getState());
        nodes[0]= new Node(temp.getStateMoveUp(), currentCost+1);
        nodes[1]= new Node(temp.getStateMoveDown(), currentCost+1);
        nodes[2]= new Node(temp.getStateMoveRight(), currentCost+1);
        nodes[3]= new Node(temp.getStateMoveLeft(), currentCost+1);
        int numNull=0;
        for (int k=0; k<4; k++){
            if (nodes[k].getState()==null || beenToState(nodes[k].getState(), prev)) numNull++;
        }
        int index=0;
        Node[] correct= new Node[4-numNull];
        for (int k=0; k<4; k++){
            if (nodes[k].getState()==null || beenToState(nodes[k].getState(), prev)) continue;
            correct[index]=nodes[k];
            index++;
        }
        return correct;
    }

    /*
     * Given a state and the goal state h1 returns the number of missplaced
     * numbers the state has from the goal state.
     */
    public static int h1(char[][] state){
        int number= 0;
        for (int k=0; k<3; k++){
            for (int c=0; c<3; c++){
                if (state[k][c]!= goal[k][c]) number++;
            }
        }
        return number;
    }

    /*
     * H2 calculates a value based on the number of spaces away the current
     * number is from its goal then adds them all together to create a value
     * for the whole state.
     */
    public static int h2(char[][] state){
        int total= 0;
        Board board1= new Board(state);
        Board board2= new Board(goal);
        for (int k=0; k<9; k++){
            int temp1[]= board1.findOnBoard(k);
            int temp2[]= board2.findOnBoard(k);
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
     * Given an array of nodes findLess goes through them and picks the one with
     * the lowest cost + h1.
     */
    public static Node findLess1(Node[] nodes){
        if (nodes.length==0) return null;
        char temp[][]= nodes[0].getState();
        int tempCost= nodes[0].getCost();
        for (int k= 1; k<nodes.length; k++){
            if (nodes[k].getState()==null) System.out.println("findLess found a null!!!"); // delete
            if ((nodes[k].getCost()+h1(nodes[k].getState()))<(tempCost+h1(temp))){
                temp= nodes[k].getState();
                tempCost= nodes[k].getCost();
            }
        }
        return new Node(temp, tempCost);
    }

    /*
     * Given an array of nodes findLess goes through them and picks the one with
     * the lowest cost + h2.
     */
    public static Node findLess2(Node[] nodes){
        if (nodes.length==0) return null;
        char temp[][]= nodes[0].getState();
        int tempCost= nodes[0].getCost();
        for (int k= 1; k<nodes.length; k++){
            if (nodes[k].getState()==null) System.out.println("findLess found a null!!!"); // delete
            if ((nodes[k].getCost()+h2(nodes[k].getState()))<(tempCost+h2(temp))){
                temp= nodes[k].getState();
                tempCost= nodes[k].getCost();
            }
        }
        return new Node(temp, tempCost);
    }

    /*
     * This method implements an A* search algorithm to find the path to the
     * given the initial state. This method uses the findLess1 method which uses
     * the h1 method to calculate a value for the state.
     */
    public static Node[] search1(char[][] i){
        int currentCost=0;
        Node start= new Node(i, currentCost);
        Queue <Node>answer= new <Node>LinkedList();
        Queue <Node>fringe= new <Node>LinkedList();
        fringe.add(start);
        while(!fringe.isEmpty()){
            Node T= (Node)fringe.remove();
            //print(T.getState());
            if (equalArrays(T.getState(), goal)){
                Node[] nodes= new Node[answer.size()];
                answer.toArray(nodes);
                return nodes;
            }
            Node[] nodes= new Node[answer.size()];
            answer.toArray(nodes);
            Node[] expand= expand(T, currentCost, nodes);
            Node N= new Node();
            int count= 1;
            if (expand.length>0) N= findLess2(expand);
            while (expand.length==0){
                expand= expand(nodes[nodes.length-count], currentCost, nodes);
                N= findLess1(expand);
                count++;
            }
            fringe.add(N);
            answer.add(N);
        }
        return new Node[0];
    }

    /*
     * This method implements an A* search algorithm to find the path to the
     * given the initial state. This method uses the findLess1 method which uses
     * the h2 method to calculate a value for the state.
     */
    public static Node[] search2(char[][] i){
        int currentCost=0;
        Node start= new Node(i, currentCost);
        Queue <Node>answer= new <Node>LinkedList();
        Queue <Node>fringe= new <Node>LinkedList();
        fringe.add(start);
        while(!fringe.isEmpty()){
            Node T= (Node)fringe.remove();
            //print(T.getState());
            if (equalArrays(T.getState(), goal)){
                Node[] nodes= new Node[answer.size()];
                answer.toArray(nodes);
                return nodes;
            }
            Node[] nodes= new Node[answer.size()];
            answer.toArray(nodes);
            Node[] expand= expand(T, currentCost, nodes);
            Node N= new Node();
            int count= 1;
            if (expand.length>0) N= findLess2(expand);
            while (expand.length==0){
                expand= expand(nodes[nodes.length-count], currentCost, nodes);
                N= findLess2(expand);
                count++;
            }
            fringe.add(N);
            answer.add(N);
        }
        return new Node[0];
    }

    // prints the 2D array that is passed to it. (only a 3x3 array)
    public static void print(char[][] c){
        if (c==null){
            System.out.println("print has been given a null array!");
            return;
        }
        for (int k=0; k<3; k++){
            for (int i=0; i<3; i++){
                System.out.print(c[k][i]);
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    /*
     * The writeToFile method writes the given array of nodes to out.txt which is
     * placed in the same directory as this program.
     */
    public static void writeToFile(Node[] nodes, String name) throws FileNotFoundException, IOException{
        File outFile= new File(name);
        FileOutputStream outFileStream= new FileOutputStream(outFile);
        DataOutputStream out= new DataOutputStream(outFileStream);
        for (int n=0; n<nodes.length; n++){
            for (int k=0; k<3; k++){
                for (int c=0; c<3; c++){
                    out.writeChar(nodes[n].getState()[k][c]);
                }
                out.writeChars("\n");
            }
            out.writeChars("\n");
        }
    }

    /*
     * This method returns true if the two characters arrays it is given have
     * the same values in the same places. (only for a 3X3 char array)
     */
    public static boolean equalArrays(char[][] one, char[][] two){
        for (int k=0; k<3; k++){
            for (int c=0; c<3; c++){
                if (one[k][c]!= two[k][c]){
                    return false;
                }
            }
        }
        return true;
    }

    /*
     * This method is used to find out if the given state is in any of the nodes
     * that are in the node array it is given. It returns true if the value
     * is found in the node array.
     */
    public static boolean beenToState(char[][] state, Node[] nodes){
        for (int k=0; k<nodes.length; k++){
            if (equalArrays(state, nodes[k].getState())) return true;
        }
        return false;
    }

}
