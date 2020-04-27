package com.promote.project.promote.mapper;

import com.promote.project.promote.domain.DailyConsume;

import java.util.List;

/**
 * 每日消費統計檔Mapper介面
 *
 * @author 6550 劉威廷
 * @date 2020-04-27
 */
public interface DailyConsumeMapper
{
    /**
     * 查詢每日消費統計檔
     *
     * @param id 每日消費統計檔ID
     * @return 每日消費統計檔
     */
    public DailyConsume selectDailyConsumeById(Long id);

    /**
     * 查詢每日消費統計檔列表
     *
     * @param dailyConsume 每日消費統計檔
     * @return 每日消費統計檔集合
     */
    public List<DailyConsume> selectDailyConsumeList(DailyConsume dailyConsume);

    /**
     * 新增每日消費統計檔
     *
     * @param dailyConsume 每日消費統計檔
     * @return 結果
     */
    public int insertDailyConsume(DailyConsume dailyConsume);

    /**
     * 修改每日消費統計檔
     *
     * @param dailyConsume 每日消費統計檔
     * @return 結果
     */
    public int updateDailyConsume(DailyConsume dailyConsume);

    /**
     * 刪除每日消費統計檔
     *
     * @param id 每日消費統計檔ID
     * @return 結果
     */
    public int deleteDailyConsumeById(Long id);

    /**
     * 批量刪除每日消費統計檔
     *
     * @param ids 需要刪除的資料ID
     * @return 結果
     */
    public int deleteDailyConsumeByIds(Long[] ids);
}