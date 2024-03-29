package begyyal.commons.object.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class XGen {

    private XGen() {
    }

    public static <E> List<E> newArrayList() {
	return new ArrayList<E>();
    }

    public static <E> List<E> newArrayList(Collection<E> c) {
	return new ArrayList<E>(c);
    }

    @SafeVarargs
    public static <E> List<E> newArrayList(E... a) {
	return new ArrayList<E>(Arrays.asList(a));
    }

    public static <E> Set<E> newHashSet() {
	return new HashSet<E>();
    }

    public static <E> Set<E> newHashSet(Collection<E> c) {
	return new HashSet<E>(c);
    }

    @SafeVarargs
    public static <E> Set<E> newHashSet(E... a) {
	return new HashSet<E>(Arrays.asList(a));
    }

    public static <K, V> Map<K, V> newHashMap() {
	return new HashMap<K, V>();
    }

    public static <K, V> ConcurrentHashMap<K, V> newConcurrentMap() {
	return new ConcurrentHashMap<K, V>();
    }
}
