package com.promote.project.system.service.impl;

import com.promote.common.constant.UserConstants;
import com.promote.common.exception.CustomException;
import com.promote.common.utils.SecurityUtils;
import com.promote.common.utils.StringUtils;
import com.promote.framework.aspectj.lang.annotation.DataScope;
import com.promote.project.system.domain.*;
import com.promote.project.system.mapper.*;
import com.promote.project.system.service.ISysConfigService;
import com.promote.project.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用者 業務層處理
 *
 * @author ruoyi
 */
@Service
public class SysUserServiceImpl implements ISysUserService {
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

    @Autowired
    private ISysConfigService configService;

    /**
     * 根據條件分頁查詢使用者列表
     *
     * @param user 使用者資訊
     * @return 使用者資訊集合資訊
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUserList(SysUser user) {
        return userMapper.selectUserList(user);
    }

    /**
     * 通過使用者名稱查詢使用者
     *
     * @param userName 使用者名稱
     * @return 使用者物件資訊
     */
    @Override
    public SysUser selectUserByUserName(String userName) {
        return userMapper.selectUserByUsername(userName);
    }

    /**
     * 通過使用者ID查詢使用者
     *
     * @param userId 使用者ID
     * @return 使用者物件資訊
     */
    @Override
    public SysUser selectUserById(Long userId) {
        return userMapper.selectUserById(userId);
    }

    /**
     * 查詢使用者所屬角色組
     *
     * @param userName 使用者名稱
     * @return 結果
     */
    @Override
    public String selectUserRoleGroup(String userName) {
        List<SysRole> list = roleMapper.selectRolesByUserName(userName);
        StringBuffer idsStr = new StringBuffer();
        for (SysRole role : list) {
            idsStr.append(role.getRoleName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 查詢使用者所屬崗位組
     *
     * @param userName 使用者名稱
     * @return 結果
     */
    @Override
    public String selectUserPostGroup(String userName) {
        List<SysPost> list = postMapper.selectPostsByUserName(userName);
        StringBuffer idsStr = new StringBuffer();
        for (SysPost post : list) {
            idsStr.append(post.getPostName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 校驗使用者名稱是否唯一
     *
     * @param userName 使用者名稱
     * @return 結果
     */
    @Override
    public String checkUserNameUnique(String userName) {
        int count = userMapper.checkUsernameUnique(userName);
        if (count > 0) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校驗使用者名稱是否唯一
     *
     * @param user 使用者資訊
     * @return
     */
    @Override
    public String checkPhoneUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校驗email是否唯一
     *
     * @param user 使用者資訊
     * @return
     */
    @Override
    public String checkEmailUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校驗使用者是否允許操作
     *
     * @param user 使用者資訊
     */
    public void checkUserAllowed(SysUser user) {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin()) {
            throw new CustomException("不允許操作超級管理員使用者");
        }
    }

    /**
     * 新增儲存使用者資訊
     *
     * @param user 使用者資訊
     * @return 結果
     */
    @Override
    @Transactional
    public int insertUser(SysUser user) {
        // 新增使用者資訊
        int rows = userMapper.insertUser(user);
        // 新增使用者崗位關聯
        insertUserPost(user);
        // 新增使用者與角色管理
        insertUserRole(user);
        return rows;
    }

    /**
     * 修改儲存使用者資訊
     *
     * @param user 使用者資訊
     * @return 結果
     */
    @Override
    @Transactional
    public int updateUser(SysUser user) {
        Long userId = user.getUserId();
        // 刪除使用者與角色關聯
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 新增使用者與角色管理
        insertUserRole(user);
        // 刪除使用者與崗位關聯
        userPostMapper.deleteUserPostByUserId(userId);
        // 新增使用者與崗位管理
        insertUserPost(user);
        return userMapper.updateUser(user);
    }

    /**
     * 修改使用者狀態
     *
     * @param user 使用者資訊
     * @return 結果
     */
    @Override
    public int updateUserStatus(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 修改使用者基本資訊
     *
     * @param user 使用者資訊
     * @return 結果
     */
    @Override
    public int updateUserProfile(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 修改使用者頭像
     *
     * @param userName 使用者ID
     * @param avatar 頭像地址
     * @return 結果
     */
    public boolean updateUserAvatar(String userName, String avatar) {
        return userMapper.updateUserAvatar(userName, avatar) > 0;
    }

    /**
     * 重置使用者密碼
     *
     * @param user 使用者資訊
     * @return 結果
     */
    @Override
    public int resetPwd(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 重置使用者密碼
     *
     * @param userName 使用者名稱
     * @param password 密碼
     * @return 結果
     */
    @Override
    public int resetUserPwd(String userName, String password) {
        return userMapper.resetUserPwd(userName, password);
    }

    /**
     * 新增使用者角色資訊
     *
     * @param user 使用者物件
     */
    public void insertUserRole(SysUser user) {
        Long[] roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles)) {
            // 新增使用者與角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Long roleId : roles) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(user.getUserId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0) {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    /**
     * 新增使用者崗位資訊
     *
     * @param user 使用者物件
     */
    public void insertUserPost(SysUser user) {
        Long[] posts = user.getPostIds();
        if (StringUtils.isNotNull(posts)) {
            // 新增使用者與崗位管理
            List<SysUserPost> list = new ArrayList<SysUserPost>();
            for (Long postId : posts) {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            if (list.size() > 0) {
                userPostMapper.batchUserPost(list);
            }
        }
    }

    /**
     * 通過使用者ID刪除使用者
     *
     * @param userId 使用者ID
     * @return 結果
     */
    @Override
    public int deleteUserById(Long userId) {
        // 刪除使用者與角色關聯
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 刪除使用者與崗位表
        userPostMapper.deleteUserPostByUserId(userId);
        return userMapper.deleteUserById(userId);
    }

    /**
     * 批量刪除使用者資訊
     *
     * @param userIds 需要刪除的使用者ID
     * @return 結果
     */
    public int deleteUserByIds(Long[] userIds) {
        for (Long userId : userIds) {
            checkUserAllowed(new SysUser(userId));
        }
        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * 匯入使用者資料
     *
     * @param userList        使用者資料列表
     * @param isUpdateSupport 是否更新支援，如果已存在，則進行更新資料
     * @param operName        操作使用者
     * @return 結果
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(userList) || userList.size() == 0) {
            throw new CustomException("匯入使用者資料不能為空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = configService.selectConfigByKey("sys.user.initPassword");
        for (SysUser user : userList) {
            try {
                // 驗證是否存在這個使用者
                SysUser u = userMapper.selectUserByUsername(user.getUsername());
                if (StringUtils.isNull(u)) {
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    this.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、帳號 " + user.getUsername() + " 匯入成功");
                } else if (isUpdateSupport) {
                    user.setUpdateBy(operName);
                    this.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、帳號 " + user.getUsername() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、帳號 " + user.getUsername() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、帳號 " + user.getUsername() + " 匯入失敗：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，匯入失敗！共 " + failureNum + " 條資料格式不正確，錯誤如下：");
            throw new CustomException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，資料已全部匯入成功！共 " + successNum + " 條，資料如下：");
        }
        return successMsg.toString();
    }
}
