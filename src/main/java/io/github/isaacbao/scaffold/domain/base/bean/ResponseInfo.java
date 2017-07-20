package io.github.isaacbao.scaffold.domain.base.bean;

import io.github.isaacbao.scaffold.util.StringUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 通用的response数据
 * Created by rongyang_lu on 2017/7/5.
 */
@ApiModel(value = "ResponseInfo", description = "通用的response数据,所有返回结果都包在ResponseInfo的data里")
public class ResponseInfo<E> {
    public static final int CODE_SUCCESS = 0;
    public static final int CODE_EXCEPTION = 1;
    public static final int INVALID_OPENID = 2;
    public static final int ARGUMENT_EXCPETION = 3;

    @ApiModelProperty(notes = "返回码")
    private int code;
    @ApiModelProperty(notes = "返回信息")
    private String message;
    @ApiModelProperty(notes = "返回数据")
    private E data;

    public ResponseInfo(int code, String message, E data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

//    public static ResponseInfo autoGenExceptionResponse(Exception e) {
//        if(e instanceof JsonParseException){
//
//        }
//        return exception(e);
//    }

    public static ResponseInfo exception(Exception e) {
        String message = e.getMessage();
        if (StringUtil.isEmpty(message)) {
            message = "服务器内部异常";
        }
        return new ResponseInfo(CODE_EXCEPTION, message, null);
    }

    public ResponseInfo() {
        super();
    }

    public static <E> ResponseInfo success(E data) {
        return new ResponseInfo(CODE_SUCCESS, "success", data);
    }

    public static <E> ResponseInfo argumentIllegal(IllegalArgumentException e) {
        return new ResponseInfo(ARGUMENT_EXCPETION, e.getMessage(), null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResponseInfo{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
