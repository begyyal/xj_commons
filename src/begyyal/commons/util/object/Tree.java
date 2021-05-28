package begyyal.commons.util.object;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.collect.Sets;

import begyyal.commons.util.object.SuperList.SuperListGen;

public class Tree<T> {

    private final T value;
    private final Tree<T> parent;
    private final Set<Tree<T>> children;

    private Tree(T v, Tree<T> parent, Set<Tree<T>> children) {
	Objects.requireNonNull(v);
	this.value = v;
	this.parent = parent;
	this.children = children;
    }

    public SuperList<Tree<T>> flat() {
	var results = SuperListGen.<Tree<T>>newi();
	recursive4flat(results, false);
	return results;
    }

    public SuperList<Tree<T>> collectTips() {
	var results = SuperListGen.<Tree<T>>newi();
	recursive4flat(results, true);
	return results;
    }

    private void recursive4flat(List<Tree<T>> results, boolean onlyTips) {
	if (!onlyTips || CollectionUtils.isEmpty(children))
	    results.add(this);
	for (var c : children)
	    c.recursive4flat(results, onlyTips);
    }

    public T getValue() {
	return value;
    }

    public Tree<T> getParent() {
	return this.parent;
    }

    public Set<Tree<T>> getChildren() {
	return Collections.unmodifiableSet(this.children);
    }

    public void compound(List<T> target) {

	if (CollectionUtils.isEmpty(target))
	    return;

	Tree<T> child = null;
	for (var c : children)
	    if (c.equals(target.get(0)))
		child = c;

	if (child == null) {
	    this.children.add(convert(target, this));
	} else if (target.size() > 1)
	    child.compound(target.subList(1, target.size()));
    }

    public void branch(T newv) {
	this.children.add(newi(newv, this));
    }

    public int getDepth() {
	return this.recursive4getDepth(0);
    }

    private int recursive4getDepth(int depth) {
	return this.parent == null ? depth : this.parent.recursive4getDepth(depth + 1);
    }

    public static <V> Tree<V> newi(V v, Tree<V> parent) {
	return new Tree<V>(v, parent, Sets.newHashSet());
    }

    public static <V> Tree<V> convert(List<V> listedV) {
	return convert(listedV, null);
    }

    public static <V> Tree<V> convert(List<V> listedV, Tree<V> parent) {

	if (CollectionUtils.isEmpty(listedV))
	    throw new IllegalArgumentException("The listed values must be not empty.");

	Tree<V> result = null;
	for (var v : listedV) {
	    var current = newi(v, parent);
	    if (parent != null)
		parent.children.add(current);
	    else
		result = current;
	    parent = current;
	}

	return result;
    }
}
