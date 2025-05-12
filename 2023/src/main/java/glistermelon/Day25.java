package glistermelon;

import java.io.IOException;
import java.util.*;

// https://en.wikipedia.org/wiki/Edmonds%E2%80%93Karp_algorithm
// If you modify this algorithm to work with an undirected graph
// with capacities of 0 and 1 as I did below,
// it will actually report the number of edge-disjoint paths
// between two nodes.
// Useful alongside Menger's theorem: https://en.wikipedia.org/wiki/Menger%27s_theorem

public class Day25 extends DaySolver {

    public Day25() throws IOException {
        super(25);
    }

    Map<String, Set<String>> graph = new HashMap<>();

    public void runSharedLogic() {}

    public String solvePart1() {

        for (String line : getPuzzleInputLines()) {
            String[] split = line.replace(":", "").split(" ");
            for (int i = 1; i < split.length; i++) connect(split[0], split[i]);
        }

        List<String> nodes = new ArrayList<>(graph.keySet());
        String node = nodes.removeLast();
        int nodeGroup = 1, otherGroup = 0;
        for (String other : nodes) {
            if (countDisjointPaths(node, other) > 3) nodeGroup++;
            else otherGroup++;
        }

        return String.valueOf(nodeGroup * otherGroup);
    }

    public String solvePart2() {
        return "Merry Christmas!";
    }

    private void connect(String c1, String c2) {
        if (!graph.containsKey(c1)) graph.put(c1, new HashSet<>());
        if (!graph.containsKey(c2)) graph.put(c2, new HashSet<>());
        graph.get(c1).add(c2);
        graph.get(c2).add(c1);
    }

    private void disconnect(String c1, String c2) {
        graph.get(c1).remove(c2);
        graph.get(c2).remove(c1);
    }

    private int countDisjointPaths(String src, String dst) {

        Map<String, Set<DirectedEdge>> edgeGraph = new HashMap<>();

        Set<UndirectedEdge> processed = new HashSet<>();
        for (String u : graph.keySet()) {
            for (String v : graph.get(u)) {

                UndirectedEdge undirectedEdge = new UndirectedEdge(u, v);
                if (processed.contains(undirectedEdge)) continue;
                processed.add(undirectedEdge);

                DirectedEdge edgeUtoV = new DirectedEdge(u, v, 1);
                DirectedEdge edgeVtoU = new DirectedEdge(v, u, 0);

                edgeUtoV.reverse = edgeVtoU;
                edgeVtoU.reverse = edgeUtoV;

                if (!edgeGraph.containsKey(u)) edgeGraph.put(u, new HashSet<>());
                if (!edgeGraph.containsKey(v)) edgeGraph.put(v, new HashSet<>());

                edgeGraph.get(u).add(edgeUtoV);
                edgeGraph.get(v).add(edgeVtoU);

            }
        }

        int pathCount = 0;

        while (true) {

            Queue<String> queue = new ArrayDeque<>();
            queue.add(src);
            Map<String, DirectedEdge> pred = new HashMap<>();
            Set<String> visited = new HashSet<>();
            visited.add(src);

            while (!queue.isEmpty() && !pred.containsKey(dst)) {
                String cur = queue.poll();
                for (DirectedEdge edge : edgeGraph.get(cur)) {
                    if (edge.flow < edge.capacity && !visited.contains(edge.dst)) {
                        visited.add(edge.dst);
                        pred.put(edge.dst, edge);
                        queue.add(edge.dst);
                    }
                }
            }

            if (!pred.containsKey(dst)) break;

            String cur = dst;
            while (!cur.equals(src)) {
                DirectedEdge edge = pred.get(cur);
                edge.flow++;
                edge.reverse.flow--;
                cur = edge.src;
            }

            pathCount++;

        }

        return pathCount;

    }

    record UndirectedEdge(String src, String dst) {

        public boolean equals(UndirectedEdge other) {
            return (src.equals(other.src) && dst.equals(other.dst)) || (src.equals(other.dst) && dst.equals(other.src));
        }

    }

    static class DirectedEdge {

        final public String src;
        final public String dst;
        public int flow;
        public DirectedEdge reverse = null;
        public int capacity;

        public DirectedEdge(String src, String dst, int capacity) {
            this.src = src;
            this.dst = dst;
            this.capacity = capacity;
            flow = 0;
        }

    }

}