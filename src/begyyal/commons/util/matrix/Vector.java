package begyyal.commons.util.matrix;

@SuppressWarnings("preview")
public record Vector(int x, int y) {
    public Vector reverse(boolean x, boolean y) {
	return new Vector(x ? -this.x : this.x, y ? -this.y : this.y);
    }
}
