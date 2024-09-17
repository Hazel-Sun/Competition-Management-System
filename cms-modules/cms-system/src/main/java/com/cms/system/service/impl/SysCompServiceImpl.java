package com.cms.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cms.common.core.utils.DateUtils;
import com.cms.common.core.utils.StringUtils;
import com.cms.common.security.utils.SecurityUtils;
import com.cms.system.domain.SysSquad;
import com.cms.system.domain.SysUserSquad;
import com.cms.system.mapper.SysSquadMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cms.system.mapper.SysCompMapper;
import com.cms.system.domain.SysComp;
import com.cms.system.service.ISysCompService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 竞赛信息管理Service业务层处理
 *
 * @author cms
 * @date 2023-04-17
 */
@Service
public class SysCompServiceImpl implements ISysCompService
{
    @Autowired
    private SysCompMapper sysCompMapper;
    /**
     * 查询竞赛信息管理
     *
     * @param compId 竞赛信息管理主键
     * @return 竞赛信息管理
     */
    @Override
    public SysComp selectSysCompByCompId(Long compId)
    {
        return sysCompMapper.selectSysCompByCompId(compId);
    }

    /**
     * 查询竞赛信息管理列表
     *
     * @param sysComp 竞赛信息管理
     * @return 竞赛信息管理
     */
    @Override
    public List<SysComp> selectSysCompList(SysComp sysComp)
    {
        return sysCompMapper.selectSysCompList(sysComp);
    }
    /**
     * 查询所有竞赛
     *
     * @return 竞赛列表
     */
    @Override
    public List<SysComp> selectCompAll()
    {
        return sysCompMapper.selectCompAll();
    }
    /**
     * 根据竞赛队伍ID获取竞赛选择框列表
     *
     * @param squadId 用户ID
     * @return 选中竞赛ID列表
     */
    @Override
    public List<Long> selectCompListBySquadId(Long squadId)
    {
        return sysCompMapper.selectCompListBySquadId(squadId);
    }
    /**
     * 根据荣誉ID获取竞赛选择框列表
     *
     * @param honorId 用户ID
     * @return 选中竞赛ID列表
     */
    @Override
    public List<Long> selectCompListByHonorId(Long honorId)
    {
        return sysCompMapper.selectCompListByHonorId(honorId);
    }
    /**
     * 新增竞赛信息管理
     *
     * @param sysComp 竞赛信息管理
     * @return 结果
     */
    @Override
    public int insertSysComp(SysComp sysComp)
    {
        int rows = sysCompMapper.insertSysComp(sysComp);
        insertSysSquad(sysComp);
        return rows;
    }

    /**
     * 修改竞赛信息管理
     *
     * @param sysComp 竞赛信息管理
     * @return 结果
     */
    @Override
    public int updateSysComp(SysComp sysComp)
    {
        sysCompMapper.deleteSysSquadByCompId(sysComp.getCompId());
        insertSysSquad(sysComp);
        return sysCompMapper.updateSysComp(sysComp);
    }

    /**
     * 批量删除竞赛信息管理
     *
     * @param compIds 需要删除的竞赛信息管理主键
     * @return 结果
     */
    @Override
    public int deleteSysCompByCompIds(Long[] compIds)
    {
        sysCompMapper.deleteSysSquadByCompIds(compIds);
        return sysCompMapper.deleteSysCompByCompIds(compIds);
    }

    /**
     * 删除竞赛信息管理信息
     *
     * @param compId 竞赛信息管理主键
     * @return 结果
     */
    @Override
    public int deleteSysCompByCompId(Long compId)
    {
        sysCompMapper.deleteSysSquadByCompId(compId);
        return sysCompMapper.deleteSysCompByCompId(compId);
    }

    /**
     * 新增竞赛队伍信息
     *
     * @param sysComp 竞赛信息管理对象
     */
    public void insertSysSquad(SysComp sysComp)
    {
        List<SysSquad> sysSquadList = sysComp.getSysSquadList();
        Long compId = sysComp.getCompId();
        if (StringUtils.isNotNull(sysSquadList))
        {
            List<SysSquad> list = new ArrayList<SysSquad>();
            for (SysSquad sysSquad : sysSquadList)
            {
                sysSquad.setCompId(compId);
                sysSquad.setCreateBy(SecurityUtils.getUsername());
                sysSquad.setSquadLeader(sysSquad.getCreateBy());
                list.add(sysSquad);
            }
            if (list.size() > 0)
            {
                sysCompMapper.batchSysSquad(list);
            }
        }
    }



}
