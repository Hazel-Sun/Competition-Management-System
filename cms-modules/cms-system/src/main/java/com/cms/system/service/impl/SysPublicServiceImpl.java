package com.cms.system.service.impl;

import java.util.List;
import com.cms.common.core.utils.DateUtils;
import com.cms.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cms.system.mapper.SysPublicMapper;
import com.cms.system.domain.SysPublic;
import com.cms.system.service.ISysPublicService;

/**
 * 赛事宣传Service业务层处理
 *
 * @author cms
 * @date 2023-05-09
 */
@Service
public class SysPublicServiceImpl implements ISysPublicService
{
    @Autowired
    private SysPublicMapper sysPublicMapper;

    /**
     * 查询赛事宣传
     *
     * @param publicId 赛事宣传主键
     * @return 赛事宣传
     */
    @Override
    public SysPublic selectSysPublicByPublicId(Long publicId)
    {
        return sysPublicMapper.selectSysPublicByPublicId(publicId);
    }

    /**
     * 查询赛事宣传列表
     *
     * @param sysPublic 赛事宣传
     * @return 赛事宣传
     */
    @Override
    public List<SysPublic> selectSysPublicList(SysPublic sysPublic)
    {
        return sysPublicMapper.selectSysPublicList(sysPublic);
    }

    /**
     * 新增赛事宣传
     *
     * @param sysPublic 赛事宣传
     * @return 结果
     */
    @Override
    public int insertSysPublic(SysPublic sysPublic)
    {
        sysPublic.setCreateBy(SecurityUtils.getUsername());
        sysPublic.setCreateTime(DateUtils.getNowDate());
        return sysPublicMapper.insertSysPublic(sysPublic);
    }

    /**
     * 修改赛事宣传
     *
     * @param sysPublic 赛事宣传
     * @return 结果
     */
    @Override
    public int updateSysPublic(SysPublic sysPublic)
    {
        return sysPublicMapper.updateSysPublic(sysPublic);
    }

    /**
     * 批量删除赛事宣传
     *
     * @param publicIds 需要删除的赛事宣传主键
     * @return 结果
     */
    @Override
    public int deleteSysPublicByPublicIds(Long[] publicIds)
    {
        return sysPublicMapper.deleteSysPublicByPublicIds(publicIds);
    }

    /**
     * 删除赛事宣传信息
     *
     * @param publicId 赛事宣传主键
     * @return 结果
     */
    @Override
    public int deleteSysPublicByPublicId(Long publicId)
    {
        return sysPublicMapper.deleteSysPublicByPublicId(publicId);
    }
}
