package com.cms.system.service;

import java.util.List;
import com.cms.system.domain.SysPublic;

/**
 * 赛事宣传Service接口
 * 
 * @author cms
 * @date 2023-05-09
 */
public interface ISysPublicService 
{
    /**
     * 查询赛事宣传
     * 
     * @param publicId 赛事宣传主键
     * @return 赛事宣传
     */
    public SysPublic selectSysPublicByPublicId(Long publicId);

    /**
     * 查询赛事宣传列表
     * 
     * @param sysPublic 赛事宣传
     * @return 赛事宣传集合
     */
    public List<SysPublic> selectSysPublicList(SysPublic sysPublic);

    /**
     * 新增赛事宣传
     * 
     * @param sysPublic 赛事宣传
     * @return 结果
     */
    public int insertSysPublic(SysPublic sysPublic);

    /**
     * 修改赛事宣传
     * 
     * @param sysPublic 赛事宣传
     * @return 结果
     */
    public int updateSysPublic(SysPublic sysPublic);

    /**
     * 批量删除赛事宣传
     * 
     * @param publicIds 需要删除的赛事宣传主键集合
     * @return 结果
     */
    public int deleteSysPublicByPublicIds(Long[] publicIds);

    /**
     * 删除赛事宣传信息
     * 
     * @param publicId 赛事宣传主键
     * @return 结果
     */
    public int deleteSysPublicByPublicId(Long publicId);
}
