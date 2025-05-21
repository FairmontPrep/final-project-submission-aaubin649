import java.util.ArrayList;

public class Client {
    public static void main(String[] args) {

        int[][] A = {
            {1, 1, 0, 0},
            {0, 1, 0, 0},
            {0, 1, 1, 0},
            {0, 0, 1, 0},
            {0, 1, 1, 0},
            {0, 1, 0, 0},
            {0, 1, 1, 0},
            {0, 0, 1, 0}
        };

        runTest(A);
    }

    public static void runTest(int[][] A) {
        int rows = A.length;
        int cols = A[0].length;
        boolean[][] visited = new boolean[rows][cols];
        ArrayList<String> path = new ArrayList<>();

        boolean found = false;
        for (int i = 0; i < rows && !found; i++) {
            for (int j = 0; j < cols && !found; j++) {
                if (A[i][j] == 1 && isEdge(i, j, rows, cols)) {
                    ArrayList<String> tempPath = new ArrayList<>();
                    boolean[][] tempVisited = new boolean[rows][cols];
                    if (dfs(i, j, A, tempVisited, tempPath)) {
                        if (isEdgeCoord(tempPath.get(0), rows, cols) &&
                            isEdgeCoord(tempPath.get(tempPath.size() - 1), rows, cols) &&
                            !tempPath.get(0).equals(tempPath.get(tempPath.size() - 1))) {
                            path = tempPath;
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

    public static boolean dfs(int row, int col, int[][] A, boolean[][] visited, ArrayList<String> path) {
        if (row < 0 || col < 0 || row >= A.length || col >= A[0].length) return false;
        if (A[row][col] != 1 || visited[row][col]) return false;

        visited[row][col] = true;
        path.add("A[" + row + "][" + col + "]");

        boolean extended = false;
        int[][] directions = { {1, 0}, {0, 1}, {-1, 0}, {0, -1} };
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (dfs(newRow, newCol, A, visited, path)) {
                extended = true;
            }
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
