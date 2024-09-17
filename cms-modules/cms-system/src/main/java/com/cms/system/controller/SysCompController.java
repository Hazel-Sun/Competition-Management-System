package com.cms.system.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.cms.common.security.utils.SecurityUtils;
import com.cms.system.api.domain.SysDept;
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
import com.cms.system.domain.SysComp;
import com.cms.system.service.ISysCompService;
import com.cms.common.core.web.controller.BaseController;
import com.cms.common.core.web.domain.AjaxResult;
import com.cms.common.core.utils.poi.ExcelUtil;
import com.cms.common.core.web.page.TableDataInfo;

/**
 * 竞赛信息管理Controller
 *
 * @author cms
 * @date 2023-04-17
 */
@RestController
@RequestMapping("/management")
public class SysCompController extends BaseController
{
    @Autowired
    private ISysCompService sysCompService;

    /**
     * 查询竞赛信息管理列表
     */
    @RequiresPermissions("system:management:list")
    @GetMapping("/list")
    public TableDataInfo list(SysComp sysComp)
    {
        startPage();
        List<SysComp> list = sysCompService.selectSysCompList(sysComp);
        return getDataTable(list);
    }

    /**
     * 导出竞赛信息管理列表
     */
    @RequiresPermissions("system:management:export")
    @Log(title = "竞赛信息管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysComp sysComp)
    {
        List<SysComp> list = sysCompService.selectSysCompList(sysComp);
        ExcelUtil<SysComp> util = new ExcelUtil<SysComp>(SysComp.class);
        util.exportExcel(response, list, "竞赛信息管理数据");
    }

    /**
     * 获取竞赛信息管理详细信息
     */
    @RequiresPermissions("system:management:query")
    @GetMapping(value = "/{compId}")
    public AjaxResult getInfo(@Validated @PathVariable("compId") Long compId)
    {
        return success(sysCompService.selectSysCompByCompId(compId));
    }

    /**
     * 新增竞赛信息管理
     */

    @RequiresPermissions("system:management:add")
    @Log(title = "竞赛信息管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysComp sysComp)
    {

        sysComp.setCreateBy(SecurityUtils.getUsername());
        return toAjax(sysCompService.insertSysComp(sysComp));
    }

    /**
     * 修改竞赛信息管理
     */
    @RequiresPermissions("system:management:edit")
    @Log(title = "竞赛信息管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysComp sysComp)
    {
        sysComp.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(sysCompService.updateSysComp(sysComp));
    }

    /**
     * 删除竞赛信息管理
     */
    @RequiresPermissions("system:management:remove")
    @Log(title = "竞赛信息管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{compIds}")
    public AjaxResult remove(@PathVariable Long[] compIds)
    {
        return toAjax(sysCompService.deleteSysCompByCompIds(compIds));
    }
    /**
     * 获取竞赛选择框列表
     */
    @GetMapping("/optionselect")
    public AjaxResult optionselect()
    {
        List<SysComp> comps = sysCompService.selectCompAll();
        return success(comps);
    }
 
    
}
