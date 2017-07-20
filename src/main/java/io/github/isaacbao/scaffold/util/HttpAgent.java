package io.github.isaacbao.scaffold.util;

import io.github.isaacbao.scaffold.system.exception.HttpRequestFailException;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 具有简单浏览器功能的http工具
 */
public class HttpAgent {

    static final Logger logger = LogManager.getLogger();

    private final Map<String, String> cookieMap = new HashMap<>();

    private String cookie;

    private String currentURL;

    private final Map<String, String> headMap = new HashMap<>();

    protected void setCookie(String cookie) {
        this.cookie = cookie;
    }

    protected Map getCookies() {
        return cookieMap;
    }

    public HttpAgent() {
        headMap.put("Accept", "*/*");
        headMap.put("Accept-Language", "en-US,en;q=0.8");
        headMap.put("Connection", "keep-alive");
        // headMap.put("Content-Type", "application/x-www-form-urlencoded");
        headMap.put("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.97 Safari/537.36");
        //    headMap.put("X-Requested-With", "XMLHttpRequest");
    }

    public String getPage(String url, Map<String, String> pm) {
        StringBuilder sb = convertRequestParam(pm);
        return getPage(url + "?" + sb.toString());
    }

    public String sendPost(String url, Map<String, String> pm) {
        StringBuilder sb = convertRequestParam(pm);
        return sendPost(url, sb.toString());
    }

    public String sendPost(String url, Map<String, String> pm, String head, String content) {
        StringBuilder sb = convertRequestParam(pm);
        return sendPost(url, sb.toString(), head, content);
    }

    private StringBuilder convertRequestParam(Map<String, String> pm) {
        StringBuilder sb = new StringBuilder();
        pm.forEach((k, v) -> {
            logger.info("key:" + k + ",value:" + v);
            sb.append(URLEncodedUtils.parse(k, StandardCharsets.UTF_8)).append("=").append(URLEncodedUtils.parse(v,
                    StandardCharsets.UTF_8));
            sb.append("&");
        });
        return sb;
    }

    public String sendDelete(String url, Map<String, String> pm) {
        StringBuilder sb = convertRequestParam(pm);
        if (sb.lastIndexOf("&") == sb.length() - 1) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sendDelete(url + "?" + sb.toString());
    }

    public String getCurrentURL() {
        return currentURL;
    }

    public String getCurrentPage() {
        return getPage(currentURL);
    }

    /**
     * @param param 请求参数
     * @return 请求的页面
     */
    public String sendPost(String url, String param) {

        currentURL = url;

        URL realUrl;
        try {
            realUrl = new URL(url);
        } catch (MalformedURLException e) {
            logger.error("", e);
            throw new HttpRequestFailException(url, param, "");
        }

        URLConnection conn;
        logger.info("HTTP post to url:" + url);
        logger.info("params:" + param);
        if (url.startsWith("https")) {
            HttpUtils.truseAllHttpsCert();
        }
        try {
            conn = realUrl.openConnection();
        } catch (IOException e) {
            logger.error("", e);
            throw new HttpRequestFailException(url, param, "");
        }

        return doSendPost(param, conn);
    }

    private String doSendPost(String param, URLConnection conn) {
        // 设置通用的请求属性
        setHead(conn);
        setCookie(conn);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // 发送POST请求必须设置如下两行
        conn.setDoOutput(true);
        conn.setDoInput(true);

        try {
            try (DataOutputStream paramsOutputStream = new DataOutputStream(conn.getOutputStream())) {
                byte[] bytes = param.getBytes(StandardCharsets.UTF_8);
                for (int i = 0; i < bytes.length; i++) {
                    paramsOutputStream.write(bytes, i, 1);
                }
                paramsOutputStream.flush();
            }
        } catch (IOException e) {
            logger.error("", e);
            throw new HttpRequestFailException(conn.getURL().toString(), param, "");
        }

        saveCookie(conn.getHeaderFields());
        if (is302(conn)) {
            return redirect(conn);
        }
        try {
            return getResponseContent(conn);
        } catch (IOException e) {
            logger.error("读取返回信息出错", e);
            throw new HttpRequestFailException(conn.getURL().toString(), param, "");
        }
    }

    /**
     * @param param 请求参数
     */
    public String sendPost(String url, String param, String head, String content) {

        currentURL = url;

        URL realUrl;
        try {
            realUrl = new URL(url);
        } catch (MalformedURLException e) {
            logger.error("", e);
            throw new HttpRequestFailException(url, param, "");
        }

        URLConnection conn;
        logger.info("HTTP post url:" + url);
        logger.info("Params:" + param);
        if (url.startsWith("https")) {
            HttpUtils.truseAllHttpsCert();
        }
        try {
            conn = realUrl.openConnection();
        } catch (IOException e) {
            logger.error("", e);
            throw new HttpRequestFailException(url, param, "");
        }
        HttpUtils.setHeader(conn, head, content);
        return doSendPost(param, conn);
    }

