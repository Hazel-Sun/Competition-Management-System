package com.cms.system.service.impl;

import java.util.List;
import com.cms.common.core.utils.DateUtils;
import com.cms.common.datascope.annotation.DataScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cms.system.mapper.SysHonorMapper;
import com.cms.system.domain.SysHonor;
import com.cms.system.service.ISysHonorService;

/**
 * 荣誉信息管理Service业务层处理
 *
 * @author cms
 * @date 2023-04-25
 */
@Service
public class SysHonorServiceImpl implements ISysHonorService
{
    @Autowired
    private SysHonorMapper sysHonorMapper;

    /**
     * 查询荣誉信息管理
     *
     * @param honorId 荣誉信息管理主键
     * @return 荣誉信息管理
     */
    @Override
    public SysHonor selectSysHonorByHonorId(Long honorId)
    {
        return sysHonorMapper.selectSysHonorByHonorId(honorId);
    }

    /**
     * 查询荣誉信息管理列表
     *
     * @param sysHonor 荣誉信息管理
     * @return 荣誉信息管理
     */
    @Override
    @DataScope(userAlias = "us")
    public List<SysHonor> selectSysHonorList(SysHonor sysHonor)
    {
        return sysHonorMapper.selectSysHonorList(sysHonor);
    }

    /**
     * 新增荣誉信息管理
     *
     * @param sysHonor 荣誉信息管理
     * @return 结果
     */
    @Override
    public int insertSysHonor(SysHonor sysHonor)
    {
        sysHonor.setCreateTime(DateUtils.getNowDate());
        return sysHonorMapper.insertSysHonor(sysHonor);
    }

    /**
     * 修改荣誉信息管理
     *
     * @param sysHonor 荣誉信息管理
     * @return 结果
     */
    @Override
    public int updateSysHonor(SysHonor sysHonor)
    {
        Long honorId = sysHonor.getHonorId();

        return sysHonorMapper.updateSysHonor(sysHonor);
    }

    /**
     * 批量删除荣誉信息管理
     *
     * @param honorIds 需要删除的荣誉信息管理主键
     * @return 结果
     */
    @Override
    public int deleteSysHonorByHonorIds(Long[] honorIds)
    {
        return sysHonorMapper.deleteSysHonorByHonorIds(honorIds);
    }

    /**
     * 删除荣誉信息管理信息
     *
     * @param honorId 荣誉信息管理主键
     * @return 结果
     */
    @Override
    public int deleteSysHonorByHonorId(Long honorId)
    {
        return sysHonorMapper.deleteSysHonorByHonorId(honorId);
    }
}
