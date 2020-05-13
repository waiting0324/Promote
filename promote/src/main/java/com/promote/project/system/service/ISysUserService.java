package com.promote.project.system.service;

import com.promote.project.system.domain.SysUser;

import java.util.List;

/**
 * 使用者 業務層
 * 
 * @author ruoyi
 */
public interface ISysUserService
{
    /**
     * 根據條件分頁查詢使用者列表
     * 
     * @param user 使用者資訊
     * @return 使用者資訊集合資訊
     */
    public List<SysUser> selectUserList(SysUser user);

    /**
     * 通過使用者名稱查詢使用者
     * 
     * @param userName 使用者名稱
     * @return 使用者物件資訊
     */
    public SysUser selectUserByUserName(String userName);

    /**
     * 通過使用者ID查詢使用者
     * 
     * @param userId 使用者ID
     * @return 使用者物件資訊
     */
    public SysUser selectUserById(Long userId);

    /**
     * 根據使用者ID查詢使用者所屬角色組
     * 
     * @param userName 使用者名稱
     * @return 結果
     */
    public String selectUserRoleGroup(String userName);

    /**
     * 根據使用者ID查詢使用者所屬崗位組
     * 
     * @param userName 使用者名稱
     * @return 結果
     */
    public String selectUserPostGroup(String userName);

    /**
     * 校驗使用者名稱是否唯一
     * 
     * @param userName 使用者名稱
     * @return 結果
     */
    public String checkUserNameUnique(String userName);

    /**
     * 校驗手機號碼是否唯一
     *
     * @param user 使用者資訊
     * @return 結果
     */
    public String checkPhoneUnique(SysUser user);

    /**
     * 校驗email是否唯一
     *
     * @param user 使用者資訊
     * @return 結果
     */
    public String checkEmailUnique(SysUser user);

    /**
     * 校驗使用者是否允許操作
     * 
     * @param user 使用者資訊
     */
    public void checkUserAllowed(SysUser user);

    /**
     * 新增使用者資訊
     * 
     * @param user 使用者資訊
     * @return 結果
     */
    public int insertUser(SysUser user);

    /**
     * 修改使用者資訊
     * 
     * @param user 使用者資訊
     * @return 結果
     */
    public int updateUser(SysUser user);

    /**
     * 修改使用者狀態
     * 
     * @param user 使用者資訊
     * @return 結果
     */
    public int updateUserStatus(SysUser user);

    /**
     * 修改使用者基本資訊
     * 
     * @param user 使用者資訊
     * @return 結果
     */
    public int updateUserProfile(SysUser user);

    /**
     * 修改使用者頭像
     * 
     * @param userName 使用者名稱
     * @param avatar 頭像地址
     * @return 結果
     */
    public boolean updateUserAvatar(String userName, String avatar);

    /**
     * 重置使用者密碼
     * 
     * @param user 使用者資訊
     * @return 結果
     */
    public int resetPwd(SysUser user);

    /**
     * 重置使用者密碼
     * 
     * @param userName 使用者名稱
     * @param password 密碼
     * @return 結果
     */
    public int resetUserPwd(String userName, String password);

    /**
     * 通過使用者ID刪除使用者
     * 
     * @param userId 使用者ID
     * @return 結果
     */
    public int deleteUserById(Long userId);

    /**
     * 批量刪除使用者資訊
     * 
     * @param userIds 需要刪除的使用者ID
     * @return 結果
     */
    public int deleteUserByIds(Long[] userIds);

    /**
     * 匯入使用者資料
     * 
     * @param userList 使用者資料列表
     * @param isUpdateSupport 是否更新支援，如果已存在，則進行更新資料
     * @param operName 操作使用者
     * @return 結果
     */
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName);

    /**
     * 根據身分證查詢 消費者
     * @param identity 身分證
     * @return 使用者Bean
     */
    public List<SysUser> selectConsumerByIdentity(String identity);

    /**
     * 根據帳號及身分證查使用者
     *
     * @param username
     * @param identity
     * @return
     */
    public SysUser getByUnameIndentity(String username,String identity);

}