    public String sendDelete(String urlString) {
        String result = "";
        logger.info("send http delete url:" + urlString);
        URL url;
        try {
            url = new URL(urlString);
            if (urlString.startsWith("https"))
                HttpUtils.truseAllHttpsCert();

            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            setHead(httpCon);
            httpCon.setRequestProperty(
                    "Content-Type", "application/x-www-form-urlencoded");
            httpCon.setRequestMethod("DELETE");
            httpCon.setDoOutput(true);
            httpCon.connect();

            saveCookie(httpCon.getHeaderFields());

            if (is302(httpCon))
                return redirect(httpCon);

            result = getResponseContent(httpCon);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public URLConnection getImage(String url) {
        logger.info("send http get url:" + url);
        try {
            currentURL = url;
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            HttpUtils.setCommonHead(connection);
            setCookie(connection);
            // 建立实际的连接
            connection.connect();
            // 保存cookie
            saveCookie(connection.getHeaderFields());
            // 定义 BufferedReader输入流来读取URL的响应

            //if(is302(connection))
            //	return redirect(connection);
            return connection;
            //result = getResponseContent(connection);
        } catch (Exception e) {
            logger.info("发送GET请求出现异常" + e);
            e.printStackTrace();
            return null;
        }
        //	logger.info("url "+url+" response:"+result);
        //return result;
    }

    public URLConnection getImage(String url, String head, String content) {
        logger.info("send http get url:" + url);
        try {
            currentURL = url;
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            HttpUtils.setCommonHead(connection);
            HttpUtils.setHeader(connection, head, content);
            setCookie(connection);
            // 建立实际的连接
            connection.connect();
            // 保存cookie
            saveCookie(connection.getHeaderFields());
            // 定义 BufferedReader输入流来读取URL的响应

            //if(is302(connection))
            //	return redirect(connection);
            return connection;
            //result = getResponseContent(connection);
        } catch (Exception e) {
            logger.error("发送GET请求出现异常" + e);
            e.printStackTrace();
            return null;
        }
        //	logger.info("url "+url+" response:"+result);
        //return result;
    }

    public String getPage(String url) {
        String result = "";
        logger.info("send http get url:" + url);
        try {
            currentURL = url;
            URL realUrl = new URL(url);

            if (url.startsWith("https"))
                HttpUtils.truseAllHttpsCert();
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            //HttpUtils.setCommonHead(connection);
            setHead(connection);
            setCookie(connection);
            // 建立实际的连接
            connection.connect();
            // 保存cookie
            saveCookie(connection.getHeaderFields());
            // 定义 BufferedReader输入流来读取URL的响应

            if (is302(connection))
                return redirect(connection);

            result = getResponseContent(connection);
        } catch (Exception e) {
            logger.info("发送GET请求出现异常" + e);
            e.printStackTrace();
        }
        logger.info("url " + url + " response:" + result);
        return result;
    }

    protected boolean is302(URLConnection conn) {
        String field0 = conn.getHeaderField(0);
        return field0.contains("302");
    }

    protected String redirect(URLConnection conn) {
        String location = conn.getHeaderField("Location");
        logger.info("request get 302 redirect to :" + location);
        return getPage(location);
    }

    protected String getResponseContent(URLConnection conn) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {//ISO-8859-1
            in.lines().forEach(sb::append);
        }
        return transferCharset(sb.toString());
    }


    private String transferCharset(String html) {
        String charset = RegexUtils.getFirstMatch("(?:charset=)[^\"^;^']*", html);
        if (charset != null && charset.length() > 8) {
            charset = charset.substring(8);
        } else {
            charset = RegexUtils.getFirstMatch("charset=\"[^\"^;^']*\"", html);
            if (charset != null && charset.length() > 10)
                charset = charset.substring(8).replace("\"", "");
        }
        if (charset == null)
            charset = "UTF-8";

        logger.info("transfer charset:" + charset);
        try {
            byte[] bytes = html.getBytes(charset);
            return new String(bytes, charset);
        } catch (UnsupportedEncodingException e) {
            logger.info("unsuppoted  charset:" + charset + ",return as UTF-8");
            e.printStackTrace();
            byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }

    private void saveCookie(Map<String, List<String>> headMap) {
        headMap.entrySet().stream().filter(entry -> "Set-Cookie".equals(entry.getKey())).forEach(entry -> {
            List<String> cookies = headMap.get(entry.getKey());
            for (String str : cookies)
                addCookie(str);
        });
//        for (Entry<String, List<String>> entry : headMap.entrySet()) {
//            if ("Set-Cookie".equals(entry.getKey())) {
//                List<String> cookies = headMap.get(entry.getKey());
//                for (String str : cookies)
//                    addCookie(str);
//            }
//        }
    }

    protected void addHead(String key, String value) {
        headMap.put(key, value);
    }

    protected void setCookie(String key, String value) {
        cookieMap.put(key, value);
    }

    protected void addCookie(String cookieStr) {
        String[] cookies = cookieStr.split(";");
        for (String cookie : cookies) {

            //将cookie从第一个=号分割为键值
            String[] cookieEntry = cookie.replaceFirst("=", ";").split(";");
            if (cookieEntry.length == 1) {
                logger.info("save single cookie " + cookieEntry[0]);
                cookieMap.put(cookieEntry[0], "");
            }
            if (cookieEntry.length >= 2) {
                logger.info("save cookie " + cookieEntry[0] + ":" + cookieEntry[1]);
                cookieMap.put(cookieEntry[0], cookieEntry[1]);
            }

        }
    }

    protected void setCookie(URLConnection conn) {
        StringBuilder sb = new StringBuilder();
        Set<Entry<String, String>> entrys = cookieMap.entrySet();
        for (Entry<String, String> entry : entrys) {
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append(";");
        }
        sb.append(cookie);
        logger.info("set cookie:" + sb);
        conn.setRequestProperty("Cookie", sb.toString());
    }

    public void clearCookies() {
        cookieMap.clear();
    }


    public void setHead(URLConnection conn) {
        headMap.forEach(conn::setRequestProperty);
    }
}
