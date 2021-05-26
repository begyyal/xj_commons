package begyyal.commons.util.object;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class Tree<T> {

    private final T value;
    private final Set<Tree<T>> children;

    private Tree(T v, Set<Tree<T>> children) {
	Objects.requireNonNull(v);
	this.value = v;
	this.children = children;
    }

    private List<Tree<T>> flat() {
	var results = Lists.<Tree<T>>newArrayList();
	recursiveForFlat(results);
	return results;
    }

    private void recursiveForFlat(List<Tree<T>> results) {
	results.add(this);
	for (var c : children)
	    c.recursiveForFlat(results);
    }

    public T getValue() {
	return value;
    }

    public void grafting(Tree<T> child) {
	this.children.add(child);
    }

    public void compound(List<T> target) {

	if (CollectionUtils.isEmpty(target))
	    return;

	Tree<T> child = null;
	for (var c : children)
	    if (c.equals(target.get(0)))
		child = c;

	if (child == null) {
	    this.grafting(newi(target));
	} else if (target.size() > 1)
	    child.compound(target.subList(1, target.size()));
    }

    public void branch(T newv) {
	this.children.add(newi(newv));
    }

    public void grow(T newv, Predicate<Tree<T>> p) {
	this.flat().stream().filter(p).forEach(t -> t.children.add(newi(newv)));
    }

    public static <V> Tree<V> newi(V v) {
	return new Tree<V>(v, Sets.newHashSet());
    }

    public static <V> Tree<V> newi(List<V> listedV) {

	if (CollectionUtils.isEmpty(listedV))
	    throw new IllegalArgumentException("The listed values must be not empty.");

	Tree<V> result = null, parent = null;
	for (var v : listedV) {
	    var current = newi(v);
	    if (parent != null)
		parent.grafting(current);
	    else
		result = current;
	    parent = current;
	}

	return result;
    }
}
