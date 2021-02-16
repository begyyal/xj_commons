package begyyal.commons.util.web.html.constant;

import org.apache.commons.lang3.StringUtils;

/**
 * Httpヘッダのプロパティ。
 *
 * @author ikkei
 */
public enum HttpHeader {

    Accept("Accept"),
    AcceptCharset("Accept-Charset"),
    AcceptEncoding("Accept-Encoding"),
    AcceptLanguage("Accept-Language"),
    Authorization("Authorization"),
    Cookie("Cookie"),
    Expect("Expect"),
    From("From"),
    Host("Host"),
    IfModifiedSince("If-Modified-Since"),
    IfMatch("IfMatch"),
    IfNoneMatch("If-None-Match"),
    IfRange("If-Range"),
    IfUnmodifiedSince("If-Unmodified-Since"),
    MaxForwards("Max-Forwards"),
    ProxyAuthorization("Proxy-Authorization"),
    Range("Range"),
    Referer("Referer"),
    TE("TE"),
    UserAgent("User-Agent"),

    AcceptRanges("Accept-Ranges"),
    Age("Age"),
    ETag("ETag"),
    Location("Location"),
    ProxyAuthenticate("Proxy-Authenticate"),
    RetryAfter("Retry-After"),
    Server("Server"),
    SetCookie("Set-Cookie"),
    Vary("Vary"),
    WWW_Authenticate("WWW-Authenticate"),

    Allow("Allow"),
    ContentEncoding("Content-Encoding"),
    ContentLanguage("Content-Language"),
    ContentLength("Content-Length"),
    ContentLocation("Content-Location"),
    ContentMD5("Content-MD5"),
    ContentRange("Content-Range"),
    ContentType("Content-Type"),
    Expires("Expires"),
    LastModified("Last-Modified"),

    CacheControl("Cache-Control"),
    Connection("Connection"),
    Date("Date"),
    Pragma("Pragma"),
    Trailer("Trailer"),
    TransferEncoding("Transfer-Encoding"),
    Upgrade("Upgrade"),
    Via("Via"),
    Warning("Warning");

    public final String str;

    private HttpHeader(String str) {
        this.str = str;
    }

    /**
     * 本インスタンスが対象文字列を表現したものかを判別する。<br>
     * 大文字/小文字の相違は許容されない。
     */
    public boolean symbolizes(String target) {
        return StringUtils.equals(target, str);
    }
}
