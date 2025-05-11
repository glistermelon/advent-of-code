package glistermelon;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

public class Day24 extends DaySolver {

    public Day24() throws IOException {
        super(24);
    }

    public static final MathContext MATH_CONTEXT = MathContext.DECIMAL128;

    List<Line> lines = new ArrayList<>();

    public void runSharedLogic() {

        int id = 0;
        for (String line : getPuzzleInputLines()) {
            BigInteger[] nums = Arrays.stream(line.replaceAll(" {2}", " ")
                            .replace(" @ ", ", ").split(", "))
                    .map(BigInteger::new).toArray(BigInteger[]::new);
            lines.add(new Line(id, new R3I(nums[0], nums[1], nums[2]), new R3I(nums[3], nums[4], nums[5])));
            id++;
        }

    }

    public String solvePart1() {

        final BigDecimal minXY = new BigDecimal("200000000000000");
        final BigDecimal maxXY = new BigDecimal("400000000000000");

        long crosses = 0;

        for (int line1Idx = 0; line1Idx < lines.size() - 1; line1Idx++) {
            for (int line2Idx = line1Idx + 1; line2Idx < lines.size(); line2Idx++) {
                Line l1 = lines.get(line1Idx), l2 = lines.get(line2Idx);
                R2BI p1 = new R2BI(l1.pos.x, l1.pos.y), p2 = new R2BI(l2.pos.x, l2.pos.y);
                R2BI v1 = new R2BI(l1.slope.x, l1.slope.y), v2 = new R2BI(l2.slope.x, l2.slope.y);
                R2BD x = l1.projIntersection(l2);
                if (
                        x != null
                                && x.x().compareTo(minXY) > 0 && x.y().compareTo(minXY) > 0
                                && x.x().compareTo(maxXY) < 0 && x.y().compareTo(maxXY) < 0
                                && sign(x.x().subtract(new BigDecimal(p1.x()))) == sign(v1.x())
                                && sign(x.x().subtract(new BigDecimal(p2.x()))) == sign(v2.x())
                ) crosses++;
            }
        }

        return String.valueOf(crosses);

    }

    public String solvePart2() {

        return "";

    }

    Map<BigInteger, List<BigInteger>> divisorsCache = new HashMap<>();
    private List<BigInteger> getDivisors(BigInteger n) {

        if (divisorsCache.containsKey(n)) return divisorsCache.get(n);

        Set<BigInteger> divs = new HashSet<>();
        for (BigInteger i = BigInteger.ONE; i.compareTo(n) <= 0; i = i.add(BigInteger.ONE)) {
            if (n.mod(i).equals(BigInteger.ZERO)) divs.add(i);
        }

        List<BigInteger> list = new ArrayList<>(divs);
        list.addAll(divs.stream().map(BigInteger::negate).toList());
        list.sort(BigInteger::compareTo);

        divisorsCache.put(n, list);

        return list;

    }

    private int sign(BigDecimal n) {
        int c = n.compareTo(BigDecimal.ZERO);
        if (c > 0) return 1;
        else if (c < 0 ) return -1;
        return 0;
    }

    private int sign(BigInteger n) {
        int c = n.compareTo(BigInteger.ZERO);
        if (c > 0) return 1;
        else if (c < 0 ) return -1;
        return 0;
    }

    record Line(int id, R3I pos, R3I slope) {

        public R3I atTime(BigInteger t) {
            return pos.add(slope.mul(t));
        }

        public boolean contains(R3I p) {
            BigInteger m = p.x().subtract(pos.x()).divide(slope.x());
            return pos.add(slope.mul(m)).equals(p);
        }

        public R2BD projIntersection(Line other) {

            R2BI p1 = new R2BI(pos.x, pos.y), p2 = new R2BI(other.pos.x, other.pos.y);
            R2BI v1 = new R2BI(slope.x, slope.y), v2 = new R2BI(other.slope.x, other.slope.y);
            if (v1.crossMagnitude(v2).compareTo(BigInteger.ZERO) == 0) {
                BigInteger m = p2.x().subtract(p1.x()).divide(v1.x());
                return p1.add(v1.mul(m)).equals(p2) ? new R2BD(p1) : null;
            }
            return new R2BD(p1).add(new R2BD(v1).mul(
                    new BigDecimal((p2.sub(p1)).crossMagnitude(v2))
                            .divide(new BigDecimal(v1.crossMagnitude(v2)), MATH_CONTEXT)
            ));

        }

    }

    record R3D(BigDecimal x, BigDecimal y, BigDecimal z) {

        public boolean equals(R3D other) {
            BigDecimal err = new BigDecimal("0.01");
            return x.subtract(other.x).abs().compareTo(err) < 0
                    && y.subtract(other.y).abs().compareTo(err) < 0
                    && z.subtract(other.z).abs().compareTo(err) < 0;
        }

        public R3D add(R3D other) {
            return new R3D(
                    x.add(other.x),
                    y.add(other.y),
                    z.add(other.z)
            );
        }

        public R3D mul(BigDecimal c) {
            return new R3D(
                    x.multiply(c),
                    y.multiply(c),
                    z.multiply(c)
            );
        }

        public String toString() {
            return "(" + x + ", " + y + ", " + z + ")";
        }

    }

    record R3I(BigInteger x, BigInteger y, BigInteger z) {

        public boolean equals(R3I other) {
            return x.equals(other.x) && y.equals(other.y) && z.equals(other.z);
        }

        public boolean isZero() {
            return x.equals(BigInteger.ZERO) && y.equals(BigInteger.ZERO) && z.equals(BigInteger.ZERO);
        }

        public R3I add(R3I other) {
            return new R3I(
                    x.add(other.x),
                    y.add(other.y),
                    z.add(other.z)
            );
        }

        public R3I sub(R3I other) {
            return new R3I(
                    x.subtract(other.x),
                    y.subtract(other.y),
                    z.subtract(other.z)
            );
        }

        public R3I mul(BigInteger c) {
            return new R3I(
                    x.multiply(c),
                    y.multiply(c),
                    z.multiply(c)
            );
        }

        public R3I div(BigInteger c) {
            return new R3I(
                    x.divide(c),
                    y.divide(c),
                    z.divide(c)
            );
        }

        public BigInteger dot(R3I other) {
            return x.multiply(other.x).add(y.multiply(other.y)).add(z.multiply(other.z));
        }

        public R3I cross(R3I other) {
            // Applying the cross product formula
            BigInteger cx = y.multiply(other.z).subtract(z.multiply(other.y));
            BigInteger cy = z.multiply(other.x).subtract(x.multiply(other.z));
            BigInteger cz = x.multiply(other.y).subtract(y.multiply(other.x));

            return new R3I(cx, cy, cz);
        }


        public String toString() {
            return "(" + x + ", " + y + ", " + z + ")";
        }

    }

}