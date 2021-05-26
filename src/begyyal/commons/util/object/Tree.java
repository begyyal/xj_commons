package begyyal.commons.util.object;

import java.util.Set;
import java.util.function.Predicate;

import com.google.common.collect.Sets;

import begyyal.commons.util.object.SuperList.SuperListGen;

public class Tree<T> {

    private final T value;
    private final Set<Tree<T>> children;

    private Tree(T v, Set<Tree<T>> children) {
	this.value = v;
	this.children = children;
    }

    private SuperList<Tree<T>> flat() {
	var results = SuperListGen.<Tree<T>>newi();
	recursiveForFlat(results);
	return results;
    }

    private void recursiveForFlat(SuperList<Tree<T>> results) {
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

    public void grow(T newv, Predicate<Tree<T>> p) {
	this.flat().stream().filter(p).forEach(t -> t.children.add(newi(newv)));
    }

    public static <V> Tree<V> newi(V v) {
	return new Tree<V>(v, Sets.newHashSet());
    }
}
