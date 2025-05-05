package glistermelon;

import java.math.BigInteger;

public record R2BI(BigInteger x, BigInteger y) {

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public R2BI add(R2BI other) {
        return new R2BI(x.add(other.x), y.add(other.y));
    }

    public R2BI sub(R2BI other) {
        return new R2BI(x.subtract(other.x), y.subtract(other.y));
    }

    public R2BI mul(BigInteger c) {
        return new R2BI(
                x.multiply(c),
                y.multiply(c)
        );
    }

    public BigInteger crossMagnitude(R2BI other) {
        return x.multiply(other.y).subtract(y.multiply(other.x));
    }

}