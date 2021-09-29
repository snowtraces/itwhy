package com.snowtraces.itwhy.util;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
public class RequestUtils {
    private static CookieStore cookieStore = new BasicCookieStore();
    private final static String USER_AGENT_CHROME = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.82 Safari/537.36 Edg/93.0.961.52";
    private static ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

    static List<String> googleTransClientList = Arrays.asList(
            "gtx",
            "t",
            "dict-chrome-ex"
    );
    private static LocalDateTime nextTime = LocalDateTime.now();
    private static int idxOfClient = 0;

    /**
     * 请求
     *
     * @return
     */
    public static String request(String url) {

        try (CloseableHttpClient httpClient = getHttpClient();
             CloseableHttpResponse response = httpClient.execute(new HttpGet(url))) {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return inputStreamToString(entity.getContent());
            } else {
                log.error("Unexpected response status: " + status);
                log.error(url);
//                idxOfClient = (idxOfClient + 1) % googleTransClientList.size();
                if (status == 429) {
                    nextTime = LocalDateTime.now().plusHours(1L);
                }
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String inputStreamToString(InputStream is) {
        if (is == null) {
            return null;
        }

        String line = "";
        StringBuilder total = new StringBuilder();

        // Wrap a BufferedReader around the InputStream
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        try {
            // Read response until the end
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return full string
        return total.toString();
    }

    /**
     * 请求
     *
     * @return
     */
    public static InputStream requestPost(String url, List<String> params, List<String> headers) {
        CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpResponse response = null;
        try {
            URIBuilder builder = new URIBuilder(url);
            HttpPost httpPost = new HttpPost(builder.build());

            // form_data
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            if (params != null && params.size() > 0) {
                for (String s : params) {
                    String[] split = s.split("[=:]");
                    entityBuilder.addPart(split[0], new StringBody(split[1], ContentType.TEXT_PLAIN));
                }
            }
            httpPost.setEntity(entityBuilder.build());

            // header
            if (headers != null && headers.size() > 0) {
                for (String s : headers) {
                    String[] split = s.split("[=:]");
                    httpPost.setHeader(split[0], split[1]);
                }
            }

            response = httpClient.execute(httpPost);

            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                return is;
            } else {
                log.error("Unexpected response status: " + status);
                log.error(url);
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return null;
        }
    }

    public static CloseableHttpClient getHttpClient() {

        RequestConfig config = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.DEFAULT)
                .setSocketTimeout(30000)
                .setConnectTimeout(30000)
                .setConnectionRequestTimeout(30000)
                .build();

        return HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .setDefaultRequestConfig(config)
                .setUserAgent(USER_AGENT_CHROME)
                .build();
    }

    private static String urlEncode(String url) throws UnsupportedEncodingException {
        return url.substring(0, url.lastIndexOf("/") + 1)
                + URLEncoder.encode(url.substring(url.lastIndexOf("/") + 1), "utf8")
                .replaceAll("\\+", "%20")
                .replaceAll("%3F", "?");
    }

    private static String encode(String param) {
        if (param == null) {
            return null;
        }

        try {
            return URLEncoder.encode(param, "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String translate(String query) {
        if (nextTime.compareTo(LocalDateTime.now()) > 0) {
            return query;
        }

        Map<String, String> params = new HashMap<>();
        String tk = null;
        try {
            tk = tk(query);
        } catch (Exception e) {
            e.printStackTrace();
        }

        params.put("client", googleTransClientList.get(idxOfClient));
        params.put("sl", "en");
        params.put("tl", "zh-CN");
        params.put("hl", "en");
        params.put("dt", "at");
        params.put("dt", "bd");
        params.put("dt", "t");
        params.put("ie", "UTF-8");
        params.put("oe", "UTF-8");
        params.put("source", "btn");
        params.put("srcrom", "1");
        params.put("ssel", "0");
        params.put("tsel", "0");
        params.put("kc", "11");
        params.put("tk", tk);
        params.put("q", query);

        String paramStrings = params.entrySet().stream().map(e -> e.getKey() + "=" + encode(e.getValue()))
                .collect(Collectors.joining("&"));

        String transJson = request("https://translate.googleapis.com/translate_a/single?" + paramStrings);

        if (transJson != null && transJson.startsWith("[[")) {
            List<Object> objects = JacksonUtils.readValue(transJson, new TypeReference<List<Object>>() {
            });
            if (objects != null && objects.size() > 0) {
                ArrayList<List<String>> transList = (ArrayList<List<String>>) objects.get(0);
                return transList.stream().map(x -> x.get(0)).collect(Collectors.joining(""));
            }
        }
        return query;
    }

    public static class Trans {

    }

    private static String tk(String val) throws Exception {
        String script = "function tk(a) {"
                + "var TKK = ((function() {var a = 561666268;var b = 1526272306;return 406398 + '.' + (a + b); })());\n"
                + "function b(a, b) { for (var d = 0; d < b.length - 2; d += 3) { var c = b.charAt(d + 2), c = 'a' <= c ? c.charCodeAt(0) - 87 : Number(c), c = '+' == b.charAt(d + 1) ? a >>> c : a << c; a = '+' == b.charAt(d) ? a + c & 4294967295 : a ^ c } return a }\n"
                + "for (var e = TKK.split('.'), h = Number(e[0]) || 0, g = [], d = 0, f = 0; f < a.length; f++) {"
                + "var c = a.charCodeAt(f);"
                + "128 > c ? g[d++] = c : (2048 > c ? g[d++] = c >> 6 | 192 : (55296 == (c & 64512) && f + 1 < a.length && 56320 == (a.charCodeAt(f + 1) & 64512) ? (c = 65536 + ((c & 1023) << 10) + (a.charCodeAt(++f) & 1023), g[d++] = c >> 18 | 240, g[d++] = c >> 12 & 63 | 128) : g[d++] = c >> 12 | 224, g[d++] = c >> 6 & 63 | 128), g[d++] = c & 63 | 128)"
                + "}"
                + "a = h;"
                + "for (d = 0; d < g.length; d++) a += g[d], a = b(a, '+-a^+6');"
                + "a = b(a, '+-3^+b+-f');"
                + "a ^= Number(e[1]) || 0;"
                + "0 > a && (a = (a & 2147483647) + 2147483648);"
                + "a %= 1E6;"
                + "return a.toString() + '.' + (a ^ h)\n"
                + "}";

        engine.eval(script);
        Invocable inv = (Invocable) engine;
        return (String) inv.invokeFunction("tk", val);
    }


    public static void main(String[] args) {
        String translate = translate("\nshow time" +
                "<pre translate=\"no\" class=\"lang-js s-code-block\"><code class=\"hljs language-javascript\"><span class=\"hljs-title class_\">Array</span>.<span class=\"hljs-property\"><span class=\"hljs-keyword\">prototype</span></span>.<span class=\"hljs-property\">remove_by_value</span> = <span class=\"hljs-keyword\">function</span>(<span class=\"hljs-params\">val</span>) {\n" +
                " <span class=\"hljs-keyword\">for</span> (<span class=\"hljs-keyword\">var</span> i = <span class=\"hljs-number\">0</span>; i &lt; <span class=\"hljs-variable language_\">this</span>.<span class=\"hljs-property\">length</span>; i++) {\n" +
                "  <span class=\"hljs-keyword\">if</span> (<span class=\"hljs-variable language_\">this</span>[i] === val) {\n" +
                "   <span class=\"hljs-variable language_\">this</span>.<span class=\"hljs-title function_\">splice</span>(i, <span class=\"hljs-number\">1</span>);\n" +
                "   i--;\n" +
                "  }\n" +
                " }\n" +
                " <span class=\"hljs-keyword\">return</span> <span class=\"hljs-variable language_\">this</span>;\n" +
                "}[\n" +
                " <span class=\"hljs-comment\">// call like</span>\n" +
                " (<span class=\"hljs-number\">1</span>, <span class=\"hljs-number\">2</span>, <span class=\"hljs-number\">3</span>, <span class=\"hljs-number\">4</span>)\n" +
                "].<span class=\"hljs-title function_\">remove_by_value</span>(<span class=\"hljs-number\">3</span>);\n" +
                "</code></pre>\n" +
                "    ");
        System.err.println(translate);
    }

}