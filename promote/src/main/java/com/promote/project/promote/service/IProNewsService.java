package com.promote.project.promote.service;

import com.promote.project.promote.domain.ProNews;

import java.util.List;

/**
 * 最新消息/公告Service介面
 * 
 * @author promote
 * @date 2020-04-27
 */
public interface IProNewsService 
{
    /**
     * 查詢最新消息/公告
     * 
     * @param id 最新消息/公告ID
     * @return 最新消息/公告
     */
    public ProNews selectProNewsById(Long id);

    /**
     * 查詢最新消息/公告列表
     * 
     * @param proNews 最新消息/公告
     * @return 最新消息/公告集合
     */
    public List<ProNews> selectProNewsList(ProNews proNews);

    /**
     * 新增最新消息/公告
     * 
     * @param proNews 最新消息/公告
     * @return 結果
     */
    public int insertProNews(ProNews proNews);

    /**
     * 修改最新消息/公告
     * 
     * @param proNews 最新消息/公告
     * @return 結果
     */
    public int updateProNews(ProNews proNews);

    /**
     * 批量刪除最新消息/公告
     * 
     * @param ids 需要刪除的最新消息/公告ID
     * @return 結果
     */
    public int deleteProNewsByIds(Long[] ids);

    /**
     * 刪除最新消息/公告資訊
     * 
     * @param id 最新消息/公告ID
     * @return 結果
     */
    public int deleteProNewsById(Long id);
}
