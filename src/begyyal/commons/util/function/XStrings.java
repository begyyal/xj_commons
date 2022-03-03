package begyyal.commons.util.function;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.function.Consumer;

import begyyal.commons.object.Pair;

public class XStrings {

    private XStrings() {
    }
    
    public static Pair<String, Integer> firstIndexOf(String str, String... targets) {
	return Arrays.stream(targets)
	    .filter(t -> str.indexOf(t) != -1)
	    .map(t -> Pair.of(t, str.indexOf(t)))
	    .sorted(Comparator.comparing(p -> p.v2))
	    .findFirst().orElse(null);
    }

    public static void applyEachToken(String str, Consumer<String> cons) {
	applyEachToken(str, cons, '\t', '\n', '\r', '\f');
    }

    public static void applyEachToken(String str, Consumer<String> cons, char... cs) {
	if (containsAny(str, cs)) {
	    StringTokenizer st = new StringTokenizer(str, new String(cs));
	    while (st.hasMoreTokens())
		cons.accept(st.nextToken());
	} else
	    cons.accept(str);
    }

    public static boolean containsAny(String str, char... cs) {
	Objects.requireNonNull(str);
	var str2char = str.toCharArray();
	for (char c : cs)
	    if (Arrays.binarySearch(str2char, c) >= 0)
		return true;
	return false;
    }

    public static boolean startsWithIgnoreCase(String str, String target) {
	Objects.requireNonNull(str);
	return target != null && str.toLowerCase().startsWith(target.toLowerCase());
    }

    public static int indexOfIgnoreCase(String str, String target) {
	Objects.requireNonNull(str);
	return target == null ? -1 : str.toLowerCase().indexOf(target.toLowerCase());
    }

    public static boolean equals(String v1, String v2) {
	if (v1 == null)
	    return v2 == null;
	if (v2 == null)
	    return v1 == null;
	return v1.equals(v2);
    }
}
