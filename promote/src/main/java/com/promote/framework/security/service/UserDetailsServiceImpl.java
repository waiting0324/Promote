package com.promote.framework.security.service;

import com.promote.common.constant.RoleConstants;
import com.promote.common.enums.UserStatus;
import com.promote.common.exception.BaseException;
import com.promote.common.utils.StringUtils;
import com.promote.framework.security.LoginUser;
import com.promote.project.promote.mapper.ConsumerInfoMapper;
import com.promote.project.promote.mapper.HostelInfoMapper;
import com.promote.project.promote.mapper.StoreInfoMapper;
import com.promote.project.system.domain.SysRole;
import com.promote.project.system.domain.SysUser;
import com.promote.project.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 使用者驗證處理
 *
 * @author ruoyi
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private HostelInfoMapper hostelInfoMapper;

    @Autowired
    private StoreInfoMapper storeInfoMapper;

    @Autowired
    private ConsumerInfoMapper consumerInfoMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        SysUser user = userService.selectUserByUserName(username);
        if (StringUtils.isNull(user))
        {
            log.info("登入使用者：{} 不存在.", username);
            throw new UsernameNotFoundException("登入使用者：" + username + " 不存在");
        }
        else if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            log.info("登入使用者：{} 已被刪除.", username);
            throw new BaseException("對不起，您的帳號：" + username + " 已被刪除");
        }
        else if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            log.info("登入使用者：{} 已被停用.", username);
            throw new BaseException("對不起，您的帳號：" + username + " 已停用");
        }

        // 根據登入者擁有的角色，設置相關基本資訊
        setUserInfo(user);

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user)
    {
        return new LoginUser(user, permissionService.getMenuPermission(user));
    }

    public void setUserInfo(SysUser user) {

        // 根據登入者擁有的角色，設置相關基本資訊
        List<SysRole> roles = user.getRoles();
        for (SysRole role : roles) {
            // 旅宿業者
            if (RoleConstants.HOSTEL_ROLE_ID.equals(role.getRoleId())) {
                user.setHostelInfo(hostelInfoMapper.selectHostelInfoById(user.getUserId()));
            }
            // 商家
            else if (RoleConstants.STORE_ROLE_ID.equals(role.getRoleId())) {
                user.setStoreInfo(storeInfoMapper.selectStoreInfoById(user.getUserId()));
            }
            // 消費者
            else if (RoleConstants.CONSUMER_ROLE_ID.equals(role.getRoleId())) {
                user.setConsumerInfo(consumerInfoMapper.selectConsumerInfoById(user.getUserId()));
            }
        }
    }
}
