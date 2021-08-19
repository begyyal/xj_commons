package begyyal.commons.util.matrix;

// javaのバージョンを上げたらrecordにする
public class Vector {

    public final int x;
    public final int y;

    private Vector(int x, int y) {
	this.x = x;
	this.y = y;
    }

    public static Vector newi(int x, int y) {
	return new Vector(x, y);
    }

    public Vector reverse(boolean x, boolean y) {
	return new Vector(x ? -this.x : this.x, y ? -this.y : this.y);
    }

    @Override
    public boolean equals(Object o) {
	if (!(o instanceof Vector))
	    return false;
	var casted = (Vector) o;
	return casted.x == this.x && casted.y == this.y;
    }
}
