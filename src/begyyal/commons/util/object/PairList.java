package begyyal.commons.util.object;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import com.google.common.base.Objects;

import begyyal.commons.util.object.SuperMap.SuperMapGen;

/**
 * 要素を{@link Pair ペア}で保持する{@link SuperList}。<br>
 * インスタンス生成は{@link PairListGen}にて実行が可能。
 *
 * @author ikkei
 */
public class PairList<V1, V2>
	extends
	SuperList<Pair<V1, V2>> {

    private static final long serialVersionUID = 1L;

    protected PairList() {
	super();
    }

    protected PairList(Collection<? extends Pair<V1, V2>> c) {
	super(c);
    }

    protected PairList(SuperList<Pair<V1, V2>> c) {
	super(c);
    }

    protected PairList(int capa, Function<SuperList<Pair<V1, V2>>, Pair<V1, V2>> squeezeFunc) {
	super(capa, squeezeFunc);
    }

    /**
     * @see SuperList#add(Object)
     */
    public boolean add(V1 v1, V2 v2) {
	return add(Pair.of(v1, v2));
    }

    /**
     * @see SuperList#add(int, Object)
     */
    public void add(int index, V1 v1, V2 v2) {
	super.add(index, Pair.of(v1, v2));
    }

    /**
     * @see SuperList#append(Object)
     */
    public PairList<V1, V2> append(V1 v1, V2 v2) {
	add(Pair.of(v1, v2));
	return this;
    }

    /**
     * @see SuperList#append(int, Object)
     */
    public PairList<V1, V2> append(int index, V1 v1, V2 v2) {
	add(index, Pair.of(v1, v2));
	return this;
    }

    /**
     * @see ArrayList#set(int, Object)
     */
    public Pair<V1, V2> set(int index, V1 v1, V2 v2) {
	return super.set(index, Pair.of(v1, v2));
    }

    public Pair<V1, V2> setV1(int index, V1 v1) {
	var newp = Pair.of(v1, get(index).getRight());
	return super.set(index, newp);
    }

    public Pair<V1, V2> setV2(int index, V2 v2) {
	var newp = Pair.of(get(index).getLeft(), v2);
	return super.set(index, newp);
    }

    public SuperList<V1> getV1List() {
	return stream()
	    .map(p -> p.getLeft())
	    .collect(SuperListGen.collect());
    }

    public SuperList<V2> getV2List() {
	return stream()
	    .map(p -> p.getRight())
	    .collect(SuperListGen.collect());
    }

    public SuperList<V1> getV1RelatedBy(V2 v2) {
	return stream()
	    .filter(p -> Objects.equal(p.getRight(), v2))
	    .map(p -> p.getLeft())
	    .collect(SuperListGen.collect());
    }

    public SuperList<V2> getV2RelatedBy(V1 v1) {
	return stream()
	    .filter(p -> Objects.equal(p.getLeft(), v1))
	    .map(p -> p.getRight())
	    .collect(SuperListGen.collect());
    }

    public SuperMap<V1, SuperList<V2>> toMap() {
	var map = SuperMapGen.<V1, SuperList<V2>>newi();
	this.forEach(p -> map.compute(p.getKey(),
	    (k, v) -> v == null ? SuperListGen.of(p.getValue()) : v.append(p.getValue())));
	return map;
    }

    public boolean contains(V1 a, V2 b) {
	return this.anyMatch(p -> Objects.equal(p.getLeft(), a) && Objects.equal(p.getRight(), b));
    }

    /**
     * @see SuperList#createImmutableClone()
     */
    public PairList<V1, V2> createImmutableClone() {
	return new ImmutablePairList<V1, V2>(this);
    }

    public static class ImmutablePairList<V1, V2>
	    extends
	    PairList<V1, V2> {

	private static final long serialVersionUID = 1L;

	private ImmutablePairList(Collection<Pair<V1, V2>> c) {
	    super(c);
	}

	/**
	 * !!変更不可!!<br>
	 * 呼び出した場合に{@link UnsupportedOperationException}をスローする。
	 */
	@Deprecated
	public Pair<V1, V2> set(int index, Pair<V1, V2> v) {
	    throw new UnsupportedOperationException();
	}

	/**
	 * !!変更不可!!<br>
	 * 呼び出した場合に{@link UnsupportedOperationException}をスローする。
	 */
	@Deprecated
	public Pair<V1, V2> remove(int index) {
	    throw new UnsupportedOperationException();
	}

	/**
	 * !!変更不可!!<br>
	 * 呼び出した場合に{@link UnsupportedOperationException}をスローする。
	 */
	@Deprecated
	public boolean add(Pair<V1, V2> v) {
	    throw new UnsupportedOperationException();
	}

	/**
	 * !!変更不可!!<br>
	 * 呼び出した場合に{@link UnsupportedOperationException}をスローする。
	 */
	@Deprecated
	public boolean addAll(Collection<? extends Pair<V1, V2>> v) {
	    throw new UnsupportedOperationException();
	}

	/**
	 * !!変更不可!!<br>
	 * 呼び出した場合に{@link UnsupportedOperationException}をスローする。
	 */
	@Deprecated
	public boolean removeIf(Predicate<? super Pair<V1, V2>> filter) {
	    throw new UnsupportedOperationException();
	}

	/**
	 * !!変更不可!!<br>
	 * 呼び出した場合に{@link UnsupportedOperationException}をスローする。
	 */
	@Deprecated
	public void sort(Comparator<? super Pair<V1, V2>> c) {
	    throw new UnsupportedOperationException();
	}

	/**
	 * !!変更不可!!<br>
	 * 呼び出した場合に{@link UnsupportedOperationException}をスローする。
	 */
	@Deprecated
	public void updateThresholdSize(int size) {
	    throw new UnsupportedOperationException();
	}

	/**
	 * !!変更不可!!<br>
	 * 呼び出した場合に{@link UnsupportedOperationException}をスローする。
	 */
	@Deprecated
	public void ensureCapacity(int minCapacity) {
	    throw new UnsupportedOperationException();
	}

	/**
	 * !!変更不可!!<br>
	 * 呼び出した場合に{@link UnsupportedOperationException}をスローする。
	 */
	@Deprecated
	public void trimToSize() {
	    throw new UnsupportedOperationException();
	}
    }

    public static class PairListGen {

	@SuppressWarnings("rawtypes")
	private static final PairList empty = of(Collections.emptyList());

	private PairListGen() {
	}

	@SuppressWarnings("unchecked")
	public static <V1, V2> PairList<V1, V2> empty() {
	    return (PairList<V1, V2>) empty;
	}

	public static <V1, V2> PairList<V1, V2> newi() {
	    return new PairList<V1, V2>();
	}

	public static <V1, V2> PairList<V1, V2> of(Collection<Pair<V1, V2>> c) {
	    return new PairList<V1, V2>(c);
	}

	/**
	 * @see SuperListGen#of(int)
	 */
	public static <V1, V2> PairList<V1, V2> of(int thresholdSize) {
	    return new PairList<V1, V2>(thresholdSize, null);
	}

	/**
	 * @see SuperListGen#of(int, Function)
	 */
	public static <V1, V2> PairList<V1, V2> of(
	    int thresholdSize,
	    Function<SuperList<Pair<V1, V2>>, Pair<V1, V2>> squeezeFunc) {
	    return new PairList<V1, V2>(thresholdSize, squeezeFunc);
	}

	public static <V1, V2> Collector<Pair<V1, V2>, ?, PairList<V1, V2>> collect() {
	    return Collectors.toCollection(PairList::new);
	}
    }
}
