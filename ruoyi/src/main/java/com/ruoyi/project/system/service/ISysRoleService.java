package com.ruoyi.project.system.service;

import java.util.List;
import java.util.Set;
import com.ruoyi.project.system.domain.SysRole;

/**
 * 角色業務層
 * 
 * @author ruoyi
 */
public interface ISysRoleService
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
     * @return 許可權列表
     */
    public Set<String> selectRolePermissionByUserId(Long userId);

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
     * 校驗角色名稱是否唯一
     * 
     * @param role 角色資訊
     * @return 結果
     */
    public String checkRoleNameUnique(SysRole role);

    /**
     * 校驗角色許可權是否唯一
     * 
     * @param role 角色資訊
     * @return 結果
     */
    public String checkRoleKeyUnique(SysRole role);

    /**
     * 校驗角色是否允許操作
     * 
     * @param role 角色資訊
     */
    public void checkRoleAllowed(SysRole role);

    /**
     * 通過角色ID查詢角色使用數量
     * 
     * @param roleId 角色ID
     * @return 結果
     */
    public int countUserRoleByRoleId(Long roleId);

    /**
     * 新增儲存角色資訊
     * 
     * @param role 角色資訊
     * @return 結果
     */
    public int insertRole(SysRole role);

    /**
     * 修改儲存角色資訊
     * 
     * @param role 角色資訊
     * @return 結果
     */
    public int updateRole(SysRole role);

    /**
     * 修改角色狀態
     * 
     * @param role 角色資訊
     * @return 結果
     */
    public int updateRoleStatus(SysRole role);

    /**
     * 修改資料許可權資訊
     * 
     * @param role 角色資訊
     * @return 結果
     */
    public int authDataScope(SysRole role);

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
