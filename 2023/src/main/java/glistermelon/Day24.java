package glistermelon;

import org.sosy_lab.common.ShutdownNotifier;
import org.sosy_lab.common.configuration.Configuration;
import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.common.log.LogManager;
import org.sosy_lab.java_smt.SolverContextFactory;
import org.sosy_lab.java_smt.api.*;
import org.sosy_lab.java_smt.api.NumeralFormula.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
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

        try (SolverContext context = SolverContextFactory.createSolverContext(
                Configuration.defaultConfiguration(),
                LogManager.createNullLogManager(),
                ShutdownNotifier.createDummy(),
                SolverContextFactory.Solvers.Z3
        )) {

            ProverEnvironment prover = context.newProverEnvironment(SolverContext.ProverOptions.GENERATE_MODELS);
            IntegerFormulaManager manager = context.getFormulaManager().getIntegerFormulaManager();

            char[] axes = new char[] { 'x', 'y', 'z' };

            Map<Character, IntegerFormula> p0 = new HashMap<>();
            Map<Character, IntegerFormula> v0 = new HashMap<>();

            for (char axis : axes) {
                p0.put(axis, manager.makeVariable("p0" + axis));
                v0.put(axis, manager.makeVariable("v0" + axis));
            }

            for (int i = 1; i <=  lines.size(); i++) {
                var line = lines.get(i - 1);
                IntegerFormula t = manager.makeVariable("t" + i);
                for (char axis : axes) {
                    // p0 + tn*v0 = pn + tn*vn
                    IntegerFormula pn = manager.makeNumber(line.pos().getComponent(axis));
                    IntegerFormula vn = manager.makeNumber(line.slope().getComponent(axis));
                    IntegerFormula lhs = manager.add(p0.get(axis), manager.multiply(t, v0.get(axis)));
                    IntegerFormula rhs = manager.add(pn, manager.multiply(t, vn));
                    BooleanFormula eq = manager.equal(lhs, rhs);
                    prover.addConstraint(eq);
                }
            }


            if (prover.isUnsat()) return "failed";

            Model model = prover.getModel();
            BigInteger sum = BigInteger.ZERO;
            for (char axis : axes) sum = sum.add(model.evaluate(p0.get(axis)));

            return sum.toString();

        }
        catch (InvalidConfigurationException | InterruptedException | SolverException exception) {
            return "Error: " + exception;
        }

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

    record R3I(BigInteger x, BigInteger y, BigInteger z) {

        public boolean equals(R3I other) {
            return x.equals(other.x) && y.equals(other.y) && z.equals(other.z);
        }

        public R3I add(R3I other) {
            return new R3I(
                    x.add(other.x),
                    y.add(other.y),
                    z.add(other.z)
            );
        }

        public R3I mul(BigInteger c) {
            return new R3I(
                    x.multiply(c),
                    y.multiply(c),
                    z.multiply(c)
            );
        }

        public BigInteger getComponent(char name) {
            return switch (name) {
                case 'x' -> x;
                case 'y' -> y;
                case 'z' -> z;
                default -> throw new RuntimeException();
            };
        }

        public String toString() {
            return "(" + x + ", " + y + ", " + z + ")";
        }

    }

}