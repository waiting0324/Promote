package com.promote.project.promote.domain;

import com.promote.framework.aspectj.lang.annotation.Excel;
import com.promote.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 最新消息/公告物件 pro_news
 * 
 * @author promote
 * @date 2020-04-27
 */
public class ProNews extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 流水號 */
    private Long id;

    /** 0最新消息, 1公告 */
    @Excel(name = "0最新消息, 1公告")
    private String type;

    /** 標題 */
    @Excel(name = "標題")
    private String subject;

    /** 內文 */
    @Excel(name = "內文")
    private String content;

    /** 點擊數 */
    @Excel(name = "點擊數")
    private Long clickCount;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setSubject(String subject) 
    {
        this.subject = subject;
    }

    public String getSubject() 
    {
        return subject;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }
    public void setClickCount(Long clickCount) 
    {
        this.clickCount = clickCount;
    }

    public Long getClickCount() 
    {
        return clickCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("type", getType())
            .append("subject", getSubject())
            .append("content", getContent())
            .append("clickCount", getClickCount())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
