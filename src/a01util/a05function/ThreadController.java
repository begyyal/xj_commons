package a01util.a05function;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * スレッドに対する汎用操作をまとめたユーティリティ。
 *
 * @author ikkei
 */
public class ThreadController {

    /**
     * シンプルなスレッドファクトリを生成する。詳細は関連事項を参照のこと。
     *
     * @see Thread#Thread(Runnable, String)
     */
    public static ThreadFactory createPlainThreadFactory(String threadPrefix) {
        return new ThreadFactory() {

            private final AtomicInteger threadNumber = new AtomicInteger(1);

            public Thread newThread(Runnable r) {
                return new Thread(r, threadPrefix + "-" + threadNumber.getAndIncrement());
            }
        };
    }

    /**
     * 指定秒で{@link Thread#sleep(long)}を行う。<br>
     *
     * @return {@link InterruptedException 割込み}が発生した場合にfalse
     */
    public static boolean sleepSec(int sec) {
        return sleep(sec * 1000l);
    }

    /**
     * 指定ミリ秒で{@link Thread#sleep(long)}を行う。<br>
     *
     * @return {@link InterruptedException 割込み}が発生した場合にfalse
     */
    public static boolean sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }
}
