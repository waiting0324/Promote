package com.ruoyi.project.system.mapper;

import java.util.List;
import com.ruoyi.project.system.domain.SysPost;

/**
 * 崗位資訊 資料層
 * 
 * @author ruoyi
 */
public interface SysPostMapper
{
    /**
     * 查詢崗位資料集合
     * 
     * @param post 崗位資訊
     * @return 崗位資料集合
     */
    public List<SysPost> selectPostList(SysPost post);

    /**
     * 查詢所有崗位
     * 
     * @return 崗位列表
     */
    public List<SysPost> selectPostAll();

    /**
     * 通過崗位ID查詢崗位資訊
     * 
     * @param postId 崗位ID
     * @return 角色物件資訊
     */
    public SysPost selectPostById(Long postId);

    /**
     * 根據使用者ID獲取崗位選擇框列表
     * 
     * @param userId 使用者ID
     * @return 選中崗位ID列表
     */
    public List<Integer> selectPostListByUserId(Long userId);

    /**
     * 查詢使用者所屬崗位組
     * 
     * @param userName 使用者名稱
     * @return 結果
     */
    public List<SysPost> selectPostsByUserName(String userName);

    /**
     * 刪除崗位資訊
     * 
     * @param postId 崗位ID
     * @return 結果
     */
    public int deletePostById(Long postId);

    /**
     * 批量刪除崗位資訊
     * 
     * @param postIds 需要刪除的崗位ID
     * @return 結果
     */
    public int deletePostByIds(Long[] postIds);

    /**
     * 修改崗位資訊
     * 
     * @param post 崗位資訊
     * @return 結果
     */
    public int updatePost(SysPost post);

    /**
     * 新增崗位資訊
     * 
     * @param post 崗位資訊
     * @return 結果
     */
    public int insertPost(SysPost post);

    /**
     * 校驗崗位名稱
     * 
     * @param postName 崗位名稱
     * @return 結果
     */
    public SysPost checkPostNameUnique(String postName);

    /**
     * 校驗崗位編碼
     * 
     * @param postCode 崗位編碼
     * @return 結果
     */
    public SysPost checkPostCodeUnique(String postCode);
}
