package a01util.a06web.a02html;

import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import a01util.a01object.SuperList;
import a01util.a01object.SuperList.SuperListGen;
import a01util.a01object.SuperMap;
import a01util.a01object.SuperMap.SuperMapGen;
import a01util.a05function.SuperStrings;
import a01util.a06web.a02html.a01const.HtmlDocType;
import a01util.a06web.a02html.a01const.HtmlTag;
import a01util.a06web.a02html.a02object.HtmlObject;
import a01util.a06web.a02html.a02object.HtmlObject.RootHtmlObject;
import b02const.Strs;

/**
 * 文字列リソースと{@link HtmlObject}間での変換を行う。<br>
 * 構造検証の細部においては書式毎のdtdやw3cおよびwhatwgが掲げる規格へ準拠するものではない。<br>
 * ※現状タグ省略に未対応のため、とりわけそうした記述がある場合にそのノード以下の構成で変換失敗となりやすい。
 *
 * @author ikkei
 */
public class HtmlParser {

    private static final String tagEnclosurePrefix = "</";

    private static final String xmlDeclarationPrefix = "<?xml";

    private static final String xmlDeclarationSuffix = "?>";

    private static final String docTypePrefix = "<!doctype";

    private static final String docTypeElement1 = "html";

    private static final String docTypeElement2 = "PUBLIC";

    private static final String commentPrefix = "<!--";

    private static final String commentSuffix = "-->";

    private static final String commentSuffixForScript = "//-->";

    /**
     * 対象の文字列リソースを{@link HtmlObject}へ変換する。<br>
     * Nullセーフである。(失敗した場合、内部ステータスに対して印付けしたオブジェクトを返却する)
     */
    public static RootHtmlObject process(SuperList<String> resource) {
        return CollectionUtils.isEmpty(resource)
                ? createFailedRoot()
                : new Generator(resource.stream()
                        .filter(s -> s != null)
                        .collect(SuperListGen.collect()))
                                .process();
    }

    /**
     * 指定された{@link HtmlObject}を文字列リソースへ変換する。<br>
     * 配下にて変換に失敗したノードを含んでいる場合は、そのノードを除いた正常なノード全てに対して変換を施す。
     */
    public static SuperList<String> process(HtmlObject o) {
        return new Decoder().process(o);
    }

    private static RootHtmlObject createFailedRoot() {
        RootHtmlObject o = HtmlObject.newRoot(null);
        o.markFailure();
        return o;
    }

    private static class Generator {

        private SuperList<String> resource;

        private HtmlObject focusObj;

        private String focusLine;

        private Generator(SuperList<String> resource) {
            this.resource = resource;
        }

        private RootHtmlObject process() {

            RootHtmlObject o = null;
            try {

                focusLine = resource.next().trim();
                if (Strs.empty.equals(focusLine))
                    removeAndNext();

                skipXmlTagIfNeeded();
                if ((focusObj = o = distinctDocType()) != null)
                    while (recursiveExtraction() != null);

            } catch (UnfinishedStatement e) {
            }

            return o == null ? createFailedRoot() : o;
        }

        private void skipXmlTagIfNeeded() throws UnfinishedStatement {

            if (!focusLine.startsWith(xmlDeclarationPrefix))
                return;
            substringAndNext(5);

            int brk;
            while ((brk = focusLine.indexOf(xmlDeclarationSuffix)) == -1)
                removeAndNext();
            substringAndNext(brk + 2);
        }

        private RootHtmlObject distinctDocType() throws UnfinishedStatement {

            if (!StringUtils.startsWithIgnoreCase(focusLine, docTypePrefix))
                return HtmlObject.newRoot(HtmlDocType.None);
            substringAndNext(9);

            if (!StringUtils.startsWithIgnoreCase(focusLine, docTypeElement1))
                return null;
            substringAndNext(docTypeElement1.length());

            if (focusLine.startsWith(Strs.bracket2end)) {
                substringAndNext(1);
                return HtmlObject.newRoot(HtmlDocType.V5);
            } else if (!StringUtils.startsWithIgnoreCase(focusLine, docTypeElement2))
                return null;
            substringAndNext(docTypeElement2.length());

            HtmlDocType type = HtmlDocType.parseByPublicIdentifier(extractQuotedValue());
            if (type == null)
                return null;

            if (!type.needSystemIdentifier())
                if (focusLine.startsWith(Strs.bracket2end)) {
                    substringAndNext(1);
                    return HtmlObject.newRoot(type);
                } else
                    return null;

            extractQuotedValue();
            if (focusLine.startsWith(Strs.bracket2end)) {
                substringAndNext(1);
                return HtmlObject.newRoot(type);
            } else
                return null;
        }

