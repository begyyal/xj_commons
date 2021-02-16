package begyyal.commons.util.object;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.collections4.CollectionUtils;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import begyyal.commons.util.object.PairList.PairListGen;
import begyyal.commons.util.object.TripleList.TripleListGen;

/**
 * 汎用操作および機能が拡張実装された{@link ArrayList}。 <br>
 * インスタンス生成は{@link SuperListGen}にて実行が可能。
 *
 * @author ikkei
 */
public class SuperList<V>
        extends
        ArrayList<V> {

    private static final long serialVersionUID = 1L;

    /**
     * 要素数上限。本変数へ値の指定がある場合、原則として追加操作後に上限を超過しないことが保証される。<br>
     * ここでの例外は、破壊的な継承実装や本機構がスレッドセーフでないことを指す。
     */
    protected int thresholdSize = -1;

    /**
     * 追加操作後に要素数が{@link #thresholdSize 上限}を超過した場合に、上限一杯まで要素を絞り込むための関数。<br>
     * 超過数だけ本関数に対して主体のリストをapplyし、得られた要素を削除することで絞り込みを行う。<br>
     * 1 - 本関数がnullかつ要素数が上限を満たしている場合、追加操作自体がスキップされる。<br>
     * 2 - 本関数がリストに存在しない要素を返却した場合、末端要素を対象とした削除にて処理が代替される。<br>
     */
    protected Function<SuperList<V>, V> squeezeFunc;

    /**
     * {@link java.util.Iterator}ライクな挙動を再現するための内部インデックス。初期値は -1。
     */
    protected int focus = -1;

    protected SuperList() {
        super();
    }

    protected SuperList(Collection<? extends V> c) {
        super(c);
    }

    protected SuperList(SuperList<V> c) {
        super(c);
        this.thresholdSize = c.thresholdSize;
        this.squeezeFunc = c.squeezeFunc;
        this.focus = c.focus;
    }

    protected SuperList(int capa, Function<SuperList<V>, V> squeezeFunc) {
        super(capa);
        this.thresholdSize = capa;
        this.squeezeFunc = squeezeFunc;
    }

    /**
     * 指定されたインデックスの要素を{@link ArrayList#get 取得}する。<br>
     * 1 - サイズを超過したインデックスを指定した場合、nullを返却する。<br>
     * 2 - 負数を指定した場合、末端要素のインデックスに引数を加算したインデックスから要素を{@link ArrayList#get 取得}する。
     */
    @Override
    public V get(int index) {
        return size() <= index || size() < -(index - 1) ? null
                : index < 0 ? super.get(size() - 1 + index) : super.get(index);
    }

    /**
     * 指定されたインデックスの要素を{@link ArrayList#get 削除}する。<br>
     * 1 - サイズを超過したインデックスを指定した場合、nullを返却する。<br>
     * 2 - 負数を指定した場合、末端要素のインデックスに引数を加算したインデックスから要素を{@link ArrayList#get 削除}する。
     */
    @Override
    public V remove(int index) {
        return size() <= index || size() < -(index - 1) ? null
                : index < 0 ? super.remove(size() - 1 + index) : super.remove(index);
    }

    /**
     * 末端へ指定された要素を{@link ArrayList#add(Object) 追加}する。
     *
     * @return 追加が成功した場合にtrue
     * @see #thresholdSize
     * @see #squeezeFunc
     */
    @Override
    public boolean add(V v) {

        if (thresholdSize == -1 || size() < thresholdSize)
            return super.add(v);
        else if (squeezeFunc == null)
            return false;

        boolean result = super.add(v);
        V squeeze = squeezeFunc.apply(this);
        remove(squeeze);

        if (thresholdSize < size()) {
            removeTip();
            return false;
        }

        return result && !Objects.equal(v, squeeze);
    }

    /**
     * 要素を{@link #add(Object) 追加}してリストを返却する。
     */
    public SuperList<V> append(V v) {
        this.add(v);
        return this;
    }

    /**
     * 指定したインデックスへ要素を{@link #add(Object) 追加}してリストを返却する。
     */
    public SuperList<V> append(int index, V v) {
        this.add(index, v);
        return this;
    }

    /**
     * 対象の要素全てを{@link ArrayList#addAll(Object) 追加}する。<br>
     * 以下を満たす場合、追加対象を上限内に収まるようにトリムした形で追加する。<br>
     * 1 - {@link #thresholdSize 要素数上限}が設定済み<br>
     * 2 - {@link #squeezeFunc 絞り込み関数}が未設定<br>
     * 3 - 処理前に要素数が上限に未到達であり、かつ追加処理によって超過する見込みがある<br>
     *
     * @return 追加が全て成功した場合にtrue
     * @see #squeezeFunc
     * @see #thresholdSize
     */
    @Override
    public boolean addAll(Collection<? extends V> v) {

        if (thresholdSize == -1 || size() + v.size() <= thresholdSize)
            return super.addAll(v);
        else if (squeezeFunc == null)
            return !isFull() && super.addAll(
                    (v instanceof List ? (List<? extends V>) v : Lists.newArrayList(v))
                            .subList(0, thresholdSize - size()));

        boolean result = super.addAll(v);
        List<V> squeeze = IntStream.range(0, size() - thresholdSize)
                .mapToObj(i -> squeezeFunc.apply(this))
                .collect(Collectors.toList());
        removeAll(squeeze);

        if (thresholdSize < size()) {
            subList(thresholdSize, size()).clear();
            return false;
        }

        return result && !CollectionUtils.containsAny(v, squeeze);
    }

    /**
     * 対象の要素全てを{@link #add(Object) 追加}する。
     *
     * @return 追加が全て成功した場合にtrue
     */
    @SuppressWarnings("unchecked")
    public boolean addAll(V... vArray) {
        boolean result = true;
        for (V v : vArray)
            result &= add(v);
        return result;
    }

    /**
     * 対象の要素全てを{@link #addAll(Collection) 追加}し、リストを返却する。
     */
    public SuperList<V> appendAll(Collection<? extends V> v) {
        addAll(v);
        return this;
    }

    /**
     * 対象の要素全てを{@link #addAll(Object...) 追加}し、リストを返却する。
     */
    @SuppressWarnings("unchecked")
    public SuperList<V> appendAll(V... vArray) {
        addAll(vArray);
        return this;
    }

    /**
     * 対象の要素全てを{@link #remove(Object) 削除}する。
     *
     * @return 指定された要素が全てリストに含まれていた場合はtrue
     */
    @SuppressWarnings("unchecked")
    public boolean removeAll(V... vArray) {
        boolean result = true;
        for (int i = 0; i < vArray.length; i++)
            if (!remove(vArray[i]))
                result = false;
        return result;
    }

    /**
     * @return 末端の要素
     */
    public V getTip() {
        return size() == 0 ? null : get(size() - 1);
    }

    /**
     * @return 削除された末端の要素か、リストが空の場合はnull
     */
    public V removeTip() {
        return size() == 0 ? null : remove(size() - 1);
    }

    /**
     * 末端インデックスの要素を指定された要素に置き換える。
     *
     * @return 以前設定されていた末端の要素
     */
    public V setTip(V v) {
        return set(size() == 0 ? 0 : size() - 1, v);
    }

    /**
     * リストを結合する。<br>
     * サイズは結合元の内で最も小さいものと同一となり、超過したインデックスの要素は切り捨てられる。
     */
    public <V2> PairList<V, V2> zip(List<V2> v2) {

        if (size() == 0 || CollectionUtils.isEmpty(v2))
            return PairListGen.empty();

        PairList<V, V2> result = PairListGen.newi();
        for (int count = 0; count < Math.min(size(), v2.size()); count++)
            result.add(get(count), v2.get(count));

        return result;
    }

    /**
     * リストを結合する。<br>
     * サイズは結合元の内で最も小さいものと同一となり、超過したインデックスの要素は切り捨てられる。
     */
    public <V2, V3> TripleList<V, V2, V3> zip(List<V2> v2, List<V3> v3) {

        if (size() == 0 || CollectionUtils.isEmpty(v2) || CollectionUtils.isEmpty(v3))
            return TripleListGen.empty();

        TripleList<V, V2, V3> result = TripleListGen.newi();
        for (int count = 0;
                count < Math.min(Math.min(size(), v2.size()), v3.size());
                count++)
            result.add(get(count), v2.get(count), v3.get(count));

        return result;
    }

    public boolean allMatch(Predicate<V> predicate) {
        return stream().allMatch(predicate);
    }

    public boolean anyMatch(Predicate<V> predicate) {
        return stream().anyMatch(predicate);
    }

    /**
     * 対象の要素が全て含まれているかを{@link #contains(Object) 判別}する。
     */
    @SuppressWarnings("unchecked")
    public boolean containsAll(V... values) {
        for (V v : values)
            if (!super.contains(v))
                return false;
        return true;
    }

    /**
     * {@link java.util.Iterator#hasNext()}を模したもの。<br>
     * カーソルに該当する変数として{@link #focus}を参照する。
     */
    public boolean hasNext() {
        return size() > focus + 1;
    }

    /**
     * {@link java.util.Iterator#next()}を模したもの。<br>
     * カーソルに該当する変数として{@link #focus}を参照する。
     */
    public V next() {
        return hasNext() ? get(++focus) : null;
    }

    /**
     * {@link #focus}が当たっている要素を削除する。<br>
     * 初期時はフォーカスが -1 のため一度{@link #next}を実施しないと本処理が有効化されないことに注意。
     */
    public void remove() {
        if (focus != -1)
            remove(focus);
    }

    /**
     * {@link #focus}が当たっている要素を削除し、実行時フォーカスの次のインデックス要素を取得する。<br>
     * ただし、削除処理に伴い実行時フォーカス以降のインデックスが1ずつ減算されるため、<br>
     * フォーカスのインクリメントを行わず実質的にフォーカスが次の要素へ移ることに注意。
     */
    public V removeAndNext() {
        remove();
        return focus != -1 && focus < size() ? get(focus) : null;
    }

    /**
     * {@link #focus}を -1 へ初期化する。
     */
    public void resetFocus() {
        focus = -1;
    }

    /**
     * @return {@link focus}
     */
    public int getFocusIndex() {
        return focus;
    }

    /**
     * リストのサイズが{@link #thresholdSize}で指定された要素数上限に達しているかを判別する。
     */
    public boolean isFull() {
        return thresholdSize == size();
    }

    /**
     * 要素数上限{@link #thresholdSize}を更新する。<br>
     * 更新後に上限超過が確認された場合は、併せて上限迄の絞り込みあるいは削除処理を施す。
     *
     * @see #squeezeFunc
     */
    public void updateThresholdSize(int size) {

        if (size < 0)
            return;
        thresholdSize = size;

        if (size < size() && squeezeFunc != null)
            removeAll(IntStream.range(0, size() - size)
                    .mapToObj(i -> squeezeFunc.apply(this))
                    .collect(Collectors.toList()));

        if (size < size())
            subList(size, size()).clear();
    }

    /**
     * 変更操作が不可能なコピーを返却する。(ビューではない)
     */
    public SuperList<V> createImmutableClone() {
        return new ImmutableSuperList<V>(this);
    }

    private static class ImmutableSuperList<V>
            extends
            SuperList<V> {

	private static final long serialVersionUID = 1L;

	private ImmutableSuperList(SuperList<V> base) {
            super(base);
        }

        public V set(int index, V v) {
            throw new UnsupportedOperationException();
        }

        public V remove(int index) {
            throw new UnsupportedOperationException();
        }

        public boolean add(V v) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends V> v) {
            throw new UnsupportedOperationException();
        }

        public boolean removeIf(Predicate<? super V> filter) {
            throw new UnsupportedOperationException();
        }

        public void sort(Comparator<? super V> c) {
            throw new UnsupportedOperationException();
        }

        public void updateThresholdSize(int size) {
            throw new UnsupportedOperationException();
        }

        public void ensureCapacity(int minCapacity) {
            throw new UnsupportedOperationException();
        }

        public void trimToSize() {
            throw new UnsupportedOperationException();
        }
    }

    public static class SuperListGen {

        @SuppressWarnings("rawtypes")
        private static final SuperList empty = of(Collections.emptyList());

        private SuperListGen() {
        }

        @SuppressWarnings("unchecked")
        public static final <V> SuperList<V> empty() {
            return (SuperList<V>) empty;
        }

        public static <V> SuperList<V> newi() {
            return new SuperList<V>();
        }

        public static <V> SuperList<V> of(Collection<? extends V> c) {
            return new SuperList<V>(c);
        }

        /**
         * 指定した数値を要素数の上限とし、<b>上限を超過する追加操作を内部的に行わない</b>リストを生成する。<br>
         *
         * @param {@link #thresholdSize}
         */
        public static <V> SuperList<V> of(int thresholdSize) {
            return new SuperList<V>(thresholdSize, null);
        }

        /**
         * 指定した数値を要素数の上限とし、<b>上限を超過しない追加操作を行う</b>リストを生成する。<br>
         * 諸追加操作での挙動は、一度追加操作を施した後に{@link #squeezeFunc}を用いて要素数を上限まで削減させるものとなる。
         *
         * @param {@link #thresholdSize}
         * @param {@link #squeezeFunc}
         */
        public static <V> SuperList<V>
                of(int thresholdSize, Function<SuperList<V>, V> squeezeFunc) {
            return new SuperList<V>(thresholdSize, squeezeFunc);
        }

        @SafeVarargs
        public static <V> SuperList<V> of(V... values) {
            return new SuperList<V>(Arrays.asList(values));
        }

        public static <T> Collector<T, ?, SuperList<T>> collect() {
            return Collectors.toCollection(SuperList::new);
        }
    }
}
