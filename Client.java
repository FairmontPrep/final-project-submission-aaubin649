import java.util.ArrayList;
import java.util.Arrays;

public class Client {
    // Global static input variable
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

    public static void runTest() {
        int rows = A.size();
        int cols = A.get(0).size();
        // boolean[][] visited = new boolean[rows][cols];
        ArrayList<String> path = new ArrayList<>();
        String startingEdge = "";
        String endingEdge = "";
        String currentDirection = "";
        boolean didTurn = false;

        boolean found = false;
        for (int i = 0; i < rows && !found; i++) {
            for (int j = 0; j < cols && !found; j++) {
                if (A.get(i).get(j) == 1 && isEdge(i, j, rows, cols)) {
                    ArrayList<String> tempPath = new ArrayList<>();
                    boolean[][] tempVisited = new boolean[rows][cols];
                    if (dfs(i, j, A, tempVisited, tempPath, startingEdge, endingEdge, didTurn, currentDirection)) {
                        if (isEdgeCoord(tempPath.get(0), rows, cols) &&
                            isEdgeCoord(tempPath.get(tempPath.size() - 1), rows, cols) &&
                            !tempPath.get(0).equals(tempPath.get(tempPath.size() - 1))) {
                            path = tempPath;
                            System.out.println("Current Path: "+path);
                            found = true;
                        }
                    }
                }
            }
        }

        System.out.println("Path coordinates:");
        System.out.println(path);
        printPathMap(path, rows, cols);
    }

    public static boolean dfs(int row, int col, ArrayList<ArrayList<Integer>> A, boolean[][] visited, ArrayList<String> path, String startingEdge, String endingEdge, boolean didTurn, String currentDirection) {
        int rows = A.size();
        int cols = A.get(0).size();

        if (row < 0 || col < 0 || row >= rows || col >= cols) return false;
        if (A.get(row).get(col) != 1 || visited[row][col]) return false;

        visited[row][col] = true;
        path.add("A[" + row + "][" + col + "]");
        

        // check where previous path and current path is to determine direction
        if (path.size() == 2){
            int prevRow = Integer.parseInt(path.get(path.size() - 2).split("\\[|\\]")[1]);
            int prevCol = Integer.parseInt(path.get(path.size() - 2).split("\\[|\\]")[3]);

            // going left or right
            if (prevRow == row){
                if (prevCol < col){
                    // going right
                    startingEdge = "Left";
                    System.out.println("Starting on Left edge");
                }
                else{
                    // going left
                    startingEdge = "Right";
                    System.out.println("Starting on Right edge");
                }
            }
            else{
                // going up or down
                if(prevRow < row){
                    // going down
                    startingEdge = "Top";
                    System.out.println("Starting on Top edge");
                }
                else{
                    // going up
                    startingEdge = "Down";
                    System.out.println("Starting on Bottom edge");
                }
            }
        }

        if(path.size() > 2 && didTurn){
            if(isEdge(row,col,rows,cols)){
                if(row == 0){
                    if(col == 0){
                        endingEdge = "Top";
                        endingEdge = "Left";
                    }
                    else if(col == cols - 1){
                        endingEdge = "Top";
                        endingEdge = "Right";
                    }
                    else{
                        endingEdge = "Top";
                    }
                }
                else if(row == rows - 1){
                    if(col == 0){
                        endingEdge = "Bottom";
                        endingEdge = "Left";
                    }
                    else if(col == cols - 1){
                        endingEdge = "Bottom";
                        endingEdge = "Right";
                    }
                    else{
                        endingEdge = "Bottom";
                    }
                }
                else if(col == 0){
                    endingEdge = "Left";
                }
                else if(col == cols - 1){
                    endingEdge = "Right";
                }

            }
        }

        // update current direction to be the direction of last coordinate and current coordinate
        if (path.size() > 2){
            int prevRow = Integer.parseInt(path.get(path.size() - 2).split("\\[|\\]")[1]);
            int prevCol = Integer.parseInt(path.get(path.size() - 2).split("\\[|\\]")[3]);

            // going left or right
            if (prevRow == row){
                if (prevCol < col){
                    // going right
                    if(!currentDirection.equals("") && !currentDirection.equals("Right")){
                        System.out.println("Did turn");
                        didTurn = true;
                    }
                    currentDirection = "Right";
                    System.out.println("Going right");
                }
                else{
                    // going left
                    if(!currentDirection.equals("") && !currentDirection.equals("Left")){
                        System.out.println("Did turn");
                        didTurn = true;
                    }
                    currentDirection = "Left";
                    System.out.println("Going left");
                }
            }
            else{
                // going up or down
                if(prevRow < row){
                    // going down
                    if(!currentDirection.equals("") && !currentDirection.equals("Down")){
                        System.out.println("Did turn");
                        didTurn = true;
                    }
                    currentDirection = "Down";
                    System.out.println("Going down");
                }
                else{
                    // going up
                    if(!currentDirection.equals("") && !currentDirection.equals("Top")){
                        System.out.println("Did turn");
                        didTurn = true;
                    }
                    currentDirection = "Top";
                    System.out.println("Going up");
                }
            }
        }

        
        
        // check if we have reach an edge and the edge is not the same as the starting edge and not opposite the starting edge
        if ( isEdge(row, col, rows, cols) && path.size() > 2) {
            System.out.println("Found edge at: A[" + row + "][" + col + "]");
            if (startingEdge.equals("Top") && (endingEdge.equals("Left") || endingEdge.equals("Right") )) {
                System.out.println("Found path from " + startingEdge + " to " + endingEdge);
                return true;
            } else if (startingEdge.equals("Bottom") && (endingEdge.equals("Left") || endingEdge.equals("Right"))) {
                System.out.println("Found path from " + startingEdge + " to " + endingEdge);
                return true;
            } else if (startingEdge.equals("Left") && (endingEdge.equals("Top") || endingEdge.equals("Bottom"))) {
                System.out.println("Found path from " + startingEdge + " to " + endingEdge);
                return true;
            } else if (startingEdge.equals("Right") && (endingEdge.equals("Top") || endingEdge.equals("Bottom"))) {
                System.out.println("Found path from " + startingEdge + " to " + endingEdge);
                return true;
            }
        }

        

        int[][] directions = { {1, 0}, {0, 1}, {-1, 0}, {0, -1} };
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            dfs(newRow, newCol, A, visited, path, startingEdge, endingEdge, didTurn, currentDirection);
        }

        return true;
    }

    public static boolean isEdge(int row, int col, int rows, int cols) {
        return row == 0 || row == rows - 1 || col == 0 || col == cols - 1;
    }

    public static boolean isEdgeCoord(String coord, int rows, int cols) {
        String[] parts = coord.split("\\[|\\]");
        int r = Integer.parseInt(parts[1]);
        int c = Integer.parseInt(parts[3]);
        return isEdge(r, c, rows, cols);
    }

    public static void printPathMap(ArrayList<String> path, int rows, int cols) {
        String[][] display = new String[rows][cols];

        // Initialize all cells with blank space
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                display[i][j] = " ";

        // Mark path coordinates with "1"
        for (String coord : path) {
            String[] parts = coord.split("\\[|\\]");
            int r = Integer.parseInt(parts[1]);
            int c = Integer.parseInt(parts[3]);
            display[r][c] = "1";
        }

        // Print the display map
        for (int i = 0; i < rows; i++) {
            System.out.print("[ ");
            for (int j = 0; j < cols; j++) {
                System.out.print(display[i][j]);
                if (j < cols - 1) System.out.print(", ");
            }
            System.out.println(" ]");
        }
    }
}
