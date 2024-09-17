package com.cms.system.mapper;

import com.cms.system.domain.SysUserSquad;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户与角色关联表 数据层
 *
 * @author ruoyi
 */
public interface SysUserSquadMapper
{
    /**
     * 通过用户ID删除用户和角色关联
     *
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteUserSquadByUserId(Long userId);

    /**
     * 批量删除用户和角色关联
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteUserSquad(Long[] ids);


    /**
     * 批量新增用户角色信息
     *
     * @param userSquadList 用户角色列表
     * @return 结果
     */
    public int batchUserSquad(List<SysUserSquad> userSquadList);


    /**
     * 删除用户和角色关联信息
     *
     * @param userSquad 用户和角色关联信息
     * @return 结果
     */
    public int deleteUserSquadInfo(SysUserSquad userSquad);

    /**
     * 批量取消授权用户角色
     *
     * @param squadId 角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    public int deleteUserSquadInfos(@Param("squadId") Long squadId, @Param("userIds") Long[] userIds);

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param squadId 角色ID
     * @return 结果
     */
    public int countUserSquadBySquadId(Long squadId);

}
