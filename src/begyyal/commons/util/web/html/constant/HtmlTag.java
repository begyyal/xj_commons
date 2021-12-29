package begyyal.commons.util.web.html.constant;

/**
 * HTMLのタグを列挙したクラス。
 */
public enum HtmlTag {

    Html("html"),

    Head("head"),
    Title("title"),
    Base("base", true),
    Link("link", true),
    Meta("Meta", true),
    Style("style"),

    Body("body"),
    Article("article"),
    Section("section"),
    Nav("nav"),
    Aside("aside"),
    H1("h1"),
    H2("h2"),
    H3("h3"),
    H4("h4"),
    H5("h5"),
    H6("h6"),
    Hgroup("hgroup"),
    Header("header"),
    Footer("footer"),
    Address("address"),

    P("p"),
    Hr("hr", true),
    Pre("pre"),
    Blockquote("blockquote"),
    Ol("ol"),
    Ul("ul"),
    Menu("menu"),
    Li("li"),
    Dl("dl"),
    Dt("dt"),
    Dd("dd"),
    Figure("figure"),
    Figcaption("figcaption"),
    Main("main"),
    Div("div"),

    A("a"),
    Em("em"),
    Strong("strong"),
    Small("small"),
    S("small"),
    Cite("cite"),
    Q("q"),
    Dfn("dfn"),
    Abbr("abbr"),
    Ruby("ruby"),
    Rt("rt"),
    Rp("rp"),
    Data("data"),
    Time("time"),
    Code("code"),
    Var("var"),
    Samp("samp"),
    Kbd("kbd"),
    Sub("sub"),
    Sup("sup"),
    I("i"),
    B("b"),
    U("u"),
    Mark("mark"),
    Bdi("bdi"),
    Bdo("bdo"),
    Span("span"),
    Br("br", true),
    Wbr("wbr", true),

    Ins("ins"),
    Del("del"),

    Picture("picture"),
    Source("source", true),
    Img("img", true),
    Iframe("iframe"),
    Embed("embed", true),
    Object("object"),
    Param("param", true),
    Video("video"),
    Audio("audio"),
    Track("track"),
    Map("map"),
    Area("area"),

    Table("table"),
    Caption("caption"),
    Colgroup("colgroup"),
    Col("col", true),
    Tbody("tbody"),
    Thead("thead"),
    Tfoot("tfoot"),
    Tr("tr"),
    Td("td"),
    Th("th"),

    Form("form"),
    Label("label"),
    Input("input", true),
    Button("button"),
    Select("select"),
    Datalist("datalist"),
    Optgroup("optgroup"),
    Option("option"),
    Textarea("textarea"),
    Output("output"),
    Progress("progress"),
    Meter("meter"),
    Fieldset("fieldset"),
    Legend("legend"),

    Details("details"),
    Summary("summary"),
    Dialog("dialog"),

    Script("script"),
    Noscript("noscript"),
    Template("template"),
    Slot("slot"),
    Canvas("canvas");

    public final String str;

    public final boolean unneedEnclosure;

    private HtmlTag(String str) {
        this.str = str;
        this.unneedEnclosure = false;
    }

    private HtmlTag(String str, boolean unneedEnclosure) {
        this.str = str;
        this.unneedEnclosure = unneedEnclosure;
    }

    /**
     * 対象文字列に合致するタグのオブジェクトを取得する。<br>
     * 比較は{@link String#equalsIgnoreCase(String) 大文字/小文字を区別せず}に行う。
     */
    public static HtmlTag parse(String str) {
        for (HtmlTag v : values())
            if (v.str.equalsIgnoreCase(str))
                return v;
        return null;
    }
}
