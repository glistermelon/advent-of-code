package glistermelon;

public class Util {

    static String doubleToString(double n, int decimalDigits) {
        String s = String.valueOf(n);
        if (s.chars().anyMatch(c -> !Character.isDigit(c))) return s;
        return s.substring(0, Integer.min(s.indexOf('.') + decimalDigits + 1, s.length()));
    }

}
