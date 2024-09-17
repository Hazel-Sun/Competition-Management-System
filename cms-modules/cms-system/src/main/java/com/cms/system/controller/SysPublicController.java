package com.cms.system.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.cms.system.domain.SysComp;
import com.cms.system.service.ISysCompService;
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
import com.cms.system.domain.SysPublic;
import com.cms.system.service.ISysPublicService;
import com.cms.common.core.web.controller.BaseController;
import com.cms.common.core.web.domain.AjaxResult;
import com.cms.common.core.utils.poi.ExcelUtil;
import com.cms.common.core.web.page.TableDataInfo;

/**
 * 赛事宣传Controller
 *
 * @author cms
 * @date 2023-05-09
 */
@RestController
@RequestMapping("/public")
public class SysPublicController extends BaseController
{
    @Autowired
    private ISysPublicService sysPublicService;
    @Autowired
    private ISysCompService CompService;

    /**
     * 查询赛事宣传列表
     */
    @RequiresPermissions("system:public:list")
    @GetMapping("/list")
    public TableDataInfo list(SysPublic sysPublic)
    {
        startPage();
        List<SysPublic> list = sysPublicService.selectSysPublicList(sysPublic);
        return getDataTable(list);
    }

    /**
     * 导出赛事宣传列表
     */
    @RequiresPermissions("system:public:export")
    @Log(title = "赛事宣传", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysPublic sysPublic)
    {
        List<SysPublic> list = sysPublicService.selectSysPublicList(sysPublic);
        ExcelUtil<SysPublic> util = new ExcelUtil<SysPublic>(SysPublic.class);
        util.exportExcel(response, list, "赛事宣传数据");
    }

    /**
     * 获取赛事宣传详细信息
     */
    @RequiresPermissions("system:public:query")
    @GetMapping(value = "/{publicId}")
    public AjaxResult getInfo(@PathVariable("publicId") Long publicId)
    {
        return success(sysPublicService.selectSysPublicByPublicId(publicId));
    }

    /**
     * 新增赛事宣传
     */
    @RequiresPermissions("system:public:add")
    @Log(title = "赛事宣传", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysPublic sysPublic)
    {
        return toAjax(sysPublicService.insertSysPublic(sysPublic));
    }

    /**
     * 修改赛事宣传
     */
    @RequiresPermissions("system:public:edit")
    @Log(title = "赛事宣传", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysPublic sysPublic)
    {
        return toAjax(sysPublicService.updateSysPublic(sysPublic));
    }

    /**
     * 删除赛事宣传
     */
    @RequiresPermissions("system:public:remove")
    @Log(title = "赛事宣传", businessType = BusinessType.DELETE)
	@DeleteMapping("/{publicIds}")
    public AjaxResult remove(@PathVariable Long[] publicIds)
    {
        return toAjax(sysPublicService.deleteSysPublicByPublicIds(publicIds));
    }

    /**
     * 获取竞赛列表
     */
    @RequiresPermissions("system:public:list")
    @GetMapping("/compList")
    public AjaxResult compList(SysComp sysComp)
    {
        return success(CompService.selectSysCompList(sysComp));
    }
}
