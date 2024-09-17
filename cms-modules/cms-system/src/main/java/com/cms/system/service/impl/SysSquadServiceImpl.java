package com.cms.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import com.cms.common.core.utils.DateUtils;
import com.cms.common.core.utils.StringUtils;
import com.cms.common.datascope.annotation.DataScope;
import com.cms.common.security.utils.SecurityUtils;
import com.cms.system.domain.*;
import com.cms.system.mapper.SysUserSquadMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cms.system.mapper.SysSquadMapper;
import com.cms.system.service.ISysSquadService;
import org.springframework.transaction.annotation.Transactional;
import com.cms.common.core.utils.file.FileUtils;

/**
 * 竞赛队伍Service业务层处理
 *
 * @author cms
 * @date 2023-04-17
 */
@Service
public class SysSquadServiceImpl implements ISysSquadService
{

    @Autowired
    private SysSquadMapper sysSquadMapper;
    @Autowired
    private SysUserSquadMapper userSquadMapper;





    /**
     * 查询竞赛队伍
     *
     * @param squadId 竞赛队伍主键
     * @return 竞赛队伍
     */
    @Override
    public SysSquad selectSysSquadBySquadId(Long squadId)
    {
        return sysSquadMapper.selectSysSquadBySquadId(squadId);
    }

    /**
     * 查询竞赛队伍列表
     *
     * @param sysSquad 竞赛队伍
     * @return 竞赛队伍
     */
    @Override
    @DataScope(userAlias = "ua")
    public List<SysSquad> selectSysSquadList(SysSquad sysSquad)
    {
        return sysSquadMapper.selectSysSquadList(sysSquad);
    }

    /**
     * 新增竞赛队伍
     *
     * @param sysSquad 竞赛队伍
     * @return 结果
     */
    @Override
    public int insertSysSquad(SysSquad sysSquad)
    {
        sysSquad.setCreateTime(DateUtils.getNowDate());
        int rows = sysSquadMapper.insertSysSquad(sysSquad);
        insertSysHonor(sysSquad);
        insertSysFileInfo(sysSquad);
        addAuthUser(sysSquad);
        insertAuthUser(sysSquad);
        countUser(sysSquad);
        return rows;
    }


    /**
     * 修改竞赛队伍
     *
     * @param sysSquad 竞赛队伍
     * @return 结果
     */
    @Override
    public int updateSysSquad(SysSquad sysSquad)
    {
        Long squadId = sysSquad.getSquadId();
        sysSquad.setMemberNum((long) userSquadMapper.countUserSquadBySquadId(squadId));
        sysSquad.setUpdateTime(DateUtils.getNowDate());
        sysSquadMapper.deleteSysHonorBySquadId(sysSquad.getSquadId());
        sysSquadMapper.deleteSysFileInfoBySquadId(sysSquad.getSquadId());
        insertSysHonor(sysSquad);
        insertSysFileInfo(sysSquad);
        return sysSquadMapper.updateSysSquad(sysSquad);
    }

    /**
     * 批量删除竞赛队伍
     *
     * @param squadIds 需要删除的竞赛队伍主键
     * @return 结果
     */
    @Override
    public int deleteSysSquadBySquadIds(Long[] squadIds)
    {
        sysSquadMapper.deleteSysHonorBySquadIds(squadIds);
        sysSquadMapper.deleteSysFileInfoBySquadIds(squadIds);
        sysSquadMapper.deleteSysUserSquadBySquadIds(squadIds);
        return sysSquadMapper.deleteSysSquadBySquadIds(squadIds);
    }

