package io.github.isaacbao.scaffold.system.exception;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * Created by rongyang_lu on 2017/7/18.
 */
public class HttpRequestFailException extends RuntimeException {
    private String url;
    private String params;
    private String responseBody;

//    public HttpRequestFailException(String url, String responseBody) {
//        super("Http请求失败");
//        try {
//            this.url = URLDecoder.decode(url, StandardCharsets.UTF_8.name());
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        this.responseBody = responseBody;
//    }

//    public HttpRequestFailException(String url, Map<String, String> params) {
//        super("Http请求失败");
//        this.url = url;
//        this.params = params;
//    }

    public HttpRequestFailException(String url, String params, String responseBody) {
        super("Http请求失败");
        this.url = url;
        try {
            this.params = URLDecoder.decode(params, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.responseBody = responseBody;
    }


    public String getUrl() {
        return url;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public String getParams() {
        return params;
    }
}
