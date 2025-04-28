package glistermelon;

import java.io.IOException;
import java.util.*;

// copyable template

public class Day17 extends DaySolver {

    public Day17() throws IOException {
        super(17);
    }

    int[][] map;

    public void runSharedLogic() {

        map = Arrays.stream(this.puzzleInput.split("\n"))
                .map(
                        s -> s.chars().map(c -> c - '0').toArray()
                ).toArray(int[][]::new);

    }

    public String solvePart1() {
        return solve(true);
    }

    public String solvePart2() {
        return solve(false);
    }

    private String solve(boolean part1) {

        Set<NodeData> visited = new HashSet<>();
        PriorityQueue<PriorityItem<NodeData>> queue
                = new PriorityQueue<>(Comparator.comparingInt(PriorityItem::priority));
        queue.add(new PriorityItem<>(new NodeData(new R2(0, 0), Dir.Right, 0), 0));
        queue.add(new PriorityItem<>(new NodeData(new R2(0, 0), Dir.Down, 0), 0));

        int minScore = Integer.MAX_VALUE;

        while (!queue.isEmpty()) {

            PriorityItem<NodeData> pItem = queue.poll();
            assert pItem != null;
            NodeData node = pItem.item();
            int score = pItem.priority();

            if (score >= minScore || visited.contains(node)) continue;
            visited.add(node);

            if (
                    (node.pos().x() == map[0].length - 1 && node.pos().y() == map.length - 1)
                    && canTurn(part1, node.time())
            ) minScore = score;

            R2 pos = node.pos();
            Dir dir = node.dir();
            int time = node.time();
            NodeData next;

            for (Dir moveDir : Dir.allDirections()) {
                if (dir.equals(moveDir.flip()) || (dir.equals(moveDir) && mustTurn(part1, time)))
                    continue;
                if (!dir.equals(moveDir) && !canTurn(part1, time)) continue;
                next = new NodeData(
                        moveDir.advance(pos),
                        moveDir,
                        dir.equals(moveDir) ? time + 1 : 1
                );
                if (inBounds(next)) {
                    queue.add(new PriorityItem<>(next, score + map[next.pos().y()][next.pos().x()]));
                }
            }

        }

        return String.valueOf(minScore);

    }

    private boolean canTurn(boolean part1, int time) {
        return part1 || time >= 4;
    }

    private boolean mustTurn(boolean part1, int time) {
        return part1 ? time == 3 : time == 10;
    }

    private record NodeData(R2 pos, Dir dir, int time) {}

    private record PriorityItem<T>(T item, int priority) {}

    private boolean inBounds(R2 p) {
        return p.x() >= 0 && p.y() >= 0 && p.x() < map[0].length && p.y() < map.length;
    }
    private boolean inBounds(NodeData n) {
        return inBounds(n.pos());
    }

}