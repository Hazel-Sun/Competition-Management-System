package com.cms.system.domain;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.cms.common.core.utils.DateUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.cms.common.core.annotation.Excel;
import com.cms.common.core.web.domain.BaseEntity;

/**
 * 竞赛信息管理对象 sys_comp
 *
 * @author cms
 * @date 2023-04-17
 */
public class SysComp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 竞赛id */
    @Excel(name = "竞赛ID")
    private Long compId;

    /** 竞赛名称 */
    @Excel(name = "竞赛名称")
    private String compName;

    /** 竞赛类型 */
    @Excel(name = "竞赛类型")
    private String compType;

    /** 主办方 */
    @Excel(name = "主办方")
    private String compSponser;

    /** 竞赛链接 */
    @Excel(name = "竞赛链接")
    private String compLink;


    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date compStartTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date compEndTime;

    /** 状态 */
    @Excel(name = "状态")
    private String status;
    /** 竞赛队伍信息 */
    private List<SysSquad> sysSquadList;


    public void setCompId(Long compId)
    {
        this.compId = compId;
    }

    public Long getCompId()
    {
        return compId;
    }
    public void setCompName(String compName)
    {
        this.compName = compName;
    }

    public String getCompName()
    {
        return compName;
    }
    public void setCompType(String compType)
    {
        this.compType = compType;
    }

    public String getCompType()
    {
        return compType;
    }
    public void setCompSponser(String compSponser)
    {
        this.compSponser = compSponser;
    }

    public String getCompSponser()
    {
        return compSponser;
    }
    public void setCompLink(String compLink)
    {
        this.compLink = compLink;
    }

    public String getCompLink()
    {
        return compLink;
    }
    public void setCompStartTime(Date compStartTime)
    {
        this.compStartTime = compStartTime;
    }

    public Date getCompStartTime()
    {
        return compStartTime;
    }
    public void setCompEndTime(Date compEndTime)
    {
        this.compEndTime = compEndTime;
    }

    public Date getCompEndTime()
    {
        return compEndTime;
    }

    public String getStatus()
    {

        return status;
    }
    public List<SysSquad> getSysSquadList()
    {
        return sysSquadList;
    }

    public void setSysSquadList(List<SysSquad> sysSquadList)
    {
        this.sysSquadList = sysSquadList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("compId", getCompId())
            .append("compName", getCompName())
            .append("compType", getCompType())
            .append("compSponser", getCompSponser())
            .append("compLink", getCompLink())
            .append("compStartTime", getCompStartTime())
            .append("compEndTime", getCompEndTime())
            .append("status", getStatus())
            .append("sysSquadList", getSysSquadList())
            .toString();
    }
}
