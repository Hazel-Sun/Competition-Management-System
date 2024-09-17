package com.cms.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.cms.common.core.annotation.Excel;
import com.cms.common.core.web.domain.BaseEntity;

/**
 * 过程文件管理对象 sys_file_info
 *
 * @author cms
 * @date 2023-05-13
 */
public class SysFileInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 文件id */
    private Long fileId;

    /** 文件名 */
    @Excel(name = "文件名")
    private String fileName;

    /** 文件路径 */
    @Excel(name = "文件路径")
    private String filePath;

    /** 文件类型 */
    @Excel(name = "文件类型")
    private String fileType;

    /** 小队id */
    @Excel(name = "小队id")
    private Long squadId;
    private SysSquad squad;

    /** 竞赛id */
    @Excel(name = "竞赛id")
    private Long compId;
    private SysComp comp;

    public void setFileId(Long fileId)
    {
        this.fileId = fileId;
    }

    public Long getFileId()
    {
        return fileId;
    }
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileName()
    {
        return fileName;
    }
    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    public String getFilePath()
    {
        return filePath;
    }
    public void setFileType(String fileType)
    {
        this.fileType = fileType;
    }

    public String getFileType()
    {
        return fileType;
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
            .append("fileId", getFileId())
            .append("fileName", getFileName())
            .append("filePath", getFilePath())
            .append("fileType", getFileType())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("squadId", getSquadId())
            .append("compId", getCompId())
                .append("comp",getComp())
                .append("squad",getSquad())
            .toString();
    }
}
