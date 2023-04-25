package begyyal.commons.object.collection;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import begyyal.commons.object.Pair;
import begyyal.commons.object.collection.XMap.XMapGen;

public class PairList<V1, V2> extends XList<Pair<V1, V2>> {

    private static final long serialVersionUID = 1L;

    protected PairList() {
	super();
    }

    protected PairList(Collection<? extends Pair<V1, V2>> c) {
	super(c);
    }

    protected PairList(XList<Pair<V1, V2>> c) {
	super(c);
    }

    protected PairList(int capa, Function<XList<Pair<V1, V2>>, Pair<V1, V2>> squeezeFunc) {
	super(capa, squeezeFunc);
    }

    public boolean add(V1 v1, V2 v2) {
	return add(Pair.of(v1, v2));
    }

    public void add(int index, V1 v1, V2 v2) {
	super.add(index, Pair.of(v1, v2));
    }

    public PairList<V1, V2> append(V1 v1, V2 v2) {
	add(Pair.of(v1, v2));
	return this;
    }

    public PairList<V1, V2> append(int index, V1 v1, V2 v2) {
	add(index, Pair.of(v1, v2));
	return this;
    }

    public Pair<V1, V2> set(int index, V1 v1, V2 v2) {
	return super.set(index, Pair.of(v1, v2));
    }

    public Pair<V1, V2> setV1(int index, V1 v1) {
	var newp = Pair.of(v1, get(index).v2);
	return super.set(index, newp);
    }

    public Pair<V1, V2> setV2(int index, V2 v2) {
	var newp = Pair.of(get(index).v1, v2);
	return super.set(index, newp);
    }

    public XList<V1> getV1List() {
	return stream().map(p -> p.v1).collect(XListGen.collect());
    }

    public XList<V2> getV2List() {
	return stream().map(p -> p.v2).collect(XListGen.collect());
    }

    public XList<V1> getV1RelatedBy(V2 v2) {
	return stream()
	    .filter(p -> Objects.equals(p.v2, v2))
	    .map(p -> p.v1)
	    .collect(XListGen.collect());
    }

    public XList<V2> getV2RelatedBy(V1 v1) {
	return stream()
	    .filter(p -> Objects.equals(p.v1, v1))
	    .map(p -> p.v2)
	    .collect(XListGen.collect());
    }

    public XMap<V1, V2> toMapWithApplyingFirstValue() {
	var map = XMapGen.<V1, V2>newi();
	this.forEach(p -> { // should not compute due to nullable of value
	    if (map.get(p.v1) == null)
		map.put(p.v1, p.v2);
	});
	return map;
    }

    public XMap<V1, XList<V2>> toMap() {
	var map = XMapGen.<V1, XList<V2>>newi();
	this.forEach(p -> map.compute(p.v1,
	    (k, v) -> v == null ? XListGen.of(p.v2) : v.append(p.v2)));
	return map;
    }

    public boolean contains(V1 a, V2 b) {
	return this.anyMatch(p -> Objects.equals(p.v1, a) && Objects.equals(p.v2, b));
    }

    /**
     * @see XList#createImmutableClone()
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

	@Deprecated
	public Pair<V1, V2> set(int index, Pair<V1, V2> v) {
	    throw new UnsupportedOperationException();
	}

	@Deprecated
	public Pair<V1, V2> remove(int index) {
	    throw new UnsupportedOperationException();
	}

	@Deprecated
	public boolean add(Pair<V1, V2> v) {
	    throw new UnsupportedOperationException();
	}

	@Deprecated
	public boolean addAll(Collection<? extends Pair<V1, V2>> v) {
	    throw new UnsupportedOperationException();
	}

	@Deprecated
	public boolean removeIf(Predicate<? super Pair<V1, V2>> filter) {
	    throw new UnsupportedOperationException();
	}

	@Deprecated
	public void sort(Comparator<? super Pair<V1, V2>> c) {
	    throw new UnsupportedOperationException();
	}

	@Deprecated
	public void updateThresholdSize(int size) {
	    throw new UnsupportedOperationException();
	}

	@Deprecated
	public void ensureCapacity(int minCapacity) {
	    throw new UnsupportedOperationException();
	}

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

	public static <V1, V2> PairList<V1, V2> newi(int thresholdSize) {
	    return new PairList<V1, V2>(thresholdSize, null);
	}

	public static <V1, V2> PairList<V1, V2> of(Collection<Pair<V1, V2>> c) {
	    return new PairList<V1, V2>(c);
	}

	public static <V1, V2> PairList<V1, V2> of(
	    int thresholdSize,
	    Function<XList<Pair<V1, V2>>, Pair<V1, V2>> squeezeFunc) {
	    return new PairList<V1, V2>(thresholdSize, squeezeFunc);
	}

	public static <V1, V2> Collector<Pair<V1, V2>, ?, PairList<V1, V2>> collect() {
	    return Collectors.toCollection(PairList::new);
	}
    }
}
