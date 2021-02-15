package a01util.a05function;

import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.function.Consumer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Stringに対する汎用操作をまとめたユーティリティ。
 *
 * @author ikkei
 */
public class SuperStrings {

    /**
     * 第1引数を主体として、第2引数以降それぞれに対して{@link String#indexOf(String)}を行い、<br>
     * 最初に出現した文字列とそのインデックスのペアを返却する。
     *
     * @return 第1引数にていずれの文字も出現しなかった場合にnull
     */
    public static Pair<String, Integer> firstIndexOf(String str, String... targets) {
        return Arrays.stream(targets)
                .filter(t -> str.indexOf(t) != -1)
                .map(t -> Pair.of(t, str.indexOf(t)))
                .sorted(Comparator.comparing(p -> p.getValue()))
                .findFirst().orElse(null);
    }

    /**
     * 対象の文字列を改行/空白/タブ等(\t|\n|\r|\f)で分割したトークンに対して対象のオペレーションを施す。
     *
     * @see StringTokenizer#StringTokenizer(String)
     */
    public static void applyEachToken(String str, Consumer<String> cons) {
        applyEachToken(str, cons, '\t', '\n', '\r', '\f');
    }

    /**
     * 対象の文字列を第3引数以降の文字で分割したトークンに対して対象のオペレーションを施す。
     *
     * @see StringTokenizer#StringTokenizer(String, String)
     */
    public static void applyEachToken(String str, Consumer<String> cons, char... cs) {
        if (StringUtils.containsAny(str, cs)) {
            StringTokenizer st = new StringTokenizer(str, new String(cs));
            while (st.hasMoreTokens())
                cons.accept(st.nextToken());
        } else
            cons.accept(str);
    }
}
