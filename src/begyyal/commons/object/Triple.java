package begyyal.commons.object;

import java.io.Serializable;
import java.util.Objects;

import begyyal.commons.constant.Strs;
import begyyal.commons.util.function.XUtils;

public class Triple<V1, V2, V3> implements Comparable<Triple<V1, V2, V3>>, Serializable {

    private static final long serialVersionUID = 1L;
    public final V1 v1;
    public final V2 v2;
    public final V3 v3;

    private Triple(V1 v1, V2 v2, V3 v3) {
	this.v1 = v1;
	this.v2 = v2;
	this.v3 = v3;
    }

    public static <V1, V2, V3> Triple<V1, V2, V3> of(V1 v1, V2 v2, V3 v3) {
	return new Triple<V1, V2, V3>(v1, v2, v3);
    }

    @Override
    public int compareTo(Triple<V1, V2, V3> o) {
	int result;
	if (o == null)
	    return -1;
	else if ((result = XUtils.compare(v1, o.v1)) == 0)
	    if ((result = XUtils.compare(v2, o.v2)) == 0)
		result = XUtils.compare(v3, o.v3);
	return result;
    }

    @Override
    public String toString() {
	return Strs.bracket1start + Objects.toString(v1) +
		Strs.comma + Objects.toString(v2) +
		Strs.comma + Objects.toString(v3) + Strs.bracket1end;
    }

    @Override
    public boolean equals(Object o) {
	if (!(o instanceof Triple))
	    return false;
	var casted = (Triple<?, ?, ?>) o;
	return Objects.equals(casted.v1, v1)
		&& Objects.equals(casted.v2, v2)
		&& Objects.equals(casted.v3, v3);
    }

    @Override
    public int hashCode() {
	return Objects.hash(v1, v2, v3);
    }
}
