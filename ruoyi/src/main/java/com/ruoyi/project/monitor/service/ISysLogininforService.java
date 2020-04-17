package com.ruoyi.project.monitor.service;

import java.util.List;
import com.ruoyi.project.monitor.domain.SysLogininfor;

/**
 * 系統訪問日誌情況資訊 服務層
 * 
 * @author ruoyi
 */
public interface ISysLogininforService
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
     * @return
     */
    public int deleteLogininforByIds(Long[] infoIds);

    /**
     * 清空系統登入日誌
     */
    public void cleanLogininfor();
}
