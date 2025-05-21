import java.util.ArrayList;

public class Client {
    static int[][] A = {
        {1, 0, 0, 0},
        {1, 0, 0, 0},
        {1, 1, 1, 0},
        {0, 0, 1, 0}
    };

    static int startRow = -1;
    static int startCol = -1;

    public static void main(String[] args) {
        ArrayList<String> answerList = new ArrayList<>();
        boolean[][] visited = new boolean[A.length][A[0].length];

        outer:
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                if (isEdge(i, j, A.length, A[0].length) && A[i][j] == 1) {
                    startRow = i;
                    startCol = j;
                    if (dfs(i, j, A, visited, answerList)) {
                        break outer;
                    }
                }
            }
        }

        System.out.println("Path coordinates:");
        System.out.println(answerList);
        printPathMap(answerList, A.length, A[0].length);
    }

    public static boolean dfs(int row, int col, int[][] A, boolean[][] visited, ArrayList<String> path) {
        if (row < 0 || col < 0 || row >= A.length || col >= A[0].length) return false;
        if (A[row][col] != 1 || visited[row][col]) return false;

        visited[row][col] = true;
        path.add("A[" + row + "][" + col + "]");

        if (path.size() > 1 && isEdge(row, col, A.length, A[0].length)) {
            if (row != startRow || col != startCol) {
                return true;
            }
        }

        int[][] directions = { {1, 0}, {0, 1}, {-1, 0}, {0, -1} };

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (dfs(newRow, newCol, A, visited, path)) {
                return true;
            }
        }

        path.remove(path.size() - 1);
        return false;
    }

    public static boolean isEdge(int row, int col, int rows, int cols) {
        return row == 0 || row == rows - 1 || col == 0 || col == cols - 1;
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
                if (j < cols - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println(" ]");
        }
    }
}
