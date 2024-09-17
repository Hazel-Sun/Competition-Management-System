package com.cms.system.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.cms.common.core.utils.StringUtils;
import com.cms.system.api.domain.SysDept;
import com.cms.system.domain.SysComp;
import com.cms.system.domain.SysSquad;
import com.cms.system.service.ISysCompService;
import com.cms.system.service.ISysSquadService;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.cms.system.domain.SysHonor;
import com.cms.system.service.ISysHonorService;
import com.cms.common.core.web.controller.BaseController;
import com.cms.common.core.web.domain.AjaxResult;
import com.cms.common.core.utils.poi.ExcelUtil;
import com.cms.common.core.web.page.TableDataInfo;

/**
 * 荣誉信息管理Controller
 *
 * @author cms
 * @date 2023-04-25
 */
@RestController
@RequestMapping("/honor")
public class SysHonorController extends BaseController
{
    @Autowired
    private ISysHonorService sysHonorService;
    @Autowired
    private ISysCompService CompService;
    @Autowired
    private ISysSquadService SquadService;

    /**
     * 查询荣誉信息管理列表
     */
    @RequiresPermissions("system:honor:list")
    @GetMapping("/list")
    public TableDataInfo list(SysHonor sysHonor)
    {
        startPage();
        List<SysHonor> list = sysHonorService.selectSysHonorList(sysHonor);
        return getDataTable(list);
    }

    /**
     * 导出荣誉信息管理列表
     */
    @RequiresPermissions("system:honor:export")
    @Log(title = "荣誉信息管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysHonor sysHonor)
    {
        List<SysHonor> list = sysHonorService.selectSysHonorList(sysHonor);
        ExcelUtil<SysHonor> util = new ExcelUtil<SysHonor>(SysHonor.class);
        util.exportExcel(response, list, "荣誉信息管理数据");
    }


    /**
     * 新增荣誉信息管理
     */
    @RequiresPermissions("system:honor:add")
    @Log(title = "荣誉信息管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysHonor sysHonor)
    {
        sysHonor.setStatus("1");
        return toAjax(sysHonorService.insertSysHonor(sysHonor));
    }

    /**
     * 修改荣誉信息管理
     */
    @RequiresPermissions("system:honor:edit")
    @Log(title = "荣誉信息管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysHonor sysHonor)
    {
        return toAjax(sysHonorService.updateSysHonor(sysHonor));
    }

    /**
     * 删除荣誉信息管理
     */
    @RequiresPermissions("system:honor:remove")
    @Log(title = "荣誉信息管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{honorIds}")
    public AjaxResult remove(@PathVariable Long[] honorIds)
    {
        return toAjax(sysHonorService.deleteSysHonorByHonorIds(honorIds));
    }

    /**
     * 根据竞赛队伍编号获取详细信息
     */
    @RequiresPermissions("system:honor:query")
    @GetMapping(value = { "/", "/{honorId}" })
    public AjaxResult getInfo(@PathVariable(value = "honorId", required = false) Long honorId)
    {return success(sysHonorService.selectSysHonorByHonorId(honorId));
    }

    /**
     * 获取竞赛列表
     */
    @RequiresPermissions("system:honor:list")
    @GetMapping("/compList")
    public AjaxResult compList(SysComp sysComp)
    {
        return success(CompService.selectSysCompList(sysComp));
    }
    /**
     * 获取竞赛列表
     */
    @RequiresPermissions("system:honor:list")
    @GetMapping("/squadList")
    public AjaxResult squadList(SysSquad sysSquad)
    {
        return success(SquadService.selectSysSquadList(sysSquad));
    }
}
