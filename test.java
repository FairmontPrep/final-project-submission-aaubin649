import java.util.ArrayList;
import java.util.Arrays;


public class test{
    // Global static input variable (as required)
    static ArrayList<ArrayList<Integer>> A = new ArrayList<>(Arrays.asList(
        new ArrayList<>(Arrays.asList(1, 0, 0, 1, 0, 0, 0, 0)),
        new ArrayList<>(Arrays.asList(0, 0, 0, 1, 0, 0, 0, 0)),
        new ArrayList<>(Arrays.asList(0, 0, 0, 1, 0, 0, 1, 0)),
        new ArrayList<>(Arrays.asList(9, 0, 0, 1, 0, 0, 0, 0)),
        new ArrayList<>(Arrays.asList(0, 0, 0, 1, 0, 0, 0, 0)),
        new ArrayList<>(Arrays.asList(0, 0, 0, 1, 0, 0, 0, 0)),
        new ArrayList<>(Arrays.asList(0, 0, 0, 1, 2, 0, 0, 0)),
        new ArrayList<>(Arrays.asList(1, 0, 0, 1, 1, 1, 1, 1))
        
    ));
    public static void main(String[] args) {
        runTest();
    }

    public static void runTest(){
        // find all the ones on the edges but exclude ones where the left or right neighbor is also a one
       
        ArrayList<String> ones = new ArrayList<>();
        for (int i = 0; i < A.size(); i++) {
            for (int j = 0; j < A.get(i).size(); j++) {
                if(i == 0 || j == 0 || i == A.size() - 1 || j == A.get(i).size() - 1){
                    if (A.get(i).get(j) == 1) {
                    // check if the left or right neighbor is also a one
                    if ((j > 0 && A.get(i).get(j - 1) == 1) || (j < A.get(i).size() - 1 && A.get(i).get(j + 1) == 1)) {
                        continue;
                    }
                    ones.add("[" + i + "][" + j + "]");
                }
                }
                
            }
        }
        // print the ones
        // System.out.println("Ones on the edges: " + ones);

        // go through the ones and do a dfs on all neighbors to create a list of connected ones
        ArrayList<ArrayList<String>> connectedOnes = new ArrayList<>();
        for (String one : ones) {
            int i = Integer.parseInt(one.substring(1, one.indexOf("][")));
            int j = Integer.parseInt(one.substring(one.indexOf("][") + 1, one.indexOf("]")));
            ArrayList<String> connected = new ArrayList<>();
            dfs(i, j, connected);
            connectedOnes.add(connected);
        }
        
    }

    public static void dfs(int row, int col, ArrayList<String> connected) {
        // check if the row and col are out of bounds
        if (row < 0 || row >= A.size() || col < 0 || col >= A.get(row).size()) {
            return;
        }
        // check if the current cell is a one
        if (A.get(row).get(col) != 1) {
            return;
        }
        // check if the current cell is already visited
        String cell = "[" + row + "][" + col + "]";
        if (connected.contains(cell)) {
            return;
        }
        // add the current cell to the connected list
        connected.add(cell);
        // mark the current cell as visited
        A.get(row).set(col, 2);
        // do a dfs on all neighbors
        dfs(row - 1, col, connected);
        dfs(row + 1, col, connected);
        dfs(row, col - 1, connected);
        dfs(row, col + 1, connected);

    }


}