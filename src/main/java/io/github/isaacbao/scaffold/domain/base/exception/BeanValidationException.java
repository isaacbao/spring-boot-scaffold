package io.github.isaacbao.scaffold.domain.base.exception;

/**
 * 数据校验失败抛出的异常
 * Created by rongyang_lu on 2017/7/10.
 */
public class BeanValidationException extends RuntimeException  {
    private static final long serialVersionUID = 1L;

    public BeanValidationException() {
        super();
    }

    public BeanValidationException(String s) {
        super(s);
    }

    public BeanValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanValidationException(Throwable cause) {
        super(cause);
    }
}
