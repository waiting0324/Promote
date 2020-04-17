package com.ruoyi.project.monitor.domain;

import java.util.Date;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.aspectj.lang.annotation.Excel.ColumnType;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 操作日誌記錄表 oper_log
 * 
 * @author ruoyi
 */
public class SysOperLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 日誌主鍵 */
    @Excel(name = "操作序號", cellType = ColumnType.NUMERIC)
    private Long operId;

    /** 操作模組 */
    @Excel(name = "操作模組")
    private String title;

    /** 業務型別（0其它 1新增 2修改 3刪除） */
    @Excel(name = "業務型別", readConverterExp = "0=其它,1=新增,2=修改,3=刪除,4=授權,5=匯出,6=匯入,7=強退,8=生成程式碼,9=清空資料")
    private Integer businessType;

    /** 業務型別陣列 */
    private Integer[] businessTypes;

    /** 請求方法 */
    @Excel(name = "請求方法")
    private String method;

    /** 請求方式 */
    @Excel(name = "請求方式")
    private String requestMethod;

    /** 操作類別（0其它 1後臺使用者 2手機端使用者） */
    @Excel(name = "操作類別", readConverterExp = "0=其它,1=後臺使用者,2=手機端使用者")
    private Integer operatorType;

    /** 操作人員 */
    @Excel(name = "操作人員")
    private String operName;

    /** 部門名稱 */
    @Excel(name = "部門名稱")
    private String deptName;

    /** 請求url */
    @Excel(name = "請求地址")
    private String operUrl;

    /** 操作地址 */
    @Excel(name = "操作地址")
    private String operIp;

    /** 操作地點 */
    @Excel(name = "操作地點")
    private String operLocation;

    /** 請求引數 */
    @Excel(name = "請求引數")
    private String operParam;

    /** 返回引數 */
    @Excel(name = "返回引數")
    private String jsonResult;

    /** 操作狀態（0正常 1異常） */
    @Excel(name = "狀態", readConverterExp = "0=正常,1=異常")
    private Integer status;

    /** 錯誤訊息 */
    @Excel(name = "錯誤訊息")
    private String errorMsg;

    /** 操作時間 */
    @Excel(name = "操作時間", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date operTime;

    public Long getOperId()
    {
        return operId;
    }

    public void setOperId(Long operId)
    {
        this.operId = operId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Integer getBusinessType()
    {
        return businessType;
    }

    public void setBusinessType(Integer businessType)
    {
        this.businessType = businessType;
    }

    public Integer[] getBusinessTypes()
    {
        return businessTypes;
    }

    public void setBusinessTypes(Integer[] businessTypes)
    {
        this.businessTypes = businessTypes;
    }

    public String getMethod()
    {
        return method;
    }

    public void setMethod(String method)
    {
        this.method = method;
    }

    public String getRequestMethod()
    {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod)
    {
        this.requestMethod = requestMethod;
    }

    public Integer getOperatorType()
    {
        return operatorType;
    }

    public void setOperatorType(Integer operatorType)
    {
        this.operatorType = operatorType;
    }

    public String getOperName()
    {
        return operName;
    }

    public void setOperName(String operName)
    {
        this.operName = operName;
    }

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public String getOperUrl()
    {
        return operUrl;
    }

    public void setOperUrl(String operUrl)
    {
        this.operUrl = operUrl;
    }

    public String getOperIp()
    {
        return operIp;
    }

    public void setOperIp(String operIp)
    {
        this.operIp = operIp;
    }

    public String getOperLocation()
    {
        return operLocation;
    }

    public void setOperLocation(String operLocation)
    {
        this.operLocation = operLocation;
    }

    public String getOperParam()
    {
        return operParam;
    }

    public void setOperParam(String operParam)
    {
        this.operParam = operParam;
    }

    public String getJsonResult()
    {
        return jsonResult;
    }

    public void setJsonResult(String jsonResult)
    {
        this.jsonResult = jsonResult;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getErrorMsg()
    {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg)
    {
        this.errorMsg = errorMsg;
    }

    public Date getOperTime()
    {
        return operTime;
    }

    public void setOperTime(Date operTime)
    {
        this.operTime = operTime;
    }
}
