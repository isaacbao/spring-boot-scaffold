package io.github.isaacbao.scaffold.pretreatment;

import io.github.isaacbao.scaffold.domain.base.bean.ResponseInfo;
import io.github.isaacbao.scaffold.domain.entity.User;
import io.github.isaacbao.scaffold.pretreatment.handler.CommonHandler;
import io.github.isaacbao.scaffold.system.exception.AuthenticationException;
import io.github.isaacbao.scaffold.util.ParamMapUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 对于请求的通用预处理，获取用户并执行对应的函数，执行完毕后将结果塞入ResponseInfo
 * Created by rongyang_lu on 2017/7/5.
 */
@Component
public class CommonPretreatment {
    private static Logger logger = LogManager.getLogger();
    public <E> ResponseInfo pretreat(HttpServletRequest request, CommonHandler<E> handler) {
        User user = null;
        try {
            user = (User) request.getSession().getAttribute("user");
        } catch (Exception e) {
            logger.error("",e);
            throw new AuthenticationException("校验登录状态失败");
        }
        Map<String, String> pm = ParamMapUtil.getParameterMap(request);
        E result = handler.execute(request, user, pm);
        return ResponseInfo.success(result);
    }
}
