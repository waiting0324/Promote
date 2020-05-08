package com.promote.project.promote.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.promote.framework.aspectj.lang.annotation.Excel;
import com.promote.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 補助額度檔物件 pro_fund_amount
 *
 * @author 6550 劉威廷
 * @date 2020-04-27
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FundAmount extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 流水號 */
    private Long id;

    /** 補助機構 (S:中企
     T:中辦
     B:商業司
     C:文化部) */
    private String fundType;

    /** 每人補助金額 */
    @Excel(name = "每人補助金額")
    private Long perFund;

    /** 最低使用金額 */
    @Excel(name = "最低使用金額")
    private Long minUseAmount;

    /** 使用期限(月份數) */
    @Excel(name = "使用期限(月份數)")
    private Long useDeadline;

    /** 補助總金額 */
    @Excel(name = "補助總金額")
    private Long fundAmount;

    /** 補助金餘額 */
    @Excel(name = "補助金餘額")
    private Long balance;

    /** 啟用日 */
    @Excel(name = "啟用日", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /** 店家核銷期限 */
    @Excel(name = "店家核銷期限", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endDate;

    /** 類別 (0夜市 1餐廳 2商圈 3藝文) */
    @Excel(name = "類別 (0夜市 1餐廳 2商圈 3藝文)")
    private String storeType;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setFundType(String fundType)
    {
        this.fundType = fundType;
    }

    public String getFundType()
    {
        return fundType;
    }
    public void setPerFund(Long perFund)
    {
        this.perFund = perFund;
    }

    public Long getPerFund()
    {
        return perFund;
    }
    public void setMinUseAmount(Long minUseAmount)
    {
        this.minUseAmount = minUseAmount;
    }

    public Long getMinUseAmount()
    {
        return minUseAmount;
    }
    public void setUseDeadline(Long useDeadline)
    {
        this.useDeadline = useDeadline;
    }

    public Long getUseDeadline()
    {
        return useDeadline;
    }
    public void setFundAmount(Long fundAmount)
    {
        this.fundAmount = fundAmount;
    }

    public Long getFundAmount()
    {
        return fundAmount;
    }
    public void setBalance(Long balance)
    {
        this.balance = balance;
    }

    public Long getBalance()
    {
        return balance;
    }
    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getStartDate()
    {
        return startDate;
    }
    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }
    public void setStoreType(String storeType)
    {
        this.storeType = storeType;
    }

    public String getStoreType()
    {
        return storeType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("fundType", getFundType())
                .append("perFund", getPerFund())
                .append("minUseAmount", getMinUseAmount())
                .append("useDeadline", getUseDeadline())
                .append("fundAmount", getFundAmount())
                .append("balance", getBalance())
                .append("startDate", getStartDate())
                .append("endDate", getEndDate())
                .append("storeType", getStoreType())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}