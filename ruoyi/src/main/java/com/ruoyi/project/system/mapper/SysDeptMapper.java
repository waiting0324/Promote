package com.ruoyi.project.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.project.system.domain.SysDept;

/**
 * 部門管理 資料層
 * 
 * @author ruoyi
 */
public interface SysDeptMapper
{
    /**
     * 查詢部門管理資料
     * 
     * @param dept 部門資訊
     * @return 部門資訊集合
     */
    public List<SysDept> selectDeptList(SysDept dept);

    /**
     * 根據角色ID查詢部門樹資訊
     * 
     * @param roleId 角色ID
     * @return 選中部門列表
     */
    public List<Integer> selectDeptListByRoleId(Long roleId);

    /**
     * 根據部門ID查詢資訊
     * 
     * @param deptId 部門ID
     * @return 部門資訊
     */
    public SysDept selectDeptById(Long deptId);

    /**
     * 根據ID查詢所有子部門
     * 
     * @param deptId 部門ID
     * @return 部門列表
     */
    public List<SysDept> selectChildrenDeptById(Long deptId);

    /**
     * 是否存在子節點
     * 
     * @param deptId 部門ID
     * @return 結果
     */
    public int hasChildByDeptId(Long deptId);

    /**
     * 查詢部門是否存在使用者
     * 
     * @param deptId 部門ID
     * @return 結果
     */
    public int checkDeptExistUser(Long deptId);

    /**
     * 校驗部門名稱是否唯一
     * 
     * @param deptName 部門名稱
     * @param parentId 父部門ID
     * @return 結果
     */
    public SysDept checkDeptNameUnique(@Param("deptName") String deptName, @Param("parentId") Long parentId);

    /**
     * 新增部門資訊
     * 
     * @param dept 部門資訊
     * @return 結果
     */
    public int insertDept(SysDept dept);

    /**
     * 修改部門資訊
     * 
     * @param dept 部門資訊
     * @return 結果
     */
    public int updateDept(SysDept dept);

    /**
     * 修改所在部門的父級部門狀態
     * 
     * @param dept 部門
     */
    public void updateDeptStatus(SysDept dept);

    /**
     * 修改子元素關係
     * 
     * @param depts 子元素
     * @return 結果
     */
    public int updateDeptChildren(@Param("depts") List<SysDept> depts);

    /**
     * 刪除部門管理資訊
     * 
     * @param deptId 部門ID
     * @return 結果
     */
    public int deleteDeptById(Long deptId);
}
