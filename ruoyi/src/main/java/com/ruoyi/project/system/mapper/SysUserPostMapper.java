package com.ruoyi.project.system.mapper;

import java.util.List;
import com.ruoyi.project.system.domain.SysUserPost;

/**
 * 使用者與崗位關聯表 資料層
 * 
 * @author ruoyi
 */
public interface SysUserPostMapper
{
    /**
     * 通過使用者ID刪除使用者和崗位關聯
     * 
     * @param userId 使用者ID
     * @return 結果
     */
    public int deleteUserPostByUserId(Long userId);

    /**
     * 通過崗位ID查詢崗位使用數量
     * 
     * @param postId 崗位ID
     * @return 結果
     */
    public int countUserPostById(Long postId);

    /**
     * 批量刪除使用者和崗位關聯
     * 
     * @param ids 需要刪除的資料ID
     * @return 結果
     */
    public int deleteUserPost(Long[] ids);

    /**
     * 批量新增使用者崗位資訊
     * 
     * @param userPostList 使用者角色列表
     * @return 結果
     */
    public int batchUserPost(List<SysUserPost> userPostList);
}
