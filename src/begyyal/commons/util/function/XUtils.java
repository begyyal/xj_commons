package begyyal.commons.util.function;

public class XUtils {

    private XUtils() {
    }

    public static boolean sleep(long millis) {
	try {
	    Thread.sleep(millis);
	} catch (InterruptedException e) {
	    return false;
	}
	return true;
    }
}
