package begyyal.commons.util.object;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 汎用操作および機能が拡張実装された{@link HashMap}。<br>
 * インスタンス生成は{@link SuperMapGen}にて実行が可能。
 */
public class SuperMap<K, V>
        extends
        HashMap<K, V> {

    private static final long serialVersionUID = 1L;

    protected SuperMap() {
        super();
    }

    protected SuperMap(Map<K, V> m) {
        super(m);
    }

    public SuperMap<K, V> append(K k, V v) {
        super.put(k, v);
        return this;
    }

    public static class SuperMapGen {

        @SuppressWarnings("rawtypes")
        private static final SuperMap empty = of(Collections.emptyMap());

        private SuperMapGen() {
        }

        @SuppressWarnings("unchecked")
        public static final <K, V> SuperMap<K, V> empty() {
            return (SuperMap<K, V>) empty;
        }

        public static <K, V> SuperMap<K, V> newi() {
            return new SuperMap<K, V>();
        }

        public static <K, V> SuperMap<K, V> of(Map<K, V> m) {
            return new SuperMap<K, V>(m);
        }

        /**
         * {@link #collect(Function, Function, BinaryOperator) コレクターを生成}する。<br>
         * 累積時にエントリが競合した場合、後勝ちとなる。
         */
        public static <T, K, V>
                Collector<T, ?, SuperMap<K, V>> collect(
                        Function<? super T, ? extends K> keyMapper,
                        Function<? super T, ? extends V> valueMapper) {
            return collect(keyMapper, valueMapper, (o1, o2) -> o2);
        }

        /**
         * {@link SuperMap}へ累積させる{@link Collector}を生成する。<br>
         * <b>マッピング関数に基づく累積後の値にnullを許容する</b>。それ以外は関連事項を参照のこと。
         *
         * @see Collectors#toMap(Function, Function, BinaryOperator)
         */
        public static <T, K, V>
                Collector<T, ?, SuperMap<K, V>> collect(
                        Function<? super T, ? extends K> keyMapper,
                        Function<? super T, ? extends V> valueMapper,
                        BinaryOperator<V> mergeFunction) {

            BiConsumer<SuperMap<K, V>, T> accumulator = (map, element) -> {
                K k = keyMapper.apply(element);
                V v = valueMapper.apply(element);
                if (map.containsKey(k)) {
                    if (v != null)
                        map.merge(k, v, mergeFunction);
                } else
                    map.put(k, v);
            };

            BinaryOperator<SuperMap<K, V>> combiner = (m1, m2) -> {
                for (Map.Entry<K, V> e : m2.entrySet())
                    m1.merge(e.getKey(), e.getValue(), mergeFunction);
                return m1;
            };

            return Collector.of(SuperMap::new, accumulator, combiner);
        }
    }
}