        private SuperList<String> recursiveExtraction() throws UnfinishedStatement {

            if (!focusLine.startsWith(Strs.bracket2start)
                    || focusLine.startsWith(tagEnclosurePrefix)) {
                if (focusObj.isRoot())
                    return markFailure();
                appendContents();
                return resource;
            } else if (focusLine.startsWith(commentPrefix))
                return searchCommentEnclosure(focusObj, focusObj.tag == HtmlTag.Script);

            Pair<String, Integer> brk = //
                    SuperStrings.firstIndexOf(focusLine, Strs.space, Strs.bracket2end);
            HtmlTag tag = HtmlTag.parse(brk == null
                    ? focusLine.substring(1)
                    : focusLine.substring(1, brk.getValue()));
            if (tag == null)
                return markFailure();

            if (brk != null)
                substringAndNext(brk.getValue() + 1);
            else
                removeAndNext();

            SuperMap<String, String> properties = null;
            if (brk == null || brk.getKey() != Strs.bracket2end) {
                properties = SuperMapGen.newi();
                if (!extractProperties(properties))
                    return markFailure();
            }

            HtmlObject child = focusObj = HtmlObject.newi(tag, focusObj, properties);

            if (!tag.unneedEnclosure)
                while (!searchTagEnclosure()) {
                    if (recursiveExtraction() == null)
                        return null;
                    focusObj = child;
                }

            return resource.isEmpty() ? null : resource;
        }

        private SuperList<String> markFailure() {
            focusObj.markFailure();
            return null;
        }

        private void appendContents() throws UnfinishedStatement {

            int brk, brk2;
            while ((brk = StringUtils.indexOfIgnoreCase(focusLine,
                    tagEnclosurePrefix + focusObj.tag.str)) == -1) {

                if ((brk2 = focusLine.indexOf(Strs.bracket2start)) != -1
                        && tryToExtractMixedChild(brk2)) {
                    appendContents();
                    return;
                }

                focusObj.append(focusLine);
                focusLine = resource.removeAndNext();
                if (focusLine == null)
                    throw new UnfinishedStatement();
            }

            if ((brk2 = focusLine.substring(0, brk).indexOf(Strs.bracket2start)) != -1
                    && tryToExtractMixedChild(brk2)) {
                appendContents();
                return;
            }

            String temp2 = focusLine.substring(brk + 2 + focusObj.tag.str.length()).trim();
            if (Strs.empty.equals(temp2)) {
                while (resource.hasNext() && StringUtils.isBlank(temp2 = resource.next()));
                if (StringUtils.isBlank(temp2))
                    throw new UnfinishedStatement();
                temp2 = temp2.trim();
                resource.resetFocus();
                resource.next();
            }

            if (temp2.startsWith(Strs.bracket2end)) {
                if (brk != 0) {
                    focusObj.append(focusLine.substring(0, brk));
                    substringAndNext(brk);
                }
            } else {
                focusObj.append(focusLine);
                focusLine = resource.removeAndNext();
                appendContents();
            }
        }

        private boolean tryToExtractMixedChild(int brk) {

            if (focusLine.substring(brk).startsWith(tagEnclosurePrefix))
                return false;

            Generator gen = new Generator(SuperListGen.of(resource));
            HtmlObject copy = focusObj.clone();
            if (brk != 0)
                copy.append(focusLine.substring(0, brk));
            gen.focusObj = copy;
            gen.focusLine = focusLine.substring(brk);
            gen.resource.next();

            try {
                if (gen.recursiveExtraction() == null)
                    return false;
            } catch (UnfinishedStatement e) {
                return false;
            }

            this.resource = gen.resource;
            this.focusLine = gen.focusLine;
            focusObj.update(copy);
            return true;
        }

