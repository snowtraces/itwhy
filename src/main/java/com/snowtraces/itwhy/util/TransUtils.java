package com.snowtraces.itwhy.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 翻译工具类
 *
 * @author snow
 */
public class TransUtils {

    public static void main(String[] args) {
        String translated = translate(testString);
        System.err.println(translated);
    }

    private static List<String> tags = Arrays.asList("pre", "svg", "table");

    public static String translate(String inString) {
        List<Seg> segs = splitSentence(inString, tags.get(0));

        if (tags.size() > 1) {
            for (int i = 1; i < tags.size(); i++) {
                int finalI = i;
                segs = segs.stream().map(seg -> {
                    if (seg.isTrans) {
                        return splitSentence(seg.src, tags.get(finalI));
                    } else {
                        return Collections.singletonList(seg);
                    }
                }).flatMap(List::stream).collect(Collectors.toList());
            }
        }

        String transSrc = segs.stream().filter(seg -> seg.getIsTrans())
                .map(Seg::getSrc)
                .collect(Collectors.joining("\n<$>\n"))
                .replaceAll("<p>", "<p>\n")
                .replaceAll("</p>", "\n</p>")
                .replaceAll("<blockquote>", "<blockquote  class=\"\">");

        String translated = RequestUtils.translate(transSrc);
        String[] transMeta = translated.split("<\\$>");

        AtomicInteger idx = new AtomicInteger(0);

        return segs.stream().map(seg -> {
            if (seg.isTrans) {
                return transMeta[idx.getAndIncrement()];
            } else {
                return seg.src;
            }
        }).collect(Collectors.joining(""));
    }

    public static List<Seg> splitSentence(String inString, String tag) {
        // html 标签分组翻译 <code></code>  <pre></pre>
        if (inString == null) {
            return Collections.emptyList();
        }
        inString = inString.trim();

        Pattern pattern = Pattern.compile("<" + tag + "((?!</" + tag + ")[\\s\\S])*</" + tag + ">");

        Matcher matcher = pattern.matcher(inString);

        int start = 0;

        List<Seg> segList = new ArrayList<>();
        while (matcher.find(start)) {
            String group = matcher.group(0);

            // 生成分组数据
            int first = matcher.start();
            int end = matcher.end();
            if (first > start) {
                segList.add(new Seg(true, inString.substring(start, first), null, start, first));
            }
            segList.add(new Seg(false, inString.substring(first, end), null, first, end));

            start = end;
        }

        if (start < inString.length()) {
            segList.add(new Seg(true, inString.substring(start, inString.length()), null, start, inString.length()));
        }

        return segList;
    }

    @Data
    @AllArgsConstructor
    public static class Seg {
        private Boolean isTrans;
        private String src;
        private String dest;
        private Integer from;
        private Integer to;
    }


