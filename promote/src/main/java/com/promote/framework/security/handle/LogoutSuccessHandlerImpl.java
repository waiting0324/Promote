package com.promote.framework.security.handle;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import com.alibaba.fastjson.JSON;
import com.promote.common.constant.Constants;
import com.promote.common.constant.HttpStatus;
import com.promote.common.utils.ServletUtils;
import com.promote.common.utils.StringUtils;
import com.promote.framework.manager.AsyncManager;
import com.promote.framework.manager.factory.AsyncFactory;
import com.promote.framework.security.LoginUser;
import com.promote.framework.security.service.TokenService;
import com.promote.framework.web.domain.AjaxResult;

/**
 * 自定義退出處理類 返回成功
 * 
 * @author ruoyi
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler
{
    @Autowired
    private TokenService tokenService;

    /**
     * 退出處理
     * 
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException
    {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser))
        {
            String userName = loginUser.getUsername();
            // 刪除使用者快取記錄
            tokenService.delLoginUser(loginUser.getToken());
            // 記錄使用者退出日誌
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGOUT, "退出成功"));
        }
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error(HttpStatus.SUCCESS, "退出成功")));
    }
}
