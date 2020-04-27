package com.promote.project.promote.mapper;

import java.util.List;
import com.promote.project.promote.domain.ConsumerInfo;

/**
 * 消費者基本資料Mapper介面
 *
 * @author 6550 劉威廷
 * @date 2020-04-27
 */
public interface ConsumerInfoMapper
{
    /**
     * 查詢消費者基本資料
     *
     * @param userId 消費者基本資料ID
     * @return 消費者基本資料
     */
    public ConsumerInfo selectConsumerInfoById(Long userId);

    /**
     * 查詢消費者基本資料列表
     *
     * @param consumerInfo 消費者基本資料
     * @return 消費者基本資料集合
     */
    public List<ConsumerInfo> selectConsumerInfoList(ConsumerInfo consumerInfo);

    /**
     * 新增消費者基本資料
     *
     * @param consumerInfo 消費者基本資料
     * @return 結果
     */
    public int insertConsumerInfo(ConsumerInfo consumerInfo);

    /**
     * 修改消費者基本資料
     *
     * @param consumerInfo 消費者基本資料
     * @return 結果
     */
    public int updateConsumerInfo(ConsumerInfo consumerInfo);

    /**
     * 刪除消費者基本資料
     *
     * @param userId 消費者基本資料ID
     * @return 結果
     */
    public int deleteConsumerInfoById(Long userId);

    /**
     * 批量刪除消費者基本資料
     *
     * @param userIds 需要刪除的資料ID
     * @return 結果
     */
    public int deleteConsumerInfoByIds(Long[] userIds);
}