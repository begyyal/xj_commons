package begyyal.commons.util.function;

import java.util.Objects;

public class XIntegers {

    private static final String regex = "^[0-9]+$";

    private XIntegers() {
    }

    public static boolean checkIfParsable(String str) {
	Objects.requireNonNull(str);
	return str.matches(regex);
    }
}
