package begyyal.commons.object.collection;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VarList extends XList<Object> {

    private static final long serialVersionUID = 1L;

    private VarList() {
        super();
    }

    private VarList(Collection<? extends Object> c) {
        super(c);
    }

    /**
     * String型の要素をまとめてリストとして返却する。
     */
    public XList<String> getStr() {
        return get(String.class);
    }

    /**
     * Integer型の要素をまとめてリストとして返却する。
     */
    public XList<Integer> getInt() {
        return get(Integer.class);
    }

    /**
     * Long型の要素をまとめてリストとして返却する。
     */
    public XList<Long> getLong() {
        return get(Long.class);
    }

    /**
     * 指定型の要素をまとめた{@link Stream}を返却する。
     */
    public <V> Stream<V> stream(Class<? extends V> clazz) {
        return stream()
                .filter(o -> clazz.isInstance(o))
                .map(o -> clazz.cast(o));
    }

    /**
     * 指定型の要素をまとめてリストとして返却する。
     */
    public <V> XList<V> get(Class<? extends V> clazz) {
        return stream(clazz).collect(XListGen.collect());
    }

    /**
     * 指定型の要素を全て{@link XList#remove(Object) 削除}する。
     */
    public boolean remove(Class<?> clazz) {
        return stream()
                .filter(o -> clazz.isInstance(o))
                .allMatch(o -> remove(o));
    }

    /**
     * 指定型に一致する要素の内で最小のインデックスの要素を返却する。
     *
     * @return 指定型に一致する要素が無い場合はnull
     */
    public <V> V getSingle(Class<? extends V> clazz) {
        return stream(clazz).findFirst().orElse(null);
    }

    public static class VarListGen {

        private VarListGen() {
        }

        public static VarList newi() {
            return new VarList();
        }

        public static VarList of(Collection<? extends Object> c) {
            return new VarList(c);
        }

        public static VarList of(Object... args) {
            return new VarList(Arrays.asList(args));
        }

        public static Collector<Object, ?, VarList> collect() {
            return Collectors.toCollection(VarList::new);
        }
    }
}