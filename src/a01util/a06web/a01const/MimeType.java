package a01util.a06web.a01const;

import org.apache.commons.lang3.StringUtils;
import a01util.a01object.SuperMap;
import a01util.a01object.SuperMap.SuperMapGen;
import b02const.Strs;

/**
 * MIMEタイプを列挙したクラス。<br>
 * ※暫定的にHTMLのみ対応
 *
 * @author ikkei
 */
public enum MimeType {

    HTML("text/html");

    private final String str;

    private MimeType(String str) {
        this.str = str;
    }

    /**
     * @return 対象文字列が、このMIMEが定義する文字列で開始されていた場合にtrue
     */
    public boolean match(String headerValue) {
        String v = StringUtils.trim(headerValue);
        return StringUtils.isNotBlank(v) && v.startsWith(str);
    }

    /**
     * 対象のContent-Type文字列からプロパティを抜粋する。
     */
    public static SuperMap<String, String> getContentTypeProperties(String contentType) {

        String v = StringUtils.remove(contentType, Strs.space);
        if (StringUtils.isBlank(v) || !v.contains(Strs.semiColon))
            return SuperMapGen.empty();

        SuperMap<String, String> properties = SuperMapGen.newi();
        v = v.substring(v.indexOf(Strs.semiColon) + 1);
        for (String p : v.split(Strs.semiColon)) {
            int boundary = StringUtils.indexOf(p, Strs.equal);
            if (boundary != -1)
                properties.put(p.substring(0, boundary), p.substring(boundary + 1));
        }

        return properties;
    }
}
