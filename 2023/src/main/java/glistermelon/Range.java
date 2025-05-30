package glistermelon;

import java.util.ArrayList;
import java.util.List;

public record Range(long start, long end) {

    public Range intersection(Range other) {

        if (start >= other.end || end <= other.start)
            return null;
        if (other.start <= start && other.end >= end)
            return this;
        if (other.start >= start && other.end <= end)
            return other;
        if (other.start < start)
            return new Range(start, other.end);
        return new Range(other.start, end);

    }

    public boolean overlaps(Range other) {
        return start < other.end && end > other.start;
    }

    public List<Range> remove(Range other) {

        List<Range> output = new ArrayList<>();
        if (start >= other.end || end <= other.start) {
            output.add(this);
            return output;
        }
        if (other.start <= start && other.end >= end) return output;
        if (other.start >= start && other.end <= end) {
            if (other.start != start) output.add(new Range(start, other.start));
            if (other.end != end) output.add(new Range(other.end, end));
            return output;
        }
        if (other.start < start) output.add(new Range(other.end, end));
        else output.add(new Range(start, other.start));
        return output;

    }

    public Range copy() {
        return new Range(start, end);
    }

    public long size() {
        return end - start;
    }

    public boolean contains(long n) {
        return n >= start && n < end;
    }

    public boolean isInclusiveEndpoint(long n) {
        return start != end && (n == start || n == end - 1);
    }

}