    private static String testString = "\n" +
            "<p>Find the <code>index</code> of the array element you want to remove using <a href=\"https://developer.mozilla.org/docs/Web/JavaScript/Reference/Global_Objects/Array/indexOf\" rel=\"noreferrer\"><code>indexOf</code></a>, and then remove that index with <a href=\"https://developer.mozilla.org/docs/Web/JavaScript/Reference/Global_Objects/Array/splice\" rel=\"noreferrer\"><code>splice</code></a>.</p>\n" +
            "<blockquote>\n" +
            "<p>The splice() method changes the contents of an array by removing\n" +
            "existing elements and/or adding new elements.</p>\n" +
            "</blockquote>\n" +
            "<p></p><div class=\"snippet\" data-lang=\"js\" data-hide=\"false\" data-console=\"true\" data-babel=\"false\">\n" +
            "<div class=\"snippet-code\">\n" +
            "<pre class=\"snippet-code-js lang-js s-code-block\"><code class=\"hljs language-javascript\"><span class=\"hljs-keyword\">const</span> array = [<span class=\"hljs-number\">2</span>, <span class=\"hljs-number\">5</span>, <span class=\"hljs-number\">9</span>];\n" +
            "\n" +
            "<span class=\"hljs-variable language_\">console</span>.<span class=\"hljs-title function_\">log</span>(array);\n" +
            "\n" +
            "<span class=\"hljs-keyword\">const</span> index = array.<span class=\"hljs-title function_\">indexOf</span>(<span class=\"hljs-number\">5</span>);\n" +
            "<span class=\"hljs-keyword\">if</span> (index &gt; -<span class=\"hljs-number\">1</span>) {\n" +
            "  array.<span class=\"hljs-title function_\">splice</span>(index, <span class=\"hljs-number\">1</span>);\n" +
            "}\n" +
            "\n" +
            "<span class=\"hljs-comment\">// array = [2, 9]</span>\n" +
            "<span class=\"hljs-variable language_\">console</span>.<span class=\"hljs-title function_\">log</span>(array); </code></pre>\n" +
            "<div class=\"snippet-result\"><div class=\"snippet-ctas\"><button type=\"button\" class=\"s-btn s-btn__primary\"><span class=\"icon-play-white _hover\"></span><span> Run code snippet</span></button><input class=\"copySnippet s-btn s-btn__filled\" type=\"button\" value=\"Copy snippet to answer\" style=\"display: none;\"><button type=\"button\" class=\"s-btn hideResults\" style=\"display: none;\">Hide results</button><div class=\"popout-code\"><a class=\"snippet-expand-link\">Expand snippet</a></div></div><div class=\"snippet-result-code\" style=\"display: none;\"><iframe name=\"sif2\" sandbox=\"allow-forms allow-modals allow-scripts\" class=\"snippet-box-edit snippet-box-result\" frameborder=\"0\"></iframe></div></div></div>\n" +
            "</div>\n" +
            "<p></p>\n" +
            "<p>The second parameter of <code>splice</code> is the number of elements to remove. Note that <code>splice</code> modifies the array in place and returns a new array containing the elements that have been removed.</p>\n" +
            "<hr>\n" +
            "<p>For the reason of completeness, here are functions. The first function removes only a single occurrence (i.e. removing the first match of <code>5</code> from <code>[2,5,9,1,5,8,5]</code>), while the second function removes all occurrences:</p>\n" +
            "<p></p><div class=\"snippet\" data-lang=\"js\" data-hide=\"false\" data-console=\"true\" data-babel=\"false\">\n" +
            "<div class=\"snippet-code\">\n" +
            "<pre class=\"snippet-code-js lang-js s-code-block\"><code class=\"hljs language-javascript\"><span class=\"hljs-keyword\">function</span> <span class=\"hljs-title function_\">removeItemOnce</span>(<span class=\"hljs-params\">arr, value</span>) {\n" +
            "  <span class=\"hljs-keyword\">var</span> index = arr.<span class=\"hljs-title function_\">indexOf</span>(value);\n" +
            "  <span class=\"hljs-keyword\">if</span> (index &gt; -<span class=\"hljs-number\">1</span>) {\n" +
            "    arr.<span class=\"hljs-title function_\">splice</span>(index, <span class=\"hljs-number\">1</span>);\n" +
            "  }\n" +
            "  <span class=\"hljs-keyword\">return</span> arr;\n" +
            "}\n" +
            "\n" +
            "<span class=\"hljs-keyword\">function</span> <span class=\"hljs-title function_\">removeItemAll</span>(<span class=\"hljs-params\">arr, value</span>) {\n" +
            "  <span class=\"hljs-keyword\">var</span> i = <span class=\"hljs-number\">0</span>;\n" +
            "  <span class=\"hljs-keyword\">while</span> (i &lt; arr.<span class=\"hljs-property\">length</span>) {\n" +
            "    <span class=\"hljs-keyword\">if</span> (arr[i] === value) {\n" +
            "      arr.<span class=\"hljs-title function_\">splice</span>(i, <span class=\"hljs-number\">1</span>);\n" +
            "    } <span class=\"hljs-keyword\">else</span> {\n" +
            "      ++i;\n" +
            "    }\n" +
            "  }\n" +
            "  <span class=\"hljs-keyword\">return</span> arr;\n" +
            "}\n" +
            "<span class=\"hljs-comment\">// Usage</span>\n" +
            "<span class=\"hljs-variable language_\">console</span>.<span class=\"hljs-title function_\">log</span>(<span class=\"hljs-title function_\">removeItemOnce</span>([<span class=\"hljs-number\">2</span>,<span class=\"hljs-number\">5</span>,<span class=\"hljs-number\">9</span>,<span class=\"hljs-number\">1</span>,<span class=\"hljs-number\">5</span>,<span class=\"hljs-number\">8</span>,<span class=\"hljs-number\">5</span>], <span class=\"hljs-number\">5</span>))\n" +
            "<span class=\"hljs-variable language_\">console</span>.<span class=\"hljs-title function_\">log</span>(<span class=\"hljs-title function_\">removeItemAll</span>([<span class=\"hljs-number\">2</span>,<span class=\"hljs-number\">5</span>,<span class=\"hljs-number\">9</span>,<span class=\"hljs-number\">1</span>,<span class=\"hljs-number\">5</span>,<span class=\"hljs-number\">8</span>,<span class=\"hljs-number\">5</span>], <span class=\"hljs-number\">5</span>))</code></pre>\n" +
            "<div class=\"snippet-result\"><div class=\"snippet-ctas\"><button type=\"button\" class=\"s-btn s-btn__primary\"><span class=\"icon-play-white _hover\"></span><span> Run code snippet</span></button><input class=\"copySnippet s-btn s-btn__filled\" type=\"button\" value=\"Copy snippet to answer\" style=\"display: none;\"><button type=\"button\" class=\"s-btn hideResults\" style=\"display: none;\">Hide results</button><div class=\"popout-code\"><a class=\"snippet-expand-link\">Expand snippet</a></div></div><div class=\"snippet-result-code\" style=\"display: none;\"><iframe name=\"sif3\" sandbox=\"allow-forms allow-modals allow-scripts\" class=\"snippet-box-edit snippet-box-result\" frameborder=\"0\"></iframe></div></div></div>\n" +
            "</div>\n" +
            "<p></p>\n" +
            "<p>In TypeScript, these functions can stay type-safe with a type parameter:</p>\n" +
            "<pre class=\"lang-js s-code-block\"><code class=\"hljs language-javascript\"><span class=\"hljs-keyword\">function</span> removeItem&lt;T&gt;(<span class=\"hljs-attr\">arr</span>: <span class=\"hljs-title class_\">Array</span>&lt;T&gt;, <span class=\"hljs-attr\">value</span>: T): <span class=\"hljs-title class_\">Array</span>&lt;T&gt; { \n" +
            "  <span class=\"hljs-keyword\">const</span> index = arr.<span class=\"hljs-title function_\">indexOf</span>(value);\n" +
            "  <span class=\"hljs-keyword\">if</span> (index &gt; -<span class=\"hljs-number\">1</span>) {\n" +
            "    arr.<span class=\"hljs-title function_\">splice</span>(index, <span class=\"hljs-number\">1</span>);\n" +
            "  }\n" +
            "  <span class=\"hljs-keyword\">return</span> arr;\n" +
            "}\n" +
            "</code></pre>\n" +
            "    ";

}
