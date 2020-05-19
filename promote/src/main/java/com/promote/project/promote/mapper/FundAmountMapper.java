package com.promote.project.promote.mapper;

import com.promote.project.promote.domain.FundAmount;

import java.util.List;

/**
 * 補助額度檔Mapper介面
 *
 * @author 6550 劉威廷
 * @date 2020-04-27
 */
public interface FundAmountMapper
{
    /**
     * 查詢補助額度檔
     *
     * @param id 補助額度檔ID
     * @return 補助額度檔
     */
    public FundAmount selectFundAmountById(Long id);

    /**
     * 查詢補助額度檔列表
     *
     * @param fundAmount 補助額度檔
     * @return 補助額度檔集合
     */
    public List<FundAmount> selectFundAmountList(FundAmount fundAmount);

    /**
     * 新增補助額度檔
     *
     * @param fundAmount 補助額度檔
     * @return 結果
     */
    public int insertFundAmount(FundAmount fundAmount);

    /**
     * 修改補助額度檔
     *
     * @param fundAmount 補助額度檔
     * @return 結果
     */
    public int updateFundAmount(FundAmount fundAmount);

    /**
     * 刪除補助額度檔
     *
     * @param id 補助額度檔ID
     * @return 結果
     */
    public int deleteFundAmountById(Long id);

    /**
     * 批量刪除補助額度檔
     *
     * @param ids 需要刪除的資料ID
     * @return 結果
     */
    public int deleteFundAmountByIds(Long[] ids);


    /**
     * 查詢補助額度檔列表
     *
     * @return 補助額度檔集合
     */
    public List<FundAmount> getAllFundAmount();
}