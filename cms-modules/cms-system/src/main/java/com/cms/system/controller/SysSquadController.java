package com.cms.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.cms.common.security.utils.SecurityUtils;
import com.cms.system.api.domain.SysUser;
import com.cms.system.domain.SysUserSquad;
import com.cms.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cms.common.log.annotation.Log;
import com.cms.common.log.enums.BusinessType;
import com.cms.common.security.annotation.RequiresPermissions;
import com.cms.system.domain.SysSquad;
import com.cms.system.domain.SysComp;
import com.cms.system.service.ISysSquadService;
import com.cms.system.service.ISysCompService;
import com.cms.common.core.web.controller.BaseController;
import com.cms.common.core.web.domain.AjaxResult;
import com.cms.common.core.utils.poi.ExcelUtil;
import com.cms.common.core.web.page.TableDataInfo;

/**
 * 竞赛队伍Controller
 *
 * @author cms
 * @date 2023-04-17
 */
@RestController
@RequestMapping("/squad")
public class SysSquadController extends BaseController
{

    @Autowired
    private ISysSquadService sysSquadService;
    @Autowired
    private ISysCompService CompService;
    @Autowired
    private ISysUserService userService;
    /**
     * 查询竞赛队伍列表
     */
    @RequiresPermissions("system:squad:list")
    @GetMapping("/list")
    public TableDataInfo list(SysSquad sysSquad)
    {
        startPage();
        List<SysSquad> list = sysSquadService.selectSysSquadList(sysSquad);
        return getDataTable(list);
    }

    /**
     * 导出竞赛队伍列表
     */
    @RequiresPermissions("system:squad:export")
    @Log(title = "竞赛队伍", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysSquad sysSquad)
    {
        List<SysSquad> list = sysSquadService.selectSysSquadList(sysSquad);
        ExcelUtil<SysSquad> util = new ExcelUtil<SysSquad>(SysSquad.class);
        util.exportExcel(response, list, "竞赛队伍数据");
    }


    /**
     * 新增竞赛队伍
     */
    @RequiresPermissions("system:squad:add")
    @Log(title = "竞赛队伍", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysSquad sysSquad)
    {
        sysSquad.setCreateBy(SecurityUtils.getUsername());
        sysSquad.setStatus("1");
        System.out.println("squad add:"+sysSquad.getSquadId());
        return toAjax(sysSquadService.insertSysSquad(sysSquad));
    }

    /**
     * 修改竞赛队伍
     */
    @RequiresPermissions("system:squad:edit")
    @Log(title = "竞赛队伍", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysSquad sysSquad)
    {
        sysSquad.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(sysSquadService.updateSysSquad(sysSquad));
    }

    /**
     * 删除竞赛队伍
     */
    @RequiresPermissions("system:squad:remove")
    @Log(title = "竞赛队伍", businessType = BusinessType.DELETE)
	@DeleteMapping("/{squadIds}")
    public AjaxResult remove(@PathVariable Long[] squadIds)
    {
        return toAjax(sysSquadService.deleteSysSquadBySquadIds(squadIds));
    }
    /**
     * 获取竞赛列表
     */
    @RequiresPermissions("system:squad:list")
    @GetMapping("/compList")
    public AjaxResult compList(SysComp sysComp)
    {
        return success(CompService.selectSysCompList(sysComp));
    }


    /**
     * 根据竞赛队伍编号获取详细信息
     */
    @RequiresPermissions("system:squad:query")
    @GetMapping(value = { "/", "/{squadId}" })
    public AjaxResult getInfo(@PathVariable(value = "squadId", required = false) Long squadId)
    {
        return success(sysSquadService.selectSysSquadBySquadId(squadId));
    }

    /**
     *
     * 查询已分配用户角色列表
     */
    @RequiresPermissions("system:squad:list")
    @GetMapping("/authUser/allocatedList")
    public TableDataInfo allocatedSquadList(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectAllocatedSquadList(user);
        return getDataTable(list);
    }

    /**
     * 查询未分配用户角色列表
     */
    @RequiresPermissions("system:squad:list")
    @GetMapping("/authUser/unallocatedList")
    public TableDataInfo unallocatedSquadList(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectUnallocatedSquadList(user);
        return getDataTable(list);
    }

    /**
     * 取消授权用户
     */
    @RequiresPermissions("system:squad:edit")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancel")
    public AjaxResult cancelAuthUser(@RequestBody SysUserSquad userSquad)
    {
        return toAjax(sysSquadService.deleteAuthUser(userSquad));
    }

    /**
     * 批量取消授权用户
     */
    @RequiresPermissions("system:role:edit")
    @Log(title = "小组管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancelAll")
    public AjaxResult cancelAuthUserAll(Long squadId, Long[] userIds)
    {
        return toAjax(sysSquadService.deleteAuthUsers(squadId, userIds));
    }

    /**
     * 批量选择用户授权
     */
    @RequiresPermissions("system:squad:edit")
    @Log(title = "小组管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/selectAll")
    public AjaxResult selectAuthUserAll(Long squadId, Long[] userIds)
    {
        return toAjax(sysSquadService.insertAuthUsers(squadId, userIds));
    }


}
