package com.cms.system.service;

import java.util.List;
import com.cms.system.domain.SysFileInfo;

/**
 * 过程文件管理Service接口
 * 
 * @author cms
 * @date 2023-05-13
 */
public interface ISysFileInfoService 
{
    /**
     * 查询过程文件管理
     * 
     * @param fileId 过程文件管理主键
     * @return 过程文件管理
     */
    public SysFileInfo selectSysFileInfoByFileId(Long fileId);

    /**
     * 查询过程文件管理列表
     * 
     * @param sysFileInfo 过程文件管理
     * @return 过程文件管理集合
     */
    public List<SysFileInfo> selectSysFileInfoList(SysFileInfo sysFileInfo);

    /**
     * 新增过程文件管理
     * 
     * @param sysFileInfo 过程文件管理
     * @return 结果
     */
    public int insertSysFileInfo(SysFileInfo sysFileInfo);

    /**
     * 修改过程文件管理
     * 
     * @param sysFileInfo 过程文件管理
     * @return 结果
     */
    public int updateSysFileInfo(SysFileInfo sysFileInfo);

    /**
     * 批量删除过程文件管理
     * 
     * @param fileIds 需要删除的过程文件管理主键集合
     * @return 结果
     */
    public int deleteSysFileInfoByFileIds(Long[] fileIds);

    /**
     * 删除过程文件管理信息
     * 
     * @param fileId 过程文件管理主键
     * @return 结果
     */
    public int deleteSysFileInfoByFileId(Long fileId);
}
