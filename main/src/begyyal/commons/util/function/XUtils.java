package begyyal.commons.util.function;

import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

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

    @SuppressWarnings("unchecked")
    public static <T> int compare(T v1, T v2) {
	if (v1 != null)
	    if (v2 == null)
		return -1;
	    else if (v1 instanceof Comparable)
		return ((Comparable<T>) v1).compareTo(v2);
	    else
		return Objects.hashCode(v1) - Objects.hashCode(v2);
	else
	    return v2 == null ? 0 : 1;
    }

    public static ThreadFactory createPlainThreadFactory(String threadPrefix) {
	return new ThreadFactory() {
	    private final AtomicInteger threadNumber = new AtomicInteger(1);

	    public Thread newThread(Runnable r) {
		return new Thread(r, threadPrefix + "-" + threadNumber.getAndIncrement());
	    }
	};
    }
}
