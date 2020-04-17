package com.ruoyi.project.system.service;

import java.util.List;
import java.util.Set;
import com.ruoyi.framework.web.domain.TreeSelect;
import com.ruoyi.project.system.domain.SysMenu;
import com.ruoyi.project.system.domain.vo.RouterVo;

/**
 * 選單 業務層
 * 
 * @author ruoyi
 */
public interface ISysMenuService
{
    /**
     * 根據使用者查詢系統選單列表
     * 
     * @param userId 使用者ID
     * @return 選單列表
     */
    public List<SysMenu> selectMenuList(Long userId);

    /**
     * 根據使用者查詢系統選單列表
     * 
     * @param menu 選單資訊
     * @param userId 使用者ID
     * @return 選單列表
     */
    public List<SysMenu> selectMenuList(SysMenu menu, Long userId);

    /**
     * 根據使用者ID查詢許可權
     * 
     * @param userId 使用者ID
     * @return 許可權列表
     */
    public Set<String> selectMenuPermsByUserId(Long userId);

    /**
     * 根據使用者ID查詢選單樹資訊
     * 
     * @param userId 使用者ID
     * @return 選單列表
     */
    public List<SysMenu> selectMenuTreeByUserId(Long userId);

    /**
     * 根據角色ID查詢選單樹資訊
     * 
     * @param roleId 角色ID
     * @return 選中選單列表
     */
    public List<Integer> selectMenuListByRoleId(Long roleId);

    /**
     * 構建前端路由所需要的選單
     * 
     * @param menus 選單列表
     * @return 路由列表
     */
    public List<RouterVo> buildMenus(List<SysMenu> menus);

    /**
     * 構建前端所需要樹結構
     * 
     * @param menus 選單列表
     * @return 樹結構列表
     */
    public List<SysMenu> buildMenuTree(List<SysMenu> menus);

    /**
     * 構建前端所需要下拉樹結構
     * 
     * @param menus 選單列表
     * @return 下拉樹結構列表
     */
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus);

    /**
     * 根據選單ID查詢資訊
     * 
     * @param menuId 選單ID
     * @return 選單資訊
     */
    public SysMenu selectMenuById(Long menuId);

    /**
     * 是否存在選單子節點
     * 
     * @param menuId 選單ID
     * @return 結果 true 存在 false 不存在
     */
    public boolean hasChildByMenuId(Long menuId);

    /**
     * 查詢選單是否存在角色
     * 
     * @param menuId 選單ID
     * @return 結果 true 存在 false 不存在
     */
    public boolean checkMenuExistRole(Long menuId);

    /**
     * 新增儲存選單資訊
     * 
     * @param menu 選單資訊
     * @return 結果
     */
    public int insertMenu(SysMenu menu);

    /**
     * 修改儲存選單資訊
     * 
     * @param menu 選單資訊
     * @return 結果
     */
    public int updateMenu(SysMenu menu);

    /**
     * 刪除選單管理資訊
     * 
     * @param menuId 選單ID
     * @return 結果
     */
    public int deleteMenuById(Long menuId);

    /**
     * 校驗選單名稱是否唯一
     * 
     * @param menu 選單資訊
     * @return 結果
     */
    public String checkMenuNameUnique(SysMenu menu);
}
