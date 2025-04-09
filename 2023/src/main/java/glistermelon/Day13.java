package glistermelon;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day13 extends DaySolver {

    public Day13() throws IOException {
        super(13);
    }

    final List<List<String>> mapsRows = new ArrayList<>();
    final List<List<String>> mapsCols = new ArrayList<>();

    public String solvePart1() {

        if (mapsRows.isEmpty()) parseInput();

        int sum = 0;

        for (int mapIndex = 0; mapIndex < mapsRows.size(); mapIndex++) {

            var rows = mapsRows.get(mapIndex);
            var cols = mapsCols.get(mapIndex);

            for (int i = 0; i < rows.size() - 1; i++) {
                boolean mirror = true;
                for (int i0 = i, i1 = i + 1; i0 >= 0 && i1 < rows.size(); i0--, i1++) {
                    if (!rows.get(i0).equals(rows.get(i1))) {
                        mirror = false;
                        break;
                    }
                }
                if (mirror) {
                    sum += 100 * (i + 1);
                    break;
                }
            }

            for (int i = 0; i < cols.size() - 1; i++) {
                boolean mirror = true;
                for (int i0 = i, i1 = i + 1; i0 >= 0 && i1 < cols.size(); i0--, i1++) {
                    if (!cols.get(i0).equals(cols.get(i1))) {
                        mirror = false;
                        break;
                    }
                }
                if (mirror) {
                    sum += i + 1;
                    break;
                }
            }

        }

        return String.valueOf(sum);

    }

    public String solvePart2() {

        if (mapsRows.isEmpty()) parseInput();

        int sum = 0;

        for (int mapIndex = 0; mapIndex < mapsRows.size(); mapIndex++) {

            var rows = mapsRows.get(mapIndex);
            var cols = mapsCols.get(mapIndex);

            for (int i = 0; i < rows.size() - 1; i++) {
                boolean mirror = true;
                boolean errorIgnored = false;
                for (int i0 = i, i1 = i + 1; i0 >= 0 && i1 < rows.size(); i0--, i1++) {
                    String s0 = rows.get(i0), s1 = rows.get(i1);
                    int diff = StringUtils.indexOfDifference(s0, s1);
                    if (diff != -1) {
                        if (errorIgnored || !s0.substring(diff + 1).equals(s1.substring(diff + 1))) {
                            mirror = false;
                            break;
                        }
                        else errorIgnored = true;
                    }
                }
                if (mirror && errorIgnored) {
                    sum += 100 * (i + 1);
                    break;
                }
            }

            for (int i = 0; i < cols.size() - 1; i++) {
                boolean mirror = true;
                boolean errorIgnored = false;
                for (int i0 = i, i1 = i + 1; i0 >= 0 && i1 < cols.size(); i0--, i1++) {
                    String s0 = cols.get(i0), s1 = cols.get(i1);
                    int diff = StringUtils.indexOfDifference(s0, s1);
                    if (diff != -1) {
                        if (errorIgnored || !s0.substring(diff + 1).equals(s1.substring(diff + 1))) {
                            mirror = false;
                            break;
                        }
                        else errorIgnored = true;
                    }
                }
                if (mirror && errorIgnored) {
                    sum += i + 1;
                    break;
                }
            }

        }

        return String.valueOf(sum);

    }

    private void parseInput() {

        List<String> rows = new ArrayList<>();
        List<String> cols = new ArrayList<>();

        List<String> lines = new ArrayList<>(List.of(getPuzzleInputLines()));
        lines.add("");

        for (String line : lines) {

            if (!line.isBlank()) {
                rows.add(line);
                if (cols.isEmpty()) cols.addAll(Collections.nCopies(line.length(), ""));
                for (int i = 0; i < line.length(); i++)
                    cols.set(i, cols.get(i) + line.charAt(i));
            }
            else {
                mapsRows.add(rows);
                mapsCols.add(cols);
                rows = new ArrayList<>();
                cols = new ArrayList<>();
            }

        }

    }

}