package glistermelon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day15 extends DaySolver {

    public Day15() throws IOException {
        super(15);
    }

    public void runSharedLogic() {}

    public String solvePart1() {

        int sum = 0;
        for (String s : puzzleInput.replaceAll("\n", "").split(","))
            sum += hashString(s);
        return String.valueOf(sum);

    }

    public String solvePart2() {

        List<List<Lens>> boxes = new ArrayList<>();
        for (int i = 0; i < 256; i++) boxes.add(new ArrayList<>());

        for (String s : puzzleInput.replaceAll("\n", "").split(",")) {

            int i = s.indexOf('-');
            if (i == -1) i = s.indexOf('=');
            char op = s.charAt(i);
            String label = s.substring(0, i);
            var box = boxes.get(hashString(label));

            if (op == '-') box.removeIf(l -> l.label.equals(label));
            else {
                Lens lens = new Lens(label, Integer.parseInt(s.substring(i + 1)));
                for (i = 0; i < box.size(); i++) {
                    if (box.get(i).label.equals(lens.label)) break;
                }
                if (i != box.size()) {
                    box.remove(i);
                    box.add(i, lens);
                }
                else box.add(lens);
            }

        }

        int power = 0;
        for (int boxIndex = 0; boxIndex < boxes.size(); boxIndex++) {
            var box = boxes.get(boxIndex);
            for (int lensIndex = 0; lensIndex < box.size(); lensIndex++) {
                var lens = box.get(lensIndex);
                power += (boxIndex + 1) * (lensIndex + 1) * lens.power();
            }
        }

        return String.valueOf(power);
    }

    private int hashString(String s) {
        int hash = 0;
        for (char c : s.toCharArray()) hash = ((hash + c) * 17) % 256;
        return hash;
    }

    record Lens(String label, int power) {
        public boolean equals(Lens other) {
            return label.equals(other.label);
        }
    }

}