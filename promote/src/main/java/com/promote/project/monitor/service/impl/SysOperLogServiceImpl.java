package com.promote.project.monitor.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.promote.common.utils.DateUtils;
import com.promote.project.promote.sender.ProQueueSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.promote.project.monitor.domain.SysOperLog;
import com.promote.project.monitor.mapper.SysOperLogMapper;
import com.promote.project.monitor.service.ISysOperLogService;

/**
 * 操作日誌 服務層處理
 *
 * @author ruoyi
 */
@Service
public class SysOperLogServiceImpl implements ISysOperLogService {

    @Autowired
    private SysOperLogMapper operLogMapper;

    @Autowired
    private ProQueueSender proQueueSender;

    /**
     * 新增操作日誌
     *
     * @param operLog 操作日誌物件
     */
    @Override
    public void insertOperlog(SysOperLog operLog) {
        proQueueSender.send("com.promote.project.monitor.domain.SysOperLog",operLog);
//        operLogMapper.insertOperlog(operLog);
    }

    /**
     * 查詢系統操作日誌集合
     *
     * @param operLog 操作日誌物件
     * @return 操作日誌集合
     */
    @Override
    public List<SysOperLog> selectOperLogList(SysOperLog operLog) {
        return operLogMapper.selectOperLogList(operLog);
    }

    /**
     * 批量刪除系統操作日誌
     *
     * @param operIds 需要刪除的操作日誌ID
     * @return 結果
     */
    public int deleteOperLogByIds(Long[] operIds) {
        return operLogMapper.deleteOperLogByIds(operIds);
    }

    /**
     * 查詢操作日誌詳細
     *
     * @param operId 操作ID
     * @return 操作日誌物件
     */
    @Override
    public SysOperLog selectOperLogById(Long operId) {
        return operLogMapper.selectOperLogById(operId);
    }

    /**
     * 清空操作日誌
     */
    @Override
    public void cleanOperLog() {
        operLogMapper.cleanOperLog();
    }


    /**
     * 新增操作日誌
     *
     * @param title         操作模組
     * @param businessType  業務型別
     * @param businessTypes 業務型別陣列
     * @param method        請求方法
     * @param requestMethod 請求方式
     * @param operatorType  操作類別
     * @param operName      操作人員
     * @param deptName      部門名稱
     * @param operUrl       請求地址
     * @param operIp        操作地址
     * @param operLocation  操作地點
     * @param operParam     請求引數
     * @param jsonResult    返回引數
     * @param status        狀態
     * @param errorMsg      錯誤訊息
     */
    @Override
    public void insertOperlog(String title, Integer businessType, Integer[] businessTypes, String method, String requestMethod, Integer operatorType, String operName, String deptName, String operUrl, String operIp, String operLocation, String operParam, String jsonResult, Integer status, String errorMsg) {
        SysOperLog sysOperLog = new SysOperLog();
        sysOperLog.setTitle(title);
        sysOperLog.setBusinessType(businessType);
        sysOperLog.setBusinessTypes(businessTypes);
        sysOperLog.setMethod(method);
        sysOperLog.setRequestMethod(requestMethod);
        sysOperLog.setOperatorType(operatorType);
        sysOperLog.setOperName(operName);
        sysOperLog.setDeptName(deptName);
        sysOperLog.setOperUrl(operUrl);
        sysOperLog.setOperIp(operIp);
        sysOperLog.setOperLocation(operLocation);
        sysOperLog.setOperParam(operParam);
        sysOperLog.setJsonResult(jsonResult);
        sysOperLog.setStatus(status);
        sysOperLog.setErrorMsg(errorMsg);
        sysOperLog.setOperTime(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", DateUtils.getTime()));
        insertOperlog(sysOperLog);
    }
}