        private boolean searchTagEnclosure() throws UnfinishedStatement {

            if (focusLine.length() < 2 + focusObj.tag.str.length()
                    || !StringUtils.startsWithIgnoreCase(focusLine,
                            tagEnclosurePrefix + focusObj.tag.str))
                return false;

            String temp = focusLine.substring(2 + focusObj.tag.str.length()).trim();
            if (Strs.empty.equals(focusLine)) {
                while (resource.hasNext() && StringUtils.isBlank(temp = resource.next()));
                if (temp == null)
                    throw new UnfinishedStatement();
                temp = temp.trim();
                resource.resetFocus();
                resource.next();
            }

            if (!temp.startsWith(Strs.bracket2end))
                return false;

            substringAndNext(2 + focusObj.tag.str.length());
            substringAndNext(1);
            return true;
        }

        private SuperList<String> searchCommentEnclosure(HtmlObject parent, boolean isScript)
                throws UnfinishedStatement {

            if (!isScript)
                focusObj = HtmlObject.newComment(parent);
            focusLine = focusLine.substring(4);

            String suffix = isScript ? commentSuffixForScript : commentSuffix;
            int brk;
            while ((brk = focusLine.indexOf(suffix)) == -1) {
                focusObj.append(focusLine);
                focusLine = resource.removeAndNext();
                if (focusLine == null)
                    throw new UnfinishedStatement();
            }

            if (brk != 0)
                focusObj.append(focusLine.substring(0, brk));

            substringAndNext(brk + suffix.length());
            return resource;
        }

        private boolean extractProperties(SuperMap<String, String> properties)
                throws UnfinishedStatement {

            Pair<String, Integer> brk;
            SuperList<String> pLines = null;
            while ((brk = SuperStrings.firstIndexOf(focusLine, Strs.equal,
                    Strs.bracket2end)) == null) {
                if (pLines == null)
                    pLines = SuperListGen.newi();
                pLines.add(focusLine);
                removeAndNext();
            }

            if (brk.getKey() == Strs.bracket2end) {
                if (pLines != null)
                    pLines.forEach(
                            l -> SuperStrings.applyEachToken(l, k -> properties.put(k, null)));
                substringAndNext(brk.getValue() + 1);
                return true;
            }

            String propertyKey = focusLine.substring(0, brk.getValue()).trim();
            if (Strs.empty.equals(propertyKey))
                if (pLines != null) {
                    propertyKey = pLines.getTip();
                    pLines.removeTip();
                } else
                    return false;

            int brk2;
            if ((brk2 = propertyKey.lastIndexOf(Strs.space)) != -1) {
                if (pLines == null)
                    pLines = SuperListGen.newi();
                pLines.add(propertyKey.substring(0, brk2));
                propertyKey = propertyKey.substring(brk2 + 1);
            }

            if (pLines != null)
                pLines.forEach(l -> SuperStrings.applyEachToken(l, k -> properties.put(k, null)));
            substringAndNext(brk.getValue() + 1);

            String propertyValue = null;
            if (focusLine.startsWith(Strs.dblQuotation))
                propertyValue = extractQuotedValue(Strs.dblQuotation);
            else if (focusLine.startsWith(Strs.quotation))
                propertyValue = extractQuotedValue(Strs.quotation);
            else if ((brk = SuperStrings.firstIndexOf(focusLine, Strs.space,
                    Strs.bracket2end)) == null) {
                propertyValue = focusLine;
                removeAndNext();
            } else {
                propertyValue = focusLine.substring(0, brk.getValue());
                substringAndNext(brk.getValue());
            }

            if (propertyValue == null)
                return false;

            properties.put(propertyKey, propertyValue);
            return extractProperties(properties);
        }

        private void removeAndNext() throws UnfinishedStatement {
            while (resource.hasNext() && StringUtils.isBlank(focusLine = resource.removeAndNext()));
            if (Strs.empty.equals(focusLine))
                resource.remove();
            else if (focusLine == null)
                throw new UnfinishedStatement();
            else
                focusLine = focusLine.trim();
        }

        private void substringAndNext(int begin) throws UnfinishedStatement {
            if (begin <= focusLine.length()) {
                focusLine = focusLine.substring(begin).trim();
                if (Strs.empty.equals(focusLine))
                    removeAndNext();
            } else
                removeAndNext();
        }

        private String extractQuotedValue() throws UnfinishedStatement {
            if (focusLine.startsWith(Strs.dblQuotation))
                return extractQuotedValue(Strs.dblQuotation);
            if (focusLine.startsWith(Strs.quotation))
                return extractQuotedValue(Strs.quotation);
            else
                return null;
        }

