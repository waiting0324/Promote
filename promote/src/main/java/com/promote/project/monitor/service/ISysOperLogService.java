package com.promote.project.monitor.service;

import java.util.Date;
import java.util.List;
import com.promote.project.monitor.domain.SysOperLog;

/**
 * 操作日誌 服務層
 * 
 * @author ruoyi
 */
public interface ISysOperLogService
{
    /**
     * 新增操作日誌
     * 
     * @param operLog 操作日誌物件
     */
    public void insertOperlog(SysOperLog operLog);

    /**
     * 查詢系統操作日誌集合
     * 
     * @param operLog 操作日誌物件
     * @return 操作日誌集合
     */
    public List<SysOperLog> selectOperLogList(SysOperLog operLog);

    /**
     * 批量刪除系統操作日誌
     * 
     * @param operIds 需要刪除的操作日誌ID
     * @return 結果
     */
    public int deleteOperLogByIds(Long[] operIds);

    /**
     * 查詢操作日誌詳細
     * 
     * @param operId 操作ID
     * @return 操作日誌物件
     */
    public SysOperLog selectOperLogById(Long operId);

    /**
     * 清空操作日誌
     */
    public void cleanOperLog();


    /**
     * 新增操作日誌
     *
     * @param title 操作模組
     * @param businessType 業務型別
     * @param businessTypes 業務型別陣列
     * @param method 請求方法
     * @param requestMethod 請求方式
     * @param operatorType 操作類別
     * @param operName 操作人員
     * @param deptName 部門名稱
     * @param operUrl 請求地址
     * @param operIp 操作地址
     * @param operLocation 操作地點
     * @param operParam 請求引數
     * @param jsonResult 返回引數
     * @param status 狀態
     * @param errorMsg 錯誤訊息
     */
    public void insertOperlog(String title, Integer businessType, Integer[] businessTypes, String method, String requestMethod, Integer operatorType, String operName, String deptName, String operUrl, String operIp, String operLocation, String operParam, String jsonResult, Integer status, String errorMsg);
}
