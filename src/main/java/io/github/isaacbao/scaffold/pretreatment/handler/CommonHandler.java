package io.github.isaacbao.scaffold.pretreatment.handler;

import io.github.isaacbao.scaffold.domain.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 处理来自request请求的handler
 * Created by rongyang_lu on 2017/7/5.
 */
public interface CommonHandler<E> {
    E execute(HttpServletRequest request, User user, Map<String,String> pm);
}
