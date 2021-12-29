package begyyal.commons.util.object;

/**
 * 汎用操作を追加した参照型のブーリアン。
 */
public class SuperBool {

    private boolean fresh = true;

    private boolean bool;

    private SuperBool(boolean b) {
        this.bool = b;
    }

    public static SuperBool newi() {
        return new SuperBool(false);
    }

    public static SuperBool of(boolean b) {
        return new SuperBool(b);
    }

    public boolean get() {
        return bool;
    }

    public void set(boolean b) {
        this.bool = b;
    }

    /**
     * 本インスタンスにおいて初めて本メソッドが呼び出された場合にのみtrueを返却する。
     */
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
