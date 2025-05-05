package glistermelon;

import java.math.BigDecimal;

public record R2BD(BigDecimal x, BigDecimal y) {

    public R2BD(R2BI other) {
        this(new BigDecimal(other.x()), new BigDecimal(other.y()));
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public R2BD add(R2BD other) {
        return new R2BD(x.add(other.x), y.add(other.y));
    }

    public R2BD sub(R2BD other) {
        return new R2BD(x.subtract(other.x), y.subtract(y));
    }

    public R2BD mul(BigDecimal c) {
        return new R2BD(
                x.multiply(c),
                y.multiply(c)
        );
    }

}