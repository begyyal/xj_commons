package begyyal.commons.util.function;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.function.Consumer;

import begyyal.commons.object.Pair;

public class XStrings {

    // Unicode C0 characters + space
    private static final char[] c0space = new char[33];
    {
	for (int i = 0; i <= 32; i++)
	    c0space[i] = (char) i;
    }

    private XStrings() {
    }

    public static Pair<Integer, Integer> firstIndexOf(String str, int... targets) {
	return Arrays.stream(targets)
	    .mapToObj(t -> {
		int i = str.indexOf(t);
		return i != -1 ? Pair.of(t, i) : null;
	    })
	    .filter(p -> p != null)
	    .sorted(Comparator.comparing(p -> p.v2))
	    .findFirst().orElse(null);
    }

    public static Pair<String, Integer> firstIndexOf(String str, String... targets) {
	return Arrays.stream(targets)
	    .map(t -> {
		int i = str.indexOf(t);
		return i != -1 ? Pair.of(t, i) : null;
	    })
	    .filter(p -> p != null)
	    .sorted(Comparator.comparing(p -> p.v2))
	    .findFirst().orElse(null);
    }

    public static void applyEachToken(String str, Consumer<String> cons) {
	applyEachToken(str, cons, c0space);
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
	if (c0space == cs) {
	    for (char c : str2char)
		if (c <= 32)
		    return true;
	} else
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
