package com.cms.system.service;

import java.util.List;
import com.cms.system.domain.SysComp;

/**
 * 竞赛信息管理Service接口
 *
 * @author cms
 * @date 2023-04-17
 */
public interface ISysCompService
{
    /**
     * 查询竞赛信息管理
     *
     * @param compId 竞赛信息管理主键
     * @return 竞赛信息管理
     */
    public SysComp selectSysCompByCompId(Long compId);

    /**
     * 查询竞赛信息管理列表
     *
     * @param sysComp 竞赛信息管理
     * @return 竞赛信息管理集合
     */
    public List<SysComp> selectSysCompList(SysComp sysComp);

    /**
     * 查询所有竞赛
     *
     * @return 岗位列表
     */
    public List<SysComp> selectCompAll();

    /**
     * 根据竞赛队伍ID获取竞赛选择框列表
     *
     * @param squadId 用户ID
     * @return 选中岗位ID列表
     */
    public List<Long> selectCompListBySquadId(Long squadId);
    /**
     * 根据竞赛队伍ID获取竞赛选择框列表
     *
     * @param honorId 用户ID
     * @return 选中岗位ID列表
     */
    public List<Long> selectCompListByHonorId(Long honorId);

    /**
     * 新增竞赛信息管理
     *
     * @param sysComp 竞赛信息管理
     * @return 结果
     */
    public int insertSysComp(SysComp sysComp);

    /**
     * 修改竞赛信息管理
     *
     * @param sysComp 竞赛信息管理
     * @return 结果
     */
    public int updateSysComp(SysComp sysComp);

    /**
     * 批量删除竞赛信息管理
     *
     * @param compIds 需要删除的竞赛信息管理主键集合
     * @return 结果
     */
    public int deleteSysCompByCompIds(Long[] compIds);

    /**
     * 删除竞赛信息管理信息
     *
     * @param compId 竞赛信息管理主键
     * @return 结果
     */
    public int deleteSysCompByCompId(Long compId);



}
