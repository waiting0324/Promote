package com.promote.project.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.promote.project.system.domain.SysUserRole;

/**
 * 使用者與角色關聯表 資料層
 * 
 * @author ruoyi
 */
public interface SysUserRoleMapper
{
    /**
     * 通過使用者ID刪除使用者和角色關聯
     * 
     * @param userId 使用者ID
     * @return 結果
     */
    public int deleteUserRoleByUserId(Long userId);

    /**
     * 批量刪除使用者和角色關聯
     * 
     * @param ids 需要刪除的資料ID
     * @return 結果
     */
    public int deleteUserRole(Long[] ids);

    /**
     * 通過角色ID查詢角色使用數量
     * 
     * @param roleId 角色ID
     * @return 結果
     */
    public int countUserRoleByRoleId(Long roleId);

    /**
     * 批量新增使用者角色資訊
     * 
     * @param userRoleList 使用者角色列表
     * @return 結果
     */
    public int batchUserRole(List<SysUserRole> userRoleList);

    /**
     * 刪除使用者和角色關聯資訊
     * 
     * @param userRole 使用者和角色關聯資訊
     * @return 結果
     */
    public int deleteUserRoleInfo(SysUserRole userRole);

    /**
     * 批量取消授權使用者角色
     * 
     * @param roleId 角色ID
     * @param userIds 需要刪除的使用者資料ID
     * @return 結果
     */
    public int deleteUserRoleInfos(@Param("roleId") Long roleId, @Param("userIds") Long[] userIds);
}
