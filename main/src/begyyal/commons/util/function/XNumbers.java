package begyyal.commons.util.function;

import java.util.Objects;

public class XNumbers {

    private static final String regexInt = "^[0-9]+$";
    private static final String regexDec = "^[0-9]+(.[0-9]+)?$";

    private XNumbers() {
    }

    public static boolean checkIfParsable(String str, boolean decimal) {
	Objects.requireNonNull(str);
	return str.matches(decimal ? regexDec : regexInt);
    }
}
