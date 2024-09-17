package com.cms.system.domain;

import java.util.List;

import com.cms.system.api.domain.SysUser;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.cms.common.core.annotation.Excel;
import com.cms.common.core.web.domain.BaseEntity;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

/**
 * 竞赛队伍对象 sys_squad
 *
 * @author cms
 * @date 2023-04-17
 */
public class SysSquad extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 小组id */
    @Excel(name = "小组id")
    private Long squadId;

    /** 小组名称 */
    @Excel(name = "小组名称")
    private String squadName;

    /** 小组组长 */
    @Excel(name = "小组组长")
    private String squadLeader;

    /** 小组人数 */
    @Excel(name = "小组人数")
    private Long memberNum;

    /** 小组成员 */
    @Excel(name = "小组成员")
    private String squadMember;

    /** 指导教师 */
    @Excel(name = "指导教师")
    private String squadTeacher;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 邮箱 */

    @Excel(name = "邮箱")
    private String email;

    /** 竞赛组 */
    private Long[] compIds;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    @Excel(name = "参与竞赛")
    private Long compId;
    private SysComp comp;

    /** 荣誉信息管理信息 */
    private List<SysHonor> sysHonorList;
    /** 过程文件管理信息 */
    private List<SysFileInfo> sysFileInfoList;

    private List<SysUserSquad> sysUserSquadList;




    public SysSquad()
    {

    }
    public SysSquad(Long squadId){this.squadId=squadId;}

    public void setSquadId(Long squadId)
    {
        this.squadId = squadId;
    }

    public Long getSquadId()
    {
        return squadId;
    }
    public void setSquadName(String squadName)
    {
        this.squadName = squadName;
    }

    public String getSquadName()
    {
        return squadName;
    }
    public void setSquadLeader(String squadLeader)
    {
        this.squadLeader = squadLeader;
    }

    public String getSquadLeader()
    {
        return squadLeader;
    }
    public void setMemberNum(Long memberNum)
    {
        this.memberNum = memberNum;
    }

    public Long getMemberNum()
    {
        return memberNum;
    }
    public void setSquadMember(String squadMember)
    {
        this.squadMember = squadMember;
    }

    public String getSquadMember()
    {
        return squadMember;
    }
    public void setSquadTeacher(String squadTeacher)
    {
        this.squadTeacher = squadTeacher;
    }

    public String getSquadTeacher()
    {
        return squadTeacher;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    @Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
    public String getPhone()
    {
        return phone;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    public String getEmail()
    {
        return email;
    }

    public Long[] getCompIds()
    {
        return compIds;
    }
    public void setCompIds(Long[] compIds)
    {
        this.compIds = compIds;
    }


    public String getStatus()
    {
        return status;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public Long getCompId(){ return compId; }
    public void setCompId( Long compId ){this.compId=compId;}


    public SysComp getComp()
    {
        return comp;
    }

    public void setComp(SysComp comp)
    {
        this.comp = comp;
    }

    public List<SysHonor> getSysHonorList()
    {
        return sysHonorList;
    }

    public void setSysHonorList(List<SysHonor> sysHonorList)
    {
        this.sysHonorList = sysHonorList;
    }

    public List<SysFileInfo> getSysFileInfoList()
    {
        return sysFileInfoList;
    }

    public void setSysFileInfoList(List<SysFileInfo> sysFileInfoList)
    {
        this.sysFileInfoList = sysFileInfoList;
    }

    public List<SysUserSquad> getSysUserSquadList(){return sysUserSquadList;}

    public void setSysUserSquadList(List<SysUserSquad>sysUserSquadList){this.sysUserSquadList=sysUserSquadList;}



    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("squadId", getSquadId())
            .append("squadName", getSquadName())
            .append("squadLeader", getSquadLeader())
            .append("memberNum", getMemberNum())
            .append("squadMember", getSquadMember())
            .append("squadTeacher", getSquadTeacher())
            .append("phone", getPhone())
            .append("email", getEmail())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("compId",getCompId())
            .append("sysHonorList", getSysHonorList())
            .append("comp",getComp())
            .append("sysFileInfoList", getSysFileInfoList())
                .append("sysUserSquadList",getSysUserSquadList())
            .toString();
    }
}
