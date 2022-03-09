package begyyal.commons.util.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class SimpleCacheResolver {

    private static final Map<Object, Map<?, ?>> //
    publicCache = new ConcurrentHashMap<Object, Map<?, ?>>();
    private static final Map<Class<?>, Map<Object, Map<?, ?>>> //
    privateCache = new ConcurrentHashMap<Class<?>, Map<Object, Map<?, ?>>>();

    @SuppressWarnings("unchecked")
    private static <K, V> Map<K, V> getMap(Object mapId, K key) {
	return (Map<K, V>) publicCache.computeIfAbsent(
	    mapId, k -> new ConcurrentHashMap<K, V>());
    }

    public static <K, V> V getAsPublic(Object mapId, K key) {
	Map<K, V> cache = getMap(mapId, key);
	return cache.get(key);
    }

    public static <K, V> V getAsPublic(Object mapId, K key, Supplier<? extends V> s) {
	Map<K, V> cache = getMap(mapId, key);
	return cache.computeIfAbsent(key, k -> s.get());
    }

    public static <K, V> V putAsPublic(Object mapId, K key, V value) {
	Map<K, V> cache = getMap(mapId, key);
	return cache.put(key, value);
    }

    @SuppressWarnings("unchecked")
    private static <K, V> Map<K, V> getMap(Class<?> caller, Object mapId, K key) {
	return (Map<K, V>) privateCache
	    .computeIfAbsent(caller, k -> new ConcurrentHashMap<Object, Map<?, ?>>())
	    .computeIfAbsent(mapId, k -> new ConcurrentHashMap<K, V>());
    }

    public static <K, V> V getAsPrivate(Class<?> caller, Object mapId, K key) {
	Map<K, V> small = getMap(caller, mapId, key);
	return small.get(key);
    }

    public static <K, V> V getAsPrivate(Class<?> caller, Object mapId, K key,
	Supplier<? extends V> s) {
	Map<K, V> small = getMap(caller, mapId, key);
	return small.computeIfAbsent(key, k -> s.get());
    }

    public static <K, V> V putAsPrivate(Class<?> caller, Object mapId, K key, V value) {
	Map<K, V> small = getMap(caller, mapId, key);
	return small.put(key, value);
    }

    public static <K, V> V removeAsPublic(Object mapId, K key) {
	Map<K, V> cache = getMap(mapId, key);
	return cache.remove(key);
    }

    public static <K, V> V removeAsPrivate(Class<?> caller, Object mapId, K key) {
	Map<K, V> small = getMap(caller, mapId, key);
	return small.remove(key);
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
