package begyyal.commons.util.cache;

import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.Maps;

public class SimpleCacheResolver {

    private static final Map<Object, Map<?, ?>> //
    publicCache = Maps.newConcurrentMap();
    private static final Map<Class<?>, Map<Object, Map<?, ?>>> //
    privateCache = Maps.newConcurrentMap();

    public static <K, V> V getAsPublic(Object mapId, K key, Supplier<? extends V> s) {
	@SuppressWarnings("unchecked")
	var cache = (Map<K, V>) publicCache.computeIfAbsent(
	    mapId, k -> Maps.<K, V>newConcurrentMap());
	return cache.computeIfAbsent(key, k -> s.get());
    }

    public static <K, V> V getAsPrivate(Class<?> caller, Object mapId, K key, Supplier<? extends V> s) {
	@SuppressWarnings("unchecked")
	var small = (Map<K, V>) privateCache
	    .computeIfAbsent(caller, k -> Maps.newConcurrentMap())
	    .computeIfAbsent(mapId, k -> Maps.<K, V>newConcurrentMap());
	return small.computeIfAbsent(key, k -> s.get());
    }

    public static void removeAsPublic(Object mapId, Object key) {
	var m = publicCache.get(mapId);
	if (m != null)
	    m.remove(key);
    }

    public static void removeAsPrivate(Class<?> caller, Object mapId, Object key) {
	Map<?, Map<?, ?>> m = privateCache.get(caller);
	Map<?, ?> s = null;
	if (m != null && (s = m.get(mapId)) != null)
	    s.remove(key);
    }

    public static void clearAsPublic(Object mapId) {
	publicCache.remove(mapId);
    }

    public static void clearAsPrivate(Class<?> caller, Object mapId) {
	Map<?, Map<?, ?>> m = privateCache.get(caller);
	if (m != null && m.containsKey(mapId))
	    m.remove(mapId);
    }

    public static void clearAll() {
	publicCache.clear();
	privateCache.clear();
    }
}
