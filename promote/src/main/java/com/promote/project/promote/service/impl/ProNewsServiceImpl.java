package com.promote.project.promote.service.impl;

import java.util.List;
import com.promote.common.utils.DateUtils;
import com.promote.project.promote.domain.ProNews;
import com.promote.project.promote.mapper.ProNewsMapper;
import com.promote.project.promote.service.IProNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 最新消息/公告Service業務層處理
 * 
 * @author promote
 * @date 2020-04-27
 */
@Service
public class ProNewsServiceImpl implements IProNewsService
{
    @Autowired
    private ProNewsMapper proNewsMapper;

    /**
     * 查詢最新消息/公告
     * 
     * @param id 最新消息/公告ID
     * @return 最新消息/公告
     */
    @Override
    public ProNews selectProNewsById(Long id)
    {
        return proNewsMapper.selectProNewsById(id);
    }

    /**
     * 查詢最新消息/公告列表
     * 
     * @param proNews 最新消息/公告
     * @return 最新消息/公告
     */
    @Override
    public List<ProNews> selectProNewsList(ProNews proNews)
    {
        return proNewsMapper.selectProNewsList(proNews);
    }

    /**
     * 新增最新消息/公告
     * 
     * @param proNews 最新消息/公告
     * @return 結果
     */
    @Override
    public int insertProNews(ProNews proNews)
    {
        proNews.setCreateTime(DateUtils.getNowDate());
        return proNewsMapper.insertProNews(proNews);
    }

    /**
     * 修改最新消息/公告
     * 
     * @param proNews 最新消息/公告
     * @return 結果
     */
    @Override
    public int updateProNews(ProNews proNews)
    {
        proNews.setUpdateTime(DateUtils.getNowDate());
        return proNewsMapper.updateProNews(proNews);
    }

    /**
     * 批量刪除最新消息/公告
     * 
     * @param ids 需要刪除的最新消息/公告ID
     * @return 結果
     */
    @Override
    public int deleteProNewsByIds(Long[] ids)
    {
        return proNewsMapper.deleteProNewsByIds(ids);
    }

    /**
     * 刪除最新消息/公告資訊
     * 
     * @param id 最新消息/公告ID
     * @return 結果
     */
    @Override
    public int deleteProNewsById(Long id)
    {
        return proNewsMapper.deleteProNewsById(id);
    }
}
