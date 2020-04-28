package com.promote.project.system.mapper;

import com.promote.project.promote.domain.ProWhitelist;
import com.promote.project.system.domain.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 使用者表 資料層
 *
 * @author ruoyi
 */
public interface SysUserMapper {
    /**
     * 根據條件分頁查詢使用者列表
     *
     * @param sysUser 使用者資訊
     * @return 使用者資訊集合資訊
     */
    public List<SysUser> selectUserList(SysUser sysUser);

    /**
     * 通過使用者名稱查詢使用者
     *
     * @param userName 使用者名稱
     * @return 使用者物件資訊
     */
    public SysUser selectUserByUsername(String username);

    /**
     * 通過使用者ID查詢使用者
     *
     * @param userId 使用者ID
     * @return 使用者物件資訊
     */
    public SysUser selectUserById(Long userId);

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
     * 修改使用者頭像
     *
     * @param username 使用者名稱
     * @param avatar   頭像地址
     * @return 結果
     */
    public int updateUserAvatar(@Param("username") String username, @Param("avatar") String avatar);

    /**
     * 重置使用者密碼
     *
     * @param username 使用者名稱
     * @param password 密碼
     * @return 結果
     */
    public int resetUserPwd(@Param("username") String username, @Param("password") String password);

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
     * 校驗使用者名稱是否唯一
     *
     * @param username 使用者名稱
     * @return 結果
     */
    public int checkUsernameUnique(String username);

    /**
     * 校驗手機號碼是否唯一
     *
     * @param phonenumber 手機號碼
     * @return 結果
     */
    public SysUser checkPhoneUnique(String phonenumber);

    /**
     * 校驗email是否唯一
     *
     * @param email 使用者郵箱
     * @return 結果
     */
    public SysUser checkEmailUnique(String email);

    /**
     * 新增使用者資訊
     *
     * @param proWhitelist 白名單資訊
     * @return 結果
     */
    public int insertUserByProWhitelist(ProWhitelist proWhitelist);

    /**
     * 旅宿業者變更密碼
     *
     * @param userId 統編
     * @param birthDay 生日
     * @param newPwd 新密碼
     * @return 結果
     */
    public int resetPwd(@Param("userId") Long userId, @Param("birthDay") String birthDay, @Param("newPwd") String newPwd);

    /**
     * 通過使用者帳號及生日查詢使用者
     *
     * @param sysUser 使用者資料
     * @return 結果
     */
    public SysUser selectUserByIdBirthday(SysUser sysUser);


    /**
     * 根據身分證查詢 消費者
     * @param identity 身分證
     * @return 使用者Bean
     */
    public List<SysUser> selectConsumerByIdentity(String identity);
}
