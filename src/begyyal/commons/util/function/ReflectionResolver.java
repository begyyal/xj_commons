package begyyal.commons.util.function;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import begyyal.commons.object.collection.XGen;
import begyyal.commons.object.collection.XList;
import begyyal.commons.object.collection.XList.XListGen;

public class ReflectionResolver {

    private ReflectionResolver() {
    }

    public static void doMethodThroughAll(
	Class<?> clazz,
	String methodName,
	Object instance,
	Object... args) {
	try {
	    doMethod(clazz, methodName, instance, args);
	} catch (Exception e) {
	}
    }

    public static <V> V doMethodThroughAll(
	Class<?> clazz,
	Class<V> rtnClazz,
	String methodName,
	Object instance,
	Object... args) {
	try {
	    return doMethod(clazz, rtnClazz, methodName, instance, args);
	} catch (Exception e) {
	    return null;
	}
    }

    public static void doMethod(
	Class<?> clazz,
	String methodName,
	Object instance,
	Object... args) throws Exception {
	doMethod(clazz, null, methodName, instance, args);
    }

    public static <V> V doMethod(
	Class<?> clazz,
	Class<V> rtnClazz,
	String methodName,
	Object instance,
	Object... args) throws Exception {

	V result = null;
	try {
	    Class<?>[] argsClazz = Arrays.stream(args)
		.map(o -> o.getClass())
		.collect(Collectors.toList())
		.toArray(new Class[] {});
	    result = doMethodCore(clazz, rtnClazz, methodName, instance, argsClazz, args);

	} catch (NoSuchMethodException e) {
	    // invokeで実行する際に求められる引数型の完全一致に対する特殊考慮
	    result = cast(rtnClazz, resolveArgsClass(clazz, rtnClazz, methodName, instance, args));

	} catch (Exception e) {
	    throw e;
	}

	return result;
    }

    private static Object resolveArgsClass(
	Class<?> clazz,
	Class<?> rtnClazz,
	String methodName,
	Object instance,
	Object[] args) throws Exception {

	List<Set<Class<?>>> argsClazzCandidates = //
		Arrays.stream(args)
		    .map(a -> getClassExpression(a.getClass()))
		    .collect(Collectors.toList());

	List<Iterator<Class<?>>> argsClazzIterators = //
		argsClazzCandidates.stream()
		    .map(set -> set.iterator())
		    .collect(Collectors.toList());

	Class<?>[] argsClazz = Arrays.stream(args)//
	    .map(o -> o.getClass())
	    .collect(Collectors.toList())
	    .toArray(new Class[] {});

	while (argsClazzIterators.stream().anyMatch(ite -> ite.hasNext())) {

	    boolean done = false;
	    int index = 0;
	    for (Iterator<Class<?>> ite : argsClazzIterators) {
		if (!done && ite.hasNext()) {
		    argsClazz[index] = ite.next();
		    IntStream.range(0, index).forEach(
			i -> argsClazzIterators.set(i, argsClazzCandidates.get(i).iterator()));
		    done = true;
		}
		index++;
	    }

	    try {
		return doMethodCore(clazz, rtnClazz, methodName, instance, argsClazz, args);
	    } catch (NoSuchMethodException e) {
	    } catch (Exception e) {
		throw e;
	    }
	}

	throw new NoSuchMethodException();
    }

    public static Set<Class<?>> getClassExpression(Class<?> clazz) {
	var set = XGen.<Class<?>>newHashSet();
	fillClassExpression(clazz, set);
	return set;
    }

    private static void fillClassExpression(Class<?> clazz, Set<Class<?>> classExpression) {
	classExpression.add(clazz);
	if (clazz.getSuperclass() != null)
	    fillClassExpression(clazz.getSuperclass(), classExpression);
	if (clazz.getInterfaces() != null)
	    Arrays.stream(clazz.getInterfaces())
		.forEach(i -> fillClassExpression(i, classExpression));
    }

    private static <V> V doMethodCore(
	Class<?> clazz,
	Class<V> rtnClazz,
	String methodName,
	Object instance,
	Class<?>[] argsClazz,
	Object[] args) throws Exception {
	return cast(rtnClazz,
	    clazz.getMethod(methodName, argsClazz).invoke(instance, args));
    }

    public static <V> V cast(Class<V> clazz, Object o) {
	return o != null && clazz != null && clazz.isInstance(o) ? clazz.cast(o) : null;
    }

    public static <V> XList<V> cast(Class<V> clazz, List<?> col) {
	return col != null && !col.isEmpty() && clazz != null
		? col.stream()
		    .filter(f -> clazz.isInstance(f))
		    .map(f -> clazz.cast(f))
		    .collect(XListGen.collect())
		: XListGen.empty();
    }

    public static <V> Stream<V> cast(Class<V> clazz, Stream<?> s) {
	return s != null && clazz != null
		? s.filter(f -> clazz.isInstance(f))
		    .map(f -> clazz.cast(f))
		: Stream.empty();
    }
}