package begyyal.commons.util.object;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Triple;

import com.google.common.base.Objects;

/**
 * 要素を{@link Triple トリプル}で保持する{@link SuperList}。<br>
 * インスタンス生成は{@link TripleListGen}にて実行が可能。
 */
public class TripleList<V1, V2, V3>
	extends
	SuperList<Triple<V1, V2, V3>> {

    private static final long serialVersionUID = 1L;

    protected TripleList() {
	super();
    }

    protected TripleList(Collection<? extends Triple<V1, V2, V3>> c) {
	super(c);
    }

    protected TripleList(SuperList<Triple<V1, V2, V3>> c) {
	super(c);
    }

    private TripleList(
	int capa,
	Function<SuperList<Triple<V1, V2, V3>>, Triple<V1, V2, V3>> squeezeFunc) {
	super(capa, squeezeFunc);
    }

    /**
     * @see SuperList#add(Object)
     */
    public boolean add(V1 v1, V2 v2, V3 v3) {
	return add(Triple.of(v1, v2, v3));
    }

    /**
     * @see SuperList#add(int, Object)
     */
    public void add(int index, V1 v1, V2 v2, V3 v3) {
	add(index, Triple.of(v1, v2, v3));
    }

    /**
     * @see SuperList#append(Object)
     */
    public TripleList<V1, V2, V3> append(V1 v1, V2 v2, V3 v3) {
	add(Triple.of(v1, v2, v3));
	return this;
    }

    /**
     * @see SuperList#append(int, Object)
     */
    public TripleList<V1, V2, V3> append(int index, V1 v1, V2 v2, V3 v3) {
	add(index, Triple.of(v1, v2, v3));
	return this;
    }

    /**
     * @see SuperList#set(int, Object)
     */
    public Triple<V1, V2, V3> set(int index, V1 v1, V2 v2, V3 v3) {
	return super.set(index, Triple.of(v1, v2, v3));
    }

    public Triple<V1, V2, V3> setV1(int index, V1 v1) {
	var v = get(index);
	var newt = Triple.of(v1, v.getMiddle(), v.getRight());
	return super.set(index, newt);
    }

    public Triple<V1, V2, V3> setV2(int index, V2 v2) {
	var v = get(index);
	var newt = Triple.of(v.getLeft(), v2, v.getRight());
	return super.set(index, newt);
    }

    public Triple<V1, V2, V3> setV3(int index, V3 v3) {
	var v = get(index);
	var newt = Triple.of(v.getLeft(), v.getMiddle(), v3);
	return super.set(index, newt);
    }

    public SuperList<V1> getV1List() {
	return stream()
	    .map(p -> p.getLeft())
	    .collect(SuperListGen.collect());
    }

    public SuperList<V2> getV2List() {
	return stream()
	    .map(p -> p.getMiddle())
	    .collect(SuperListGen.collect());
    }

    public SuperList<V3> getV3List() {
	return stream()
	    .map(p -> p.getRight())
	    .collect(SuperListGen.collect());
    }

    public boolean contains(V1 a, V2 b, V3 c) {
	return this.anyMatch(t -> Objects.equal(t.getLeft(), a)
		&& Objects.equal(t.getMiddle(), b)
		&& Objects.equal(t.getRight(), c));
    }

    /**
     * @see SuperList#createImmutableClone()
     */
    public TripleList<V1, V2, V3> createImmutableClone() {
	return new ImmutableTripleList<V1, V2, V3>(this);
    }

    public static class ImmutableTripleList<V1, V2, V3>
	    extends
	    TripleList<V1, V2, V3> {

	private static final long serialVersionUID = 1L;

	private ImmutableTripleList(SuperList<Triple<V1, V2, V3>> c) {
	    super(c);
	}

	/**
	 * !!変更不可!!<br>
	 * 呼び出した場合に{@link UnsupportedOperationException}をスローする。
	 */
	@Deprecated
	public Triple<V1, V2, V3> set(int index, Triple<V1, V2, V3> v) {
	    throw new UnsupportedOperationException();
	}

	/**
	 * !!変更不可!!<br>
	 * 呼び出した場合に{@link UnsupportedOperationException}をスローする。
	 */
	@Deprecated
	public Triple<V1, V2, V3> remove(int index) {
	    throw new UnsupportedOperationException();
	}

	/**
	 * !!変更不可!!<br>
	 * 呼び出した場合に{@link UnsupportedOperationException}をスローする。
	 */
	@Deprecated
	public boolean add(Triple<V1, V2, V3> v) {
	    throw new UnsupportedOperationException();
	}

	/**
	 * !!変更不可!!<br>
	 * 呼び出した場合に{@link UnsupportedOperationException}をスローする。
	 */
	@Deprecated
	public boolean addAll(Collection<? extends Triple<V1, V2, V3>> v) {
	    throw new UnsupportedOperationException();
	}

	/**
	 * !!変更不可!!<br>
	 * 呼び出した場合に{@link UnsupportedOperationException}をスローする。
	 */
	@Deprecated
	public boolean removeIf(Predicate<? super Triple<V1, V2, V3>> filter) {
	    throw new UnsupportedOperationException();
	}

	/**
	 * !!変更不可!!<br>
	 * 呼び出した場合に{@link UnsupportedOperationException}をスローする。
	 */
	@Deprecated
	public void sort(Comparator<? super Triple<V1, V2, V3>> c) {
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

    public static class TripleListGen {

	@SuppressWarnings("rawtypes")
	private static final TripleList empty = of(Collections.emptyList());

	private TripleListGen() {
	}

	@SuppressWarnings("unchecked")
	public static <V1, V2, V3> TripleList<V1, V2, V3> empty() {
	    return (TripleList<V1, V2, V3>) empty;
	}

	public static <V1, V2, V3> TripleList<V1, V2, V3> newi() {
	    return new TripleList<V1, V2, V3>();
	}

	public static <V1, V2, V3> TripleList<V1, V2, V3>
	    of(Collection<Triple<V1, V2, V3>> c) {
	    return new TripleList<V1, V2, V3>(c);
	}

	/**
	 * @see SuperListGen#of(int)
	 */
	public static <V1, V2, V3> TripleList<V1, V2, V3> of(int capa) {
	    return new TripleList<V1, V2, V3>(capa, null);
	}

	/**
	 * @see SuperListGen#of(int, Function)
	 */
	public static <V1, V2, V3> TripleList<V1, V2, V3> of(
	    int capa,
	    Function<SuperList<Triple<V1, V2, V3>>, Triple<V1, V2, V3>> squeezeFunc) {
	    return new TripleList<V1, V2, V3>(capa, squeezeFunc);
	}

	public static <V1, V2, V3> Collector<Triple<V1, V2, V3>, ?, TripleList<V1, V2, V3>>
	    collect() {
	    return Collectors.toCollection(TripleList::new);
	}
    }
}
