package com.cms.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.cms.common.core.annotation.Excel;
import com.cms.common.core.web.domain.BaseEntity;

/**
 * 赛事宣传对象 sys_public
 *
 * @author cms
 * @date 2023-05-09
 */
public class SysPublic extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 宣传id */
    @Excel(name = "宣传id")
    private Long publicId;

    /** 宣传标题 */
    @Excel(name = "宣传标题")
    private String publicTitle;

    /** 宣传图 */
    @Excel(name = "宣传图")
    private String publicPic;

    /** 宣传内容 */
    @Excel(name = "宣传内容")
    private String publicContent;

    /** 宣传状态 */
    @Excel(name = "宣传状态")
    private String publicStatus;

    /** 竞赛Id */
    @Excel(name = "竞赛Id")
    private Long compId;

    private SysComp comp;

    public void setPublicId(Long publicId)
    {
        this.publicId = publicId;
    }

    public Long getPublicId()
    {
        return publicId;
    }
    public void setPublicTitle(String publicTitle)
    {
        this.publicTitle = publicTitle;
    }

    public String getPublicTitle()
    {
        return publicTitle;
    }
    public void setPublicPic(String publicPic)
    {
        this.publicPic = publicPic;
    }

    public String getPublicPic()
    {
        return publicPic;
    }
    public void setPublicContent(String publicContent)
    {
        this.publicContent = publicContent;
    }

    public String getPublicContent()
    {
        return publicContent;
    }
    public void setPublicStatus(String publicStatus)
    {
        this.publicStatus = publicStatus;
    }

    public String getPublicStatus()
    {
        return publicStatus;
    }
    public void setCompId(Long compId)
    {
        this.compId = compId;
    }

    public Long getCompId()
    {
        return compId;
    }

    public SysComp getComp()
    {
        return comp;
    }

    public void setComp(SysComp comp)
    {
        this.comp = comp;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("publicId", getPublicId())
            .append("publicTitle", getPublicTitle())
            .append("publicPic", getPublicPic())
            .append("publicContent", getPublicContent())
            .append("publicStatus", getPublicStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("compId", getCompId())
            .append("comp",getComp())
            .toString();
    }
}
