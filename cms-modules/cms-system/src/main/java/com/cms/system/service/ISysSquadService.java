package com.cms.system.service;

import java.util.List;
import com.cms.system.domain.SysSquad;
import com.cms.system.domain.SysUserRole;
import com.cms.system.domain.SysUserSquad;

/**
 * 竞赛队伍Service接口
 *
 * @author cms
 * @date 2023-04-17
 */
public interface ISysSquadService
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
     * 批量删除竞赛队伍
     *
     * @param squadIds 需要删除的竞赛队伍主键集合
     * @return 结果
     */
    public int deleteSysSquadBySquadIds(Long[] squadIds);

    /**
     * 删除竞赛队伍信息
     *
     * @param squadId 竞赛队伍主键
     * @return 结果
     */
    public int deleteSysSquadBySquadId(Long squadId);
    /**
     * 取消授权用户角色
     *
     * @param userSquad 用户和角色关联信息
     * @return 结果
     */
    public int deleteAuthUser(SysUserSquad userSquad);

    /**
     * 批量取消授权用户角色
     *
     * @param squadId 角色ID
     * @param userIds 需要取消授权的用户数据ID
     * @return 结果
     */
    public int deleteAuthUsers(Long squadId, Long[] userIds);

    /**
     * 批量选择授权用户角色
     *
     * @param squadId 角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    public int insertAuthUsers(Long squadId, Long[] userIds);
    /**
     * 授权用户角色
     *
     * @param sysSquad 角色ID
     * @return 结果
     */
    public void addAuthUser(SysSquad sysSquad);


}
