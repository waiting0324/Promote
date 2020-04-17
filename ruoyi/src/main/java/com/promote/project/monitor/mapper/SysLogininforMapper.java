package com.promote.project.monitor.mapper;

import java.util.List;
import com.promote.project.monitor.domain.SysLogininfor;

/**
 * 系統訪問日誌情況資訊 資料層
 * 
 * @author ruoyi
 */
public interface SysLogininforMapper
{
    /**
     * 新增系統登入日誌
     * 
     * @param logininfor 訪問日誌物件
     */
    public void insertLogininfor(SysLogininfor logininfor);

    /**
     * 查詢系統登入日誌集合
     * 
     * @param logininfor 訪問日誌物件
     * @return 登入記錄集合
     */
    public List<SysLogininfor> selectLogininforList(SysLogininfor logininfor);

    /**
     * 批量刪除系統登入日誌
     * 
     * @param infoIds 需要刪除的登入日誌ID
     * @return 結果
     */
    public int deleteLogininforByIds(Long[] infoIds);

    /**
     * 清空系統登入日誌
     * 
     * @return 結果
     */
    public int cleanLogininfor();
}
