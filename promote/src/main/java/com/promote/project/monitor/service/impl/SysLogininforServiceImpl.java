package com.promote.project.monitor.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.promote.project.monitor.domain.SysLogininfor;
import com.promote.project.monitor.mapper.SysLogininforMapper;
import com.promote.project.monitor.service.ISysLogininforService;

/**
 * 系統訪問日誌情況資訊 服務層處理
 * 
 * @author ruoyi
 */
@Service
public class SysLogininforServiceImpl implements ISysLogininforService
{

    @Autowired
    private SysLogininforMapper logininforMapper;

    /**
     * 新增系統登入日誌
     * 
     * @param logininfor 訪問日誌物件
     */
    @Override
    public void insertLogininfor(SysLogininfor logininfor)
    {
        logininforMapper.insertLogininfor(logininfor);
    }

    /**
     * 查詢系統登入日誌集合
     * 
     * @param logininfor 訪問日誌物件
     * @return 登入記錄集合
     */
    @Override
    public List<SysLogininfor> selectLogininforList(SysLogininfor logininfor)
    {
        return logininforMapper.selectLogininforList(logininfor);
    }

    /**
     * 批量刪除系統登入日誌
     * 
     * @param infoIds 需要刪除的登入日誌ID
     * @return
     */
    @Override
    public int deleteLogininforByIds(Long[] infoIds)
    {
        return logininforMapper.deleteLogininforByIds(infoIds);
    }

    /**
     * 清空系統登入日誌
     */
    @Override
    public void cleanLogininfor()
    {
        logininforMapper.cleanLogininfor();
    }
}
