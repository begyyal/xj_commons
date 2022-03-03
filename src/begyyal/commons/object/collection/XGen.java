package begyyal.commons.object.collection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class XGen {

    private XGen() {
    }

    public static <E> List<E> newArrayList() {
	return new ArrayList<E>();
    }

    public static <E> Set<E> newHashSet() {
	return new HashSet<E>();
    }

    public static <K, V> ConcurrentHashMap<K, V> newConcurrentMap() {
	return new ConcurrentHashMap<K, V>();
    }
}
