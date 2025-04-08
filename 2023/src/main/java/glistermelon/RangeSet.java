package glistermelon;

import java.util.ArrayList;
import java.util.List;

public class RangeSet {

    private final List<Range> ranges = new ArrayList<>();

    public RangeSet(Range range) {
        ranges.add(range);
    }

    public RangeSet(List<Range> inputRanges) {
        ranges.addAll(inputRanges);
    }

    public RangeSet intersection(RangeSet otherSet) {
        List<Range> newRanges = new ArrayList<>();
        for (Range range : ranges) {
            range = range.copy();
            for (Range other : otherSet.ranges) {
                assert range != null;
                range = range.intersection(other);
            }
            newRanges.add(range);
        }
        return new RangeSet(newRanges);
    }
    public RangeSet intersection(Range other) {
        List<Range> newRanges = new ArrayList<>();
        for (Range range : ranges) {
            range = range.copy().intersection(other);
            newRanges.add(range);
        }
        return new RangeSet(newRanges);
    }

    public RangeSet remove(RangeSet otherSet) {
        List<Range> newRanges1 = new ArrayList<>(ranges);
        List<Range> newRanges2 = new ArrayList<>();
        for (Range other : otherSet.ranges) {
            for (Range range : newRanges1) newRanges2.addAll(range.remove(other));
            newRanges1.clear();
            newRanges1.addAll(newRanges2);
            newRanges2.clear();
        }
        return new RangeSet(newRanges1);
    }
    public RangeSet remove(Range other) {
        List<Range> newRanges1 = new ArrayList<>(ranges);
        List<Range> newRanges2 = new ArrayList<>();
        for (Range range : newRanges1) newRanges2.addAll(range.remove(other));
        return new RangeSet(newRanges2);
    }

    public RangeSet copy() {
        return new RangeSet(ranges.stream().map(Range::copy).toList());
    }

    public long size() {
        return ranges.stream().mapToLong(Range::size).sum();
    }

}
