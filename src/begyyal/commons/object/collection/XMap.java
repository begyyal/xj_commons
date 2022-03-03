package begyyal.commons.object.collection;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class XMap<K, V> extends HashMap<K, V> {

    private static final long serialVersionUID = 1L;

    protected XMap() {
	super();
    }

    protected XMap(Map<K, V> m) {
	super(m);
    }

    public XMap<K, V> append(K k, V v) {
	super.put(k, v);
	return this;
    }

    public static class XMapGen {

	@SuppressWarnings("rawtypes")
	private static final XMap empty = of(Collections.emptyMap());

	private XMapGen() {
	}

	@SuppressWarnings("unchecked")
	public static final <K, V> XMap<K, V> empty() {
	    return (XMap<K, V>) empty;
	}

	public static <K, V> XMap<K, V> newi() {
	    return new XMap<K, V>();
	}

	public static <K, V> XMap<K, V> of(Map<K, V> m) {
	    return new XMap<K, V>(m);
	}

	/**
	 * {@link #collect(Function, Function, BinaryOperator) コレクターを生成}する。<br>
	 * 累積時にエントリが競合した場合、後勝ちとなる。
	 */
	public static <T, K, V>
	    Collector<T, ?, XMap<K, V>> collect(
		Function<? super T, ? extends K> keyMapper,
		Function<? super T, ? extends V> valueMapper) {
	    return collect(keyMapper, valueMapper, (o1, o2) -> o2);
	}

	/**
	 * {@link XMap}へ累積させる{@link Collector}を生成する。<br>
	 * <b>マッピング関数に基づく累積後の値にnullを許容する</b>。それ以外は関連事項を参照のこと。
	 *
	 * @see Collectors#toMap(Function, Function, BinaryOperator)
	 */
	public static <T, K, V>
	    Collector<T, ?, XMap<K, V>> collect(
		Function<? super T, ? extends K> keyMapper,
		Function<? super T, ? extends V> valueMapper,
		BinaryOperator<V> mergeFunction) {

	    BiConsumer<XMap<K, V>, T> accumulator = (map, element) -> {
		K k = keyMapper.apply(element);
		V v = valueMapper.apply(element);
		if (map.containsKey(k)) {
		    if (v != null)
			map.merge(k, v, mergeFunction);
		} else
		    map.put(k, v);
	    };

	    BinaryOperator<XMap<K, V>> combiner = (m1, m2) -> {
		for (Map.Entry<K, V> e : m2.entrySet())
		    m1.merge(e.getKey(), e.getValue(), mergeFunction);
		return m1;
	    };

	    return Collector.of(XMap::new, accumulator, combiner);
	}
    }
}
