package io.github.isaacbao.scaffold.system.annotation;

import java.lang.annotation.*;

/**
 * 字段是否为元字段，元字段是指由系统生成并控制，不能由客户端更改的字段
 * 主要用在FieldStter中排除不能被客户端更改的字段
 * Created by Thinkpad on 2015/11/22.
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MetaField {
    String value() default "";
}


