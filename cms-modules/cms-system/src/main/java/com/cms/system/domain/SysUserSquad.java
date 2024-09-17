package com.cms.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用户和角色关联 sys_user_squad
 * 
 * @author ruoyi
 */
public class SysUserSquad
{
    /** 用户ID */
    private Long userId;
    
    /** 角色ID */
    private Long squadId;

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getSquadId()
    {
        return squadId;
    }

    public void setSquadId(Long squadId)
    {
        this.squadId = squadId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("squadId", getSquadId())
            .toString();
    }
}
