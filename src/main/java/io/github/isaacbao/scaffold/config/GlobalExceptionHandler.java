package io.github.isaacbao.scaffold.config;

import io.github.isaacbao.scaffold.domain.base.bean.ResponseInfo;
import io.github.isaacbao.scaffold.system.exception.JsonParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一处理异常
 * Created by rongyang_lu on 2017/7/14.
 */
@ControllerAdvice
class GlobalExceptionHandler {
    private static Logger logger = LogManager.getLogger();

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseInfo defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        logger.error("", e);
        return ResponseInfo.exception(e);
    }

    @ExceptionHandler(value = JsonParseException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseInfo JsonParseExceptionHandler(HttpServletRequest req, JsonParseException e) throws Exception {
        logger.error("json转换失败：" + e.getJsonString());
        return ResponseInfo.exception(e);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseInfo JsonParseExceptionHandler(HttpServletRequest req, IllegalArgumentException e) throws Exception {
        logger.error("参数不合法：" + e.getMessage());
        return ResponseInfo.argumentIllegal(e);
    }
}