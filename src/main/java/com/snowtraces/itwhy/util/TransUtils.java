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

        String transSrc = segs.stream().filter(seg -> seg.getIsTrans())
                .map(Seg::getSrc)
                .collect(Collectors.joining("\n<$>\n"))
                .replaceAll("<p>", "<p>\n")
                .replaceAll("</p>", "\n</p>")
                .replaceAll("<blockquote>", "<blockquote translate=\"no\">");

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


    private static String testString = "<p>\n" +
            "我想编写一个循环遍历 15 个字符串（可能是数组？）的脚本，这可能吗？\n" +
            "</p>\n" +
            "\n" +
            "<p>\n" +
            "就像是：\n" +
            "</p><pre class=\"lang-sh s-code-block\"><code class=\"hljs language-bash\"><span class=\"hljs-keyword\">for</span> databaseName <span class=\"hljs-keyword\">in</span> listOfNames\n" +
            "<span class=\"hljs-keyword\">then</span>\n" +
            "  <span class=\"hljs-comment\"># Do something</span>\n" +
            "end\n" +
            "</code></pre> ";

}
