package com.promote.project.promote.mapper;

import com.promote.project.promote.domain.StoreHisMail;
import com.promote.project.promote.domain.StoreInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 14.	店家查詢明細郵件表Mapper介面
 *
 * @author 6382 劉威廷
 * @date 2020-05-14
 */
public interface ProStoreHisMailMapper {
    /**
     * 申請歷史明細(APP專用)
     *
     * @param storeHisMail 店家條件資料
     * @return
     */
    public int insertProStoreHisMail(StoreHisMail storeHisMail);

}