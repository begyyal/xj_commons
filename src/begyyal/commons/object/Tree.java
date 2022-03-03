package begyyal.commons.object;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import begyyal.commons.object.collection.XGen;
import begyyal.commons.object.collection.XList;
import begyyal.commons.object.collection.XList.XListGen;

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

    public XList<Tree<T>> flat() {
	var results = XListGen.<Tree<T>>newi();
	recursive4flat(results, false);
	return results;
    }

    public XList<Tree<T>> collectTips() {
	var results = XListGen.<Tree<T>>newi();
	recursive4flat(results, true);
	return results;
    }

    public XList<Tree<T>> traceRoots() {
	var results = XListGen.<Tree<T>>newi();
	recursive4roots(results);
	return results;
    }

    private void recursive4flat(List<Tree<T>> results, boolean onlyTips) {
	if (!onlyTips || children == null || children.isEmpty())
	    results.add(this);
	for (var c : children)
	    c.recursive4flat(results, onlyTips);
    }

    private void recursive4roots(List<Tree<T>> results) {
	results.add(0, this);
	if (parent != null)
	    parent.recursive4roots(results);
    }

    public T getValue() {
	return value;
    }

    public Tree<T> getParent() {
	return this.parent;
    }

    public void addChild(T child) {
	this.children.add(newi(child, this));
    }

    public Set<Tree<T>> getChildren() {
	return Collections.unmodifiableSet(this.children);
    }

    public void compound(List<T> target) {

	if (target == null || target.isEmpty())
	    return;

	Tree<T> child = null;
	for (var c : children)
	    if (c.getValue().equals(target.get(0)))
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
	return new Tree<V>(v, parent, XGen.newHashSet());
    }

    public static <V> Tree<V> convert(List<V> listedV) {
	return convert(listedV, null);
    }

    public static <V> Tree<V> convert(List<V> listedV, Tree<V> parent) {

	if (listedV == null || listedV.isEmpty())
	    throw new IllegalArgumentException("The listed values must not be empty.");

	Tree<V> result = null;
	for (var v : listedV) {
	    var current = newi(v, parent);
	    if (parent != null)
		parent.children.add(current);
	    if (result == null)
		result = current;
	    parent = current;
	}

	return result;
    }
}
