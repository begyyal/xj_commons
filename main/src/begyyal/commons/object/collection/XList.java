package begyyal.commons.object.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import begyyal.commons.object.collection.PairList.PairListGen;
import begyyal.commons.object.collection.TripleList.TripleListGen;

public class XList<V> extends ArrayList<V> {

    private static final long serialVersionUID = 1L;

    protected int thresholdSize = -1;
    protected Function<XList<V>, V> squeezeFunc;
    protected int focus = -1;

    protected XList() {
	super();
    }

    protected XList(Collection<? extends V> c) {
	super(c);
    }

    protected XList(XList<V> c) {
	super(c);
	this.thresholdSize = c.thresholdSize;
	this.squeezeFunc = c.squeezeFunc;
	this.focus = c.focus;
    }

    protected XList(int capa, Function<XList<V>, V> squeezeFunc) {
	super(capa);
	this.thresholdSize = capa;
	this.squeezeFunc = squeezeFunc;
    }

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
	    remove(v);
	    return false;
	}

	return result && !Objects.equals(v, squeeze);
    }

    public XList<V> append(V v) {
	this.add(v);
	return this;
    }

    public XList<V> append(int index, V v) {
	this.add(index, v);
	return this;
    }

    @Override
    public boolean addAll(Collection<? extends V> v) {

	if (v == null)
	    return false;
	else if (thresholdSize == -1 || size() + v.size() <= thresholdSize)
	    return super.addAll(v);
	else if (squeezeFunc == null)
	    return !isFull() && super.addAll(
		(v instanceof List ? (List<? extends V>) v : new ArrayList<V>(v))
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

	return result && !v.contains(squeeze);
    }

    @SuppressWarnings("unchecked")
    public boolean addAll(V... vArray) {
	boolean result = true;
	for (V v : vArray)
	    result &= add(v);
	return result;
    }

    public XList<V> appendAll(Collection<? extends V> v) {
	addAll(v);
	return this;
    }

    @SuppressWarnings("unchecked")
    public XList<V> appendAll(V... vArray) {
	addAll(vArray);
	return this;
    }

    @SuppressWarnings("unchecked")
    public boolean removeAll(V... vArray) {
	boolean result = true;
	for (int i = 0; i < vArray.length; i++)
	    if (!remove(vArray[i]))
		result = false;
	return result;
    }

    public V getTip() {
	return size() == 0 ? null : get(size() - 1);
    }

    public V removeTip() {
	return size() == 0 ? null : remove(size() - 1);
    }

    public V setTip(V v) {
	return set(size() == 0 ? 0 : size() - 1, v);
    }

    public <V2> PairList<V, V2> zip(List<V2> v2) {

	if (size() == 0 || v2 == null || v2.isEmpty())
	    return PairListGen.empty();

	PairList<V, V2> result = PairListGen.newi();
	int ul = Math.min(size(), v2.size());
	for (int count = 0; count < ul; count++)
	    result.add(get(count), v2.get(count));

	return result;
    }

    public <V2, V3> TripleList<V, V2, V3> zip(List<V2> v2, List<V3> v3) {

	if (size() == 0 || v2 == null || v2.isEmpty() || v3 == null || v3.isEmpty())
	    return TripleListGen.empty();

	TripleList<V, V2, V3> result = TripleListGen.newi();
	int ul = Math.min(Math.min(size(), v2.size()), v3.size());
	for (int count = 0; count < ul; count++)
	    result.add(get(count), v2.get(count), v3.get(count));

	return result;
    }

    public boolean allMatch(Predicate<V> predicate) {
	return stream().allMatch(predicate);
    }

    public boolean anyMatch(Predicate<V> predicate) {
	return stream().anyMatch(predicate);
    }

    @SuppressWarnings("unchecked")
    public boolean containsAll(V... values) {
	for (V v : values)
	    if (!super.contains(v))
		return false;
	return true;
    }

    public boolean hasNext() {
	return size() > focus + 1;
    }

    public V next() {
	return hasNext() ? get(++focus) : null;
    }

    public void remove() {
	if (focus != -1)
	    remove(focus);
    }

    public V removeAndNext() {
	remove();
	return focus != -1 && focus < size() ? get(focus) : null;
    }

    public void resetFocus() {
	focus = -1;
    }

    public void setFocusIndex(int focus) {
	if (focus < -1 || focus > size() - 1)
	    throw new IndexOutOfBoundsException("Focus index must be between -1 and size-1.");
	this.focus = focus;
    }

    public int getFocusIndex() {
	return focus;
    }

    public boolean isFull() {
	return thresholdSize == size();
    }

    public void updateThresholdSize(int size) {
	if (size < 0)
	    return;
	thresholdSize = size;
	if (size < size() && squeezeFunc != null)
	    for (int i = 0; i < size() - size; i++)
		remove(squeezeFunc.apply(this));
	if (size < size())
	    subList(size, size()).clear();
    }

    public int indexOf(Predicate<V> pred) {
	for (int i = 0; i < size(); i++)
	    if (pred.test(get(i)))
		return i;
	return -1;
    }

    public XList<V> reverse() {
	int size = size();
	for (int i = size - 1; i >= 0; i--)
	    add(get(i));
	removeRange(0, size);
	return this;
    }

    public XList<V> distinct() {
	int mod = 0, s = size();
	var array = this.toArray();
	for (int i = 0; i < s; i++)
	    if (indexOfRange(array, array[i], 0, i) >= 0)
		remove(i - mod++);
	return this;
    }

    private static int indexOfRange(Object[] array, Object o, int start, int end) {
	if (o == null) {
	    for (int i = start; i < end; i++)
		if (array[i] == null)
		    return i;
	} else
	    for (int i = start; i < end; i++)
		if (o.equals(array[i]))
		    return i;
	return -1;
    }

    public XList<V> createImmutableClone() {
	return new ImmutableXList<V>(this);
    }

    public XList<V> createPartialList(int from, int to) {
	rangeCheck(from, to, size());
	var l = new XList<V>();
	for (int i = from; i < to; i++)
	    l.add(get(i));
	return l;
    }

    private static void rangeCheck(int fromIndex, int toIndex, int size) {
	if (fromIndex < 0)
	    throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
	if (toIndex > size)
	    throw new IndexOutOfBoundsException("toIndex = " + toIndex);
	if (fromIndex > toIndex)
	    throw new IllegalArgumentException(
		"fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
    }

    public static class ImmutableXList<V> extends XList<V> {

	private static final long serialVersionUID = 1L;

	private ImmutableXList(Collection<? extends V> base) {
	    super(base);
	}

	@Deprecated
	public V set(int index, V v) {
	    throw new UnsupportedOperationException();
	}

	@Deprecated
	public V remove(int index) {
	    throw new UnsupportedOperationException();
	}

	@Deprecated
	public boolean add(V v) {
	    throw new UnsupportedOperationException();
	}

	@Deprecated
	public boolean addAll(Collection<? extends V> v) {
	    throw new UnsupportedOperationException();
	}

	@Deprecated
	public boolean removeIf(Predicate<? super V> filter) {
	    throw new UnsupportedOperationException();
	}

	@Deprecated
	public void sort(Comparator<? super V> c) {
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

    public static class XListGen {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static final ImmutableXList empty = new ImmutableXList(
	    Collections.emptyList());

	private XListGen() {
	}

	@SuppressWarnings("unchecked")
	public static final <V> ImmutableXList<V> empty() {
	    return (ImmutableXList<V>) empty;
	}

	public static <V> XList<V> newi() {
	    return new XList<V>();
	}

	public static <V> XList<V> newi(int thresholdSize) {
	    return new XList<V>(thresholdSize, null);
	}

	public static <V> XList<V> of(Collection<? extends V> c) {
	    return new XList<V>(c);
	}

	public static <V> XList<V>
	    of(int thresholdSize, Function<XList<V>, V> squeezeFunc) {
	    return new XList<V>(thresholdSize, squeezeFunc);
	}

	@SafeVarargs
	public static <V> XList<V> of(V... values) {
	    return new XList<V>(Arrays.asList(values));
	}

	@SafeVarargs
	public static <V> ImmutableXList<V> immutableOf(V... values) {
	    return new ImmutableXList<V>(Arrays.asList(values));
	}

	public static <V> ImmutableXList<V> immutableOf(Collection<? extends V> c) {
	    return new ImmutableXList<V>(c);
	}

	public static <T> Collector<T, ?, XList<T>> collect() {
	    return Collectors.toCollection(XList::new);
	}
    }
}
