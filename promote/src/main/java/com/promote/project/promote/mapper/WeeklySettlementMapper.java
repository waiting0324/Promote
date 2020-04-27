package com.promote.project.promote.mapper;

import com.promote.project.promote.domain.WeeklySettlement;

import java.util.List;

/**
 * 週結檔Mapper介面
 *
 * @author 6550 劉威廷
 * @date 2020-04-27
 */
public interface WeeklySettlementMapper
{
    /**
     * 查詢週結檔
     *
     * @param id 週結檔ID
     * @return 週結檔
     */
    public WeeklySettlement selectWeeklySettlementById(Long id);

    /**
     * 查詢週結檔列表
     *
     * @param weeklySettlement 週結檔
     * @return 週結檔集合
     */
    public List<WeeklySettlement> selectWeeklySettlementList(WeeklySettlement weeklySettlement);

    /**
     * 新增週結檔
     *
     * @param weeklySettlement 週結檔
     * @return 結果
     */
    public int insertWeeklySettlement(WeeklySettlement weeklySettlement);

    /**
     * 修改週結檔
     *
     * @param weeklySettlement 週結檔
     * @return 結果
     */
    public int updateWeeklySettlement(WeeklySettlement weeklySettlement);

    /**
     * 刪除週結檔
     *
     * @param id 週結檔ID
     * @return 結果
     */
    public int deleteWeeklySettlementById(Long id);

    /**
     * 批量刪除週結檔
     *
     * @param ids 需要刪除的資料ID
     * @return 結果
     */
    public int deleteWeeklySettlementByIds(Long[] ids);
}