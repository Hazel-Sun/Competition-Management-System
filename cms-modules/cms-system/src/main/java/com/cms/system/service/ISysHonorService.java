package com.cms.system.service;

import java.util.List;
import com.cms.system.domain.SysHonor;

/**
 * 荣誉信息管理Service接口
 * 
 * @author cms
 * @date 2023-04-25
 */
public interface ISysHonorService 
{
    /**
     * 查询荣誉信息管理
     * 
     * @param honorId 荣誉信息管理主键
     * @return 荣誉信息管理
     */
    public SysHonor selectSysHonorByHonorId(Long honorId);

    /**
     * 查询荣誉信息管理列表
     * 
     * @param sysHonor 荣誉信息管理
     * @return 荣誉信息管理集合
     */
    public List<SysHonor> selectSysHonorList(SysHonor sysHonor);

    /**
     * 新增荣誉信息管理
     * 
     * @param sysHonor 荣誉信息管理
     * @return 结果
     */
    public int insertSysHonor(SysHonor sysHonor);

    /**
     * 修改荣誉信息管理
     * 
     * @param sysHonor 荣誉信息管理
     * @return 结果
     */
    public int updateSysHonor(SysHonor sysHonor);

    /**
     * 批量删除荣誉信息管理
     * 
     * @param honorIds 需要删除的荣誉信息管理主键集合
     * @return 结果
     */
    public int deleteSysHonorByHonorIds(Long[] honorIds);

    /**
     * 删除荣誉信息管理信息
     * 
     * @param honorId 荣誉信息管理主键
     * @return 结果
     */
    public int deleteSysHonorByHonorId(Long honorId);
}
