package com.promote.project.system.mapper;

import java.util.List;
import com.promote.project.system.domain.SysRole;

/**
 * 角色表 資料層
 * 
 * @author ruoyi
 */
public interface SysRoleMapper
{
    /**
     * 根據條件分頁查詢角色資料
     * 
     * @param role 角色資訊
     * @return 角色資料集合資訊
     */
    public List<SysRole> selectRoleList(SysRole role);

    /**
     * 根據使用者ID查詢角色
     * 
     * @param userId 使用者ID
     * @return 角色列表
     */
    public List<SysRole> selectRolePermissionByUserId(Long userId);

    /**
     * 查詢所有角色
     * 
     * @return 角色列表
     */
    public List<SysRole> selectRoleAll();

    /**
     * 根據使用者ID獲取角色選擇框列表
     * 
     * @param userId 使用者ID
     * @return 選中角色ID列表
     */
    public List<Integer> selectRoleListByUserId(Long userId);

    /**
     * 通過角色ID查詢角色
     * 
     * @param roleId 角色ID
     * @return 角色物件資訊
     */
    public SysRole selectRoleById(Long roleId);

    /**
     * 根據使用者ID查詢角色
     * 
     * @param userName 使用者名稱
     * @return 角色列表
     */
    public List<SysRole> selectRolesByUserName(String userName);

    /**
     * 校驗角色名稱是否唯一
     * 
     * @param roleName 角色名稱
     * @return 角色資訊
     */
    public SysRole checkRoleNameUnique(String roleName);

    /**
     * 校驗角色許可權是否唯一
     * 
     * @param roleKey 角色許可權
     * @return 角色資訊
     */
    public SysRole checkRoleKeyUnique(String roleKey);

    /**
     * 修改角色資訊
     * 
     * @param role 角色資訊
     * @return 結果
     */
    public int updateRole(SysRole role);

    /**
     * 新增角色資訊
     * 
     * @param role 角色資訊
     * @return 結果
     */
    public int insertRole(SysRole role);

    /**
     * 通過角色ID刪除角色
     * 
     * @param roleId 角色ID
     * @return 結果
     */
    public int deleteRoleById(Long roleId);

    /**
     * 批量刪除角色資訊
     * 
     * @param roleIds 需要刪除的角色ID
     * @return 結果
     */
    public int deleteRoleByIds(Long[] roleIds);
}
