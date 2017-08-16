package io.github.isaacbao.scaffold.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 */
public class HttpUtils {

    static final Logger logger = LogManager.getLogger();

    public static void setCommonHead(URLConnection conn) {
        conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
        conn.setRequestProperty("Cache-Control", "max-age=0");
        conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.124 Safari/537.36");
    }

    public static void setHeader(URLConnection conn, String headName, String content) {
        conn.setRequestProperty(headName, content);
    }

    public static void setHeader(URLConnection conn, Map<String, String> header) {
        header.forEach(conn::setRequestProperty);

    }

    public static void truseAllHttpsCert() {
        try {
            SSLUtils.trustAllHttpsCertificates();
        } catch (Exception e) {
            e.printStackTrace();
        }
        HostnameVerifier hv = new HostnameVerifier() {
            @Override
            public boolean verify(String urlHostName, SSLSession session) {
                logger.warn("Warning: URL Host: " + urlHostName + " vs. "
                        + session.getPeerHost());
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }

    public static String urlEncodeWithUTF8(String str) {
        try {
            return URLEncoder.encode(str, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String urlEncode(String str, Charset charset) {
        try {
            return URLEncoder.encode(str, charset.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }
}
