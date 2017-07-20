package io.github.isaacbao.scaffold.system.exception;

/**
 * json字符串转换成java bean 失败抛的异常
 * Created by rongyang_lu on 2017/7/12.
 */
public class JsonParseException extends RuntimeException {

    private String jsonString;

    public JsonParseException(Exception e, String jsonString) {
        super("JSON转换失败");
        this.jsonString = jsonString;
    }

    public String getJsonString() {
        return jsonString;
    }

}
