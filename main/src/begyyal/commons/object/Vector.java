package begyyal.commons.object;

import begyyal.commons.util.function.XMath;

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

    public Vector[] decompose() {
	int gcd = Math.abs(XMath.gcd(this.x, this.y));
	if (gcd == 0)
	    return null;
	int xFactor = this.x / gcd;
	int yFactor = this.y / gcd;
	var result = new Vector[gcd];
	for (int i = 1; i <= gcd; i++)
	    result[i - 1] = new Vector(i * xFactor, i * yFactor);
	return result;
    }

    public boolean sameSlope(Vector v) {
	if (this.x == 0 || v.x == 0)
	    return this.x == v.x && XMath.simplify(y) == XMath.simplify(v.y);
	if (this.y == 0 || v.y == 0)
	    return this.y == v.y && XMath.simplify(x) == XMath.simplify(v.x);
	return this.x / this.y == v.x / v.y;
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
	return (31 + this.x) * 31 + this.y;
    }
}
