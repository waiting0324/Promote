package com.ruoyi.project.system.mapper;

import java.util.List;
import com.ruoyi.project.system.domain.SysRoleMenu;

/**
 * 角色與選單關聯表 資料層
 * 
 * @author ruoyi
 */
public interface SysRoleMenuMapper
{
    /**
     * 查詢選單使用數量
     * 
     * @param menuId 選單ID
     * @return 結果
     */
    public int checkMenuExistRole(Long menuId);

    /**
     * 通過角色ID刪除角色和選單關聯
     * 
     * @param roleId 角色ID
     * @return 結果
     */
    public int deleteRoleMenuByRoleId(Long roleId);

    /**
     * 批量新增角色選單資訊
     * 
     * @param roleMenuList 角色選單列表
     * @return 結果
     */
    public int batchRoleMenu(List<SysRoleMenu> roleMenuList);
}
