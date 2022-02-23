package begyyal.commons.util.cache;

import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.Maps;

public class SimpleCacheResolver {

    // key型、KVマップ
    private static final Map<Class<?>, Map<?, ?>> //
    publicCache = Maps.newConcurrentMap();
    // 呼び出し元クラス、key型、KVマップ
    private static final Map<Class<?>, Map<Class<?>, Map<?, ?>>> //
    privateCache = Maps.newConcurrentMap();

    public static <K, V> V getAsPublic(K key, Supplier<? extends V> s) {
	@SuppressWarnings("unchecked")
	var cache = (Map<K, V>) publicCache.computeIfAbsent(
	    key.getClass(), k -> Maps.<K, V>newConcurrentMap());
	return cache.computeIfAbsent(key, k -> s.get());
    }

    public static <K, V> V getAsPrivate(K key, Supplier<? extends V> s) {
	return getAsPrivate(getCaller(), key, s);
    }

    public static <K, V> V getAsPrivate(Class<?> caller, K key, Supplier<? extends V> s) {
	@SuppressWarnings("unchecked")
	var small = (Map<K, V>) privateCache
	    .computeIfAbsent(caller, k -> Maps.newConcurrentMap())
	    .computeIfAbsent(key.getClass(), k -> Maps.<K, V>newConcurrentMap());
	return small.computeIfAbsent(key, k -> s.get());
    }

    public static void removeAsPublic(Object key) {
	var m = publicCache.get(key.getClass());
	if (m != null)
	    m.remove(key);
    }

    public static void removeAsPrivate(Object key) {
	removeAsPrivate(getCaller(), key);
    }

    public static void removeAsPrivate(Class<?> caller, Object key) {
	Map<?, Map<?, ?>> m = privateCache.get(caller);
	Map<?, ?> s = null;
	if (m != null && (s = m.get(key.getClass())) != null)
	    s.remove(key);
    }

    public static void clearAsPublic(Class<?> key) {
	publicCache.remove(key);
    }

    public static void clearAsPrivate(Class<?> key) {
	clearAsPrivate(getCaller(), key);
    }

    public static void clearAsPrivate(Class<?> caller, Class<?> key) {
	Map<?, Map<?, ?>> m = privateCache.get(caller);
	if (m != null && m.containsKey(key))
	    m.remove(key);
    }

    private static Class<?> getCaller() {
	Class<?> caller;
	try {
	    caller = Class.forName(Thread.currentThread().getStackTrace()[3].getClassName());
	} catch (ClassNotFoundException e) {
	    throw new RuntimeException(
		"Caller class is not found, it may need to input the class by overload method.");
	}
	return caller;
    }

    public static void clearAll() {
	publicCache.clear();
	privateCache.clear();
    }
}
