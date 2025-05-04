package glistermelon;

import java.io.IOException;
import java.util.*;

public class Day23 extends DaySolver {

    public Day23() throws IOException {
        super(23);
    }

    char[][] map;

    public void runSharedLogic() {

        map = Arrays.stream(this.puzzleInput.split("\n"))
                .map(String::toCharArray).toArray(char[][]::new);

    }

    public String solvePart1() {
        return solve(true);
    }

    public String solvePart2() {
        return solve(false);
    }

    public String solve(boolean applySlopes) {

        R2 startPos = null, endPos = null;
        for (int x = 0; x < map[0].length; x++) {
            if (map[0][x] == '.') {
                startPos = new R2(x, 0);
                break;
            }
        }
        for (int x = 0; x < map[0].length; x++) {
            if (map[map.length - 1][x] == '.') {
                endPos = new R2(x, map.length - 1);
                break;
            }
        }

        List<R2> nodes = new ArrayList<>();
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                R2 p = new R2(x, y);
                int count = 0;
                for (R2 a : p.adjacent()) {
                    if (inBounds(a) && map[a.y()][a.x()] != '#') count++;
                }
                if (count > 2) nodes.add(p);
            }
        }
        nodes.add(startPos);
        nodes.add(endPos);

        Map<R2, List<R2>> graph = new HashMap<>();
        Map<R2, Map<R2, Integer>> distances = new HashMap<>();
        for (R2 node : nodes) distances.put(node, new HashMap<>());

        for (R2 node : nodes) {

            Set<R2> visited = new HashSet<>();
            Set<R2> buffer = new HashSet<>();
            buffer.add(node);

            for (int step = 1; !buffer.isEmpty(); step++) {

                visited.addAll(buffer);
                Set<R2> next = new HashSet<>();

                for (R2 p : buffer) {
                    Dir d = applySlopes ? Dir.fromChar(map[p.y()][p.x()]) : null;
                    if (d != null) next.add(d.advance(p));
                    else {
                        for (R2 a : p.adjacent()) {
                            if (inBounds(a) && map[a.y()][a.x()] != '#')
                                next.add(a);
                        }
                    }
                }
                next.removeAll(visited);

                List<R2> remove = new ArrayList<>();
                for (R2 p : next) {

                    if (!nodes.contains(p)) continue;

                    graph.computeIfAbsent(node, k -> new ArrayList<>()).add(p);

                    if (step > distances.get(node).getOrDefault(p, 0))
                        distances.get(node).put(p, step);

                    remove.add(p);

                }
                remove.forEach(next::remove);

                buffer = next;

            }
        }

        return String.valueOf(findLongestPath(startPos, endPos, graph, distances, null));

    }

    private boolean inBounds(R2 p) {
        return p.x() >= 0 && p.y() >= 0 && p.x() < map[0].length && p.y() < map.length;
    }

    private int findLongestPath(
            R2 startPos, R2 endPos, Map<R2, List<R2>> graph, Map<R2, Map<R2, Integer>> distances, List<R2> visited
    ) {

        if (visited == null) {
            visited = new ArrayList<>();
            visited.add(startPos);
        }

        if (startPos.equals(endPos)) {
            int tiles = 0;
            for (int i = 0; i < visited.size() - 1; i++)
                tiles += distances.get(visited.get(i)).get(visited.get(i + 1));
            return tiles;
        }

        int longest = 0;

        for (R2 node : graph.get(startPos)) {
            if (visited.contains(node)) continue;
            visited.add(node);
            int result = findLongestPath(node, endPos, graph, distances, visited);
            visited.removeLast();
            if (result > longest) longest = result;
        }

        return longest;

    }

}