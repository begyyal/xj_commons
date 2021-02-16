package begyyal.commons.util.web.html.constant;

/**
 * 文書型を列挙したクラス。
 *
 * @author ikkei
 */
public enum HtmlDocType {

    V5,
    V4_1_Strict(
            "-//W3C//DTD HTML 4.01//EN",
            "http://www.w3.org/TR/html4/strict.dtd"),
    V4_1_Transitional(
            "-//W3C//DTD HTML 4.01 Transitional//EN",
            "http://www.w3.org/TR/html4/loose.dtd"),
    V4_1_Frameset(
            "-//W3C//DTD HTML 4.01 Frameset//EN",
            "http://www.w3.org/TR/html4/frameset.dtd"),
    X1_Strict(
            "-//W3C//DTD XHTML 1.0 Strict//EN",
            "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"),
    X1_Transitional(
            "-//W3C//DTD XHTML 1.0 Transitional//EN",
            "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"),
    X1_Frameset(
            "-//W3C//DTD XHTML 1.0 Frameset//EN",
            "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd"),
    V3_2("-//W3C//DTD HTML 3.2 Final//EN"),
    C1("-//W3C//DTD Compact HTML 1.0 Draft//EN"),

    None();

    public final String publicIdentifier;

    public final String systemIdentifier;

    private HtmlDocType(String publicIdentifier, String systemIdentifier) {
        this.publicIdentifier = publicIdentifier;
        this.systemIdentifier = systemIdentifier;
    }

    private HtmlDocType(String publicIdentifier) {
        this(publicIdentifier, null);
    }

    private HtmlDocType() {
        this(null, null);
    }

    /**
     * 指定した文字列と{@link String#equals(Object)}にて公開識別子が一致する文書型を返却する。
     *
     * @return 列挙定数のいずれとも一致しなかった場合はnull
     */
    public static HtmlDocType parseByPublicIdentifier(String str) {
        for (HtmlDocType type : values())
            if (type.needPublicIdentifier() && type.publicIdentifier.equals(str))
                return type;
        return null;
    }

    /**
     * 本インスタンスが表現する文書型を記す上で公開識別子が求められるかを判別する。
     */
    public boolean needPublicIdentifier() {
        return publicIdentifier != null;
    }

    /**
     * 本インスタンスが表現する文書型を記す上でシステム識別子が求められるかを判別する。<br>
     * ※現状、巷では慣例的なシステム識別子の省略が散見されており、諸ブラウザの実装も省略を許容していることに注意
     */
    public boolean needSystemIdentifier() {
        return systemIdentifier != null;
    }
}
