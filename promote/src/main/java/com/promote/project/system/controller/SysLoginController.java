package com.promote.project.system.controller;

import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import com.promote.common.constant.Constants;
import com.promote.common.utils.ServletUtils;
import com.promote.framework.security.LoginUser;
import com.promote.framework.security.service.SysLoginService;
import com.promote.framework.security.service.SysPermissionService;
import com.promote.framework.security.service.TokenService;
import com.promote.framework.web.domain.AjaxResult;
import com.promote.project.system.domain.SysMenu;
import com.promote.project.system.domain.SysUser;
import com.promote.project.system.service.ISysMenuService;

/**
 * 登入驗證
 * 
 * @author ruoyi
 */
@RestController
public class SysLoginController
{
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    /**
     * 登入方法
     * 
     * @param username 使用者名稱
     * @param password 密碼
     * @param captcha 驗證碼
     * @param uuid 唯一標識
     * @return 結果
     */
    @PostMapping("/login")
    public AjaxResult login(String username, String password, String code, String uuid)
    {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(username, password, code, uuid);
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 獲取使用者資訊
     * 
     * @return 使用者資訊
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 許可權集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 獲取路由資訊
     * 
     * @return 路由資訊
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 使用者資訊
        SysUser user = loginUser.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId());
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
