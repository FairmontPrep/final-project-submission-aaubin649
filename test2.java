import java.util.ArrayList;
import java.util.Arrays;

public class test2 {
    // Global static input variable
    static ArrayList<ArrayList<Integer>> A = new ArrayList<>(Arrays.asList(
        new ArrayList<>(Arrays.asList(0, 0, 1, 0, 0)),
        new ArrayList<>(Arrays.asList(0, 0, 1, 0, 0)),
        new ArrayList<>(Arrays.asList(0, 0, 1, 1, 0)),
        new ArrayList<>(Arrays.asList(0, 0, 1, 0, 0)),
        new ArrayList<>(Arrays.asList(0, 0, 1, 1, 1))
    ));

    public static void main(String[] args) {
        runTest(A);
    }

    public static void runTest(ArrayList<ArrayList<Integer>> A) {
        int rows = A.size();
        int cols = A.get(0).size();
        ArrayList<String> path = new ArrayList<>();

        boolean found = false;

        for (int i = 0; i < rows && !found; i++) {
            for (int j = 0; j < cols && !found; j++) {
                if (A.get(i).get(j) == 1 && isEdge(i, j, rows, cols)) {
                    boolean[][] visited = new boolean[rows][cols];
                    ArrayList<String> tempPath = new ArrayList<>();
                    if (dfs(i, j, -1, -1, A, visited, tempPath)) {
                        boolean startIsEdge = isEdgeCoord(tempPath.get(0), rows, cols);
                        boolean endIsEdge = isEdgeCoord(tempPath.get(tempPath.size() - 1), rows, cols);
                        boolean adjacentWalls = areAdjacentWalls(tempPath.get(0), tempPath.get(tempPath.size() - 1), rows, cols);
                        boolean turnExists = hasTurn(tempPath);

                        // Debug logging
                        System.out.println("Temp Path: " + tempPath);
                        System.out.println("Start is edge: " + startIsEdge);
                        System.out.println("End is edge: " + endIsEdge);
                        System.out.println("Adjacent walls: " + adjacentWalls);
                        System.out.println("Has 90° turn: " + turnExists);

                        if (startIsEdge && endIsEdge && adjacentWalls && turnExists) {
                            path = tempPath;
                            found = true;
                        } else {
                            System.out.println("Path rejected:");
                            System.out.println("  " + tempPath);
                            System.out.println("  startIsEdge = " + startIsEdge);
                            System.out.println("  endIsEdge = " + endIsEdge);
                            System.out.println("  adjacentWalls = " + adjacentWalls);
                            System.out.println("  hasTurn = " + turnExists);
                        }
                    }
                }
            }
        }

        System.out.println("Path coordinates:");
        System.out.println(path);
        printPathMap(path, rows, cols);
    }

    public static boolean dfs(int row, int col, int prevRow, int prevCol,
                              ArrayList<ArrayList<Integer>> A,
                              boolean[][] visited,
                              ArrayList<String> path) {
        if (row < 0 || col < 0 || row >= A.size() || col >= A.get(0).size()) return false;
        if (A.get(row).get(col) != 1 || visited[row][col]) return false;

        visited[row][col] = true;
        path.add("A[" + row + "][" + col + "]");

        if (path.size() > 1 &&
            isEdge(row, col, A.size(), A.get(0).size()) &&
            !isEdge(prevRow, prevCol, A.size(), A.get(0).size()) &&
            !sameWall("A[" + row + "][" + col + "]", path.get(0), A.size(), A.get(0).size())) {
            return true;
        }

        int[][] directions = { {0, -1}, {-1, 0}, {0, 1}, {1, 0} };

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            ArrayList<String> newPath = new ArrayList<>(path);
            boolean[][] newVisited = copyVisited(visited);
            if (dfs(newRow, newCol, row, col, A, newVisited, newPath)) {
                path.clear();
                path.addAll(newPath);
                return true;
            }
        }

        return false;
    }

    public static boolean[][] copyVisited(boolean[][] original) {
        boolean[][] copy = new boolean[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            copy[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return copy;
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

    public static boolean sameWall(String coord1, String coord2, int rows, int cols) {
        String[] parts1 = coord1.split("\\[|\\]");
        String[] parts2 = coord2.split("\\[|\\]");
        int r1 = Integer.parseInt(parts1[1]);
        int c1 = Integer.parseInt(parts1[3]);
        int r2 = Integer.parseInt(parts2[1]);
        int c2 = Integer.parseInt(parts2[3]);

        return getWall(r1, c1, rows, cols).equals(getWall(r2, c2, rows, cols));
    }

    public static boolean areAdjacentWalls(String coord1, String coord2, int rows, int cols) {
        String[] parts1 = coord1.split("\\[|\\]");
        String[] parts2 = coord2.split("\\[|\\]");
        int r1 = Integer.parseInt(parts1[1]);
        int c1 = Integer.parseInt(parts1[3]);
        int r2 = Integer.parseInt(parts2[1]);
        int c2 = Integer.parseInt(parts2[3]);
    
        String wall1 = getWall(r1, c1, rows, cols);
        String wall2 = getWall(r2, c2, rows, cols);
    
        // Debug print
        System.out.println("getWall for " + coord1 + " = " + wall1);
        System.out.println("getWall for " + coord2 + " = " + wall2);
    
        if (wall1.equals("none") || wall2.equals("none") || wall1.equals(wall2)) return false;
    
        return (wall1.equals("top") && (wall2.equals("left") || wall2.equals("right"))) ||
               (wall1.equals("bottom") && (wall2.equals("left") || wall2.equals("right"))) ||
               (wall1.equals("left") && (wall2.equals("top") || wall2.equals("bottom"))) ||
               (wall1.equals("right") && (wall2.equals("top") || wall2.equals("bottom")));
    }
    

    public static String getWall(int r, int c, int rows, int cols) {
        String wall;
        if (r == 0) wall = "top";
        else if (r == rows - 1) wall = "bottom";
        else if (c == 0) wall = "left";
        else if (c == cols - 1) wall = "right";
        else wall = "none";

        System.out.println("getWall for A[" + r + "][" + c + "] = " + wall);
        return wall;
    }

    public static boolean hasTurn(ArrayList<String> path) {
        if (path.size() < 3) return false;

        int[] dir1 = getDirection(path.get(0), path.get(1));
        for (int i = 2; i < path.size(); i++) {
            int[] dir2 = getDirection(path.get(i - 1), path.get(i));
            if (dir1[0] != dir2[0] || dir1[1] != dir2[1]) return true;
        }
        return false;
    }

    public static int[] getDirection(String from, String to) {
        String[] partsFrom = from.split("\\[|\\]");
        String[] partsTo = to.split("\\[|\\]");
        int r1 = Integer.parseInt(partsFrom[1]);
        int c1 = Integer.parseInt(partsFrom[3]);
        int r2 = Integer.parseInt(partsTo[1]);
        int c2 = Integer.parseInt(partsTo[3]);
        return new int[]{r2 - r1, c2 - c1};
    }

    public static void printPathMap(ArrayList<String> path, int rows, int cols) {
        String[][] display = new String[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                display[i][j] = " ";

        for (String coord : path) {
            String[] parts = coord.split("\\[|\\]");
            int r = Integer.parseInt(parts[1]);
            int c = Integer.parseInt(parts[3]);
            display[r][c] = "1";
        }

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
