package com.promote.project.promote.mapper;

import com.promote.project.promote.domain.HostelInfo;

import java.util.List;

/**
 * 旅宿業基本資料Mapper介面
 *
 * @author 6550 劉威廷
 * @date 2020-04-27
 */
public interface HostelInfoMapper
{
    /**
     * 查詢旅宿業基本資料
     *
     * @param userId 旅宿業基本資料ID
     * @return 旅宿業基本資料
     */
    public HostelInfo selectHostelInfoById(Long userId);

    /**
     * 查詢旅宿業基本資料列表
     *
     * @param hostelInfo 旅宿業基本資料
     * @return 旅宿業基本資料集合
     */
    public List<HostelInfo> selectHostelInfoList(HostelInfo hostelInfo);

    /**
     * 新增旅宿業基本資料
     *
     * @param hostelInfo 旅宿業基本資料
     * @return 結果
     */
    public int insertHostelInfo(HostelInfo hostelInfo);

    /**
     * 修改旅宿業基本資料
     *
     * @param hostelInfo 旅宿業基本資料
     * @return 結果
     */
    public int updateHostelInfo(HostelInfo hostelInfo);

    /**
     * 刪除旅宿業基本資料
     *
     * @param userId 旅宿業基本資料ID
     * @return 結果
     */
    public int deleteHostelInfoById(Long userId);

    /**
     * 批量刪除旅宿業基本資料
     *
     * @param userIds 需要刪除的資料ID
     * @return 結果
     */
    public int deleteHostelInfoByIds(Long[] userIds);
}