        private String extractQuotedValue(String quatation)
                throws UnfinishedStatement {

            substringAndNext(1);
            int brk;
            String value = Strs.empty;
            while ((brk = focusLine.indexOf(quatation)) == -1) {
                value += (focusLine + Strs.space);
                focusLine = resource.removeAndNext();
                if (focusLine == null)
                    throw new UnfinishedStatement();
            }
            value += focusLine.substring(0, brk);

            substringAndNext(brk + 1);
            return value;
        }

        @SuppressWarnings("serial")
	private class UnfinishedStatement
                extends
                Exception {
            UnfinishedStatement() {
                if (focusObj != null)
                    focusObj.markFailure();
            }
        }
    }

    private static class Decoder {

        private static final String indent = "    ";

        private final SuperList<String> resource = SuperListGen.newi();

        private Decoder() {
        }

        private SuperList<String> process(HtmlObject o) {

            if (o instanceof RootHtmlObject) {
                decodeRoot((RootHtmlObject) o);
                for (HtmlObject child : o.getChildren())
                    recursiveDecode(child, 0);
            } else
                recursiveDecode(o, 0);

            return resource;
        }

        private void decodeRoot(RootHtmlObject casted) {

            StringBuilder sb = new StringBuilder();
            sb.append(docTypePrefix).append(Strs.space).append(docTypeElement1);
            if (!casted.docType.needPublicIdentifier())
                resource.add(sb.append(Strs.bracket2end).toString());
            else {
                sb.append(Strs.space).append(docTypeElement2).append(Strs.space)
                        .append(Strs.dblQuotation)
                        .append(casted.docType.publicIdentifier)
                        .append(Strs.dblQuotation);
                if (!casted.docType.needSystemIdentifier())
                    resource.add(sb.append(Strs.bracket2end).toString());
                else
                    resource.add(sb.append(Strs.space).append(Strs.dblQuotation)
                            .append(casted.docType.systemIdentifier).append(Strs.dblQuotation)
                            .append(Strs.bracket2end).toString());
            }
        }

        private void recursiveDecode(HtmlObject o, int depth) {
            if (o.isComment())
                decodeComment(o, depth);
            else
                decodeNormal(o, depth);
        }

        private void decodeComment(HtmlObject o, int depth) {

            StringBuilder sb = new StringBuilder();
            SuperList<String> contents = o.getContents();

            sb.append(commentPrefix).append(Strs.space);

            if (contents.hasNext()) {
                addWithIndent(depth, sb.append(contents.next()).toString());
                while (contents.hasNext())
                    addWithIndent(depth, contents.next());
                resource.setTip(resource.getTip() + Strs.space + commentSuffix);

            } else
                addWithIndent(depth, sb.append(commentSuffix).toString());
        }

        private void decodeNormal(HtmlObject o, int depth) {

            StringBuilder sb = new StringBuilder();
            sb.append(Strs.bracket2start).append(o.tag.str).append(Strs.space);
            for (Map.Entry<String, String> entry : o.getProperties().entrySet()) {
                sb.append(entry.getKey());
                if (entry.getValue() != null)
                    sb.append(Strs.equal).append(Strs.dblQuotation).append(entry.getValue())
                            .append(Strs.dblQuotation);
                sb.append(Strs.space);
            }
            addWithIndent(depth,
                    sb.replace(sb.length() - 1, sb.length(), Strs.bracket2end).toString());

            String tagEnclosure = null;
            if (o.tag != null && !o.tag.unneedEnclosure)
                tagEnclosure = tagEnclosurePrefix + o.tag.str + Strs.bracket2end;

            if (o.getChildrenAndContents().size() != 1 || o.getContents().isEmpty()) {
                for (Object cc : o.getChildrenAndContents())
                    if (cc instanceof HtmlObject)
                        recursiveDecode((HtmlObject) cc, depth + 1);
                    else
                        addWithIndent(depth, (String) cc);
                if (tagEnclosure != null)
                    if (o.getChildrenAndContents().isEmpty()) {
                        resource.setTip(resource.getTip() + tagEnclosure);
                    } else
                        addWithIndent(depth, tagEnclosure);

            } else if (tagEnclosure != null)
                resource.setTip(resource.getTip() + o.getContents().getTip() + tagEnclosure);
        }

        private void addWithIndent(int depth, String str) {
            String pre = Strs.empty;
            for (int i = 0; i < depth; i++)
                pre += indent;
            resource.add(pre + str);
        }
    }
}
