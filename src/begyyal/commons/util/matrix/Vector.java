package begyyal.commons.util.matrix;

import java.util.Objects;

// javaのバージョンを上げたらrecordにする
public class Vector {

    public final int x;
    public final int y;

    public Vector(int x, int y) {
	this.x = x;
	this.y = y;
    }

    public Vector reverse(boolean x, boolean y) {
	return new Vector(x ? -this.x : this.x, y ? -this.y : this.y);
    }

    public Vector compound(Vector v) {
	return new Vector(this.x + v.x, this.y + v.y);
    }

    @Override
    public boolean equals(Object o) {
	if (!(o instanceof Vector))
	    return false;
	var casted = (Vector) o;
	return casted.x == this.x && casted.y == this.y;
    }

    @Override
    public int hashCode() {
	return Objects.hash(this.x, this.y);
    }
}
