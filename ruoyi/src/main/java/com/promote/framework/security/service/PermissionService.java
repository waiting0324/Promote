package com.promote.framework.security.service;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.promote.common.utils.ServletUtils;
import com.promote.common.utils.StringUtils;
import com.promote.framework.security.LoginUser;
import com.promote.project.system.domain.SysRole;

/**
 * RuoYi首創 自定義許可權實現，ss取自SpringSecurity首字母
 * 
 * @author ruoyi
 */
@Service("ss")
public class PermissionService
{
    /** 所有許可權標識 */
    private static final String ALL_PERMISSION = "*:*:*";

    /** 管理員角色許可權標識 */
    private static final String SUPER_ADMIN = "admin";

    private static final String ROLE_DELIMETER = ",";

    private static final String PERMISSION_DELIMETER = ",";

    @Autowired
    private TokenService tokenService;

    /**
     * 驗證使用者是否具備某許可權
     * 
     * @param permission 許可權字串
     * @return 使用者是否具備某許可權
     */
    public boolean hasPermi(String permission)
    {
        if (StringUtils.isEmpty(permission))
        {
            return false;
        }
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions()))
        {
            return false;
        }
        return hasPermissions(loginUser.getPermissions(), permission);
    }

    /**
     * 驗證使用者是否不具備某許可權，與 hasPermi邏輯相反
     *
     * @param permission 許可權字串
     * @return 使用者是否不具備某許可權
     */
    public boolean lacksPermi(String permission)
    {
        return hasPermi(permission) != true;
    }

    /**
     * 驗證使用者是否具有以下任意一個許可權
     *
     * @param permissions 以 PERMISSION_NAMES_DELIMETER 為分隔符的許可權列表
     * @return 使用者是否具有以下任意一個許可權
     */
    public boolean hasAnyPermi(String permissions)
    {
        if (StringUtils.isEmpty(permissions))
        {
            return false;
        }
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions()))
        {
            return false;
        }
        Set<String> authorities = loginUser.getPermissions();
        for (String permission : permissions.split(PERMISSION_DELIMETER))
        {
            if (permission != null && hasPermissions(authorities, permission))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 判斷使用者是否擁有某個角色
     * 
     * @param role 角色字串
     * @return 使用者是否具備某角色
     */
    public boolean hasRole(String role)
    {
        if (StringUtils.isEmpty(role))
        {
            return false;
        }
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getUser().getRoles()))
        {
            return false;
        }
        for (SysRole sysRole : loginUser.getUser().getRoles())
        {
            String roleKey = sysRole.getRoleKey();
            if (SUPER_ADMIN.contains(roleKey) || roleKey.contains(StringUtils.trim(role)))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 驗證使用者是否不具備某角色，與 isRole邏輯相反。
     *
     * @param role 角色名稱
     * @return 使用者是否不具備某角色
     */
    public boolean lacksRole(String role)
    {
        return hasRole(role) != true;
    }

    /**
     * 驗證使用者是否具有以下任意一個角色
     *
     * @param roles 以 ROLE_NAMES_DELIMETER 為分隔符的角色列表
     * @return 使用者是否具有以下任意一個角色
     */
    public boolean hasAnyRoles(String roles)
    {
        if (StringUtils.isEmpty(roles))
        {
            return false;
        }
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getUser().getRoles()))
        {
            return false;
        }
        for (String role : roles.split(ROLE_DELIMETER))
        {
            if (hasRole(role))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 判斷是否包含許可權
     * 
     * @param permissions 許可權列表
     * @param permission 許可權字串
     * @return 使用者是否具備某許可權
     */
    private boolean hasPermissions(Set<String> permissions, String permission)
    {
        return permissions.contains(ALL_PERMISSION) || permissions.contains(StringUtils.trim(permission));
    }
}
