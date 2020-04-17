package com.promote.project.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.promote.common.constant.UserConstants;
import com.promote.common.exception.CustomException;
import com.promote.common.utils.StringUtils;
import com.promote.framework.aspectj.lang.annotation.DataScope;
import com.promote.project.system.domain.SysRole;
import com.promote.project.system.domain.SysRoleDept;
import com.promote.project.system.domain.SysRoleMenu;
import com.promote.project.system.mapper.SysRoleDeptMapper;
import com.promote.project.system.mapper.SysRoleMapper;
import com.promote.project.system.mapper.SysRoleMenuMapper;
import com.promote.project.system.mapper.SysUserRoleMapper;
import com.promote.project.system.service.ISysRoleService;

/**
 * 角色 業務層處理
 * 
 * @author ruoyi
 */
@Service
public class SysRoleServiceImpl implements ISysRoleService
{
    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysRoleDeptMapper roleDeptMapper;

    /**
     * 根據條件分頁查詢角色資料
     * 
     * @param role 角色資訊
     * @return 角色資料集合資訊
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysRole> selectRoleList(SysRole role)
    {
        return roleMapper.selectRoleList(role);
    }

    /**
     * 根據使用者ID查詢許可權
     * 
     * @param userId 使用者ID
     * @return 許可權列表
     */
    @Override
    public Set<String> selectRolePermissionByUserId(Long userId)
    {
        List<SysRole> perms = roleMapper.selectRolePermissionByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms)
        {
            if (StringUtils.isNotNull(perm))
            {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 查詢所有角色
     * 
     * @return 角色列表
     */
    public List<SysRole> selectRoleAll()
    {
        return roleMapper.selectRoleAll();
    }

    /**
     * 根據使用者ID獲取角色選擇框列表
     * 
     * @param userId 使用者ID
     * @return 選中角色ID列表
     */
    public List<Integer> selectRoleListByUserId(Long userId)
    {
        return roleMapper.selectRoleListByUserId(userId);
    }

    /**
     * 通過角色ID查詢角色
     * 
     * @param roleId 角色ID
     * @return 角色物件資訊
     */
    public SysRole selectRoleById(Long roleId)
    {
        return roleMapper.selectRoleById(roleId);
    }

    /**
     * 校驗角色名稱是否唯一
     * 
     * @param role 角色資訊
     * @return 結果
     */
    @Override
    public String checkRoleNameUnique(SysRole role)
    {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = roleMapper.checkRoleNameUnique(role.getRoleName());
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校驗角色許可權是否唯一
     * 
     * @param role 角色資訊
     * @return 結果
     */
    @Override
    public String checkRoleKeyUnique(SysRole role)
    {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = roleMapper.checkRoleKeyUnique(role.getRoleKey());
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校驗角色是否允許操作
     * 
     * @param role 角色資訊
     */
    public void checkRoleAllowed(SysRole role)
    {
        if (StringUtils.isNotNull(role.getRoleId()) && role.isAdmin())
        {
            throw new CustomException("不允許操作超級管理員角色");
        }
    }

    /**
     * 通過角色ID查詢角色使用數量
     * 
     * @param roleId 角色ID
     * @return 結果
     */
    @Override
    public int countUserRoleByRoleId(Long roleId)
    {
        return userRoleMapper.countUserRoleByRoleId(roleId);
    }

    /**
     * 新增儲存角色資訊
     * 
     * @param role 角色資訊
     * @return 結果
     */
    @Override
    @Transactional
    public int insertRole(SysRole role)
    {
        // 新增角色資訊
        roleMapper.insertRole(role);
        return insertRoleMenu(role);
    }

    /**
     * 修改儲存角色資訊
     * 
     * @param role 角色資訊
     * @return 結果
     */
    @Override
    @Transactional
    public int updateRole(SysRole role)
    {
        // 修改角色資訊
        roleMapper.updateRole(role);
        // 刪除角色與選單關聯
        roleMenuMapper.deleteRoleMenuByRoleId(role.getRoleId());
        return insertRoleMenu(role);
    }

    /**
     * 修改角色狀態
     * 
     * @param role 角色資訊
     * @return 結果
     */
    public int updateRoleStatus(SysRole role)
    {
        return roleMapper.updateRole(role);
    }

    /**
     * 修改資料許可權資訊
     * 
     * @param role 角色資訊
     * @return 結果
     */
    @Override
    @Transactional
    public int authDataScope(SysRole role)
    {
        // 修改角色資訊
        roleMapper.updateRole(role);
        // 刪除角色與部門關聯
        roleDeptMapper.deleteRoleDeptByRoleId(role.getRoleId());
        // 新增角色和部門資訊（資料許可權）
        return insertRoleDept(role);
    }

    /**
     * 新增角色選單資訊
     * 
     * @param role 角色物件
     */
    public int insertRoleMenu(SysRole role)
    {
        int rows = 1;
        // 新增使用者與角色管理
        List<SysRoleMenu> list = new ArrayList<SysRoleMenu>();
        for (Long menuId : role.getMenuIds())
        {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(role.getRoleId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (list.size() > 0)
        {
            rows = roleMenuMapper.batchRoleMenu(list);
        }
        return rows;
    }

    /**
     * 新增角色部門資訊(資料許可權)
     *
     * @param role 角色物件
     */
    public int insertRoleDept(SysRole role)
    {
        int rows = 1;
        // 新增角色與部門（資料許可權）管理
        List<SysRoleDept> list = new ArrayList<SysRoleDept>();
        for (Long deptId : role.getDeptIds())
        {
            SysRoleDept rd = new SysRoleDept();
            rd.setRoleId(role.getRoleId());
            rd.setDeptId(deptId);
            list.add(rd);
        }
        if (list.size() > 0)
        {
            rows = roleDeptMapper.batchRoleDept(list);
        }
        return rows;
    }

    /**
     * 通過角色ID刪除角色
     * 
     * @param roleId 角色ID
     * @return 結果
     */
    @Override
    public int deleteRoleById(Long roleId)
    {
        return roleMapper.deleteRoleById(roleId);
    }

    /**
     * 批量刪除角色資訊
     * 
     * @param roleIds 需要刪除的角色ID
     * @return 結果
     */
    public int deleteRoleByIds(Long[] roleIds)
    {
        for (Long roleId : roleIds)
        {
            checkRoleAllowed(new SysRole(roleId));
            SysRole role = selectRoleById(roleId);
            if (countUserRoleByRoleId(roleId) > 0)
            {
                throw new CustomException(String.format("%1$s已分配,不能刪除", role.getRoleName()));
            }
        }
        return roleMapper.deleteRoleByIds(roleIds);
    }
}
