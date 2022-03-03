package begyyal.commons.object;

public class XBool {

    private boolean fresh = true;

    private boolean bool;

    private XBool(boolean b) {
        this.bool = b;
    }

    public static XBool newi() {
        return new XBool(false);
    }

    public static XBool of(boolean b) {
        return new XBool(b);
    }

    public boolean get() {
        return bool;
    }

    public void set(boolean b) {
        this.bool = b;
    }

    public boolean isFirstTouch() {
        boolean result = fresh;
        fresh = false;
        return result;
    }

    public void reverse() {
        bool = !bool;
    }

    public boolean getAndReverse() {
        boolean result = bool;
        bool = !bool;
        return result;
    }
}
