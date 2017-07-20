package io.github.isaacbao.scaffold.system.exception;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 *
 * Created by rongyang_lu on 2017/7/18.
 */
public class HttpRequestTimeoutException extends RuntimeException {
    private String url;
    private String params;

    public HttpRequestTimeoutException(String url) {
        super("Http请求超时");
        try {
            this.url = URLDecoder.decode(url, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public HttpRequestTimeoutException(String url, String params) {
        super("Http请求超时");
        this.url = url;
        try {
            this.params = URLDecoder.decode(params, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String getParams() {
        return params;
    }

    public String getUrl() {
        return url;
    }
}
