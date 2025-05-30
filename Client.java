import java.util.*;

public class Client {

    static List<List<Integer>> A = Arrays.asList(
        Arrays.asList(1, 0, 0, 0, 1),
        Arrays.asList(1, 1, 1, 0, 1),
        Arrays.asList(0, 0, 1, 1, 1),
        Arrays.asList(0, 0, 0, 0, 1),
        Arrays.asList(1, 1, 1, 1, 1)
    );

    public static void main(String[] args) {
        execute();
    }

    public static void execute() {
        List<String> route = tracePath(A);
        System.out.println("Final Path Coordinates:");
        System.out.println(route);
        System.out.println("Map View of Path:");
        renderPath(A, route);
    }

    public static List<String> tracePath(List<List<Integer>> map) {
        int rows = map.size();
        int cols = map.get(0).size();
        List<int[]> entries = new ArrayList<>();

        for (int r = 0; r < rows; r++) {
            if (map.get(r).get(0) == 1) entries.add(new int[]{r, 0});
            if (map.get(r).get(cols - 1) == 1) entries.add(new int[]{r, cols - 1});
        }

        for (int c = 0; c < cols; c++) {
            if (map.get(0).get(c) == 1) entries.add(new int[]{0, c});
            if (map.get(rows - 1).get(c) == 1) entries.add(new int[]{rows - 1, c});
        }

        for (int[] start : entries) {
            boolean[][] seen = new boolean[rows][cols];
            List<String> path = new ArrayList<>();
            int edge = identifyEdge(start[0], start[1], rows, cols);
            if (search(map, seen, start[0], start[1], edge, path, null, 0)) {
                return path;
            }
        }

        return Collections.emptyList();
    }

    private static boolean search(List<List<Integer>> map, boolean[][] seen, int r, int c, int originEdge, List<String> path, int[] prevDir, int turns) {
        int rows = map.size(), cols = map.get(0).size();
        if (r < 0 || r >= rows || c < 0 || c >= cols || seen[r][c] || map.get(r).get(c) != 1)
            return false;

        seen[r][c] = true;
        path.add("A[" + r + "][" + c + "]");
        int currentEdge = identifyEdge(r, c, rows, cols);

        if (path.size() > 1 && currentEdge != -1 && currentEdge != originEdge && turns >= 1)
            return true;

        int[][] moves = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] dir : moves) {
            int updatedTurns = turns;
            if (prevDir != null && (dir[0] != prevDir[0] || dir[1] != prevDir[1])) {
                updatedTurns++;
            }
            if (search(map, seen, r + dir[0], c + dir[1], originEdge, path, dir, updatedTurns)) {
                return true;
            }
        }

        path.remove(path.size() - 1);
        return false;
    }

    private static int identifyEdge(int r, int c, int rows, int cols) {
        if (r == 0) return 0;
        if (r == rows - 1) return 1;
        if (c == 0) return 2;
        if (c == cols - 1) return 3;
        return -1;
    }

    public static void renderPath(List<List<Integer>> map, List<String> path) {
        int rows = map.size(), cols = map.get(0).size();
        String[][] view = new String[rows][cols];
        for (String[] row : view) Arrays.fill(row, " ");

        for (String coord : path) {
            int r = Integer.parseInt(coord.substring(coord.indexOf('[') + 1, coord.indexOf(']')));
            int c = Integer.parseInt(coord.substring(coord.lastIndexOf('[') + 1, coord.lastIndexOf(']')));
            view[r][c] = "1";
        }

        for (String[] row : view) {
            System.out.println(Arrays.toString(row));
        }
    }
}
