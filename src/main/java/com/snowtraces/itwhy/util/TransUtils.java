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

    private static List<String> notTransTags = Arrays.asList("pre", "svg", "table");

    public static String translate(String inString) {

        // 1. 非翻译块
        List<Seg> segs = splitSentence(inString, notTransTags.get(0));
        if (notTransTags.size() > 1) {
            for (int i = 1; i < notTransTags.size(); i++) {
                int finalI = i;
                segs = segs.stream().map(seg -> {
                    if (seg.isTrans) {
                        return splitSentence(seg.src, notTransTags.get(finalI));
                    } else {
                        return Collections.singletonList(seg);
                    }
                }).flatMap(List::stream).collect(Collectors.toList());
            }
        }

        // 2. 待翻译内容清理
        String transSrc = segs.stream().filter(Seg::getIsTrans)
                .map(Seg::getSrc)
                .collect(Collectors.joining("\n<$>\n"))
                .replaceAll("<p>", "<p>\n")
                .replaceAll("</p>", "\n</p>")
                .replaceAll("<(blockquote|code)>", "<$1 translate=\"no\">")
                .replaceAll("<a ", "<a translate=\"no\" ");

        // 3. 翻译
        String translated = RequestUtils.translate(transSrc);
        String[] transMeta = translated.split("<\\$>");

        AtomicInteger idx = new AtomicInteger(0);
        return segs.stream().map(seg -> {
            if (seg.isTrans) {
                String transText = transMeta[idx.getAndIncrement()];
                // 4. 翻译结果清理
                // 标签闭合，hr修正
                transText = transText.replaceAll("< ?/ ?(a|code)>", "</$1>")
                        .replaceAll("< (a|code) ", "<$1 ")
                        .replaceAll("<小时>", "<hr>");
                
                return transText;
            } else {
                return seg.src;
            }
        }).collect(Collectors.joining(""));
    }

    public static List<Seg> splitSentence(String inString, String tag) {
        if (inString == null) {
            return Collections.emptyList();
        }
        inString = inString.trim();

        Pattern pattern = Pattern.compile("<" + tag + "((?!</" + tag + ")[\\s\\S])*</" + tag + ">");

        Matcher matcher = pattern.matcher(inString);

        int start = 0;

        List<Seg> segList = new ArrayList<>();
        while (matcher.find(start)) {
            // 生成分组数据
            int first = matcher.start();
            int end = matcher.end();
            if (first > start) {
                segList.add(new Seg(true, inString.substring(start, first), null, start, first));
            }

            String notTansString = inString.substring(first, end);
            
            // 移除代码块中的span高亮内容
            if (tag.equals("pre")) {
                notTansString = notTansString
                        .replaceAll("<span[^>]*>", "")
                        .replaceAll("</span>", "");

            }
            segList.add(new Seg(false, notTansString, null, first, end));

            start = end;
        }

        if (start < inString.length()) {
            segList.add(new Seg(true, inString.substring(start), null, start, inString.length()));
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


    private static String testString = "<p>I don't know how you are expecting <code>array.remove(int)</code> to behave. There are three possibilities I can think of that you might want.</p>\n" +
            "<p>To remove an element of an array at an index <code>i</code>:</p>\n" +
            "<pre class=\"lang-js s-code-block\"><code class=\"hljs language-javascript\">array.<span class=\"hljs-title function_\">splice</span>(i, <span class=\"hljs-number\">1</span>);\n" +
            "</code></pre>\n" +
            "<p>If you want to remove every element with value <code>number</code> from the array:</p>\n" +
            "<pre class=\"lang-js s-code-block\"><code class=\"hljs language-javascript\"><span class=\"hljs-keyword\">for</span> (<span class=\"hljs-keyword\">var</span> i = array.<span class=\"hljs-property\">length</span> - <span class=\"hljs-number\">1</span>; i &gt;= <span class=\"hljs-number\">0</span>; i--) {\n" +
            " <span class=\"hljs-keyword\">if</span> (array[i] === number) {\n" +
            "  array.<span class=\"hljs-title function_\">splice</span>(i, <span class=\"hljs-number\">1</span>);\n" +
            " }\n" +
            "}\n" +
            "</code></pre>\n" +
            "<p>If you just want to make the element at index <code>i</code> no longer exist, but you don't want the indexes of the other elements to change:</p>\n" +
            "<pre class=\"lang-js s-code-block\"><code class=\"hljs language-javascript\"><span class=\"hljs-keyword\">delete</span> array[i];\n" +
            "</code></pre>\n";

}
