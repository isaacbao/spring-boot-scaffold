package io.github.isaacbao.scaffold.system.exception;

/**
 * 权限错误，如：session失效用户未登录、第三方应用appid appescret错误
 * Created by rongyang_lu on 2017/7/20.
 */
public class AuthenticationException extends RuntimeException{
    public AuthenticationException() {
    }

    public AuthenticationException(Throwable cause) {
        super(cause);
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
