package com.ruoyi.project.system.mapper;

import java.util.List;
import com.ruoyi.project.system.domain.SysRoleDept;

/**
 * 角色與部門關聯表 資料層
 * 
 * @author ruoyi
 */
public interface SysRoleDeptMapper
{
    /**
     * 通過角色ID刪除角色和部門關聯
     * 
     * @param roleId 角色ID
     * @return 結果
     */
    public int deleteRoleDeptByRoleId(Long roleId);

    /**
     * 批量刪除角色部門關聯資訊
     * 
     * @param ids 需要刪除的資料ID
     * @return 結果
     */
    public int deleteRoleDept(Long[] ids);

    /**
     * 查詢部門使用數量
     * 
     * @param deptId 部門ID
     * @return 結果
     */
    public int selectCountRoleDeptByDeptId(Long deptId);

    /**
     * 批量新增角色部門資訊
     * 
     * @param roleDeptList 角色部門列表
     * @return 結果
     */
    public int batchRoleDept(List<SysRoleDept> roleDeptList);
}
