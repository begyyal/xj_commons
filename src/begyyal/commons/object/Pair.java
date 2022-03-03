package begyyal.commons.object;

import java.io.Serializable;
import java.util.Objects;

import begyyal.commons.constant.Strs;
import begyyal.commons.util.function.XUtils;

public class Pair<V1, V2> implements Comparable<Pair<V1, V2>>, Serializable {

    private static final long serialVersionUID = 1L;
    public final V1 v1;
    public final V2 v2;

    private Pair(V1 v1, V2 v2) {
	this.v1 = v1;
	this.v2 = v2;
    }

    public static <V1, V2> Pair<V1, V2> of(V1 v1, V2 v2) {
	return new Pair<V1, V2>(v1, v2);
    }

    @Override
    public int compareTo(Pair<V1, V2> o) {
	int result;
	if (o == null)
	    return -1;
	else if ((result = XUtils.compare(v1, o.v1)) == 0)
	    result = XUtils.compare(v2, o.v2);
	return result;
    }

    @Override
    public String toString() {
	return Strs.bracket1start + Objects.toString(v1) +
		Strs.comma + Objects.toString(v2) + Strs.bracket1end;
    }

    @Override
    public boolean equals(Object o) {
	if (!(o instanceof Pair))
	    return false;
	var casted = (Pair<?, ?>) o;
	return Objects.equals(casted.v1, v1) && Objects.equals(casted.v2, v2);
    }

    @Override
    public int hashCode() {
	return Objects.hash(v1, v2);
    }
}
