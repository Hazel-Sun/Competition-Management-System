package com.cms.system.mapper;

import java.util.List;

import com.cms.system.domain.SysFileInfo;
import com.cms.system.domain.SysHonor;
import com.cms.system.domain.SysSquad;
import com.cms.system.domain.SysUserSquad;

/**
 * 竞赛队伍Mapper接口
 *
 * @author cms
 * @date 2023-04-17
 */
public interface SysSquadMapper
{
    /**
     * 查询竞赛队伍
     *
     * @param squadId 竞赛队伍主键
     * @return 竞赛队伍
     */
    public SysSquad selectSysSquadBySquadId(Long squadId);

    /**
     * 查询竞赛队伍列表
     *
     * @param sysSquad 竞赛队伍
     * @return 竞赛队伍集合
     */
    public List<SysSquad> selectSysSquadList(SysSquad sysSquad);

    /**
     * 新增竞赛队伍
     *
     * @param sysSquad 竞赛队伍
     * @return 结果
     */
    public int insertSysSquad(SysSquad sysSquad);

    /**
     * 修改竞赛队伍
     *
     * @param sysSquad 竞赛队伍
     * @return 结果
     */
    public int updateSysSquad(SysSquad sysSquad);

    /**
     * 删除竞赛队伍
     *
     * @param squadId 竞赛队伍主键
     * @return 结果
     */
    public int deleteSysSquadBySquadId(Long squadId);

    /**
     * 批量删除竞赛队伍
     *
     * @param squadIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysSquadBySquadIds(Long[] squadIds);
    /**
     * 批量删除荣誉信息管理
     *
     * @param squadIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysHonorBySquadIds(Long[] squadIds);

    /**
     * 批量新增荣誉信息管理
     *
     * @param sysHonorList 荣誉信息管理列表
     * @return 结果
     */
    public int batchSysHonor(List<SysHonor> sysHonorList);


    /**
     * 通过竞赛队伍主键删除荣誉信息管理信息
     *
     * @param squadId 竞赛队伍ID
     * @return 结果
     */
    public int deleteSysHonorBySquadId(Long squadId);

    public int deleteSysUserSquadBySquadId(Long squadId);

    /**
     * 批量删除过程文件管理
     *
     * @param squadIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysFileInfoBySquadIds(Long[] squadIds);

    public int deleteSysUserSquadBySquadIds(Long[] squadIds);

    /**
     * 批量新增过程文件管理
     *
     * @param sysFileInfoList 过程文件管理列表
     * @return 结果
     */
    public int batchSysFileInfo(List<SysFileInfo> sysFileInfoList);
    /**
     * 批量新增用户角色信息
     *
     * @param sysUserSquadList 用户角色列表
     * @return 结果
     */
    public int batchSysUserSquad(List<SysUserSquad> sysUserSquadList);

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param squadId 角色ID
     * @return 结果
     */
    public int countUserSquadBySquadId(Long squadId);

    /**
     * 通过竞赛队伍主键删除过程文件管理信息
     *
     * @param squadId 竞赛队伍ID
     * @return 结果
     */
    public int deleteSysFileInfoBySquadId(Long squadId);
}
