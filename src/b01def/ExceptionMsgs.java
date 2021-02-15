package b01def;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import a01util.a05function.CodeKiller;

/**
 * 例外クラスと説明表記を列挙したクラス。
 *
 * @author ikkei
 */
public enum ExceptionMsgs {

    NPE(
            NullPointerException.class,
            "ランタイムエラーが発生しました。"),
    ClassCast(
            ClassCastException.class,
            "ランタイムエラーが発生しました。"),
    IllegalArgument(
            IllegalArgumentException.class,
            "ランタイムエラーが発生しました。"),

    IOE(
            IOException.class,
            "データの入出力に失敗しました。"),
    NoSuchMethod(
            NoSuchMethodException.class,
            "メソッドの呼び出しに失敗しました。"),
    Interrupted(
            InterruptedException.class,
            "実行中のスレッドに割込が発生したため処理が中断されました。"),
    MalformedUrl(
            MalformedURLException.class,
            "URLの書式が不正です。"),
    Security(
            SecurityException.class,
            "セキュリティ・マネージャからセキュリティ違反が検出されました。");

    public final Class<? extends Exception> clazz;

    public final String msg;

    private ExceptionMsgs(Class<? extends Exception> clazz, String msg) {
        this.clazz = clazz;
        this.msg = msg;
    }

    /**
     * 対象の例外に対応する説明表記を返却する。<br>
     *
     * @return 対応する例外クラスが列挙されていなかった場合はNull
     */
    public static String resolve(Exception e) {

        if (e instanceof ExecutionException)
            return resolve(CodeKiller.cast(Exception.class, e.getCause()));

        return Arrays.stream(values())
                .filter(v -> v.clazz.isInstance(e))
                .sorted((a, b) -> a.clazz.isAssignableFrom(b.clazz) ? 1 : -1)
                .map(v -> v.msg)
                .findFirst().orElse(null);
    }
}
