package begyyal.commons.util.cache;

import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.Maps;

public class SimpleCacheResolver {

    private static final Map<Class<?>, Map<?, ?>> cache = Maps.newConcurrentMap();

    public static <K, V> V get(K key, Supplier<V> s) {
	@SuppressWarnings("unchecked")
	var miniCache = (Map<K, V>) cache.computeIfAbsent(key.getClass(), k -> {
	    var m = Maps.<K, V>newConcurrentMap();
	    m.put(key, s.get());
	    return m;
	});
	return miniCache.computeIfAbsent(key, k -> s.get());
    }

    public static void remove(Object key) {
	cache.remove(key.getClass());
    }

    public static void clear() {
	cache.clear();
    }
}
