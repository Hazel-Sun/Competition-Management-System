package com.cms.system.domain;

import java.util.Date;
import java.util.List;
import com.cms.common.core.annotation.Excels;
import com.cms.system.api.domain.SysDept;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.cms.common.core.annotation.Excel;
import com.cms.common.core.web.domain.BaseEntity;

/**
 * 荣誉信息管理对象 sys_honor
 *
 * @author cms
 * @date 2023-05-05
 */
public class SysHonor extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 荣誉id */
    @Excel(name = "荣誉id")
    private Long honorId;

    /** 荣誉名 */
    @Excel(name = "荣誉名")
    private String honorName;

    /** 荣誉类型 */
    @Excel(name = "荣誉类型")
    private String honorType;

    /** 竞赛id */
    @Excel(name = "竞赛id")
    private Long compId;

    private SysComp comp;

    /** 竞赛小队 */
    @Excel(name = "小队id")
    private Long squadId;

    private SysSquad squad;


    /** 备注 */
    @Excel(name = "备注")
    private String honorContent;

    /** 获奖/成果获得日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "获奖/成果获得日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date honorDate;

    /**  */
    @Excel(name = "")
    private String status;

    public void setHonorId(Long honorId)
    {
        this.honorId = honorId;
    }

    public Long getHonorId()
    {
        return honorId;
    }
    public void setHonorName(String honorName)
    {
        this.honorName = honorName;
    }

    public String getHonorName()
    {
        return honorName;
    }
    public void setHonorType(String honorType)
    {
        this.honorType = honorType;
    }

    public String getHonorType()
    {
        return honorType;
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

    public void setSquadId(Long squadId)
    {
        this.squadId = squadId;
    }


    public Long getSquadId()
    {
        return squadId;
    }
    public SysSquad getSquad()
    {
        return squad;
    }
    public void setSquad(SysSquad squad)
    {
        this.squad=squad;
    }

    public void setHonorContent(String honorContent)
    {
        this.honorContent = honorContent;
    }


    public String getHonorContent()
    {
        return honorContent;
    }
    public void sethonorDate(Date honorDate)
    {
        this.honorDate = honorDate;
    }

    public Date gethonorDate()
    {
        return honorDate;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }



    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("honorId", getHonorId())
                .append("honorName", getHonorName())
                .append("honorType", getHonorType())
                .append("compId", getCompId())
                .append("squadId", getSquadId())
                .append("honorContent", getHonorContent())
                .append("honorDate", gethonorDate())
                .append("status", getStatus())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("comp",getComp())
                .append("squad",getSquad())
                .toString();
    }
}
