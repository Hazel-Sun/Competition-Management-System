package com.cms.system.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

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
import com.cms.system.domain.SysFileInfo;
import com.cms.system.service.ISysFileInfoService;
import com.cms.common.core.web.controller.BaseController;
import com.cms.common.core.web.domain.AjaxResult;
import com.cms.common.core.utils.poi.ExcelUtil;
import com.cms.common.core.web.page.TableDataInfo;
import com.cms.common.core.utils.file.FileUtils;

/**
 * 过程文件管理Controller
 *
 * @author cms
 * @date 2023-05-13
 */
@RestController
@RequestMapping("/processinfo")
public class SysFileInfoController extends BaseController
{
    @Autowired
    private ISysFileInfoService sysFileInfoService;
    @Autowired
    private ISysCompService CompService;
    @Autowired
    private ISysSquadService SquadService;

    /**
     * 查询过程文件管理列表
     */
    @RequiresPermissions("system:processinfo:list")
    @GetMapping("/list")
    public TableDataInfo list(SysFileInfo sysFileInfo)
    {
        startPage();
        List<SysFileInfo> list = sysFileInfoService.selectSysFileInfoList(sysFileInfo);
        return getDataTable(list);
    }

    /**
     * 导出过程文件管理列表
     */
    @RequiresPermissions("system:processinfo:export")
    @Log(title = "过程文件管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysFileInfo sysFileInfo)
    {
        List<SysFileInfo> list = sysFileInfoService.selectSysFileInfoList(sysFileInfo);
        ExcelUtil<SysFileInfo> util = new ExcelUtil<SysFileInfo>(SysFileInfo.class);
        util.exportExcel(response, list, "过程文件管理数据");
    }

    /**
     * 获取过程文件管理详细信息
     */
    @RequiresPermissions("system:processinfo:query")
    @GetMapping(value = "/{fileId}")
    public AjaxResult getInfo(@PathVariable("fileId") Long fileId)
    {
        return success(sysFileInfoService.selectSysFileInfoByFileId(fileId));
    }

    /**
     * 新增过程文件管理
     */
    @RequiresPermissions("system:processinfo:add")
    @Log(title = "过程文件管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysFileInfo sysFileInfo)
    {
        return toAjax(sysFileInfoService.insertSysFileInfo(sysFileInfo));
    }

    /**
     * 修改过程文件管理
     */
    @RequiresPermissions("system:processinfo:edit")
    @Log(title = "过程文件管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysFileInfo sysFileInfo)
    {
        return toAjax(sysFileInfoService.updateSysFileInfo(sysFileInfo));
    }

    /**
     * 删除过程文件管理
     */
    @RequiresPermissions("system:processinfo:remove")
    @Log(title = "过程文件管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{fileIds}")
    public AjaxResult remove(@PathVariable Long[] fileIds)
    {
        return toAjax(sysFileInfoService.deleteSysFileInfoByFileIds(fileIds));
    }

    /**
     * 获取竞赛列表
     */
    @RequiresPermissions("system:processinfo:list")
    @GetMapping("/compList")
    public AjaxResult compList(SysComp sysComp)
    {
        return success(CompService.selectSysCompList(sysComp));
    }
    /**
     * 获取竞赛列表
     */
    @RequiresPermissions("system:processinfo:list")
    @GetMapping("/squadList")
    public AjaxResult squadList(SysSquad sysSquad)
    {
        return success(SquadService.selectSysSquadList(sysSquad));
    }


}
