package begyyal.commons.object.collection;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import begyyal.commons.object.Triple;

public class TripleList<V1, V2, V3> extends XList<Triple<V1, V2, V3>> {

    private static final long serialVersionUID = 1L;

    protected TripleList() {
	super();
    }

    protected TripleList(Collection<? extends Triple<V1, V2, V3>> c) {
	super(c);
    }

    protected TripleList(XList<Triple<V1, V2, V3>> c) {
	super(c);
    }

    private TripleList(
	int capa,
	Function<XList<Triple<V1, V2, V3>>, Triple<V1, V2, V3>> squeezeFunc) {
	super(capa, squeezeFunc);
    }

    public boolean add(V1 v1, V2 v2, V3 v3) {
	return add(Triple.of(v1, v2, v3));
    }

    public void add(int index, V1 v1, V2 v2, V3 v3) {
	add(index, Triple.of(v1, v2, v3));
    }

    public TripleList<V1, V2, V3> append(V1 v1, V2 v2, V3 v3) {
	add(Triple.of(v1, v2, v3));
	return this;
    }

    public TripleList<V1, V2, V3> append(int index, V1 v1, V2 v2, V3 v3) {
	add(index, Triple.of(v1, v2, v3));
	return this;
    }

    public Triple<V1, V2, V3> set(int index, V1 v1, V2 v2, V3 v3) {
	return super.set(index, Triple.of(v1, v2, v3));
    }

    public Triple<V1, V2, V3> setV1(int index, V1 v1) {
	var v = get(index);
	var newt = Triple.of(v1, v.v2, v.v3);
	return super.set(index, newt);
    }

    public Triple<V1, V2, V3> setV2(int index, V2 v2) {
	var v = get(index);
	var newt = Triple.of(v.v1, v2, v.v3);
	return super.set(index, newt);
    }

    public Triple<V1, V2, V3> setV3(int index, V3 v3) {
	var v = get(index);
	var newt = Triple.of(v.v1, v.v2, v3);
	return super.set(index, newt);
    }

    public XList<V1> getV1List() {
	return stream().map(p -> p.v1).collect(XListGen.collect());
    }

    public XList<V2> getV2List() {
	return stream().map(p -> p.v2).collect(XListGen.collect());
    }

    public XList<V3> getV3List() {
	return stream().map(p -> p.v3).collect(XListGen.collect());
    }

    public boolean contains(V1 a, V2 b, V3 c) {
	return this.anyMatch(t -> Objects.equals(t.v1, a)
		&& Objects.equals(t.v2, b)
		&& Objects.equals(t.v3, c));
    }

    public TripleList<V1, V2, V3> createImmutableClone() {
	return new ImmutableTripleList<V1, V2, V3>(this);
    }

    public static class ImmutableTripleList<V1, V2, V3>
	    extends
	    TripleList<V1, V2, V3> {

	private static final long serialVersionUID = 1L;

	private ImmutableTripleList(XList<Triple<V1, V2, V3>> c) {
	    super(c);
	}

	@Deprecated
	public Triple<V1, V2, V3> set(int index, Triple<V1, V2, V3> v) {
	    throw new UnsupportedOperationException();
	}

	@Deprecated
	public Triple<V1, V2, V3> remove(int index) {
	    throw new UnsupportedOperationException();
	}

	@Deprecated
	public boolean add(Triple<V1, V2, V3> v) {
	    throw new UnsupportedOperationException();
	}

	@Deprecated
	public boolean addAll(Collection<? extends Triple<V1, V2, V3>> v) {
	    throw new UnsupportedOperationException();
	}

	@Deprecated
	public boolean removeIf(Predicate<? super Triple<V1, V2, V3>> filter) {
	    throw new UnsupportedOperationException();
	}

	@Deprecated
	public void sort(Comparator<? super Triple<V1, V2, V3>> c) {
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

	public static <V1, V2, V3> TripleList<V1, V2, V3> newi(int capa) {
	    return new TripleList<V1, V2, V3>(capa, null);
	}
	
	public static <V1, V2, V3> TripleList<V1, V2, V3>
	    of(Collection<Triple<V1, V2, V3>> c) {
	    return new TripleList<V1, V2, V3>(c);
	}

	public static <V1, V2, V3> TripleList<V1, V2, V3> of(
	    int capa,
	    Function<XList<Triple<V1, V2, V3>>, Triple<V1, V2, V3>> squeezeFunc) {
	    return new TripleList<V1, V2, V3>(capa, squeezeFunc);
	}

	public static <V1, V2, V3> Collector<Triple<V1, V2, V3>, ?, TripleList<V1, V2, V3>>
	    collect() {
	    return Collectors.toCollection(TripleList::new);
	}
    }
}
