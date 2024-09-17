package com.cms.system.service.impl;

import java.util.List;
import com.cms.common.core.utils.DateUtils;
import com.cms.common.datascope.annotation.DataScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cms.system.mapper.SysFileInfoMapper;
import com.cms.system.domain.SysFileInfo;
import com.cms.system.service.ISysFileInfoService;
import com.cms.common.core.utils.file.FileUtils;

/**
 * 过程文件管理Service业务层处理
 *
 * @author cms
 * @date 2023-05-13
 */
@Service
public class SysFileInfoServiceImpl implements ISysFileInfoService
{
    @Autowired
    private SysFileInfoMapper sysFileInfoMapper;

    /**
     * 查询过程文件管理
     *
     * @param fileId 过程文件管理主键
     * @return 过程文件管理
     */
    @Override
    public SysFileInfo selectSysFileInfoByFileId(Long fileId)
    {
        return sysFileInfoMapper.selectSysFileInfoByFileId(fileId);
    }

    /**
     * 查询过程文件管理列表
     *
     * @param sysFileInfo 过程文件管理
     * @return 过程文件管理
     */
    @Override
    @DataScope(userAlias = "us")
    public List<SysFileInfo> selectSysFileInfoList(SysFileInfo sysFileInfo)
    {
        return sysFileInfoMapper.selectSysFileInfoList(sysFileInfo);
    }

    /**
     * 新增过程文件管理
     *
     * @param sysFileInfo 过程文件管理
     * @return 结果
     */
    @Override
    public int insertSysFileInfo(SysFileInfo sysFileInfo)
    {
        sysFileInfo.setCreateTime(DateUtils.getNowDate());
        String url = sysFileInfo.getFilePath();
        sysFileInfo.setFileName(FileUtils.getName(url));
        return sysFileInfoMapper.insertSysFileInfo(sysFileInfo);
    }

    /**
     * 修改过程文件管理
     *
     * @param sysFileInfo 过程文件管理
     * @return 结果
     */
    @Override
    public int updateSysFileInfo(SysFileInfo sysFileInfo)
    {
        String url = sysFileInfo.getFilePath();
        sysFileInfo.setFileName(FileUtils.getName(url));
        return sysFileInfoMapper.updateSysFileInfo(sysFileInfo);
    }

    /**
     * 批量删除过程文件管理
     *
     * @param fileIds 需要删除的过程文件管理主键
     * @return 结果
     */
    @Override
    public int deleteSysFileInfoByFileIds(Long[] fileIds)
    {
        return sysFileInfoMapper.deleteSysFileInfoByFileIds(fileIds);
    }

    /**
     * 删除过程文件管理信息
     *
     * @param fileId 过程文件管理主键
     * @return 结果
     */
    @Override
    public int deleteSysFileInfoByFileId(Long fileId)
    {
        return sysFileInfoMapper.deleteSysFileInfoByFileId(fileId);
    }
}