    /**
     * 删除竞赛队伍信息
     *
     * @param squadId 竞赛队伍主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSysSquadBySquadId(Long squadId)
    {
        sysSquadMapper.deleteSysHonorBySquadId(squadId);
        sysSquadMapper.deleteSysUserSquadBySquadId(squadId);
        sysSquadMapper.deleteSysFileInfoBySquadId(squadId);
        return sysSquadMapper.deleteSysSquadBySquadId(squadId);
    }
    /**
     * 新增荣誉信息管理信息
     *
     * @param sysSquad 竞赛队伍对象
     */
    public void insertSysHonor(SysSquad sysSquad)
    {
        List<SysHonor> sysHonorList = sysSquad.getSysHonorList();
        Long squadId = sysSquad.getSquadId();
        System.out.println("wtfffffffffff:" + squadId);
        if (StringUtils.isNotNull(sysHonorList))
        {
            List<SysHonor> list = new ArrayList<SysHonor>();
            for (SysHonor sysHonor : sysHonorList)
            {
                System.out.println("current squard squardid:" + sysSquad.getSquadId());
                sysHonor.setSquadId(squadId);
                System.out.println("current honor squardid:" + sysHonor.getSquadId());
                list.add(sysHonor);
            }
            if (list.size() > 0)
            {
                sysSquadMapper.batchSysHonor(list);
            }
        }
    }
    /**
     * 新增过程文件管理信息
     *
     * @param sysSquad 竞赛队伍对象
     */
    public void insertSysFileInfo(SysSquad sysSquad)
    {
        List<SysFileInfo> sysFileInfoList = sysSquad.getSysFileInfoList();
        Long squadId = sysSquad.getSquadId();
        if (StringUtils.isNotNull(sysFileInfoList))
        {
            List<SysFileInfo> list = new ArrayList<SysFileInfo>();
            for (SysFileInfo sysFileInfo : sysFileInfoList)
            {
                sysFileInfo.setSquadId(squadId);
                String url =sysFileInfo.getFilePath();
                sysFileInfo.setFileName(FileUtils.getName(url));
                list.add(sysFileInfo);
            }
            if (list.size() > 0)
            {
                sysSquadMapper.batchSysFileInfo(list);
            }
        }
    }
    /**
     * 取消授权用户角色
     *
     * @param userSquad 用户和角色关联信息
     * @return 结果
     */
    @Override
    public int deleteAuthUser(SysUserSquad userSquad)
    {
        return userSquadMapper.deleteUserSquadInfo(userSquad);
    }

    /**
     * 批量取消授权用户角色
     *
     * @param squadId 角色ID
     * @param userIds 需要取消授权的用户数据ID
     * @return 结果
     */
    @Override
    public int deleteAuthUsers(Long squadId, Long[] userIds)
    {
        return userSquadMapper.deleteUserSquadInfos(squadId, userIds);
    }

    /**
     * 批量选择授权用户角色
     *
     * @param squadId 角色ID
     * @param userIds 需要授权的用户数据ID
     * @return 结果
     */
    @Override
    public int insertAuthUsers(Long squadId, Long[] userIds)
    {
        // 新增用户与角色管理
        List<SysUserSquad> list = new ArrayList<SysUserSquad>();
        for (Long userId : userIds)
        {
            SysUserSquad ua = new SysUserSquad();
            ua.setUserId(userId);
            ua.setSquadId(squadId);
            list.add(ua);
        }
        return userSquadMapper.batchUserSquad(list);
    }
    public void insertAuthUser(SysSquad sysSquad)
    {
        // 新增用户与角色管理
        Long squadId = sysSquad.getSquadId();
        List<SysUserSquad> list = new ArrayList<SysUserSquad>();
        SysUserSquad ua = new SysUserSquad();
        ua.setUserId(SecurityUtils.getUserId());
        ua.setSquadId(squadId);
        list.add(ua);
        sysSquadMapper.batchSysUserSquad(list);
    }
    public void countUser(SysSquad sysSquad)
    {
        Long squadId = sysSquad.getSquadId();
        sysSquad.setMemberNum((long) sysSquadMapper.countUserSquadBySquadId(squadId));
    }
    /**
     * 批量选择授权用户角色
     *
     * @param sysSquad 角色ID
     * @return 结果
     */
    @Override
    public void addAuthUser(SysSquad sysSquad)
    {
        List<SysUserSquad> sysUserSquadList = sysSquad.getSysUserSquadList();
        Long squadId = sysSquad.getSquadId();
        if (StringUtils.isNotNull(sysUserSquadList))
        {
            List<SysUserSquad> list = new ArrayList<SysUserSquad>();
            for (SysUserSquad sysUserSquad : sysUserSquadList)
            {
                sysUserSquad.setSquadId(squadId);
                list.add(sysUserSquad);
            }
            if (list.size() > 0)
            {
                sysSquadMapper.batchSysUserSquad(list);
            }
        }

    }


}
